package com.kimfy.notenoughblocks.client.file.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kimfy.notenoughblocks.NotEnoughBlocks;
import com.kimfy.notenoughblocks.common.block.IBlockProperties;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.FileUtilities;
import com.kimfy.notenoughblocks.common.util.Utilities;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * # === UNSTABLE === # === UNSTABLE === # === UNSTABLE === # === UNSTABLE === # === UNSTABLE === # === UNSTABLE === #
 * The purpose of this Class is to handle writing blockstate files for all blocks under this mod
 * # === UNSTABLE === # === UNSTABLE === # === UNSTABLE === # === UNSTABLE === # === UNSTABLE === # === UNSTABLE === #
 */
@SideOnly(Side.CLIENT)
public class OneEight
{
    private static Logger logger = NotEnoughBlocks.logger;
    public static List<Block> blocks = new ArrayList<>();
    // The folder my blockstate files are written to
    private static File blockstateFolder = new File("resourcepacks/" + Constants.MOD_NAME + "/assets/" + Constants.MOD_ID + "/blockstates/");

    static
    {
        blocks = getBlocksFromMod(Constants.MOD_ID);
        blockstateFolder.mkdirs();
    }

    public static void writeBlockStateFiles()
    {
        if (!blocks.isEmpty())
        {
            for (Block block : blocks)
            {
                List<BlockJson> blockJsons = ((IBlockProperties) block).getData();
                String shape = blockJsons.get(0).getShape();
                String registryName = block.getRegistryName().getResourcePath();
                String blockName = registryName;//block.getRegistryName().substring(block.getRegistryName().indexOf(':') + 1);
                String fileName = blockName + ".json";
                File outputFile = new File(blockstateFolder.getAbsolutePath() + "/" + fileName);

                String blockStateTemplatePath = "blockstates/templates/" + shape + ".json";
                ResourceLocation blockStateTemplateRL = new ResourceLocation(Constants.MOD_ID, blockStateTemplatePath);
                boolean isBlockStateTemplatePresent = FileUtilities.exists(blockStateTemplateRL);

                if (!outputFile.exists() && isBlockStateTemplatePresent)
                {
                    String blockStateTemplate = FileUtilities.getFileFromAssets(blockStateTemplateRL);

                    switch (shape)
                    {
                        case "slab":
                        {
                            writeBlockStateForSlab(blockName, blockJsons, null, outputFile);
                            break;
                        }
                        default:
                        {
                            writeBlockState(blockName, blockJsons, blockStateTemplate, outputFile, Shape.get(shape).isMetadataBlock() ? "item,metadata=#metadata" : "item");
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void registerItemModels()
    {
        if (blocks == null || blocks.isEmpty())
        {
            NotEnoughBlocks.logger.error("Cannot register item models. No blocks from NotEnoughBlocks present!");
            return;
        }
        //logger.info("[NEB]: registerItemModels()");
        if (blockstateFolder.listFiles().length >= 1)
        {
            //logger.info("[NEB]: listFiles() is over 1");
            Gson gson = new Gson();

            for (File f : blockstateFolder.listFiles())
            {
                //logger.info("[NEB]: Found file: " + f.getName());

                if (f.isFile() && f.getName().endsWith(".json"))
                {
                    // logger.info("[NEB]: " + f.getName() + " is a json. Moving on");
                    try
                    {
                        Type type = new TypeToken<Map<String, Object>>() {}.getType();

                        Map<String, Object> blockStateMap = gson.fromJson(new FileReader(f), type);
                        if (blockStateMap.containsKey("inventory_renders"))
                        {
                            String blockName = (String) blockStateMap.get("block_name");
                            @SuppressWarnings("unchecked") // Is this a bad idea?
                            List<Map<String, Object>> inventoryRenders = (List<Map<String, Object>>) blockStateMap.get("inventory_renders");
                            for (Map<String, Object> entry : inventoryRenders)
                            {
                                //String blockName = (String) entry.get("block");
                                int metadata = ((Number) entry.get("metadata")).intValue();
                                String variant   = (String) entry.get("variant");

                                //logger.info("===== # ===== # ===== # ===== # ===== # ===== # ===== # ===== # ===== # =====");
                                //logger.info("INFORMATION: Got block with given information:");
                                //logger.info("Name: " + blockName);
                                //logger.info("Metadata: " + metadata);
                                //logger.info("Variant: " + variant);
                                //logger.info("===== # ===== # ===== # ===== # ===== # ===== # ===== # ===== # ===== # =====");

                                ResourceLocation rl = new ResourceLocation(Constants.MOD_ID, blockName);
                                Block block = Block.blockRegistry.getObject(rl);

                                if (block != null)
                                {
                                    //logger.info("[NEB]: block is not null, registering item model");
                                    registerItem(block, metadata, blockName, variant);
                                }
                                else
                                {
                                    logger.error("===== # ===== # ===== # ===== # ===== # ===== # ===== # ===== # ===== # =====");
                                    logger.error("ERROR: Could not register item variant for block with given information:");
                                    logger.error("Name: " + blockName);
                                    logger.error("Metadata: " + metadata);
                                    logger.error("Variant: " + variant);
                                    logger.error("Report this to the mod author!");
                                    logger.error("===== # ===== # ===== # ===== # ===== # ===== # ===== # ===== # ===== # =====");
                                }
                            }
                        }
                        else
                        {
                            logger.error("Key: inventory_renders not found in blockstate.json: " + f.getName() + ". Report this to the mod author!");
                        }
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                        // Handle this
                    }
                }
            }
        }
    }

    private static void registerItem(Item item, int metadata, ModelResourceLocation mrl)
    {
        ModelLoader.setCustomModelResourceLocation(item, metadata, mrl);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, metadata, mrl);
    }

    private static void registerItem(Block block, int metadata, ModelResourceLocation mrl)
    {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), metadata, mrl);
    }

    private static void registerItem(Block block, int metadata, String blockName, String variant)
    {
        try
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), metadata, MRL(blockName, variant));
        }
        catch (Exception e)
        {
            NotEnoughBlocks.logger.error("Exception when registering item in OneEight#registerItem. Block {}, metadata {}, blockName {}", block, metadata, blockName, e);
        }
    }

    private static ModelResourceLocation MRL(String name, String variant)
    {
        return new ModelResourceLocation(Constants.MOD_ID + ":" + name, variant);
    }

    public static List<Block> getBlocksFromMod(String modId)
    {
        List<Block> temp = new ArrayList<>();

        for (Block block : GameData.getBlockRegistry())
        {
            if (block != null)
            {
                try
                {
                    if (block.getRegistryName().getResourceDomain().equals(modId))
                    {
                        temp.add(block);
                    }
                }
                catch (NullPointerException e)
                {
                    NotEnoughBlocks.logger.error("ERROR: NPE when invoking getRegistryName() on Block {}", block, e);
                }
            }
        }
        return temp;
    }

    public static Map<String, String> prefixTextureMap(Map<String, String> textureMap)
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

    // private static Gson gson = new Gson();
    // private static Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static void writeBlockStateForSlab(String blockName, List<BlockJson> blockJsons, String blockStateTemplate, File blockStateFile)
    {
        if (!blockName.contains("slab_double"))
        {
            //FileReader fileReader = new FileReader(blockStateTemplate);
            //Type type = new TypeToken<Map<String, Object>>(){}.getType();
            //Map<String, Object> blockStateFileMap = gson.fromJson(fileReader, type);

            Map<String, Object> blockStateFileMap = new LinkedHashMap<>();
            blockStateFileMap.put("forge_marker", (int) 1);
            blockStateFileMap.put("block_name", blockName);

            Map<String, Object> variants = new LinkedHashMap<>(16);
            List<Map<String, Object>> itemRenders = new ArrayList<>();

            String variantBottom = "half=bottom,metadata=";
            String variantUpper  = "half=top,metadata=";
            String variantItem   = "item,metadata=";

            for (int metadata = 0; metadata < blockJsons.size(); metadata++)
            {
                Map<String, String> textureMap = prefixTextureMap(blockJsons.get(metadata).getTextureMap());

                Map<String, Object> variantBottomMap = new LinkedHashMap<>(3); // Keys: model, parent, textures
                Map<String, Object> variantUpperMap  = new LinkedHashMap<>(3); // Keys: -----||-----
                Map<String, Object> variantItemMap   = new LinkedHashMap<>(1); // Keys: model

                variantBottomMap.put("model", Constants.MOD_ID + ":half_slab");
                variantBottomMap.put("parent", Constants.MOD_ID + ":block/half_slab");

                variantUpperMap.put("model", Constants.MOD_ID + ":upper_slab");
                variantUpperMap.put("parent", Constants.MOD_ID + ":block/upper_slab");

                variantBottomMap.put("textures", textureMap);
                variantUpperMap.put("textures", textureMap);

                variantItemMap.put("model", Constants.MOD_ID + ":inventory/slab_inventory");
                variantItemMap.put("textures", textureMap);

                variants.put(variantBottom + metadata, variantBottomMap);
                variants.put(variantUpper + metadata, variantUpperMap);
                variants.put(variantItem + metadata, variantItemMap);

                Map<String, Object> itemRender = new LinkedHashMap<>(2);
                itemRender.put("metadata", (int) metadata);
                itemRender.put("variant", variantItem + metadata);
                itemRenders.add(metadata, itemRender);
            }

            blockStateFileMap.put("variants", variants);
            blockStateFileMap.put("inventory_renders", itemRenders);

            try
            {
                FileWriter fileWriter = new FileWriter(blockStateFile);
                fileWriter.write(Utilities.gson.toJson(blockStateFileMap));
                fileWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else // Double Slab
        {
            File out = new File(blockstateFolder.getAbsolutePath() + "/" + blockName + ".json");
            writeBlockState(blockName, blockJsons, FileUtilities.getFileFromAssets(new ResourceLocation(Constants.MOD_ID, "blockstates/templates/cube.json")), out, "item,metadata=#metadata");
        }
    }

    @SuppressWarnings("unchecked")
    private static void writeBlockState(String blockName, List<BlockJson> blockJsons, String blockStateTemplate, File blockStateFile, String inventoryRenderVariant)
    {
        /**
         * Returns true if there's only one variant/block in the blockJsons list. Determines whether or not to
         * use the defaults section in the forge blockstate
         */
        boolean populateDefaultsSection = blockJsons.size() == 1;
        Map<String, Object> blockStateFileMap = new LinkedHashMap<>();
        blockStateFileMap.put("forge_marker", 1);
        blockStateFileMap.put("block_name", blockName);

        Map<String, Object> variants = new LinkedHashMap<>();
        List<Map> inventoryRenders = new ArrayList<>();

        for (int metadata = 0; metadata < blockJsons.size(); metadata++)
        {
            BlockJson modelBlock = blockJsons.get(metadata);
            Map<String, Object> inventoryRender = new LinkedHashMap<>();
            inventoryRender.put("metadata", metadata);
            //inventoryRender.put("variant", "east=false,metadata="+ String.valueOf(metadata) +",north=true,south=true,up=true,west=false");
            inventoryRender.put("variant",inventoryRenderVariant.replace("#metadata", String.valueOf(metadata)));
            inventoryRenders.add(inventoryRender);
            Map<String, Object> templateVariants = null;

            Map<String, Object> templateVariantsMap = Utilities.gson.fromJson(blockStateTemplate, Map.class);
            templateVariants = getTemplateVariants((Map<String, Object>) templateVariantsMap.get("variants"), metadata);

            if (!populateDefaultsSection)
            {
                for (Map.Entry<String, Object> variant : templateVariants.entrySet())
                {
                    if (variant.getValue() instanceof List) // If it's a List it means the variant has some randomization to the model. E.g sand, gravel
                    {
                        List<Map<String, Object>> variantList = (List<Map<String, Object>>) variant.getValue();
                        for (Map<String, Object> map : variantList)
                        {
                            map.put("textures", prefixTextureMap(blockJsons.get(metadata).getTextureMap()));
                        }
                    }
                    else // "metadata=#metadata": {}
                    {
                        Map<String, Object> val = (Map<String, Object>) variant.getValue();

                        /*
                            The reason we're removing the model kv-pair from the map is
                            so that we can put the sunflower model in there as it differs
                            from the basic 'double_plant' top
                         */
                        if (modelBlock.isSunflower())
                        {
                            // Check if variant is half=upper
                            if (variant.getKey().contains("half=upper"))
                            {
                                val.remove("model");
                                val.put("model", Constants.MOD_ID + ":double_plant_sunflower_top");
                            }
                        }
                        val.put("textures", prefixTextureMap(blockJsons.get(metadata).getTextureMap()));
                    }
                }
            }
            else if (populateDefaultsSection && metadata == 0)
            {
                // Add in a defaults section - used for stairs, so we don't populate every single fucking variant
                // when it doesn't have to be populated

                // Add the defaults section to blockstate file
                Map<String, Object> defaultsMap = new LinkedHashMap<>(1);
                defaultsMap.put("textures", prefixTextureMap(blockJsons.get(0).getTextureMap()));
                blockStateFileMap.put("defaults", defaultsMap);
            }

            variants.putAll(templateVariants);
        }

        /********************************************************************************/
        // Copy 'defaults>transform' from template file and insert it into new blockstate
        /********************************************************************************/
        Map<String, Object> templateMap = Utilities.gson.fromJson(blockStateTemplate, Map.class);
        if (templateMap.containsKey("defaults")) // If template file contains defaults section
        {
            Map<String, Object> templateDefaults = (Map<String, Object>) templateMap.get("defaults");
            // Copy defaults->transform map from template json to blockstateFileMap

            if (templateDefaults.containsKey("transform"))
            {
                Map<String, Object> templateTransforms = (Map<String, Object>) templateDefaults.get("transform");
                // There is a transform block in the template map, copy it over to blockstatefilemap

                if (blockStateFileMap.containsKey("defaults"))
                {
                    Map<String, Object> blockstateDefaults = (Map<String, Object>) blockStateFileMap.get("defaults");

                    blockstateDefaults.put("transform", templateTransforms);
                }
                else
                {
                    // The blockstatefilemap does not have a defaults block, create it and put the transform section in there
                    Map<String, Object> defaultsMap = new LinkedHashMap<>(1);
                    defaultsMap.put("transform", templateTransforms);
                    blockStateFileMap.put("defaults", defaultsMap);
                }
            }
        }

        blockStateFileMap.put("variants", variants);
        blockStateFileMap.put("inventory_renders", inventoryRenders);

        try
        {
            FileWriter fileWriter = new FileWriter(blockStateFile);
            fileWriter.write(Utilities.gson.toJson(blockStateFileMap));
            fileWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> getTemplateVariants(Map<String, Object> variants, int metadata)
    {
        Map<String, Object> ret = new LinkedHashMap<>(32);

        for (Map.Entry<String, Object> entry : variants.entrySet())
        {
            String key = entry.getKey();
            Object val = entry.getValue();
            ret.put(key.replace("#metadata", String.valueOf(metadata)), val);
        }
        return ret;
    }
}