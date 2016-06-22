package com.kimfy.notenoughblocks.client.file.json.blockstate;

import com.google.gson.*;
import com.kimfy.notenoughblocks.common.block.IBlockProperties;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.Log;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public final class OneEightV2
{
    public static void load()
    {
        OneEightV2 oe = new OneEightV2();
    }

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(BlockState.class, new BlockState.Deserializer())
            .registerTypeAdapter(BlockState.class, new BlockState.Serializer())
            .registerTypeAdapter(Variant.class, new Variant.Deserializer())
            .registerTypeAdapter(Variant.class, new Variant.Serializer())
            .registerTypeAdapter(Model.class, new Model.Deserializer())
            .registerTypeAdapter(Model.class, new Model.Serializer())
            .registerTypeAdapter(Item.class, new Item.Deserializer())
            .registerTypeAdapter(Item.class, new Item.Serializer())
            .registerTypeAdapter(Default.class, new Default.Deserializer())
            .registerTypeAdapter(Default.class, new Default.Serializer())
            .create();

    public OneEightV2()
    {
        this.getBlocks();
        if (!this.blocks.isEmpty())
        {
            BLOCK_STATE_FOLDER.mkdirs();
            this.populateShapesTable();
            this.writeBlockStates();
            this.registerItemModels();
        }
    }

    private void registerItemModels()
    {
        JsonObject src;
        for (File file : BLOCK_STATE_FOLDER.listFiles())
        {
            src = this.parseFile(file);
            if (src == null)
            {
                Log.error("Error when parsing {}. Report to mod author!", file.getAbsolutePath());
                continue;
            }

            String blockName = JsonUtils.getString(src, "block_name");
            List<Item> items = this.getItemRenders(src);
            items.forEach(i -> this.registerItemModel(blockName, i));
        }
    }

    @Nullable
    private JsonObject parseFile(File file)
    {
        String content = null;
        try
        {
            content = FileUtils.readFileToString(file);
            JsonParser parser = new JsonParser();
            return parser.parse(content).getAsJsonObject();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private List<Item> getItemRenders(JsonObject src)
    {
        List<Item> itemRenders = new LinkedList<>();
        for (JsonElement e : JsonUtils.getJsonArray(src, "item_renders"))
        {
            itemRenders.add(GSON.fromJson(e, Item.class));
        }
        return itemRenders;
    }

    /**
     * Registers a custom Model Resource Location for the given block.
     *
     * @param blockName The name of the block to register an item block model for.
     * @param item Item parsed from the block state this block came from.
     */
    private void registerItemModel(String blockName, Item item)
    {
        Block block = Block.REGISTRY.getObject(new ResourceLocation(Constants.MOD_ID, blockName));
        if (block == Blocks.AIR) return;
        ModelLoader.setCustomModelResourceLocation(
                net.minecraft.item.Item.getItemFromBlock(block),
                item.getMetadata(),
                new ModelResourceLocation(String.format("%s:%s", Constants.MOD_ID, blockName), item.getVariant()));
    }
    private List<IBlockProperties> blocks = new ArrayList<>();

    /** Contains the parsed blockstate jsons for all shapes that exist */
    private Map<Shape, BlockState> shapes = new HashMap<>();

    private void getBlocks()
    {
        Block.REGISTRY.forEach(block -> {
            if (block.getRegistryName().getResourceDomain().equals(Constants.MOD_ID))
                this.blocks.add((IBlockProperties) block);
        });
    }

    private static final Comparator<Shape> ALPHABETICAL = (s1, s2) -> s1.toString().compareTo(s2.toString());

    /**
     * Parses all templates for each shape gotten from every block in {@link OneEightV2#blocks}
     */
    private void populateShapesTable()
    {
        List<Shape> shapes = blocks.stream().map(IBlockProperties::getShape).collect(Collectors.toList());
        shapes.sort(ALPHABETICAL);

        for (Shape shape : shapes)
        {
            if (this.shapes.containsKey(shape))
                continue;

            BlockState blockState = BlockState.fromShape(shape);
            this.shapes.put(shape, blockState);
        }
    }

    private void writeBlockStates()
    {
        for (IBlockProperties iblock : this.blocks)
        {
            Shape shape = iblock.getShape();
            if (this.blockStateExists(((Block) iblock))) continue;
            this.createBlockStateFor(iblock, shape, this.shapes.get(shape).deepCopy());
        }
    }

    private boolean blockStateExists(Block block)
    {
        String blockName = block.getRegistryName().getResourcePath();
        return Arrays.stream(BLOCK_STATE_FOLDER.list())
                .anyMatch(f -> f.equals(String.format("%s.%s", blockName, "json")));
    }

    private void createBlockStateFor(IBlockProperties block, Shape shape, BlockState blockState)
    {
        List<Variant> templateVariants = this.getTemplateVariantsFor(blockState);
        blockState.getVariants().clear();
        blockState.setItemRenders(new LinkedList<>());
        blockState.setBlockName(((Block) block).getRegistryName().getResourcePath());

        boolean isMetadataBlock = shape.isMetadataBlock();
        for (int i = 0; i < block.getData().size(); i++)
        {
            final int metadata = i;
            // Copy template variants and add them to the blockState
            List<Variant> variants = this.copyTemplateVariants(templateVariants);
            blockState.getVariants().addAll(variants);

            // Edit variant names so #metadata becomes the actual metadata
            if (isMetadataBlock)
            {
                variants.forEach(variant -> {
                    String name = variant.getName().replace("#metadata", String.valueOf(metadata));
                    variant.setName(name);
                });
            }
            this.setTextureMap(block, blockState, variants, metadata);
            this.setItemRenders(shape, blockState, metadata);
        }
        this.writeBlockStateToFile(blockState, ((Block) block).getRegistryName());
    }

    private List<Variant> getTemplateVariantsFor(BlockState blockState)
    {
        return blockState.getVariants().stream()
                .map(variant -> variant.deepCopy())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private void setItemRenders(Shape shape, BlockState blockState, int metadata)
    {
        blockState.getItemRenders().add(new Item()
                .setMetadata(metadata)
                .setVariant(shape.isMetadataBlock() ? String.format("item,metadata=%s", metadata) : "item"));
    }

    private List<Variant> copyTemplateVariants(List<Variant> variants)
    {
        return variants.stream().map(variant -> variant.deepCopy()).collect(Collectors.toCollection(LinkedList::new));
    }

    private void setTextureMap(IBlockProperties block, BlockState blockState, List<Variant> variants, int metadata)
    {
        Map<String, String> textureMap = prefixTextureMap(block.getBlockAgent().get(metadata).getTextureMap());
        if (block.getData().size() == 1 || !block.getShape().isMetadataBlock()) // Use defaults section as there is only one block in this block state
        {
            // Add textureMap to defaults section
            Default defaults = new Default();
            defaults.setTextures(textureMap);
            blockState.setDefaults(defaults);
        }
        else // Add texturemap to each model in every variant
        {
            // Go through all models and add the texturemap to each one
            variants.forEach(variant -> {
                if (variant.getModel() != null)
                {
                    variant.getModel().setTextures(textureMap);
                }
                else if (variant.getModels() != null)
                {
                    variant.getModels().forEach(model -> model.setTextures(textureMap));
                }
            });
        }
    }

    private Map<String, String> prefixTextureMap(Map<String, String> textureMap)
    {
        Map<String, String> ret = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : textureMap.entrySet())
        {
            String side = entry.getKey();
            String texture = entry.getValue();
            ret.put(side, !texture.contains(":") ? String.format("%s:blocks/%s", Constants.MOD_ID, texture) : texture);
        }
        return ret;
    }

    private static final File BLOCK_STATE_FOLDER = new File(
            String.format("resourcepacks/%s/assets/%s/blockstates/",
            Constants.MOD_NAME, Constants.MOD_ID));

    private void writeBlockStateToFile(@Nonnull BlockState blockState, @Nonnull ResourceLocation registryName)
    {
        String fileName = String.format("%s.json", registryName.getResourcePath());
        File output = new File(String.format("%s/%s", BLOCK_STATE_FOLDER.getAbsolutePath(), fileName));
        String content = OneEightV2.GSON.toJson(blockState, BlockState.class);
        Log.info("Writing block state json for {}", registryName);

        try
        {
            FileUtils.writeStringToFile(output, content);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}