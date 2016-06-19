package com.kimfy.notenoughblocks.client.file.json.blockstate;

import com.google.gson.*;
import com.kimfy.notenoughblocks.common.block.IBlockProperties;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.FileUtilities;
import com.kimfy.notenoughblocks.common.util.Log;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import net.minecraft.block.Block;

import javax.annotation.Nonnull;
import java.io.File;
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
 * 2. From the template, generate a new JsonObject and deepCopy the variants and insert the correct information.
 * 3. Output file ends up in path mentioned above
 */
public class OneEightV2
{
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
            // Special case for double slabs, it's assigned slab as it's shape, when in reality it's a cube
            // fix this with a getter overriding getShape() in double slab class?
            if (((Block) iblock).getRegistryName().getResourcePath().contains("slab_double"))
            {
                // FIXME: This CAN return null if no CUBE blocks have been registered/parsed
                //this.createBlockStateFor(iblock, Shape.CUBE, this.shapes.get(Shape.CUBE));
            }
            //this.createBlockStateFor(iblock, shape, this.shapes.get(shape).deepCopy());
        }
    }

    private void createBlockStateFor(IBlockProperties block, Shape shape, BlockState blockState)
    {
        List<Variant> templateVariants = blockState.getVariants().stream()
                .map(variant -> variant.deepCopy())
                .collect(Collectors.toList());
        blockState.getVariants().clear();

        // TODO rename keys, texturemap, metadata block/defaults section, item_renders
    }

    private static final File BLOCK_STATE_FOLDER = new File(String.format("resourcepacks/%s/assets/%s/blockstates/", Constants.MOD_NAME, Constants.MOD_ID));

    private void writeBlockStateToFile(@Nonnull BlockState blockState, @Nonnull String blockName)
    {
        String fileName = String.format("%s.json", blockName);
        File output = new File(String.format("%s/%s", BLOCK_STATE_FOLDER.getAbsolutePath(), fileName));
        String content = OneEightV2.GSON.toJson(blockState, BlockState.class);
        Log.info("Processing {}... Content \n{}", output.getAbsolutePath(), content);
        FileUtilities.write(output, content);
    }
}