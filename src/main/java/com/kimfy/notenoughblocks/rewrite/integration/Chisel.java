package com.kimfy.notenoughblocks.rewrite.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kimfy.notenoughblocks.rewrite.block.IBlockProperties;
import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import com.kimfy.notenoughblocks.util.CollectionUtilities;
import com.kimfy.notenoughblocks.util.Constants;
import com.kimfy.notenoughblocks.util.Utilities;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An attempt to explain what is going on in this class. Definitely need to re-write this at some point.
 * Right now it's working so do not touch!
 *
 * Life-cycle of this Class:
 * Called during CommonProxy.init() because IMC messages are handled between FMLInit and FMLPostInit.
 *
 * Reads the json file located at 'assets/modid/json/chisel/chisel.json' which has all the information
 * needed to give a block it's own Chisel group.
 * Entry for Stone:
 *
 *    {
 *    "displayName": "Stone",       // The block's localized name or display name
 *    "unlocalizedName": "stone",   // The name this block was registered with
 *    "metadata": 0,                // The metadata of this block
 *    "chiselGroups": [
 *    "stonebricksmooth"            // The group to put this block in
 *    ],
 *    "register": true              // If true means it will be processed, if false it won't.
 *    }
 *
 * The way this works with NotEnoughBlocks generated blocks is it loops through these entries in the json file
 * and for each entry, it finds blocks from the block registry registered from NotEnoughBlocks that has the same
 * displayName as the json entry. So if Stone was the current entry we were iterating over, it'd check the block
 * registry for blocks with the name of Stone that belong to NotEnoughBlocks.
 *
 * When or if it finds a Block with the same name, I cast it to IBlockProperties and retrieve the JsonBlock object
 * for the given block by using '((IBlockProperties) block).getData.get((jsonEntry.getMetadata()))' When it's retrieved,
 * I call JsonBlock.setChiselGroups() on the block which attaches a new Chisel object with the values of the current
 * json entry we're iterating over. So for Stone, it'd be put in the stonebricksmooth Chisel group.
 *
 * Note:
 *  To add entries for 1.8 or 1.9 blocks, simply go to the chisel.json file and add entries with the displayName of the
 *  block you want to add. E.g if I wanted to add spruce wood doors to a Chisel group, I'd create a new entry at the bottom
 *  of the .json file like:
 *
 *    {
 *    "displayName": "Spruce Wood Door",
 *    "unlocalizedName": "spruce_door",
 *    "metadata": 0,
 *    "chiselGroups": [
 *    "spruce_door"
 *    ],
 *    "register": true
 *    }
 *
 * Simple as that!
 */
public class Chisel implements Serializable
{
    protected static final transient Logger logger = LogManager.getLogger(Constants.MOD_NAME + ":Chisel");
    public static final transient String MOD_ID = "chisel";

    public Chisel() {}

    protected String group = null;
    protected List<String> groups = new ArrayList<>();

    public Chisel setChiselGroup(String chiselGroup)
    {
        this.group = chiselGroup;
        return this;
    }

    public Chisel setChiselGroups(List<String> chiselGroups)
    {
        this.groups = chiselGroups;
        return this;
    }

    public List<String> getChiselGroup()
    {
        if (group == null)
        {
            return groups;
        }
        return Arrays.asList(group);
    }

    public static void addVariation(String group, String modId, String unlocalizedName, int metadata)
    {
        if (!group.isEmpty() && !unlocalizedName.isEmpty() && metadata >= 0)
        {
            String toSend = (new StringBuilder())
                    .append(group)
                    .append("|")
                    .append(modId)
                    .append(":")
                    .append(unlocalizedName)
                    .append("|")
                    .append(metadata)
                    .toString();

            if (!toSend.isEmpty())
            {
                if (FMLInterModComms.sendMessage(MOD_ID, "variation:add", toSend))
                {
                    logger.info("Successfully sent ".concat(toSend).concat(" to chisel"));
                }
                else
                {
                    logger.error("Error: Something went wrong when sending `"
                                         .concat(toSend)
                                         .concat("` to chisel.\nReport this!")
                                );
                }
            }
        }
    }

    /**
     * Checks if the given Block/Object is NEB's
     * @param o
     * @return
     */
    private static boolean isBlockMine(Object o)
    {
        // return o instanceof IBlockProperties;
        return GameRegistry.findUniqueIdentifierFor((Block)o).modId.equals(Constants.MOD_ID);
    }

    /**
     * Loops through IBlockProperties.getData() and adds
     * the blocks to the given Chisel group if there are any
     *
     * The block is expected to be an instance of IBlockProperties
     * @param o
     */
    private static void handleIntegration(Object o)
    {
        Block block = (Block) o;
        List<JsonBlock> list = ((IBlockProperties) block).getData();

        for (int i = 0; i < list.size(); i++)
        {
            JsonBlock jsonBlock = list.get(i);
            Chisel chisel = jsonBlock.getChisel();

            if (chisel != null)
            {
                for (String group : chisel.getChiselGroup())
                {
                    Chisel.addVariation(group, Constants.MOD_ID, jsonBlock.getUnlocalizedName(), i);
                }
            }
        }
    }

    /**
     * Copies the current blockRegistry to a list
     * Filters out blocks that are from NotEnoughBlocks
     * Handles each block's Chisel integration with
     * {@link Chisel#handleIntegration(Object)}
     */
    public static void loadNotEnoughBlocksIntegration()
    {
        if (Loader.isModLoaded(MOD_ID))
        {
            readJson();

            List<Object> blockList = CollectionUtilities.getBlockRegistryAsList();

            blockList.stream()
                     .filter(CollectionUtilities::notNull)
                     .filter(Chisel::isBlockMine)
                     .forEach(Chisel::handleIntegration);
        }
    }

    /**
     * TODO: Rename method
     *
     * Loops through the current blockRegistry, grabs blocks registered from Minecraft
     * For each block it generates an entry in 'chisel.json' located in
     * 'assets/modid/json/chisel.json' to be read later on for adding blocks to their
     * respective chisel groups.
     *
     * This method should only be called when there's something wrong with the json
     * file located in the assets folder.
     */
    public static void loadMinecraftIntegration()
    {
        /*
        List<Object> blockList = CollectionUtilities.getBlockRegistryAsList();

        blockList.stream()
                .filter(CollectionUtilities::notNull)
                .filter(block -> Utilities.isBlockFromMod(block, "minecraft"))
                .forEach(o -> {
                             Block block = (Block) o;
                             Item item = Item.getItemFromBlock(block);
                             List<Block> subBlocks = new ArrayList<>();
                             block.getSubBlocks(item, null, subBlocks);
                             Map<String, Integer> names = new HashMap<>();

                             try
                             {
                                 for (int i = 0; i < subBlocks.size(); i++)
                                 {
                                     names.put(Utilities.getDisplayNameFromItemAndMetadata(item, i), i);
                                 }
                             }
                             catch (NullPointerException e)
                             {
                                 names.put(block.getUnlocalizedName(), 0);
                             }
                             //names.forEach((K, V) -> logger.info(K + "|" + V + "|" + getChiselGroupBasedOnDisplayName(K) + "|" + block.getUnlocalizedName().replace("tile.", "")));
                            names.forEach((displayName, metadata) -> {
                                             values.add(createJsonOutput(displayName, GameRegistry.findUniqueIdentifierFor(block).name, metadata));
                                          });
                         }
                        );
        */
        //writeJson();
    }

    /**
     * Creates JsonChisel object ready to be inserted into the 'chisel.json' file.
     * Sets JsonChisel.chiselGroup equal to the unlocalizedName because that's apparently
     * how Chisel is doing it, they're very inconsistant so going through the file and changing
     * values is a must. E.g 'minecraft:stone:0' is put in the 'stonebricksmooth' group, wtf?
     *
     * @param displayName The display name for this block
     * @param unlocalizedName The name this block was registered with
     * @param metadata This block's metadata
     * @return JsonChisel object containing the necessary information needed to construct a Json entry
     */
    private static JsonChisel createJsonOutput(String displayName, String unlocalizedName, int metadata)
    {
        return new JsonChisel().setDisplayName(displayName).setUnlocalizedName(unlocalizedName).setMetadata(metadata).setChiselGroups(Arrays.asList(unlocalizedName));
    }

    /**
     * Holds a reference to each JsonChisel object when writeJson() is called
     */
    private static List<JsonChisel> values = new ArrayList<>();

    /**
     * =====================================================================
     * When called, will construct a pretty json String from the values List
     * DO NOT CALL UNLESS THERE IS SOMETHING WRONG WITH THE JSON FILE:
     * /src/main/resources/assets/modid/json/chisel.json
     * THE ONLY TIME THIS SHOULD BE CALLED IS IF THAT FILE DOES NOT EXIST
     * =====================================================================
     */
    private static void writeJson()
    {
        try
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(values);
            logger.info(json);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Reads the chisel.json file located in '/assets/notenoughblocks/json/chisel/chisel.json'.
     * Processes the values retrieved from the readings too with processJson(values)
     */
    private static void readJson()
    {
        String path = "/assets/notenoughblocks/json/chisel/chisel.json";
        try
        {
            Type type = new TypeToken<List<JsonChisel>>(){}.getType();
            List<JsonChisel> values = new Gson().fromJson(new FileReader(Chisel.class.getResource(path).getFile()), type);
            processJson(values);
        }
        catch (FileNotFoundException|NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * For each element in the List it makes sure JsonChisel.register is true
     * - meaning it should be added in a Chisel group. If true - it is processed
     * with processEntry()
     *
     * @param blocks This list is generated when the 'chisel.json' file is deserialized
     */
    private static void processJson(List<JsonChisel> blocks)
    {
        if (blocks != null)
        {
            blocks.stream()
                    .filter(CollectionUtilities::notNull)
                    .filter(e -> e.register)
                    .forEach(Chisel::processEntry);
            ;
        }
    }
    /*needs to be called before loadNotEnoughBlocksIntegration otherwise won't work*/

    /**
     * Attaches a Chisel object to the NotEnoughBlocks generated block
     * that has the same name as the passed object.
     *
     * E.g if the entry passed has a display name of Sandstone -
     * All blocks that have the name of Sandstone from NotEnoughBlocks
     * are put in the Chisel group given by jsonChisel.getChiselGroups().
     *
     * Also registered any vanilla blocks - as the json entries are basically
     * vanilla entries.
     * @param jsonChisel
     */
    private static void processEntry(JsonChisel jsonChisel)
    {
        String displayName = jsonChisel.getDisplayName();
        for (String group : jsonChisel.getChiselGroups())
        {
            addVariation(group, "minecraft", jsonChisel.getUnlocalizedName(), jsonChisel.getMetadata());
        }

        CollectionUtilities.getBlockRegistryAsList().stream()
                .filter(CollectionUtilities::notNull)
                .filter(block -> Utilities.isBlockFromMod(block, Constants.MOD_ID))
                .forEach(o -> {
                          Block block = (Block)o;
                             List<JsonBlock> jsonBlockList = ((IBlockProperties)block).getData();

                            for (int i = 0; i < jsonBlockList.size(); i++)
                            {
                                JsonBlock jsonBlock = jsonBlockList.get(i);

                                /* Stops it from overwriting the Chisel group if it was added manually in a resource pack json*/
                                if (jsonBlock.getChisel() == null)
                                {
                                    if (jsonBlock.getDisplayName().equals(displayName))
                                    {
                                        jsonBlock.setChiselGroup(jsonChisel.getChiselGroups());
                                    }
                                }
                            }
                         });
    }
}

class JsonChisel implements Serializable
{
    JsonChisel() {}

    String displayName = null;
    String unlocalizedName = null;
    int metadata = 0;
    String chiselGroup = null;
    List<String> chiselGroups = null;

    /**
     * Set to true if this block should be registered with Chisel
     * Examples of blocks that shouldn't be registered:
     * air
     */
    boolean register = true;

    public String getDisplayName()
    {
        return displayName;
    }

    public JsonChisel setDisplayName(String displayName)
    {
        this.displayName = displayName;
        return this;
    }

    public String getUnlocalizedName()
    {
        return unlocalizedName;
    }

    public JsonChisel setUnlocalizedName(String unlocalizedName)
    {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    public int getMetadata()
    {
        return metadata;
    }

    public JsonChisel setMetadata(int metadata)
    {
        this.metadata = metadata;
        return this;
    }

    public JsonChisel setChiselGroups(List<String> chiselGroups)
    {
        this.chiselGroups = chiselGroups;
        return this;
    }

    public List<String> getChiselGroups()
    {
        return chiselGroup == null ? chiselGroups : Arrays.asList(chiselGroup);
    }
}
