package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.Log;
import com.kimfy.notenoughblocks.common.util.block.Drop;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BlockAgent<T extends Block & IBlockProperties> implements IBlockProperties
{
    private final T block;
    private final List<BlockJson> data;
    private final int blockCount;
    private final Shape SHAPE;

    public BlockAgent(T block, List<BlockJson> data)
    {
        this.block = block;
        this.blockCount = data.size();
        this.data = data;
        this.SHAPE = getModelBlock().getShape();
    }

    @Getter private boolean isBeaconBase;
    @Getter private boolean isStained;

    @Override
    public void setBeaconBaseable(boolean isBeaconBase)
    {
        this.isBeaconBase = isBeaconBase;
    }

    @Override
    public void setBlockStainable(boolean isStained)
    {
        this.isStained = isStained;
    }

    @Override
    public void setSlipperiness(float slipperiness)
    {
        this.block.slipperiness = slipperiness;
    }

    @Override
    public List<BlockJson> getData()
    {
        return this.data;
    }

    public BlockAgent getBlockAgent()
    {
        return this;
    }

    // TODO: Do not use reflection here
    @Override
    public void setBlockMaterial(Material material)
    {
        try
        {
            ReflectionHelper.setPrivateValue(Block.class, this.block, material, "field_149764_J", "x", "blockMaterial");
        }
        catch (Exception e) {} // Catching so it doesn't crash when trying to set "blockMaterial" in obfuscated env
    }

    // TODO: Maybe not do this and generify it upon block creation instead? I don't know how I'd do it
    @Override
    public void setBlockSoundType(SoundType soundType)
    {
        // Routed through because setBlockSoundType is protected (1.9.4)
        try
        {
            ReflectionHelper.setPrivateValue(Block.class, this.block, soundType, "field_149762_H", "v", "blockSoundType");
        }
        catch (Exception e) {} // Catching so it doesn't crash when trying to set "blockSoundType" in obfuscated env
    }

    public List<IProperty> blockStateProperties = new ArrayList<>();

    @Override
    public IProperty[] getBlockStateProperties()
    {
        return blockStateProperties.toArray(new IProperty[blockStateProperties.size()]);
    }

    @Override
    public void addBlockStateProperty(IProperty<?> property)
    {
        if (property == null)
            throw new NullPointerException("IProperty cannot be null");
        this.blockStateProperties.add(property);
    }

    @Override
    public void addBlockStateProperties(IProperty<?>... properties)
    {
        for (IProperty<?> p : properties) addBlockStateProperty(p);
    }

    @Override
    public Shape getShape()
    {
        return this.SHAPE;
    }

    private BlockJson modelBlock;

    public BlockJson getModelBlock()
    {
        if (data == null)
        {
            throw new NullPointerException("BlockAgent#getModelBlock(): data is null");
        }
        if (data.isEmpty())
        {
            throw new RuntimeException("BlockAgent#getModelBlock(): data is empty");
        }

        if (modelBlock == null)
        {
            modelBlock = get(0);
        }

        return modelBlock;
    }

    // TODO: Do not use reflection here
    @Override
    public void setUseNeighborBrightness(boolean useNeighborBrightness)
    {
        try
        {
            ReflectionHelper.setPrivateValue(Block.class, this.block, useNeighborBrightness, "field_149783_u", "p", "useNeighborBrightness");
        }
        catch (Exception e) {} // Catching so it doesn't crash when trying to set "useNeighborBrightness" in obfuscated env
    }

    // TODO: Do not use reflection here
    @Override
    public void setTranslucency(boolean translucent)
    {
        try
        {
            ReflectionHelper.setPrivateValue(Block.class, this.block, translucent, "field_149785_s", "n", "translucent");
        }
        catch (Exception e) {} // Catching so it doesn't crash when trying to set "translucent" in obfuscated env
    }

    @Override
    public void register(ResourceLocation registryName)
    {
        GameRegistry.register(block, registryName);
    }

    /* ========== Helpers ========== */

    public BlockJson get(int metadata)
    {
        if (metadata >= data.size())
        {
            Log.error("BlockAgent#get: cannot access data({}) because data()#size = {}", metadata, this.data.size());
            return get(0);
        }
        return data.get(metadata);
    }

    public BlockJson get(IBlockState state)
    {
        return get(this.block.damageDropped(state));
    }

    public int getBlockVariant(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return block.damageDropped(state);
    }

    /* ========== Delegated from Block ========== */

    private static java.util.Random RANDOM = new java.util.Random();

    @SuppressWarnings("unused")
    public Material getMaterial(IBlockState state)
    {
        return get(state).getMaterial();
    }

    @SuppressWarnings("unused")
    public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos)
    {
        return get(state).getHardness();
    }

    @SuppressWarnings("unused")
    public int getLightOpacity(IBlockState state)
    {
        return get(state).getLightOpacity();
    }

    @SuppressWarnings("unused")
    public int getLightValue(IBlockState state)
    {
        return (int)(15.0F * get(state).getLightLevel());
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public boolean isTranslucent(IBlockState state)
    {
        return get(state).isTranslucent();
    }

    @SuppressWarnings("unused")
    public boolean getUseNeighborBrightness(IBlockState state)
    {
        return get(state).isNeighborBrightness();
    }

    @SuppressWarnings("unused")
    public boolean canProvidePower(IBlockState state)
    {
        return get(state).isCanProvidePower();
    }


    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
    {
        IntStream.range(0, blockCount).forEach(metadata -> list.add(new ItemStack(item, 1, metadata)));
    }

    @SuppressWarnings("unused")
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return get(state).isSilkTouch();
    }

    @SuppressWarnings("unused")
    public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon)
    {
        return this.get(world.getBlockState(pos)).isBeaconBase();
    }

    @SuppressWarnings("unused")
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        List<ItemStack> ret = new ArrayList<>();
        int metadata = state.getBlock().damageDropped(state);

        if (!get(metadata).getDrop().isEmpty())
        {
            BlockJson model = get(metadata);
            ret.addAll(model.getDrop().stream().map(Drop::getItemStack).collect(Collectors.toCollection(ArrayList<ItemStack>::new)));
        }
        else // Fallback to Block#getDrops()
        {
            Random rand = world instanceof World ? ((World)world).rand : RANDOM;

            int count = block.quantityDropped(state, fortune, rand);

            IntStream.range(0, count).forEach(i -> {
                Item item = block.getItemDropped(state, rand, fortune);
                if (item != null)
                {
                    ret.add(new ItemStack(item, 1, block.damageDropped(state)));
                }
            });
        }
        return ret;
    }
}