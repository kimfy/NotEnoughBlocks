package com.kimfy.notenoughblocks.common.file.json;

import com.google.gson.*;
import com.kimfy.notenoughblocks.common.util.JsonUtilities;
import com.kimfy.notenoughblocks.common.util.Log;
import com.kimfy.notenoughblocks.common.util.Utilities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Deserializer for JSON files
 */
public class ModJsonDeserializer implements JsonDeserializer
{
    @Override
    public Map<String, Object> deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject json = jsonElement.getAsJsonObject();
        Map<String, Object> ret = new HashMap<>(1);
        ret.put("blocks", getBlockList(json));
        return ret;
    }

    private List<BlockJson> getBlockList(JsonObject node)
    {
        List<BlockJson> ret = new ArrayList<>();

        if (JsonUtilities.hasField(node, "blocks"))
        {
            for (JsonElement e : JsonUtilities.getJsonArray(node, "blocks"))
            {
                JsonObject model = e.getAsJsonObject();
                BlockJson block = Utilities.GSON.fromJson(model, BlockJson.class);
                ret.add(block);
            }
        }
        else
        {
            Log.error("Malformed JSON: Missing blocks array - you should probably make one, as without one I won't be doing much");
        }

        return ret;
    }
}