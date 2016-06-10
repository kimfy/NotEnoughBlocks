package com.kimfy.notenoughblocks.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonUtilities
{
    @Nullable
    public static Map<String, String> getMap(@Nonnull JsonObject json, @Nullable String propertyName, @Nullable Map<String, String> fallBack)
    {
        if (!JsonUtils.hasField(json, propertyName)) return fallBack;

        Map<String, String> ret = new LinkedHashMap<>();

        for (Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, propertyName).entrySet())
        {
            ret.put(entry.getKey(), entry.getValue().getAsString());
        }

        return ret;
    }
}