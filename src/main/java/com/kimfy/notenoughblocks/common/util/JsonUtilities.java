package com.kimfy.notenoughblocks.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;

import javax.annotation.Nullable;
import java.util.Map;

public class JsonUtilities
{
    protected static final Gson GSON = new GsonBuilder().create();

    @Nullable
    public static <K, V> Map<K, V> getMap(@Nullable JsonObject json, @Nullable String propertyName, @Nullable Map<K, V> fallBack)
    {
        if (!JsonUtils.hasField(json, propertyName)) return fallBack;
        json = JsonUtils.getJsonObject(json, propertyName);
        return GSON.<Map<K, V>>fromJson(json, Map.class);
    }
}