package com.kimfy.notenoughblocks.common.file.json;

import com.google.common.base.Joiner;
import com.kimfy.notenoughblocks.NotEnoughBlocks;
import com.kimfy.notenoughblocks.common.block.*;
import com.kimfy.notenoughblocks.common.item.NEBItemBlockSlab;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.Utilities;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.*;

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
        //logger.info("Registering " + blocks.size() + " block(s) in category " + blocks.get(0).getShape());
        //for (int i = 0; i < blocks.size(); i++)
        //{
        //    BlockJson temp = blocks.get(i);
        //    logger.info(i + ": " + temp.getDisplayName());
        //}

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
                    BlockSlab slabHalf = new NEBBlockSlabHalf(material, Utilities.deepCloneList(blocks));
                    BlockSlab slabDouble = new NEBBlockSlabDouble(material, Utilities.deepCloneList(blocks));

                    setBlockProperties((Block & IBlockProperties) slabHalf, blocks, unlocalizedName);
                    String slabDoubleUnlocalizedName = json.getName() + "_" + model.getShape() + "_double_" + index;
                    setBlockProperties((Block & IBlockProperties) slabDouble, blocks, slabDoubleUnlocalizedName);

                    GameRegistry.registerBlock(slabHalf, NEBItemBlockSlab.class, unlocalizedName, slabHalf, slabDouble, false);
                    GameRegistry.registerBlock(slabDouble, NEBItemBlockSlab.class, slabDoubleUnlocalizedName, slabHalf, slabDouble, true);
                }
                else
                {
                    Constructor<?> con = cls.getConstructor(Material.class, List.class);
                    block = (Block & IBlockProperties) con.newInstance(material, Utilities.deepCloneList(blocks));

                    setBlockProperties((Block & IBlockProperties) block, blocks, unlocalizedName);
                    GameRegistry.registerBlock(block, shape.getItemClass(), unlocalizedName);
                }
            }
        }
        catch (Exception e)
        {
            NotEnoughBlocks.logger.error("Failed to create Block for Blocks {} ", BlockJson.getDisplayNamesFromBlocks(blocks), e);
        }
    }

    private <T extends Block & IBlockProperties> void setBlockProperties(T block, List<BlockJson> blocks, String unlocalizedName)
    {
        blocks.forEach( v -> v.unlocalizedName(unlocalizedName));
        BlockJson model = blocks.get(0);

        block.setUnlocalizedName(Constants.MOD_ID + ":" + unlocalizedName);
        block.setCreativeTab(model.getRealCreativeTab());
        block.setHardness(model.getHardness());
        block.setResistance(model.getResistance());
        block.setLightLevel(model.getLightLevel());

        /* IBlockProperties setters */
        block.setBeaconBaseable(model.isBeaconBase());
        block.setBlockLightOpacity(model.getLightOpacity());
        //block.setBlockOpaqueness(model.isOpaque()); // Removed because inconvenient for everyone
        block.setBlockStainable(model.isStained());
        block.setSlipperiness(model.getSlipperiness());

        block.setBlockSoundType(model.getRealSoundType());
        block.setBlockMaterial(model.getRealMaterial());

        /* Metadata specific operations */
        for (int metadata = 0; metadata < blocks.size(); metadata++)
        {
            BlockJson modelBlock = blocks.get(metadata);

            block.isSilkTouchable(modelBlock.isSilkTouch(), metadata);
            setDisplayName(block, unlocalizedName, metadata, modelBlock.getDisplayName());
        }
    }

    private static Map<String, String> lang = new HashMap<>();

    // TODO: Add support for all shapes
    /*
        Shapes that need support:
        Stairs
        Slabs
        Doors
        Rotating
     */
    private static void setDisplayName(Block block, String unlocalizedName, int metadata, String displayName)
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
            InputStream stream = new ByteArrayInputStream(converted.getBytes(Charsets.UTF_8));

            // Compare String bytes with stream.getBytes?
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
                    NotEnoughBlocks.logger.info("[" + Constants.MOD_NAME + "] " + "Writing lang file...");
                    // Delete file if it exists and write to a new one
                    langFile.delete();
                    FileUtils.writeByteArrayToFile(langFile, converted.getBytes());
                }
                else
                {
                    // Language file is equal to the code generated one. Do nothing!
                    NotEnoughBlocks.logger.info("[" + Constants.MOD_NAME + "] " + "Language file already exists, ignoring!");
                }
            }
            catch (IOException e)
            {
                NotEnoughBlocks.logger.error("ERROR: Failed to write lang file! Report this!");
            }

            /*LanguageManager languageManager = Minecraft.getMinecraft().getLanguageManager();

            try
            {
                Field currentLocaleField = ReflectionHelper.findField(LanguageManager.class, "currentLocale", "field_135049_a");
                net.minecraft.client.resources.Locale locale = (net.minecraft.client.resources.Locale) currentLocaleField.get(languageManager);

                Field propertiesField = ReflectionHelper.findField(net.minecraft.client.resources.Locale.class, "properties", "field_135032_a");
                Map<String, String> propertiesMap = (Map<String, String>) propertiesField.get(locale);

                propertiesMap.putAll(lang);
                String conv = Joiner.on('\n').withKeyValueSeparator("=").join(propertiesMap);
                System.out.println(conv);
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            */
        }
    }
}