package com.kimfy.notenoughblocks.common.file.json;

import com.google.gson.*;
import com.kimfy.notenoughblocks.NotEnoughBlocks;
import com.kimfy.notenoughblocks.common.util.Version;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Type;
import java.util.*;

public class ModJsonDeserializer implements JsonDeserializer
{
    @Override
    public Map<String, Object> deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject json = jsonElement.getAsJsonObject();
        Map<String, Object> ret = new HashMap<>(1);
        NotEnoughBlocks.logger.info("Deserializing JSON...");

        Version version = Version.valueOf(JsonUtils.getString(json, "version", "default").toUpperCase());
        ret.put("blocks", getBlockList(json, version));
        return ret;
    }

    private List<BlockJson> getBlockList(JsonObject node, Version version)
    {
        List<BlockJson> ret = new ArrayList<>();
        Gson gson = new Gson();

        if (JsonUtils.hasField(node, "blocks"))
        {
            for (JsonElement e : JsonUtils.getJsonArray(node, "blocks"))
            {
                JsonObject model = e.getAsJsonObject();
                BlockJson block = gson.fromJson(model, BlockJson.class);

                /**
                 * Switch on the version if there are any values from the JSON that should
                 * be treated differently in certain versions than simply getting and setting
                 * it's value. An example of a k/v that will be treated differently is when the
                 * user specifies a drop - this has to be parsed before being set in the Block.
                 *
                 * Useful for experimental features.
                 */
                switch (version)
                {
                    case DEVELOPER:
                    {
                        if (JsonUtils.hasField(model, "drop"))
                        {
                            block.drop(parseBlockDrops(model.get("drop")));
                            NotEnoughBlocks.logger.info("Block {} has \"drop\" section: {}", block.getDisplayName(), block.getDrop());
                        }
                        break;
                    }
                    default:
                        break;
                }
                ret.add(block);
            }
        }
        else
        {
            NotEnoughBlocks.logger.error("Malformed JSON: Missing blocks array - you should probably make one, as without one I won't be doing much");
        }

        return ret;
    }

    private List<ItemStack> parseBlockDrops(JsonElement node)
    {
        List<ItemStack> drops = new ArrayList<>();
        if (node.isJsonArray())
        {
            for (JsonElement e : node.getAsJsonArray())
            {
                if (e.isJsonObject()) drops.add(getItemStack(e.getAsJsonObject()));
                if (e.isJsonPrimitive()) drops.add(getItemStack(e.getAsJsonPrimitive()));
            }
        }
        else if (node.isJsonObject())
        {
            drops.add(getItemStack(node.getAsJsonObject()));
        }
        else if (node.isJsonPrimitive())
        {
            drops.add(getItemStack(node.getAsJsonPrimitive()));
        }
        else
        {
            NotEnoughBlocks.logger.error("ERROR: \"drop\": Expected Array, Object or String for Block {}! Do not report this, make sure the file is valid! Refer to the wiki for more information!", node.getAsJsonObject().get("displayName").getAsString());
        }
        return drops;
    }

    /**
     * Converts a {@link JsonPrimitive#getAsJsonPrimitive()} to an {@link ItemStack}
     *
     * @param node The {@link JsonPrimitive} object of the value
     * @return The {@link ItemStack} from the parsed information or <code>Item.getItemFromBlock(Blocks.dirt)</code>
     */
    private ItemStack getItemStack(JsonPrimitive node)
    {
        String content = node.getAsString();
        String[] split = content.split(":#");
        String modId, item;
        int metadata, amount;
        ItemStack ret;

        if (content.matches("^([A-z0-9].*):([A-z0-9].*):(\\d.*)#(\\d){1,2}-(\\d){1,2}$"))
        {
            // modid:item:0#0-9
            modId    = split[0];
            item     = split[1];
            metadata = Integer.valueOf(split[2]);
            amount   = getRandomIntInRange(Integer.valueOf(split[3]), Integer.valueOf(split[4]));
        }
        else if (content.matches("^([A-z0-9].*):([A-z0-9].*):(\\d.*)#\\d+$"))
        {
            // modid:item:0#1
            modId    = split[0];
            item     = split[1];
            metadata = Integer.valueOf(split[2]);
            amount   = Integer.valueOf(split[3]);
        }
        else if (content.matches("^([A-z0-9].*):([A-z0-9].*):(\\d)+$"))
        {
            // modid:item:0
            modId    = split[0];
            item     = split[1];
            metadata = Integer.valueOf(split[2]);
            amount = 1;
        }
        else if (content.matches("^([A-z0-9]+):([A-z0-9]+)$"))
        {
            // modid:item
            modId = split[0];
            item  = split[1];
            metadata = 0;
            amount = 1;
        }
        else if (content.matches("^[A-z0-9]+$"))
        {
            // item
            modId = "minecraft";
            item = split[0];
            metadata = 0;
            amount = 1;
        }
        else if (content.matches("^([A-z0-9]+):([0-9]+)#(\\d+)-(\\d+)$"))
        {
            // item:0#0-10
            modId = "minecraft";
            item     = split[0];
            metadata = Integer.valueOf(split[1]);
            amount   = getRandomIntInRange(Integer.valueOf(split[2]), Integer.valueOf(split[3]));
        }
        else if (content.matches("^([A-z0-9]+):(\\d+)#(\\d+)$"))
        {
            // item:0#9
            modId = "minecraft";
            item     = split[0];
            metadata = Integer.valueOf(split[1]);
            amount   = Integer.valueOf(split[2]);
        }
        else
        {
            NotEnoughBlocks.logger.error("Could not convert String {} to an Item! Make sure it is properly formatted. Refer to the wiki for valid formats! Setting Item to minecraft:dirt:0#1", content);
            modId    = "minecraft";
            item     = "dirt";
            metadata = 0;
            amount   = 1;
        }

        Item toFind = GameRegistry.findItem(modId, item);
        if (toFind == null)
        {
            NotEnoughBlocks.logger.error("Couldn't find Item {}, defaulting to minecraft:dirt:0#1!", content);
            toFind = Item.getItemFromBlock(net.minecraft.init.Blocks.dirt);
        }
        return new ItemStack(toFind, amount, metadata);
    }

    /**
     * Convert a {@link JsonObject} to an {@link ItemStack}. Expected JSON: <br>
     * <pre>
     * {
     *     "modid": "minecraft",
     *     "name": "crafting_table",
     *     "metadata": 0,
     *     "amount": 1,
     *     "min": 3,
     *     "max": 9
     * }
     * </pre>
     * JsonObject params:
     * <ul>
     *     <li>modid: the modid/owner of the item. Defaults to 'minecraft' if no modid is present</li>
     *     <li>name: the name of the item. Defaults to 'dirt'</li>
     *     <li>metadata: the metadata of the item. Defaults to 0</li>
     *     <li>amount: the size of the ItemStack. Defaults to 1</li>
     *     <li>min: the minimum size of the return ItemStack</li>
     *     <li>max: the maximum size of the return ItemStack</li>
     * </ul>
     * @param node
     * @return
     */
    private ItemStack getItemStack(JsonObject node)
    {
        String modId = JsonUtils.getString(node, "modid", "minecraft");
        String name  = JsonUtils.getString(node, "name", "dirt");
        int metadata = JsonUtils.getInt(node, "metadata", 0);
        int amount   = getDropAmount(node);

        Item toFind = GameRegistry.findItem(modId, name);

        if (toFind != null)
        {
            return new ItemStack(toFind, amount, metadata);
        }
        else
        {
            NotEnoughBlocks.logger.error("Could not find Item with name {} and metadata {} from mod {}. Double check your JSON for errors!", name, metadata, modId);
            return null;
        }
    }

    private int getDropAmount(JsonObject node)
    {
        return JsonUtils.hasField(node, "amount") ? JsonUtils.getInt(node, "amount", 1) : JsonUtils.hasField(node, "min") && JsonUtils.hasField(node, "max") ? getRandomIntInRange(JsonUtils.getInt(node, "min"), JsonUtils.getInt(node, "max")) : 1;
    }

    private int getRandomIntInRange(int min, int max)
    {
        if (min >= max)
        {
            NotEnoughBlocks.logger.error("ERROR: min {} cannot be higher or equal to max {}. Defaulting to 1", min, max);
            return 1;
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}