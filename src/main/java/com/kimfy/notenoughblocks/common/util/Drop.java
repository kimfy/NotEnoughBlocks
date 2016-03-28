package com.kimfy.notenoughblocks.common.util;

import com.google.gson.JsonElement;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * <h1>Drop</h1>
 * <p>
 * A Drop is a wrapper for ItemStack that
 * will return an ItemStack with a random
 * size between Drop#min and Drop#max. If
 * these aren't set, it'll default to Drop#amount
 * which is set to 1.
 * </p>
 *
 * <h3>BlockJsonSerializer</h3>
 * -------------------------------------------<br>
 * +parseDrop(JsonElement e); List<Drop>
 *
 * <br>
 * <h3>MinecraftUtilities</h3>
 * -------------------------------------------<br>
 * +findItem(String modid, String name); Item
 * +getOwner(Item); String
 * +toItemStack(String str); ItemStack
 *
 * <br>
 * <h3>BlockJson</h3>
 * -------------------------------------------<br>
 * +setBlockDrops(List<Drop> list);
 * +drops; List<Drop>
 *
 * <br>
 * <h3>BlockAgent</h3>
 * -------------------------------------------<br>
 * +get(meta).getBlockDrops(); List<Drop>
 *
 * <br>
 * <h3>Drop</h3>
 * -------------------------------------------<br>
 * +getItemStack(); ItemStack
 */
public class Drop
{
    private Random random = new Random();
    private int min;
    private int max;
    private int amount = 1;
    private final ItemStack itemStack;

    public Drop(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    public Drop(ItemStack itemStack, int min, int max)
    {
        this.itemStack = itemStack;
        this.min = min;
        this.max = max;
    }

    public Drop(ItemStack itemStack, int amount)
    {
        this.itemStack = itemStack;
        this.amount = amount;
    }

    public ItemStack getItemStack()
    {
        itemStack.stackSize = getStackSize();
        return itemStack;
    }

    public int getStackSize()
    {
        return (max != 0) && (amount > 0) ? amount : (max > min) ? random.nextInt((max - min) + 1) + min : amount;
    }

    private String getItemStackName()
    {
        return this.itemStack.getDisplayName();
    }

    @Override
    public String toString()
    {
        return "Drop{" +
                "itemName=" + getItemStackName() +
                ", metadata=" + itemStack.getItemDamage() +
                '}';
    }

    public static class Deserializer
    {
        public static List<Drop> deserialize(JsonElement element)
        {
            List<Drop> drops = new ArrayList<>();

            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString())
            {
                drops.add(toDrop(element.getAsString()));
            }

            return drops;
        }

        public static Drop toDrop(String str)
        {
            String[] split = str.split("[:#]");
            System.out.println(Arrays.asList(split));
            int min, max;

            if (str.matches(MinecraftUtilities.MODID_NAME_META_MIN_MAX))
            {
                int[] amounts = getAmountFromMinimumMaximum(str);
                int meta = Integer.valueOf(split[2]);
                Item item = MinecraftUtilities.getItem(split[0], split[1]);

                return new Drop(new ItemStack(item, 1, meta), amounts[0], amounts[1]);
            }
            else if (str.matches(MinecraftUtilities.MODID_NAME_META_AMOUNT))
            {
                int amount = Integer.valueOf(split[3]);
                int meta = Integer.valueOf(split[2]);
                Item item = MinecraftUtilities.getItem(split[0], split[1]);

                return new Drop(new ItemStack(item, 1, meta), amount);
            }

            else if (str.matches(MinecraftUtilities.MODID_NAME_MIN_MAX))
            {
                int[] amounts = getAmountFromMinimumMaximum(str);
                Item item = MinecraftUtilities.getItem(split[0], split[1]);

                return new Drop(new ItemStack(item), amounts[0], amounts[1]);
            }
            else if (str.matches(MinecraftUtilities.MODID_NAME_AMOUNT))
            {
                int amount = Integer.valueOf(split[2]);
                Item item = MinecraftUtilities.getItem(split[0], split[1]);

                return new Drop(new ItemStack(item), amount);
            }

            else if (str.matches(MinecraftUtilities.NAME_META_MIN_MAX))
            {
                int[] amounts = getAmountFromMinimumMaximum(str);
                Item item = MinecraftUtilities.getItem(split[0]);

                return new Drop(new ItemStack(item, 1, Integer.valueOf(split[1])), amounts[0], amounts[1]);
            }
            else if (str.matches(MinecraftUtilities.NAME_META_AMOUNT))
            {
                Item item = MinecraftUtilities.getItem(split[0]);
                return new Drop(new ItemStack(item, 1, Integer.valueOf(split[1])), Integer.valueOf(split[2]));
            }

            else if (str.matches(MinecraftUtilities.NAME_MIN_MAX))
            {
                int[] amounts = getAmountFromMinimumMaximum(str);
                Item item = MinecraftUtilities.getItem(split[0]);

                return new Drop(new ItemStack(item), amounts[0], amounts[1]);
            }
            else if (str.matches(MinecraftUtilities.NAME_AMOUNT))
            {
                Item item = MinecraftUtilities.getItem(split[0]);
                return new Drop(new ItemStack(item), Integer.valueOf(split[1]));
            }
            else
            {
                return new Drop(MinecraftUtilities.toItemStack(str));
            }
        }

        private static int[] getAmountFromMinimumMaximum(String str)
        {
            String minMax = str.substring(str.lastIndexOf('#') + 1);
            String[] minMaxArr = minMax.split("-");
            int min = Integer.valueOf(minMaxArr[0]);
            int max = Integer.valueOf(minMaxArr[1]);

            return new int[] {min, max};
        }
    }
}
