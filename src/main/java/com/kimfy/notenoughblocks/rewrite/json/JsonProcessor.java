package com.kimfy.notenoughblocks.rewrite.json;

import codechicken.nei.api.API;
import com.kimfy.notenoughblocks.rewrite.block.IBlockProperties;
import com.kimfy.notenoughblocks.rewrite.block.NEBBlockSlab;
import com.kimfy.notenoughblocks.rewrite.integration.ForgeMultipart;
import com.kimfy.notenoughblocks.rewrite.integration.MineFactoryReloaded;
import com.kimfy.notenoughblocks.rewrite.integration.NotEnoughItems;
import com.kimfy.notenoughblocks.rewrite.item.NEBItemBlockSlab;
import com.kimfy.notenoughblocks.util.Constants;
import com.kimfy.notenoughblocks.util.Utilities;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class that handles this: 
 * <html>
 * 	<ul>
 * 		<li>Logic for reading from JSON</li>
 * 		<li>Logic for categorizing entries</li>
 * 		<li>Logic for registering entries</li>
 * </ul>
 * </html>
 */
public class JsonProcessor
{
    private static final Logger logger = LogManager.getLogger(Constants.MOD_NAME + ":JSONProcessor");
    public static List<Json> jsons = new ArrayList<Json>();
    
	public static void initialize()
	{
	    readJson();
	    
	    processJson();
	}

	public static void readJson()
	{
		// By this point, no JSON's should be empty
		// All resource packs have had their JSONS
		// Written to but we should check if they
		// aren't actually empty before doing
		// anything anyways
		File jsonDirectory = new File(Constants.JSON_PATH);

		if ((jsonDirectory.listFiles()).length > 0)
		{
			for (File f : jsonDirectory.listFiles())
			{
				if (f.getName().endsWith(".json"))
				{
					logger.info("Found JSON: " + f.getName());
					jsons.add(new Json(f));
				}
			}
		}
	}

	public static void processJson()
	{
		if (!jsons.isEmpty())
		{
			for (Json json : jsons)
			{
				// Generate the categories we need
				generateDynamicCategories(json);

				// Process the data
				register(json);

				// Clear the categories after every Json is done
				// processing
				categories.clear();

				logger.info("Done processing: " + json.getName().concat(".json"));
			}
		}
		else
		{
			logger.info("There are no JSON files to process!");
		}
	}
	
    private static Map<String, Category> categories = new HashMap<>();

	private static void generateDynamicCategories(Json data)
	{
		for (JsonBlock block : data.getData().blocks)
		{

			if (categories.isEmpty())
			{
				categories.put(block.getCategory().toString(), block.getCategory());

			}
			else if (!categories.containsKey(block.getCategory().toString()))
			{
				categories.put(block.getCategory().toString(), block.getCategory());
			}
		}
	}

	/**
     * Checks the given List<> for the amount
     * of blocks that are in the given category.
     * 
     * @param blocks The List<> containing the JsonBlock's to perform on
     * @param category The Category to check
     * @return An integer that represents the amount of Blocks are in the Category
     */
	private static int getBlocksInCategory(List<JsonBlock> blocks, Category category)
	{
		int tmp = 0;

		for (JsonBlock block : blocks)
		{
			if (category.toString().equals(block.getCategory().toString()))
			{
				tmp++;
			}
		}
		return tmp;
	}

    private static void register(Json data)
    {
        List<JsonBlock> blockList = new ArrayList<>();

        int numInCategory = 0;
        int categorySize = 0;
        int index = 1;

        for (Map.Entry<String, Category> cat : categories.entrySet())
        {
            String categoryName = cat.getKey();
            Category category = cat.getValue();
            categorySize = getBlocksInCategory(data.getData().blocks, category);

            for (JsonBlock block : data.getData().blocks)
            {
                if (categoryName.equals(block.getCategory().toString()))
                {
                    //logger.info(categoryName + " >>> " + block.getCategory().toString());
                    //logger.info("Matching categories ^^^");
                    numInCategory++;

                    // Add block to blockList, increment blocksInCategory by 1
                    // as long as blockList is NOT higher than category.getMaxSubBlocks()
                    blockList.add(block);
                    //logger.info("Added: " + block.getDisplayName() + " to the list of blocks to register. Number of blocks in category: " + numInCategory);

                    if (blockList.size() == category.getMaxSubBlocks() || numInCategory == categorySize)
                    {
                        //logger.info("Index: " + index);
                        registerBlocks(blockList, data, index++);
                        blockList.clear();
                        // Pretend current category shape is "NORMAL",
                        // If blockList.size() is equal to 16
                        // We want to register this current set of blocks
                        // So send blockList to registerBlocks(),
                        // increment index by 1, set blockNum to 0
                        // and clear the blockList.
                    }
                }
            }
            numInCategory = 0;
        }
    }

    static int uuid = 0;
    
	private static void registerBlocks(List<JsonBlock> blocks, Json data, int index)
	{
        /*logger.info("Registering " + blocks.size() + " block(s) in category " + blocks.get(0).getStringShape());*/

	    JsonBlock.Shape shape      = blocks.get(0).getShape();
	    JsonBlock jsonBlock        = blocks.get(0);

	    Material material          = jsonBlock.getObjectMaterial();
	    Block.SoundType soundType  = jsonBlock.getObjectSoundType();
	    CreativeTabs creativeTab   = jsonBlock.getObjectCreativeTab();

	    int metadata               = blocks.size();
	    String unlocalizedName     = data.getName() + "_" + jsonBlock.getStringShape() + "_" + uuid++;
	    String[][] textures        = new String[metadata][6];

	    Class<?> cls;
	    Block b = null;

        switch (shape)
        {
            case SLAB:
            {
                NEBBlockSlab blockSlab = new NEBBlockSlab(false, material, unlocalizedName, blocks);
                setProperties(blockSlab, blocks, unlocalizedName);

                String doubleSlabUnlocalizedName = data.getName() + "_" + jsonBlock.getStringShape() + "_full" + "_" + uuid;
                NEBBlockSlab blockDoubleSlab = new NEBBlockSlab(true, material, doubleSlabUnlocalizedName, Utilities.deepCloneList(blocks));
                setProperties(blockDoubleSlab, blocks, doubleSlabUnlocalizedName);
                blockDoubleSlab.setCreativeTab(null);

                GameRegistry.registerBlock(blockSlab, NEBItemBlockSlab.class, unlocalizedName, blockSlab, blockDoubleSlab, false);
                GameRegistry.registerBlock(blockDoubleSlab, NEBItemBlockSlab.class, doubleSlabUnlocalizedName, blockSlab, blockDoubleSlab, true);
                break;
            }
            default:
            {
				try {
					
					cls = shape.getBlockClass();
					
					if (cls != null)
					{
						Constructor<?> con = cls.getConstructor(Material.class, List.class);
		        		b = (Block & IBlockProperties) con.newInstance(material, blocks);
		        		setProperties((Block & IBlockProperties) b, blocks, unlocalizedName);
		        		GameRegistry.registerBlock(b, shape.getItemClass(), unlocalizedName);	
					}
	        		
				} catch (
						NoSuchMethodException | SecurityException
						| InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e
					)
				{
					e.printStackTrace(); // TODO: Better error  handling
				}
                break;
            }
        }
	}
	
	private static <T extends Block & IBlockProperties> void setProperties(T block, List<JsonBlock> blocks, String unlocalizedName)
	{
	    if (block != null)
	    {
			blocks.forEach(b -> b.setUnlocalizedName(unlocalizedName));
	        JsonBlock source = blocks.get(0);
	        block.setData(Utilities.deepCloneList(blocks));
	        block.setBlockName(Constants.MOD_ID + ":" + unlocalizedName);
	        block.setSubBlocks(blocks.size());
	        block.setCreativeTab(source.getObjectCreativeTab());
	        block.setStepSound(source.getObjectSoundType());
	        block.setHardness(source.getHardness());
	        block.setResistance(source.getResistance());
	        block.setBeaconBaseAble(source.isBeaconBase());
	        block.setBlockLightOpacity(source.getLightOpacity());
	        block.setLightLevel(source.getLightLevel());
	        block.setBlockOpaqueness(source.isOpaque());
	        block.setBlockStainable(source.isStained());
	        block.slipperiness = source.getSlipperiness();
	        
	        String[][] textures;
	        if (source.getShape() == JsonBlock.Shape.BED)
	        {
                textures = new String[blocks.size()][7];
	        }
	        else
	        {
	            textures = new String[blocks.size()][6];
	        }
            
	        
	        for (int i = 0; i < blocks.size(); i++)
	        {
	            // Silk Touch
	            if (blocks.get(i).isSilkTouch())
	                block.setBlockSilkTouchable(block, i);
	            
	            // Textures
	            textures[i] = blocks.get(i).getBlockTextures();
	            
	            // Display Name/Localized Name
                LanguageRegistry.instance().addStringLocalization("tile." + Constants.MOD_ID + ":" + unlocalizedName + "_" + i + ".name", blocks.get(i).getDisplayName());
	            
	            // MFR Interaction
	            if (Loader.isModLoaded(MineFactoryReloaded.MOD_ID))
	            {
	                if (blocks.get(i).getMfr() != null)
	                {
	                    ; // TODO: Implement
	                }   
	            }
	            
	            // ForgeMultipart Interaction
	            if (Loader.isModLoaded(ForgeMultipart.MOD_ID))
	            {
	                if (blocks.get(i).getFmp() != null)
	                {
	                    ; // TODO: Implementation
	                }
	            }

                // NEI Interaction
                if (Loader.isModLoaded(NotEnoughItems.MOD_ID))
                {
                    if (blocks.get(i).getNei() != null)
                    {
                        NotEnoughItems nei = blocks.get(i).getNei();

                        if (nei.isHide())
                        {
                            API.hideItem(new ItemStack(block, 0, i));
                        }
                    }
                }
	        }
	        block.registerTextures(textures);
	    }
	}
}
