package com.kimfy.notenoughblocks.common.util;

import com.kimfy.notenoughblocks.common.block.IBlockProperties;
import com.kimfy.notenoughblocks.common.util.block.Materials;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MinecraftUtilities
{
    public static final String MODID_NAME_META         = "^([A-z0-9]+):([A-z0-9]+):([0-9]+)$";                   // modid:name:meta
    public static final String MODID_NAME_META_MIN_MAX = "^([A-z0-9]+):([A-z0-9]+):([0-9]+)#([0-9]+)-([0-9]+)$"; // modid:name:meta#min-max
    public static final String MODID_NAME_META_AMOUNT  = "^([A-z0-9]+):([A-z0-9]+):([0-9]+)#([0-9]+)$";          // modid:name:meta#amount

    public static final String MODID_NAME         = "^([A-z0-9]+):([A-z0-9]+)$";                   // modid:name
    public static final String MODID_NAME_MIN_MAX = "^([A-z0-9]+):([A-z0-9]+)#([0-9]+)-([0-9]+)$"; // modid:name#min-max
    public static final String MODID_NAME_AMOUNT  = "^([A-z0-9]+):([A-z0-9]+)#([0-9]+)$";          // modid:name#amount

    public static final String NAME_META         = "^([A-z0-9]+):([0-9]+)$";                   // name:meta
    public static final String NAME_META_MIN_MAX = "^([A-z0-9]+):([0-9]+)#([0-9]+)-([0-9]+)$"; // name:meta#min-max
    public static final String NAME_META_AMOUNT  = "^([A-z0-9]+):([0-9]+)#([0-9]+)$";          // name:meta#amount

    public static final String NAME         = "^([A-z0-9]+)$";                   // name
    public static final String NAME_MIN_MAX = "^([A-z0-9]+)#([0-9]+)-([0-9]+)$"; // name#min-max
    public static final String NAME_AMOUNT  = "^([A-z0-9]+)#([0-9]+)$";          // name#amount

    public static ItemStack strToItemStack(String str)
    {
        String[] split = str.split("[:]");
        if (str.matches(MODID_NAME_META))
        {
            Item item = getItem(split[0], split[1]);
            return new ItemStack(item, 0, Integer.valueOf(split[2]));
        }
        else if (str.matches(MODID_NAME))
        {
            Item item = getItem(split[0], split[1]);
            return new ItemStack(item);
        }
        else if (str.matches(NAME_META))
        {
            Item item = getItem(split[0]);
            return new ItemStack(item, 1, Integer.valueOf(split[1]));
        }
        else if (str.matches(NAME))
        {
            Item item = getItem(split[0]);
            return new ItemStack(item);
        }
        else
        {
            throw new IllegalArgumentException("Input String \"" + String.valueOf(str) + "\" is not valid. Refer to the wiki for valid formats. If you believe this is a bug, please report it to the mod author!");
        }
    }

    public static String itemStackToString(ItemStack stack)
    {
        ResourceLocation rl = stack.getItem().getRegistryName();
        return rl.getResourceDomain() + ":" + rl.getResourcePath() + ":" + stack.getMetadata();
    }

    public static Item getItem(String modid, String name)
    {
        if (modid != null && name != null)
        {
            ResourceLocation rl = new ResourceLocation(modid, name);
            return  itemExists(rl) ? Item.REGISTRY.getObject(rl) : null;
        }
        return null;
    }

    public static Item getItem(String name)
    {
        return getItem("minecraft", name);
    }

    public static boolean itemExists(ResourceLocation rl)
    {
        return Item.REGISTRY.containsKey(rl);
    }

    /**
     * Overwrites the final field {@link Block#blockState} with a new BlockState. The reason this method
     * exists is so I can dynamically create Block State properties. They have to be final for some reason
     * but that's <i>impossible</i> when the Block State is set in the constructor of {@link Block}, I think.
     *
     * @param block The block to give a new Block State
     * @param <T>
     */
    public static <T extends Block & IBlockProperties> void overwriteBlockState(T block)
    {
        try
        {                                                                                                                        // deobf        obf  srg_name
            ReflectionHelper.setPrivateValue(Block.class, block, createOverrideBlockState(block, block.getBlockStateProperties()), "blockState", "A", "field_176227_L");
        }
        catch (Exception e)
        {
            // Do nothing as it's MOST likely because the field does not exist in the current environment (obf/deobf)
            // e.printStackTrace();
        }
    }

    private static BlockStateContainer createOverrideBlockState(Block block, IProperty<?>... properties)
    {
        return new BlockStateContainer(block, properties);
    }

    public static class EventHandler
    {
        public EventHandler()
        {
        }

        @SubscribeEvent
        public void onPlayerClickBlock(PlayerInteractEvent.RightClickBlock event)
        {
            BlockPos pos = event.getPos();
            World world = event.getWorld();
            EntityPlayer player = event.getEntityPlayer();

            if (pos != null && world != null && player != null && world.isRemote && event.getHand() == EnumHand.MAIN_HAND && player.capabilities.isCreativeMode && event.getItemStack() == null) // Process on client only
            {
                try
                {
                    IBlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();
                    ResourceLocation rl = block.getRegistryName();

                    Arrays.asList(
                            TextFormatting.GOLD + "" + TextFormatting.BOLD  + "Block Properties:",
                            "  Owner: " + rl.getResourceDomain(),
                            "  Path: " + rl.getResourcePath(),
                            "  Material: " + Materials.toString(block.getMaterial(state)),
                            "  Hardness: " + ReflectionHelper.getPrivateValue(Block.class, block, "blockHardness"),
                            "  Resistance: " + ReflectionHelper.getPrivateValue(Block.class, block, "blockResistance"),
                            TextFormatting.GREEN + "" + TextFormatting.BOLD + "Render Properties:",
                            "  \"fullBlock\": " + ReflectionHelper.getPrivateValue(Block.class, block, "fullBlock"),
                            "  \"lightOpacity\": " + ReflectionHelper.getPrivateValue(Block.class, block, "lightOpacity"),
                            "  \"translucent\": " + ReflectionHelper.getPrivateValue(Block.class, block, "translucent"),
                            "  \"useNeighborBrightness\": " + ReflectionHelper.getPrivateValue(Block.class, block, "useNeighborBrightness"),
                            "  \"isFullyOpaque()\": " + block.isFullyOpaque(state),
                            "  \"isOpaqueCube()\": " + block.isOpaqueCube(state),
                            "  \"isVisuallyOpaque()\": " + block.isVisuallyOpaque()
                    ).stream()
                            .map(TextComponentString::new)
                            .collect(Collectors.toList())
                            .forEach(player::addChatComponentMessage);
                    event.setUseBlock(Event.Result.DENY);
                }
                catch (Exception e)
                {
                    Log.error("DEBUG: Something went wrong when retrieving properties from Block {}", world.getBlockState(pos).getBlock(), e);
                }
            }
        }
    }
}
