package com.kimfy.notenoughblocks.client.file.json.blockstate;

import com.google.gson.*;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

@Getter
class Variant
{
    /**
     * Name of a variant, which consists of the relevant block states separated by commas. A block with just one variant
     * uses "normal" as a name for its variant. Each variant can have one model or an array of models and contains their
     * properties. If set to an array, the model will randomly be chosen from the options given, with each option being
     * specified in separate subsidiary Object-tags.
     * Source: http://minecraft.gamepedia.com/Model
     *
     * Does not have a SerializedName as it is set through {@link Variant#setName} after it has been deserialied
     */
    private String name;
    private Model  model;

    /**
     * Is populated if the variant is an array of models. E.g. grass
     */
    private List<Model> models;

    public Variant() {}

    public Variant setModel(Model model)
    {
        this.model = model;
        return this;
    }

    public Variant setName(String name)
    {
        this.name = name;
        return this;
    }

    /**
     * @see Variant#deepCopy(Variant) for the javadoc
     * @return A deep copied {@link Variant}
     */
    public Variant deepCopy()
    {
        return Variant.deepCopy(this);
    }

    /**
     * Deep copies the given variant by deserializing it and serializing it through {@link Variant.Deserializer}
     * and {@link Variant.Serializer}. This has proven to be very fast so I'm keeping it.
     *
     * @param variant The variant to deepCopy
     * @return A deep copied {@link Variant}
     */
    public static Variant deepCopy(Variant variant)
    {
        return OneEightV2.GSON.fromJson(OneEightV2.GSON.toJsonTree(variant, Variant.class), Variant.class);
    }

    public static class Deserializer implements JsonDeserializer<Variant>
    {
        @Nonnull
        public Variant deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            Variant variant = new Variant();

            if (src.isJsonObject())
            {
                Model model = OneEightV2.GSON.fromJson(src, Model.class);
                variant.model = model;
            }
            else if (src.isJsonArray())
            {
                variant.models = new LinkedList<>();
                JsonArray models = src.getAsJsonArray();

                for (JsonElement element : models)
                {
                    Model model = OneEightV2.GSON.fromJson(element, Model.class);
                    variant.models.add(model);
                }
            }

            return variant;
        }
    }

    public static class Serializer implements JsonSerializer<Variant>
    {
        // Here we either return Variant as a JsonArray if it has a list of models or a simple Model Object
        @Nullable
        public JsonElement serialize(Variant variant, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonElement json = null;

            if (variant.getModel() != null)
            {
                json = OneEightV2.GSON.toJsonTree(variant.getModel(), Model.class);
            }
            else if (variant.getModels() != null)
            {
                JsonArray variants = new JsonArray();
                json = variants;
                variant.getModels().forEach(model -> variants.add(OneEightV2.GSON.toJsonTree(model, Model.class)));
            }

            return json;
        }
    }
}