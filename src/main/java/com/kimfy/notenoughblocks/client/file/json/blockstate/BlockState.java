package com.kimfy.notenoughblocks.client.file.json.blockstate;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.kimfy.notenoughblocks.common.util.FileUtilities;
import com.kimfy.notenoughblocks.common.util.Log;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.JsonUtils;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
class BlockState
{
    /**
     * Version of the Forge block state format to use. Defaults to 1 as we always want to fall back on version 1 and
     * not vanilla Minecraft's block state format because we're not always sure if we're using Forge features or not.
     *
     * @see <a href="http://mcforge.readthedocs.io/en/latest/blockstates/forgeBlockstates/">MinecraftForge wiki</a>
     */
    @SerializedName("forge_marker") private int forgeMarker = 1;

    /**
     * Registry name of the block this block state belongs to. This is used for registering the item block for this
     * block.
     */
    @SerializedName("block_name") private String blockName;

    /**
     * Defaults section for this block state.
     *
     * @see Default
     */
    @SerializedName("defaults") private Default defaults;

    /**
     * Holds a reference to all variants in this block state.
     */
    @SerializedName("variants") private List<Variant> variants;

    /**
     * Holds a reference to all item renders for the current block. The Item's inside are used for registering
     * item render stuff to the Model Loader.
     */
    @SerializedName("item_renders") private List<Item> itemRenders;

    public BlockState() {}

    /**
     * Deserializes the Block State using template gotten from the given Shape. The Block State is expected to be
     * located in assets/modid/blockstates/templates/ as "{@link Shape#toString}.json", E.g "cube.json"
     *
     * @param shape The shape to get a {@link BlockState} for
     * @return A {@link BlockState} parsed from a template file
     */
    public static BlockState fromShape(Shape shape)
    {
        return OneEightV2.GSON.fromJson(
                FileUtilities.getFileFromAssets(String.format("blockstates/templates/%s.json", shape.toString())),
                BlockState.class);
    }

    /**
     * @see BlockState#deepCopy(BlockState) for the javadoc
     * @return A deep copied {@link BlockState}
     */
    public BlockState deepCopy()
    {
        return BlockState.deepCopy(this);
    }

    /**
     * Deep copies the given block state by deserializing it and serializing it through {@link BlockState.Deserializer}
     * and {@link BlockState.Serializer}. This has proven to be very fast so I'm keeping it.
     *
     * @param blockState The block state to deepCopy
     * @return A deep copied {@link BlockState}
     */
    public static BlockState deepCopy(BlockState blockState)
    {
        return OneEightV2.GSON.fromJson(OneEightV2.GSON.toJsonTree(blockState, BlockState.class), BlockState.class);
    }

    public static class Deserializer implements JsonDeserializer<BlockState>
    {
        @Override
        public BlockState deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException
        {
            JsonObject json = src.getAsJsonObject();
            BlockState blockState = new BlockState();

            if (JsonUtils.hasField(json, "forge_marker"))
                blockState.setForgeMarker(JsonUtils.getInt(json, "forge_marker"));

            if (JsonUtils.hasField(json, "block_name"))
                blockState.setBlockName(JsonUtils.getString(json, "block_name"));

            if (JsonUtils.hasField(json, "defaults"))
                blockState.setDefaults(OneEightV2.GSON.fromJson(
                        JsonUtils.getJsonObject(json, "defaults"),
                        Default.class));

            if (JsonUtils.hasField(json, "variants"))
            {
                blockState.setVariants(new LinkedList<>());
                JsonObject variants = JsonUtils.getJsonObject(json, "variants");

                for (Map.Entry<String, JsonElement> entry : variants.entrySet())
                {
                    String variantName = entry.getKey();
                    JsonElement variantElement = entry.getValue();
                    Variant variant = OneEightV2.GSON.fromJson(variantElement, Variant.class).setName(variantName);
                    blockState.getVariants().add(variant);
                }
            }
            else
            {
                Log.warn("BlockState does not contain any variants");
            }

            if (JsonUtils.hasField(json, "item_renders"))
            {
                blockState.setItemRenders(new LinkedList<>());
                JsonArray items = JsonUtils.getJsonArray(json, "item_renders");
                items.forEach(item -> blockState.getItemRenders().add(OneEightV2.GSON.fromJson(item, Item.class)));
            }

            return blockState;
        }
    }

    public static class Serializer implements JsonSerializer<BlockState>
    {
        @Override
        public JsonElement serialize(BlockState blockState, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject json = new JsonObject();

            json.add("forge_marker", new JsonPrimitive(blockState.getForgeMarker()));

            if (blockState.getBlockName() != null)
                json.add("block_name", new JsonPrimitive(blockState.getBlockName()));

            if (blockState.getDefaults() != null)
            {
                json.add("defaults", OneEightV2.GSON.toJsonTree(blockState.getDefaults(), Default.class));
            }

            if (blockState.getVariants() != null)
            {
                JsonObject variants = new JsonObject();
                json.add("variants", variants);

                // Add each variant as a Key, Value. The value may be any type but must extend JsonElement.
                for (Variant variant : blockState.getVariants())
                {
                    JsonElement jsonVariant = OneEightV2.GSON.toJsonTree(variant, Variant.class);
                    variants.add(variant.getName(), jsonVariant);
                }
            }

            if (blockState.getItemRenders() != null)
            {
                JsonArray itemRenders = new JsonArray();
                json.add("item_renders", itemRenders);
                blockState.getItemRenders().forEach(item ->
                        itemRenders.add(OneEightV2.GSON.toJsonTree(item, Item.class)));
            }

            return json;
        }
    }
}