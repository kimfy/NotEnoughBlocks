package com.kimfy.notenoughblocks.common.file.json;

import com.google.gson.*;
import com.kimfy.notenoughblocks.NotEnoughBlocks;
import com.kimfy.notenoughblocks.common.util.Drop;
import com.kimfy.notenoughblocks.common.util.Version;
import net.minecraft.client.Minecraft;
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
                    case DEVELOPER: // TODO: Implement List of Object, Primitive and Object/Primitive around
                    {
                        if (JsonUtils.hasField(model, "drop") || JsonUtils.hasField(model, "drops"))
                        {
                            block.drop(Drop.Deserializer.deserialize(model.get("drop")));
                            NotEnoughBlocks.logger.info("Block {} has List<Drop> field {}", block.getDisplayName(), block.getDrop());
                            NotEnoughBlocks.logger.info("Block {} has \"drop\" section: {}", block.getDisplayName(), model.get("drop"));
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
}