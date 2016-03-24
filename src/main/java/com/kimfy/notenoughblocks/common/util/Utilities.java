package com.kimfy.notenoughblocks.common.util;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.SerializationUtils;

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
        
        for (BlockJson block : list) {
            cloned.add( (BlockJson) SerializationUtils.clone(block));
        }
        return cloned;
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
            return (GameRegistry.findUniqueIdentifierFor(item)).modId;
        }
        catch (NullPointerException e)
        {
            return "minecraft";
        }
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
            return (GameRegistry.findUniqueIdentifierFor(block)).modId;
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
