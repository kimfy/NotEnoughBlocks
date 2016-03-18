package com.kimfy.notenoughblocks.rewrite.item;

import com.kimfy.notenoughblocks.rewrite.block.NEBBlockSlab;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class NEBItemBlockSlab extends ItemSlab
{
    private final boolean fullSlab;
    private final NEBBlockSlab blockSlab;
    private final NEBBlockSlab blockFullSlab;

    public NEBItemBlockSlab(Block block, NEBBlockSlab blockSlab, NEBBlockSlab blockFullSlab, Boolean fullSlab)
    {
        super(block, blockSlab, blockFullSlab, fullSlab);
        this.fullSlab = fullSlab;
        this.blockSlab = blockSlab;
        this.blockFullSlab = blockFullSlab;
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        return this.getUnlocalizedName() + "_" + (is.getItemDamage() & 7);
    }

    @Override
    public IIcon getIconFromDamage(int metadata)
    {
        return Block.getBlockFromItem(this).getIcon(2, metadata);
    }
}
