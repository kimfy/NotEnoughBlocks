package com.kimfy.notenoughblocks.common.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;

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
            return  itemExists(rl) ? Item.itemRegistry.getObject(rl) : null;
        }
        return null;
    }

    public static Item getItem(String name)
    {
        return getItem("minecraft", name);
    }

    public static boolean itemExists(ResourceLocation rl)
    {
        return GameData.getItemRegistry().containsKey(rl);
    }
}
