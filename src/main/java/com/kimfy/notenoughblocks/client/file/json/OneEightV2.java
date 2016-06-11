package com.kimfy.notenoughblocks.client.file.json;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.kimfy.notenoughblocks.common.block.IBlockProperties;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.FileUtilities;
import com.kimfy.notenoughblocks.common.util.JsonUtilities;
import com.kimfy.notenoughblocks.common.util.Log;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import net.minecraft.block.Block;
import net.minecraft.util.JsonUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

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
    /** Contains the parsed blockstate jsons for all shapes that exist */
    private Map<Shape, BlockState> shapes = new HashMap<>();

    public OneEightV2()
    {
        this.getBlocks();
        if (!this.blocks.isEmpty())
        {
            this.populateShapesTable();
            this.writeBlockStates();
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

    private void populateShapesTable()
    {
        for (Shape shape : blocks.stream().map(IBlockProperties::getShape).collect(Collectors.toList()))
        {
            if (this.shapes.containsKey(shape))
                continue;
            this.shapes.put(shape, BlockState.fromShape(shape));
        }
    }

    private void writeBlockStates()
    {
        for (IBlockProperties iblock : blocks)
        {
            Shape shape = iblock.getShape();
            if (((Block) iblock).getRegistryName().getResourcePath().contains("slab_double"))
            {
                // FIXME: This CAN return null if no CUBE blocks have been registered/parsed
                this.createBlockStateFor(iblock, Shape.CUBE, this.shapes.get(Shape.CUBE));
            }
            this.createBlockStateFor(iblock, shape, this.shapes.get(shape).copy());
        }
    }

    private void createBlockStateFor(@Nonnull IBlockProperties block, @Nonnull Shape shape, @Nonnull BlockState blockState)
    {
        List<BlockState.Variant> originalVariants = blockState.variants.stream()
                .map(v -> v.copy())
                .collect(Collectors.toList());
        blockState.variants.clear();

        Map<String, String> textureMap;
        if (shape.isMetadataBlock())
        {
            // Populate defaults section with textures
            BlockState.Default def = new BlockState.Default();
            def.textures = block.getData().get(0).getTextureMap();
        }

        for (int i = 0; i < block.getData().size(); i++)
        {
            final int metadata = i;
            // For each subBlock, insert a new set of Variants into the current blockstate
            // ^-- need a way to copy the required Variants for each metadata... Hmmm
            // Replace #metadata in all keys with the iterator(i)/metadata
            blockState.variants.addAll(
                    originalVariants.stream()
                            .map(v -> v.copy())
                            .map(v -> v.setName(v.name.replace("#metadata", String.valueOf(metadata))))
                            .collect(Collectors.toList()));

            // Populate all, every single variant and model with textures
            blockState.variants.forEach(v -> v.setTextures(block.getData().get(metadata).getTextureMap()));
        }
        originalVariants.clear();
        // Serialize blockstate into json, write to file
        this.writeBlockStateToFile(blockState, ((Block) block).getRegistryName().getResourcePath());
    }

    private static final File BLOCK_STATE_FOLDER = new File(String.format("resourcepacks/%s/assets/%s/blockstates/", Constants.MOD_NAME, Constants.MOD_ID));

    private void writeBlockStateToFile(@Nonnull BlockState blockState, @Nonnull String blockName)
    {
        String fileName = String.format("%s.json", blockName);
        File output = new File(String.format("%s/%s", BLOCK_STATE_FOLDER.getAbsolutePath(), fileName));
        String content = BlockState.GSON.toJson(blockState, BlockState.class);
        Log.info("Processing {}... Content \n{}", output.getAbsolutePath(), content);
        FileUtilities.write(output, content);
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

        /** Defaults to 1 because we always want to use Forge's blockstate version **/
        private int forge_marker = 1;
        /**
         * Holds a reference to all variants in the current BlockState. Can be null as we may support "multipart" in
         * the future when Forge updates it's BlockState format to V2.
         */
        @Nullable protected List<Variant> variants;
        @Nullable protected Default defaults;

        public BlockState() {}

        public static BlockState fromShape(Shape shape)
        {
            return GSON.fromJson(
                    FileUtilities.getFileFromAssets(String.format("blockstates/templates/%s.json", shape)),
                    BlockState.class);
        }

        public BlockState copy()
        {
            return copy(this);
        }

        public static BlockState copy(BlockState blockState)
        {
            // ¯\_(ツ)_/¯
            return GSON.fromJson(GSON.toJson(blockState, BlockState.class), BlockState.class);
        }

        private static class Default implements Serializable
        {
            @Nullable private Map<String, String> textures;

            public Default() {}

            public void setTextures(Map<String, String> textures)
            {
                this.textures = textures;
            }
        }

        private static class Variant
        {
            @Nullable private String name;
            @Nullable private List<Model> models;
            private String model;
            private int x, y;
            private boolean uvLock;
            private Map<String, String> textures;

            public Variant() {}

            public Variant setTextures(Map<String, String> textures)
            {
                if (this.models != null)
                {
                    this.models.forEach(model -> model.textures = this.prefixTextureMap(textures));
                    return this;
                }
                this.textures = textures;
                return this;
            }

            public Variant setName(@Nonnull String name)
            {
                this.name = name;
                return this;
            }

            public Variant copy()
            {
                return copy(this);
            }

            public static Variant copy(Variant variant)
            {
                // ¯\_(ツ)_/¯
                return GSON.fromJson(GSON.toJson(variant, Variant.class), Variant.class).setName(variant.name);
            }

            private Map<String, String> prefixTextureMap(Map<String, String> textureMap)
            {
                Map<String, String> ret = new LinkedHashMap<>(textureMap.size());

                for (Map.Entry<String, String> entry : textureMap.entrySet())
                {
                    String side = entry.getKey();
                    String texture = entry.getValue();

                    ret.put(side, !texture.contains(":") ? Constants.MOD_ID + ":blocks/" + texture : texture);
                }

                return ret;
            }

            private static class Deserializer implements JsonDeserializer<Variant>
            {
                @Override
                public Variant deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
                {
                    Variant variant = new Variant();
                    if (src.isJsonObject()) // A single model
                    {
                        JsonObject json  = src.getAsJsonObject();
                        variant.model    = JsonUtils.getString(json, "model"); // Crashes when deserializing grass.json
                        variant.x        = JsonUtils.getInt(json, "x", 0);
                        variant.y        = JsonUtils.getInt(json, "y", 0);
                        variant.uvLock   = JsonUtils.getBoolean(json, "uvlock", false);
                        variant.textures = JsonUtilities.<String, String>getMap(json, "textures", null);
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
            private String model;
            private int x, y, z;
            @SerializedName("uvlock")
            private boolean uvLock = false;
            @SerializedName("weight")
            private int weight = 1;
            @SerializedName("textures")
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
                blockState.forge_marker = JsonUtils.getInt(json, "forge_marker", 1);

                if (JsonUtils.hasField(json, "variants"))
                {
                    this.getVariants(blockState, JsonUtils.getJsonObject(json, "variants"));
                }

                return blockState;
            }

            private void getVariants(BlockState blockState, JsonObject json)
            {
                blockState.variants = new LinkedList<>();
                for (Map.Entry<String, JsonElement> entry : json.entrySet())
                {
                    JsonElement element = entry.getValue();
                    Variant variant = GSON.fromJson(element, Variant.class);
                    Log.info("Deserializing variant {} ", entry.getKey());
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
                blockState.addProperty("forge_marker", src.forge_marker);
                if (src.defaults != null && src.defaults.textures != null)
                {
                    blockState.add("defaults", GSON.toJsonTree(src.defaults, Default.class)); // TODO TEST THIS
                }

                if (src.variants != null)
                {
                    JsonObject variants = new JsonObject();
                    for (Variant variant : src.variants)
                    {
                        // If true, will set the variant to an Object
                        if (variant.models == null)
                        {
                            JsonObject jsonVariant = new JsonObject();
                            jsonVariant.add("model", new JsonPrimitive(variant.model));
                            if (variant.x != 0) jsonVariant.add("x", new JsonPrimitive(variant.x));
                            if (variant.y != 0) jsonVariant.add("y", new JsonPrimitive(variant.y));
                            if (variant.uvLock) jsonVariant.add("uvlock", new JsonPrimitive(variant.uvLock));
                            if (variant.textures != null) jsonVariant.add("textures", GSON.toJsonTree(variant.textures));
                            variants.add(variant.name, jsonVariant);
                        }
                        else // If true, will set variant to an array, and fill array with Objects of Model
                        {
                            JsonArray jsonModels = new JsonArray();
                            for (Model model : variant.models)
                            {
                                JsonObject jsonModel = new JsonObject();
                                jsonModel.add("model", new JsonPrimitive(model.model));
                                if (model.x != 0) jsonModel.add("x", new JsonPrimitive(model.x));
                                if (model.y != 0) jsonModel.add("y", new JsonPrimitive(model.y));
                                if (model.z != 0) jsonModel.add("z", new JsonPrimitive(model.z));
                                if (model.uvLock) jsonModel.add("uvlock", new JsonPrimitive(model.uvLock));
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