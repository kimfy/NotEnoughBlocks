package com.kimfy.notenoughblocks.common.file.json;

import com.google.common.base.Joiner;
import com.kimfy.notenoughblocks.common.block.*;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.Log;
import com.kimfy.notenoughblocks.common.util.Utilities;
import com.kimfy.notenoughblocks.common.util.block.Recipe;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

public class JsonProcessor
{
    public static File jsonFolder = new File(Constants.PATH_MOD_CONFIG_JSON);
    public static List<Json> jsons = new ArrayList<>();

    public void loadFiles()
    {
        for (File file : jsonFolder.listFiles())
        {
            String fileName = file.getName();

            if (isJson(file))
            {
                Log.info("Found JSON file " + fileName);
                jsons.add(new Json(file, fileName.replace(".json", "")));
            }
            else
            {
                Log.info("File " + fileName + " is not a JSON file. SKIPPING");
            }
        }
    }

    private boolean isJson(File file)
    {
        return file.isFile() && file.getName().endsWith(".json");
    }

    public void processData()
    {
        if (!jsons.isEmpty())
        {
            for (Json json : jsons)
            {
                json.read();

                generateDynamicCategories(json);

                register(json);

                categories.clear();
            }
        }
        else
        {
            Log.info("There are no JSON files to process...");
        }
    }

    private static Map<String, Category> categories = new LinkedHashMap<>();

    private void generateDynamicCategories(Json json)
    {
        for (BlockJson block : json.getBlocks())
        {
            Category category = block.getCategory();
            String strCategory = category.toString();

            if (categories.isEmpty())
            {
                categories.put(strCategory, category);
            }
            else if (!categories.containsKey(strCategory))
            {
                categories.put(strCategory, category);
            }
        }
    }

    private int getBlockAmountInCategory(List<BlockJson> blocks, Category category)
    {
        int tmp = 0;

        for (BlockJson block : blocks)
        {
            if (category.toString().equals(block.getCategory().toString()))
            {
                tmp++;
            }
        }
        return tmp;
    }

    private void register(Json json)
    {
        List<BlockJson> blockList = new ArrayList<>();
        int numInCategory = 0;
        int categorySize = 0;
        int index = 1;

        for (Map.Entry<String, Category> entry : categories.entrySet())
        {
            String categoryName = entry.getKey();
            Category category = entry.getValue();
            categorySize = getBlockAmountInCategory(json.getBlocks(), category);

            for (BlockJson block : json.getBlocks())
            {
                if (categoryName.equals(block.getCategory().toString()))
                {
                    numInCategory++;

                    blockList.add(block);

                    if (blockList.size() == category.getMaxBlocks() || numInCategory == categorySize)
                    {
                        registerBlocks(blockList, json, index++);
                        blockList.clear();
                    }
                }
            }
            numInCategory = 0;
        }
    }

    private void registerBlocks(List<BlockJson> blocks, Json json, int index)
    {
        BlockJson model = blocks.get(0);
        Shape shape = model.getShape();

        Material material = model.getMaterial();
        String unlocalizedName = json.getName() + "_" + model.getShape() + "_" + index;
        ResourceLocation rl = new ResourceLocation(Constants.MOD_ID, unlocalizedName);
        Class<?> blockClass;
        Class<?> itemClass;
        Block block;
        Item item;

        try
        {
            blockClass = shape.getBlockClass();
            itemClass = shape.getItemClass();
            if (blockClass != null)
            {
                if (shape == Shape.SLAB) // TODO: Generify this
                {
                    Constructor<?> itemConstructor = itemClass.getConstructor(Block.class, NEBBlockSlabHalf.class, NEBBlockSlabDouble.class);

                    NEBBlockSlabHalf slabHalf     = new NEBBlockSlabHalf(material, Utilities.deepCloneList(blocks));
                    NEBBlockSlabDouble slabDouble = new NEBBlockSlabDouble(material, Utilities.deepCloneList(blocks));

                    Item itemSlabHalf   = (Item) itemConstructor.newInstance(slabHalf, slabHalf, slabDouble);
                    Item itemSlabDouble = (Item) itemConstructor.newInstance(slabDouble, slabHalf, slabDouble);

                    String slabDoubleUnlocalizedName = json.getName() + "_" + model.getShape() + "_double_" + index;
                    setBlockProperties((Block & IBlockProperties) slabHalf, blocks, unlocalizedName);
                    setBlockProperties((Block & IBlockProperties) slabDouble, blocks, slabDoubleUnlocalizedName);

                    ResourceLocation rlHalfSlab = new ResourceLocation(Constants.MOD_ID, unlocalizedName);
                    GameRegistry.register(slabHalf, rlHalfSlab);
                    GameRegistry.register(itemSlabHalf, rlHalfSlab);

                    ResourceLocation rlDoubleSlab = new ResourceLocation(Constants.MOD_ID, slabDoubleUnlocalizedName);
                    GameRegistry.register(slabDouble, rlDoubleSlab);
                    GameRegistry.register(itemSlabDouble, rlDoubleSlab);
                }
                else
                {
                    Constructor<?> blockConstructor = blockClass.getConstructor(Material.class, List.class); // block
                    Constructor<?> itemConstructor = itemClass.getConstructor(Block.class);

                    block = (Block & IBlockProperties) blockConstructor.newInstance(material, Utilities.deepCloneList(blocks));
                    item = (Item) itemConstructor.newInstance(block);

                    GameRegistry.register(block, rl);
                    GameRegistry.register(item, rl);
                    setBlockProperties((Block & IBlockProperties) block, blocks, unlocalizedName);
                }
            }
        }
        catch (Exception e)
        {
            Log.error("Failed to create Block for Blocks {} ", BlockJson.getDisplayNamesFromBlocks(blocks), e);
        }
    }

    private <T extends Block & IBlockProperties> void setBlockProperties(T block, List<BlockJson> blocks, String unlocalizedName)
    {
        blocks.forEach( v -> v.setUnlocalizedName(unlocalizedName));
        BlockJson model = blocks.get(0);

        /* Block setters */
        block.setUnlocalizedName(Constants.MOD_ID + ":" + unlocalizedName);
        block.setCreativeTab(model.getCreativeTab());
        block.setHardness(model.getHardness());
        block.setResistance(model.getResistance());
        block.setLightLevel(model.getLightLevel());
        block.setLightOpacity(model.getLightOpacity());

        /* IBlockProperties setters */
        block.setBeaconBaseable(model.isBeaconBase());
        block.setBlockStainable(model.isStained());
        block.setSlipperiness(model.getSlipperiness());
        block.setUseNeighborBrightness(model.isNeighborBrightness());
        block.setTranslucency(model.isTranslucent());
        block.setBlockSoundType(model.getStepSound());
        block.setBlockMaterial(model.getMaterial());

        /* Metadata specific operations */
        for (int metadata = 0; metadata < blocks.size(); metadata++)
        {
            BlockJson modelBlock = blocks.get(metadata);
            this.setDisplayName(block, unlocalizedName, metadata, modelBlock.getDisplayName());
            this.setBlockRecipe(modelBlock, block, metadata);
        }
    }

    private void setBlockRecipe(BlockJson json, Block block, int metadata)
    {
        Recipe recipe = json.getRecipe();
        if (recipe != null)
        {
            recipe.setOutput(block, metadata);
        }
    }

    private static Map<String, String> lang = new HashMap<>();

    private void setDisplayName(Block block, String unlocalizedName, int metadata, String displayName)
    {
        String key;

        if (block instanceof NEBBlockDoor || block instanceof NEBBlockStair)
        {
            lang.put("tile." + Constants.MOD_ID + ":" + unlocalizedName + ".name", displayName);
        }
        else
        {
            key = "tile." + Constants.MOD_ID + ":" + unlocalizedName + "_" + metadata + ".name";
            lang.put(key, displayName);
        }
    }

    public static void injectLanguageMap()
    {
        if (lang != null && !lang.isEmpty())
        {
            String converted = Joiner.on('\n').withKeyValueSeparator("=").join(lang);
            String path = "resourcepacks/" + Constants.MOD_ID + "/assets/" + Constants.MOD_ID + "/lang/";
            File langDir = new File(path);
            File langFile = new File(path + "en_US.lang");

            try
            {
                if (!langDir.exists())
                {
                    langDir.mkdirs();
                }
                if (!langFile.exists())
                {
                    langFile.createNewFile();
                }
                int langMapSize = converted.getBytes().length;
                int langFileSize = Long.valueOf(langFile.length()).intValue();

                if (langMapSize != langFileSize)
                {
                    Log.info("Writing lang file...");
                    // Delete file if it exists and write to a new one
                    langFile.delete();
                    FileUtils.writeByteArrayToFile(langFile, converted.getBytes());
                }
                else
                {
                    // Language file is equal to the code generated one. Do nothing!
                    Log.info("Language file already exists, ignoring!");
                }
            }
            catch (IOException e)
            {
                Log.error("Failed to write lang file! Report this!");
            }
        }
    }
}
