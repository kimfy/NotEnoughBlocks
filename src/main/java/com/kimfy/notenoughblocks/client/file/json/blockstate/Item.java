package com.kimfy.notenoughblocks.client.file.json.blockstate;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;

@Getter
@Setter
class Item
{
    /**
     * Metadata value of the Item block to use when registering the model
     */
    @SerializedName("metadata") private int metadata;

    /**
     * Name of the variant property to use when registering the Item model for a block state
     */
    @SerializedName("variant") private String variant;

    public Item() {}

    public static class Deserializer implements JsonDeserializer<Item>
    {
        public Item deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            JsonObject json = src.getAsJsonObject();
            Item item = new Item();

            if (JsonUtils.hasField(json, "metadata")) item.setMetadata(JsonUtils.getInt(json, "metadata"));
            if (JsonUtils.hasField(json, "variant")) item.setVariant(JsonUtils.getString(json, "variant"));

            return item;
        }
    }

    public static class Serializer implements JsonSerializer<Item>
    {
        public JsonElement serialize(Item item, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject json = new JsonObject();
            if (item.getMetadata() >= 0) json.add("metadata", new JsonPrimitive(item.getMetadata()));
            if (item.getVariant() != null) json.add("variant", new JsonPrimitive(item.getVariant()));
            return json;
        }
    }
}