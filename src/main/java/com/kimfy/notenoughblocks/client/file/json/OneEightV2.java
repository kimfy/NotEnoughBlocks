package com.kimfy.notenoughblocks.client.file.json;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.kimfy.notenoughblocks.common.block.IBlockProperties;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.FileUtilities;
import com.kimfy.notenoughblocks.common.util.Log;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The purpose of this is class is to handle the creation of all blockstate json files. The files will end up in
 * ./resourcepacks/NotEnoughBlocks/assets/notenoughblocks/blockstates.
 * <p>
 * Each shape has it's own template blockstate file, this file is located in the mod's assets folder.
 * Expected behaviour:
 * 1. Iterate through all blocks that have been registered from NEB. Get the block's shape and get the template from
 * there.
 * 2. From the template, generate a new JsonObject and copy the variants and insert the correct information.
 * 3. Output file ends up in path mentioned above
 */
public class OneEightV2
{
    public static void load()
    {
        Log.info("OneEightV2 start");
        OneEightV2 oe = new OneEightV2();
        Log.info("OneEightV2 end");
    }

    private List<IBlockProperties> blocks = new ArrayList<>();

    public OneEightV2()
    {
        this.getBlocks();
        if (!this.blocks.isEmpty())
        {
            this.writeBlockStates();
            this.test();
        }
    }

    private void getBlocks()
    {
        for (Block block : Block.REGISTRY)
        {
            if (block != null && block.getRegistryName().getResourceDomain().equals(Constants.MOD_ID))
            {
                this.blocks.add((IBlockProperties) block);
            }
        }
    }

    private void writeBlockStates()
    {
        for (IBlockProperties iblock : blocks)
        {
            Block block = (Block) iblock;
            Shape shape = iblock.getShape();
            this.writeBlockStateFor(block, shape);
        }
    }

    private void test()
    {
        String fileGrass = FileUtilities.getFileFromAssets(new ResourceLocation(Constants.MOD_ID, "blockstates/templates/grass.json"));
        BlockState blockState = BlockState.GSON.fromJson(fileGrass, BlockState.class);
        Log.info(BlockState.GSON.toJson(blockState, BlockState.class));
        Minecraft.getMinecraft().displayCrashReport(null);
    }

    private void writeBlockStateFor(Block block, Shape shape)
    {
    }

    private static class BlockState
    {
        public static final Gson GSON = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .registerTypeAdapter(BlockState.class, new BlockState.Deserializer())
                .registerTypeAdapter(BlockState.class, new BlockState.Serializer())
                .registerTypeAdapter(Variant.class, new Variant.Deserializer())
                .create();

        /**
         * Holds a reference to all variants in the current BlockState. Can be null as we may support "multipart" in
         * the future when Forge updates it's BlockState format to V2.
         */
        @Nullable private List<Variant> variants;

        public BlockState() {}

        private static class Variant
        {
            @Nullable private String name;
            @Nullable private List<Model> models;
            private String model;
            private int x, y;
            private boolean uvLock;
            private Map<String, String> textures;

            public Variant() {}

            public void setName(@Nonnull String name)
            {
                this.name = name;
            }

            private static class Deserializer implements JsonDeserializer<Variant>
            {
                @Override
                public Variant deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
                {
                    Variant variant = new Variant();
                    if (src.isJsonObject()) // A single model
                    {
                        JsonObject json = src.getAsJsonObject();
                        variant.model   = JsonUtils.getString(json, "model");
                        variant.x       = JsonUtils.getInt(json, "x", 0);
                        variant.y       = JsonUtils.getInt(json, "y", 0);
                        variant.uvLock  = JsonUtils.getBoolean(json, "uvLock", false);
                        //variant.textures = JsonUtilities.getMap(json, "textures", null);
                        variant.textures = JsonUtils.hasField(json, "textures") ? GSON.fromJson(json.get("textures").getAsJsonObject(), Map.class) : null;
                    }
                    else if (src.isJsonArray()) // Array of models
                    {
                        variant.models = new ArrayList<>();
                        for (JsonElement mElement : src.getAsJsonArray())
                        {
                            variant.models.add(GSON.fromJson(mElement, Model.class));
                        }
                    }
                    return variant;
                }
            }
        }

        private static class Model implements Serializable
        {
            @SerializedName("model")
            private String modelPath;
            private int x, y, z;
            private boolean uvLock = false;
            private int weight = 1;
            private Map<String, String> textures;

            public Model() {}
        }

        private static class Deserializer implements JsonDeserializer<BlockState>
        {
            @Override
            public BlockState deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
            {
                JsonObject json = src.getAsJsonObject();
                BlockState blockState = new BlockState();

                if (JsonUtils.hasField(json, "variants"))
                {
                    this.getVariants(blockState, JsonUtils.getJsonObject(json, "variants"));
                }

                return blockState;
            }

            private void getVariants(BlockState blockState, JsonObject json)
            {
                blockState.variants = new ArrayList<>();
                for (Map.Entry<String, JsonElement> entry : json.entrySet())
                {
                    JsonElement element = entry.getValue();
                    Variant variant = GSON.fromJson(element, Variant.class);
                    variant.setName(entry.getKey());
                    blockState.variants.add(variant);
                }
            }
        }

        private static class Serializer implements JsonSerializer<BlockState>
        {
            @Override
            public JsonElement serialize(BlockState src, Type typeOfSrc, JsonSerializationContext context)
            {
                JsonObject blockState = new JsonObject();
                if (src.variants != null)
                {
                    JsonObject variants = new JsonObject();
                    for (Variant variant : src.variants)
                    {
                        if (variant.models == null)
                        {
                            JsonObject jsonVariant = new JsonObject();
                            jsonVariant.add("model", new JsonPrimitive(variant.model));
                            if (variant.x != 0) jsonVariant.add("x", new JsonPrimitive(variant.x));
                            if (variant.y != 0) jsonVariant.add("y", new JsonPrimitive(variant.y));
                            if (variant.uvLock) jsonVariant.add("uvLock", new JsonPrimitive(variant.uvLock));
                            if (variant.textures != null) jsonVariant.add("textures", GSON.toJsonTree(variant.textures));
                            variants.add(variant.name, jsonVariant);
                        }
                        else
                        {
                            JsonArray jsonModels = new JsonArray();
                            for (Model model : variant.models)
                            {
                                JsonObject jsonModel = new JsonObject();
                                jsonModel.add("model", new JsonPrimitive(model.modelPath));
                                if (model.x != 0) jsonModel.add("x", new JsonPrimitive(model.x));
                                if (model.y != 0) jsonModel.add("y", new JsonPrimitive(model.y));
                                if (model.z != 0) jsonModel.add("z", new JsonPrimitive(model.z));
                                if (model.uvLock) jsonModel.add("uvLock", new JsonPrimitive(model.uvLock));
                                if (model.weight != 1) jsonModel.add("weight", new JsonPrimitive(model.weight));
                                if (model.textures != null) jsonModel.add("textures", GSON.toJsonTree(model.textures));
                                jsonModels.add(jsonModel);
                            }
                            variants.add(variant.name, jsonModels);
                        }
                    }
                    blockState.add("variants", variants);
                }
                return blockState;
            }
        }
    }
}