package com.kimfy.notenoughblocks.client.file.json.blockstate;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.kimfy.notenoughblocks.common.util.JsonUtilities;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Contains the properties of a model, if more than one model is used for the same variant. All specified models
 * alternate in the game.
 */
@Setter
@Getter
class Model
{
    /**
     * Specifies the path to the model file of the block, starting in assets/minecraft/models/block.
     */
    @SerializedName("model") private String model;

    /**
     * Rotation of the model on the x-axis in increments of 90 degrees.
     */
    @SerializedName("x") private int x;

    /**
     * Rotation of the model on the y-axis in increments of 90 degrees.
     */
    @SerializedName("y") private int y;

    /**
     * Can be true or false (default). Locks the rotation of the texture of a block, if set to true. This way the
     * texture will not rotate with the block when using the x and y-tags above.
     */
    @SerializedName("uvlock") private boolean uvLock;

    /**
     * Sets the probability of the model for being used in the game, defaults to 1 (=100%). If more than one model is
     * used for the same variant, the probability for each model will be calculated by dividing the sum of the weight
     * of each model by the weight of the corresponding model.
     */
    @SerializedName("weight") private int weight;

    /**
     * Texture map for the current model. Key values depends completely on what shape the current model is.
     */
    @SerializedName("textures") private Map<String, String> textures;

    public Model() {}

    public static class Deserializer implements JsonDeserializer<Model>
    {
        public Model deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            JsonObject json = src.getAsJsonObject();
            Model model = new Model();

            if (JsonUtils.hasField(json, "model")) model.setModel(JsonUtils.getString(json, "model", "null"));
            if (JsonUtils.hasField(json, "x")) model.setX(JsonUtils.getInt(json, "x", 0));
            if (JsonUtils.hasField(json, "y")) model.setY(JsonUtils.getInt(json, "y", 0));
            if (JsonUtils.hasField(json, "uvlock")) model.setUvLock(JsonUtils.getBoolean(json, "uvlock", false));
            if (JsonUtils.hasField(json, "weight")) model.setWeight(JsonUtils.getInt(json, "weight", 1));
            if (JsonUtils.hasField(json, "textures")) model.setTextures(JsonUtilities.getMap(json, "textures", null));

            return model;
        }
    }

    public static class Serializer implements JsonSerializer<Model>
    {
        public JsonElement serialize(Model model, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject json = new JsonObject();
            if (model.getModel() != null) json.add("model", new JsonPrimitive(model.getModel()));
            if (model.getX() > 0) json.add("x", new JsonPrimitive(model.getX()));
            if (model.getY() > 0) json.add("y", new JsonPrimitive(model.getY()));
            if (model.isUvLock()) json.add("uvlock", new JsonPrimitive(model.isUvLock()));
            if (model.getWeight() > 0) json.add("weight", new JsonPrimitive(model.getWeight()));
            if (model.getTextures() != null)
                json.add("textures", OneEightV2.GSON.toJsonTree(model.getTextures(), Map.class));

            return json;
        }
    }
}