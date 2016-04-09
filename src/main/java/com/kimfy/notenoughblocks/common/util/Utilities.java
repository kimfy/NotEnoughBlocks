package com.kimfy.notenoughblocks.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.block.Drop;
import com.kimfy.notenoughblocks.common.util.block.Recipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Utilities
{

    /**
     * Deep clones the given List. Had to implement
     * this because Java's default cloning sucks ass and
     * only makes a shallow copy of the Object.
     * 
     * @return A deep-copied <code> List <BlockJson> </code>
     */
    public static List<BlockJson> deepCloneList(List<BlockJson> list) {
        List<BlockJson> cloned = new ArrayList<BlockJson>(list.size());

        list.forEach(b -> cloned.add(deepClone(b)));

        // for (BlockJson block : list) {
        //     cloned.add( (BlockJson) SerializationUtils.clone(block));
        // }
        return cloned;
    }

    public static final GsonBuilder GSON_BUILDER = new GsonBuilder()
            .disableHtmlEscaping ()
            .setPrettyPrinting()
            .registerTypeAdapter(BlockJson.class, new BlockJson.Deserializer())
            .registerTypeAdapter(BlockJson.class, new BlockJson.Serializer())
            .registerTypeAdapter(Drop.class, new Drop.Deserializer())
            .registerTypeAdapter(Drop.class, new Drop.Serializer())
            .registerTypeAdapter(Recipe.class, new Recipe.Deserializer())
            .registerTypeAdapter(Recipe.class, new Recipe.Serializer());

    public static final Gson GSON = GSON_BUILDER.create();

    public static BlockJson deepClone(BlockJson block)
    {
        String clone = GSON.toJson(block, BlockJson.class);
        return GSON.fromJson(clone, BlockJson.class);
    }

    /**
     * Checks what mod this item belongs to
     * @param item The item to perform this action on
     * @return the modId or 'minecraft' if the item given was not registered properly
     */
    public static String getModOwnerFromItem(Item item)
    {
        try
        {
            //return (GameRegistry.findUniqueIdentifierFor(item)).modId;
            //FIXME: Port to 1.9
        }
        catch (NullPointerException e)
        {
            return "minecraft";
        }
        return "";
    }

    /**
     * Checks what mod this block belongs to
     * @param block The block to perform this action on
     * @return the modId or 'minecraft' if the block given was not registered properly
     */
    public static String getModOwnerForBlock(Block block)
    {
        try
        {
            return block.getRegistryName().getResourceDomain();
        }
        catch (NullPointerException e)
        {
            return "minecraft";
        }
    }

    public static boolean isBlockFromMod(Object o, String modId)
    {
        return getModOwnerForBlock((Block) o).equals(modId);
    }

    public static String getDisplayNameFromItemAndMetadata(Item item, int metadata)
    {
        return item.getItemStackDisplayName(new ItemStack(item, 1, metadata));
    }
}
