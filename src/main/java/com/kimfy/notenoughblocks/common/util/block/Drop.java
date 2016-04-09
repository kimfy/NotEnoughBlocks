package com.kimfy.notenoughblocks.common.util.block;

import com.google.gson.*;
import com.kimfy.notenoughblocks.NotEnoughBlocks;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.MinecraftUtilities;
import com.kimfy.notenoughblocks.common.util.Utilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.Random;

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

    public Item getItem()
    {
        return this.item;
    }

    // JSON -> Drop
    public static class Deserializer implements JsonDeserializer<Drop>
    {
        @Override
        public Drop deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            if (element.isJsonPrimitive())
            {
                return toDrop(element.getAsString());
            }
            else if (element.isJsonObject())
            {
                return toDrop(element.getAsJsonObject());
            }
            else
            {
                NotEnoughBlocks.logger.error("Type {} is not supported as drop. It has to be either a String or Object", element.getClass());
            }
            return null;
        }

        /**
         * Performs a recursive search through the given {@link JsonElement} to populate
         * the given {@link BlockJson} with {@link Drop}s. This allows for very deeply
         * nested arrays in the JSON object.
         *
         * @param block The {@link BlockJson} to populate with drops
         * @param json The {@link JsonElement} to get values from
         */
        public static void walk(BlockJson block, JsonElement json)
        {
            if (json.isJsonPrimitive() || json.isJsonObject())
            {
                Drop drop = Utilities.GSON.fromJson(json, Drop.class);
                block.getDrop().add(drop);
            }
            else if (json.isJsonArray())
            {
                for (JsonElement entry : json.getAsJsonArray())
                {
                    walk(block, entry);
                }
            }
        }

        /**
         * Deserializes a {@link JsonObject} to a {@link Drop}
         */
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
            min = JsonUtils.getInt(model, "min", 0);
            max = JsonUtils.getInt(model, "max", 0);

            Item item = Item.itemRegistry.getObject(new ResourceLocation(modid, itemName));
            if (min == 0 && max == 0)
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
                ItemStack ret = MinecraftUtilities.strToItemStack(str);
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

    // Drop -> JSON
    public static class Serializer implements JsonSerializer<Drop>
    {
        @Override
        public JsonElement serialize(Drop src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject json = new JsonObject();
            String modid = src.getItem().getRegistryName().getResourceDomain();
            String name = src.getItem().getRegistryName().getResourcePath();
            int metadata = src.metadata;

            json.add("modid", new JsonPrimitive(modid));
            json.add("name", new JsonPrimitive(name));
            json.add("metadata", new JsonPrimitive(metadata));

            if (src.min == 0 && src.max == 0)
            {
                json.add("amount", new JsonPrimitive(src.amount));
            }
            else
            {
                json.add("min", new JsonPrimitive(src.min));
                json.add("max", new JsonPrimitive(src.max));
            }

            return json;
        }
    }
}
