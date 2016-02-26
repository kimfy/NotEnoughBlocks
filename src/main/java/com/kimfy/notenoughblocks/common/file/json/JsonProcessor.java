package com.kimfy.notenoughblocks.common.file.json;

import com.kimfy.notenoughblocks.NotEnoughBlocks;
import com.kimfy.notenoughblocks.common.block.IBlockProperties;
import com.kimfy.notenoughblocks.common.block.NEBBlockSlabDouble;
import com.kimfy.notenoughblocks.common.block.NEBBlockSlabHalf;
import com.kimfy.notenoughblocks.common.item.NEBItemBlockSlab;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.Utilities;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonProcessor
{
    public static File jsonFolder = new File(Constants.PATH_MOD_CONFIG_JSON);
    public static List<Json> jsons = new ArrayList<>();
    private static Logger logger = NotEnoughBlocks.logger;

    public void loadFiles()
    {
        for (File file : jsonFolder.listFiles())
        {
            String fileName = file.getName();

            if (isJson(file))
            {
                logger.info("Found JSON file " + fileName);
                jsons.add(new Json(file, fileName.replace(".json", "")));
            }
            else
            {
                logger.info("File " + fileName + " is not a JSON file. SKIPPING");
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
            logger.info("There are no JSON files to process. SKIPPING");
        }
    }

    private static Map<String, Category> categories = new LinkedHashMap<>();

    void generateDynamicCategories(Json json)
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

    int getBlockAmountInCategory(List<BlockJson> blocks, Category category)
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
        logger.info("Registering " + blocks.size() + " block(s) in category " + blocks.get(0).getShape());
        for (int i = 0; i < blocks.size(); i++)
        {
            BlockJson temp = blocks.get(i);
            logger.info(i + ": " + temp.getDisplayName());
        }

        BlockJson model = blocks.get(0);
        Shape shape = model.getRealShape();

        Material material = model.getRealMaterial();
        String unlocalizedName = json.getName() + "_" + model.getShape() + "_" + index;

        Class<?> cls;
        Block block = null;

        try
        {
            cls = shape.getBlockClass();

            if (cls != null)
            {
                if (shape == Shape.SLAB) // TODO: Generify this
                {
                    BlockSlab slabHalf = new NEBBlockSlabHalf(material, blocks);
                    BlockSlab slabDouble = new NEBBlockSlabDouble(material, blocks);

                    setBlockProperties((Block & IBlockProperties) slabHalf, blocks, unlocalizedName);
                    String slabDoubleUnlocalizedName = json.getName() + "_" + model.getShape() + "_double_" + index;
                    setBlockProperties((Block & IBlockProperties) slabDouble, blocks, slabDoubleUnlocalizedName);

                    GameRegistry.registerBlock(slabHalf, NEBItemBlockSlab.class, unlocalizedName, slabHalf, slabDouble, false);
                    GameRegistry.registerBlock(slabDouble, NEBItemBlockSlab.class, slabDoubleUnlocalizedName, slabHalf, slabDouble, true);
                }
                else
                {
                    Constructor<?> con = cls.getConstructor(Material.class, List.class);
                    block = (Block & IBlockProperties) con.newInstance(material, blocks);

                    setBlockProperties((Block & IBlockProperties) block, blocks, unlocalizedName);
                    GameRegistry.registerBlock(block, shape.getItemClass(), unlocalizedName);
                }
            }
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e.getCause());
        }
    }

    private <T extends Block & IBlockProperties> void setBlockProperties(T block, List<BlockJson> blocks, String unlocalizedName)
    {
        blocks.forEach( v -> v.unlocalizedName(unlocalizedName));
        BlockJson model = blocks.get(0);

        block.setUnlocalizedName(Constants.MOD_ID + ":" + unlocalizedName);
        block.setCreativeTab(model.getRealCreativeTab());
        block.setStepSound(model.getRealSoundType());
        block.setHardness(model.getHardness());
        block.setResistance(model.getResistance());
        block.setLightLevel(model.getLightLevel());

        /* IBlockProperties.class methods */
        block.setData(Utilities.deepCloneList(blocks));
        block.setBeaconBaseable(model.isBeaconBase());
        block.setBlockLightOpacity(model.getLightOpacity());
        block.setBlockOpaqueness(model.isOpaque());
        block.setBlockStainable(model.isStained());
        block.setSlipperiness(model.getSlipperiness());

        /* Metadata specific operations */
        for (int metadata = 0; metadata < blocks.size(); metadata++)
        {
            BlockJson entry = blocks.get(metadata);

            block.isSilkTouchable(entry.isSilkTouch(), metadata);
            setDisplayName(unlocalizedName, metadata, entry.getDisplayName());
        }
    }


    private static void setDisplayName(String unlocalizedName, int metadata, String displayName)
    {
        LanguageRegistry.instance()
                .addStringLocalization("tile." + Constants.MOD_ID + ":" + unlocalizedName + "_" + metadata + ".name", displayName);
    }
}