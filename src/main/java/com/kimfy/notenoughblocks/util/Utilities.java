package com.kimfy.notenoughblocks.util;

import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import cpw.mods.fml.common.registry.GameRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

public class Utilities
{
    @Getter
    @AllArgsConstructor
    public enum EnumMaterial
    {
        AIR(Material.air),
        GRASS(Material.grass),
        GROUND(Material.ground),
        WOOD(Material.wood),
        ROCK(Material.rock),
        IRON(Material.iron),
        ANVIL(Material.anvil),
        WATER(Material.water),
        LAVA(Material.lava),
        LEAVES(Material.leaves),
        PLANTS(Material.leaves),
        VINE(Material.vine),
        SPONGE(Material.sponge),
        CLOTH(Material.cloth),
        FIRE(Material.fire),
        SAND(Material.sand),
        CIRCUITS(Material.circuits),
        CARPET(Material.carpet),
        GLASS(Material.glass),
        REDSTONE_LIGHT(Material.redstoneLight),
        TNT(Material.tnt),
        CORAL(Material.coral),
        ICE(Material.ice),
        PACKED_ICE(Material.packedIce),
        SNOW(Material.snow),
        CRAFTED_SNOW(Material.craftedSnow),
        CACTUS(Material.cactus),
        CLAY(Material.clay),
        GOURD(Material.gourd),
        DRAGON_EGG(Material.dragonEgg),
        PORTAL(Material.portal),
        CAKE(Material.cake),
        WEB(Material.web),
        PISTON(Material.piston);
        
        private Material material;
    }
    
    @Getter
    @AllArgsConstructor
    public enum EnumSoundType
    {
    	STONE(Block.soundTypeStone),
    	WOOD(Block.soundTypeWood),
    	GRAVEL(Block.soundTypeGravel),
    	GRASS(Block.soundTypeGrass),
    	PISTON(Block.soundTypePiston),
    	METAL(Block.soundTypeMetal),
    	GLASS(Block.soundTypeGlass),
    	CLOTH(Block.soundTypeCloth),
    	SAND(Block.soundTypeSand),
    	SNOW(Block.soundTypeSnow),
    	LADDER(Block.soundTypeLadder),
    	ANVIL(Block.soundTypeAnvil);
    	
    	private Block.SoundType soundType;
    }
    
    @Getter
    @AllArgsConstructor
    public enum EnumCreativeTab
    {
    	BLOCKS(CreativeTabs.tabBlock),
    	BUILDINGBLOCKS(CreativeTabs.tabBlock),
    	DECORATIONS(CreativeTabs.tabDecorations),
    	REDSTONE(CreativeTabs.tabRedstone),
    	TRANSPORT(CreativeTabs.tabTransport),
    	MISC(CreativeTabs.tabMisc),
    	SEARCH(CreativeTabs.tabAllSearch),
    	FOOD(CreativeTabs.tabFood),
    	TOOLS(CreativeTabs.tabTools),
    	COMBAT(CreativeTabs.tabCombat),
    	BREWING(CreativeTabs.tabBrewing),
    	MATERIALS(CreativeTabs.tabMaterials),
    	INVENTORY(CreativeTabs.tabInventory);
    	
    	private CreativeTabs creativeTab;
    }
    
    @Getter
    @AllArgsConstructor
    public enum EnumSensitivity
    {
        EVERYTHING(BlockPressurePlate.Sensitivity.everything),
        MOBS(BlockPressurePlate.Sensitivity.mobs),
        PLAYERS(BlockPressurePlate.Sensitivity.players);
        
        private BlockPressurePlate.Sensitivity sensitivity;
    }

    @Getter
    @AllArgsConstructor
    public enum EnumBiome
    {
        OCEAN(BiomeGenBase.ocean),
        PLAINS(BiomeGenBase.plains),
        DESERT(BiomeGenBase.desert),
        EXTREMEHILLS(BiomeGenBase.extremeHills),
        FOREST(BiomeGenBase.forest),
        TAIGA(BiomeGenBase.taiga),
        SWAMPLAND(BiomeGenBase.swampland),
        RIVER(BiomeGenBase.river),
        HELL(BiomeGenBase.hell),
        SKY(BiomeGenBase.sky),
        FROZENOCEAN(BiomeGenBase.frozenOcean),
        FROZENRIVER(BiomeGenBase.frozenRiver),
        ICEPLAINS(BiomeGenBase.icePlains),
        ICEMOUNTAINS(BiomeGenBase.iceMountains),
        MUSHROOMISLAND(BiomeGenBase.mushroomIsland),
        MUSHROOMISLANDSHORE(BiomeGenBase.mushroomIslandShore),
        BEACH(BiomeGenBase.beach),
        DESERTHILLS(BiomeGenBase.desertHills),
        FORESTHILLS(BiomeGenBase.forestHills),
        TAIGAHILLS(BiomeGenBase.taigaHills),
        EXTREMEHILLSEDGE(BiomeGenBase.extremeHillsEdge),
        JUNGLE(BiomeGenBase.jungle),
        JUNGLEHILLS(BiomeGenBase.jungleHills),
        JUNGLEEDGE(BiomeGenBase.jungleEdge),
        DEEPOCEAN(BiomeGenBase.deepOcean),
        STONEBEACH(BiomeGenBase.stoneBeach),
        COLDBEACH(BiomeGenBase.coldBeach),
        BIRCHFOREST(BiomeGenBase.birchForest),
        BIRCHFORESTHILLS(BiomeGenBase.birchForestHills),
        ROOFEDFOREST(BiomeGenBase.roofedForest),
        COLDTAIGA(BiomeGenBase.coldTaiga),
        COLDTAIGAHILLS(BiomeGenBase.coldTaigaHills),
        MEGATAIGA(BiomeGenBase.megaTaiga),
        MEGATAIGAHILLS(BiomeGenBase.megaTaigaHills),
        EXTREMEHILLSPLUS(BiomeGenBase.extremeHillsPlus),
        SAVANNA(BiomeGenBase.savanna),
        SAVANNAPLATEAU(BiomeGenBase.savanna),
        MESA(BiomeGenBase.mesa),
        MESAPLATEAU_F(BiomeGenBase.mesaPlateau_F),
        MESAPLATEAU(BiomeGenBase.mesaPlateau);

        private BiomeGenBase biome;
    }
    
    /**
     * Deep clones the given List. Had to implement
     * this because Java's default cloning sucks ass and
     * only makes a shallow copy of the Object.
     * 
     * @return A deep-copied <code> List <JsonBlock> </code>
     */
    public static List<JsonBlock> deepCloneList(List<JsonBlock> list) {
        List<JsonBlock> cloned = new ArrayList<JsonBlock>(list.size());
        
        for (JsonBlock block : list) {
            cloned.add( (JsonBlock) SerializationUtils.clone(block) );
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
