package com.kimfy.notenoughblocks.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.kimfy.notenoughblocks.NotEnoughBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <h1>Drop</h1>
 * <p>
 * A Drop is a wrapper for Item that
 * will return an ItemStack with a random
 * size between Drop#min and Drop#max. If
 * these aren't set, it'll default to Drop#amount
 * which is set to 1.
 * </p>
 *
 * <h3>BlockJson.Serializer</h3>
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
    private transient Random random = new Random();
    private int min;
    private int max;
    private int amount = 1;
    private int metadata = 0;
    private final Item item;

    public Drop(Item item, int metadata, int amount, int min, int max)
    {
        this.item = item;
        this.metadata = metadata;
        this.amount = amount;
        this.min = min;
        this.max = max;
    }

    public Drop(ItemStack itemStack)
    {
        this.item = itemStack.getItem();
        this.metadata = itemStack.getItemDamage();
        this.amount = 1;
    }

    public ItemStack getItemStack()
    {
        return new ItemStack(item, getStackSize(), metadata);
    }

    public int getStackSize()
    {
        return (max == 0) ? amount : (max > min) ? random.nextInt((max - min) + 1) + min : amount;
    }

    private String getItemStackName()
    {
        return this.getItemStack().getDisplayName();
    }

    @Override
    public String toString()
    {
        return "Drop{" +
                "itemName=" + getItemStackName() +
                ", metadata=" + metadata +
                '}';
    }

    public static class Deserializer
    {
        public static Object deserialize(JsonElement json)
        {
            if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString())
            {
                return toDrop(json.getAsString());
            }
            else if (json.isJsonObject())
            {
                return toDrop(json.getAsJsonObject());
            }
            else if (json.isJsonArray())
            {
                List<Drop> ret = new ArrayList<>();
                for (JsonElement e : json.getAsJsonArray())
                {
                    ret.add((Drop) deserialize(e));
                }
                return ret;
            }
            else
            {
                NotEnoughBlocks.logger.error("Unsupported type: " + json.getClass());
            }
            return null;
        }

        public static List<Drop> deserializeList(JsonElement element)
        {
            List<Drop> drops = new ArrayList<>();
            Object drop = deserialize(element);
            if (drop instanceof Drop)
            {
                drops.add((Drop) drop);
            }
            else if (drop instanceof List)
            {
                drops.<Drop>addAll((List<Drop>) drop);
            }
            return drops;
        }

        public static Drop toDrop(JsonObject model)
        {
            if (!model.has("name"))
            {
                NotEnoughBlocks.logger.error("Error: drop {} is missing the name key", model.toString(), new JsonSyntaxException("Missing required members! Do not report this!"));
            }

            String modid, itemName;
            int metadata, min, max, amount;
            modid = JsonUtils.getString(model, "modid", "minecraft");
            itemName = JsonUtils.getString(model, "name");
            metadata = JsonUtils.getInt(model, "metadata", 0);
            amount = JsonUtils.getInt(model, "amount", 1);
            min = JsonUtils.getInt(model, "min", -1);
            max = JsonUtils.getInt(model, "max", -1);

            Item item = MinecraftUtilities.getItem(modid, itemName);
            if (min == -1 && max == -1)
            {
                return new Drop(item, metadata, amount, 0, 0);
            }
            return new Drop(item, metadata, 1, min, max);
        }

        public static Drop toDrop(String str)
        {
            String[] split = str.split("[:#]");
            int min, max;

            if (str.matches(MinecraftUtilities.MODID_NAME_META_MIN_MAX))
            {
                int[] amounts = getAmountFromMinimumMaximum(str);
                int meta = Integer.valueOf(split[2]);
                Item item = MinecraftUtilities.getItem(split[0], split[1]);

                return new Drop(item, meta, 1, amounts[0], amounts[1]);
            }
            else if (str.matches(MinecraftUtilities.MODID_NAME_META_AMOUNT))
            {
                int amount = Integer.valueOf(split[3]);
                int meta = Integer.valueOf(split[2]);
                Item item = MinecraftUtilities.getItem(split[0], split[1]);

                return new Drop(item, meta, amount, 0, 0);
            }

            else if (str.matches(MinecraftUtilities.MODID_NAME_MIN_MAX))
            {
                int[] amounts = getAmountFromMinimumMaximum(str);
                Item item = MinecraftUtilities.getItem(split[0], split[1]);

                return new Drop(item, 0, 1, amounts[0], amounts[1]);
            }
            else if (str.matches(MinecraftUtilities.MODID_NAME_AMOUNT))
            {
                int amount = Integer.valueOf(split[2]);
                Item item = MinecraftUtilities.getItem(split[0], split[1]);

                return new Drop(item, 0, amount, 0, 0);
            }

            else if (str.matches(MinecraftUtilities.NAME_META_MIN_MAX))
            {
                int[] amounts = getAmountFromMinimumMaximum(str);
                int meta = Integer.valueOf(split[1]);
                Item item = MinecraftUtilities.getItem(split[0]);

                return new Drop(item, meta, 1, amounts[0], amounts[1]);
            }
            else if (str.matches(MinecraftUtilities.NAME_META_AMOUNT))
            {
                Item item = MinecraftUtilities.getItem(split[0]);
                int meta = Integer.valueOf(split[1]);
                return new Drop(item, meta, Integer.valueOf(split[2]), 0, 0);
            }

            else if (str.matches(MinecraftUtilities.NAME_MIN_MAX))
            {
                int[] amounts = getAmountFromMinimumMaximum(str);
                Item item = MinecraftUtilities.getItem(split[0]);

                return new Drop(item, 0, 1, amounts[0], amounts[1]);
            }
            else if (str.matches(MinecraftUtilities.NAME_AMOUNT))
            {
                Item item = MinecraftUtilities.getItem(split[0]);
                return new Drop(item, 0, Integer.valueOf(split[1]), 0, 0);
            }
            else
            {
                ItemStack ret = MinecraftUtilities.toItemStack(str);
                return new Drop(ret);
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
