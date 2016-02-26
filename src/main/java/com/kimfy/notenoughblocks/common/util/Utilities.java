package com.kimfy.notenoughblocks.common.util;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
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
     * Checks if the player is holding the given Item and that it's got the same metadata
     * @param player The player
     * @param item The item to check for
     * @param metadata The metadata value of the item to check agians
     * @return True if player is holding it and false if not
     */
    public static boolean playerIsHoldingItemWithMetadata(EntityPlayer player, Item item, int metadata)
    {
        if (item != null && metadata >= 0)
        {
            return player.getHeldItem().getItem() == item && player.getHeldItem().getItemDamage() == metadata;
        }
        return false;
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
