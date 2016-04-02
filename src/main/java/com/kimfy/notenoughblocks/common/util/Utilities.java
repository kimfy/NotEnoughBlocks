package com.kimfy.notenoughblocks.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

    public static GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(BlockJson.class, new BlockJson.Deserializer())
            .registerTypeAdapter(BlockJson.class, new BlockJson.Serializer());

    public static Gson gson = gsonBuilder.create();

    public static BlockJson deepClone(BlockJson block)
    {
        String clone = gson.toJson(block, BlockJson.class);
        return gson.fromJson(clone, BlockJson.class);
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
            return GameRegistry.findUniqueIdentifierFor(block).modId;
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
