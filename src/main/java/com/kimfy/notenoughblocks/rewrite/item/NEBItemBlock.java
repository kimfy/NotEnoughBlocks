package com.kimfy.notenoughblocks.rewrite.item;

import com.kimfy.notenoughblocks.rewrite.block.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;

public class NEBItemBlock extends ItemBlock
{
    protected final Block block;
    
    public NEBItemBlock(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.block = block;
    }
    
    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        int metadata = is.getItemDamage();
        return this.getUnlocalizedName() + "_" + metadata;
    }
    
    /**
     * Gets an icon index based on an item's damage value
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int metadata)
    {
        return block.getIcon(2, metadata);
    }

    /* Stops Forge from complaining about missing item textures */
    @Override
    protected String getIconString()
    {
        return "apple";
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        int metadata = p_82790_1_.getItemDamage();

        if (!(block instanceof NEBBlockDoor))
        {
            if (((IBlockProperties)block).getData().get(metadata).getRenderColor() > -1)
            {
                if (block instanceof NEBBlockDoublePlant || block instanceof NEBBlockTallGrass)
                {
                    return ColorizerGrass.getGrassColor(0.5D, 1.0D);
                }
                else if (block instanceof NEBBlockReed)
                {
                    return ((IBlockProperties) block).getData().get(metadata).getRenderColor();
                }
                return ((IBlockProperties) block).getData().get(metadata).getRenderColor();
            }
        }
        return super.getColorFromItemStack(p_82790_1_, p_82790_2_);
    }
}