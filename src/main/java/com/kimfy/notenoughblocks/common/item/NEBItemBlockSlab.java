package com.kimfy.notenoughblocks.common.item;

import com.kimfy.notenoughblocks.common.block.NEBBlockSlabDouble;
import com.kimfy.notenoughblocks.common.block.NEBBlockSlabHalf;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

public class NEBItemBlockSlab extends ItemSlab
{
    public NEBItemBlockSlab(Block block, NEBBlockSlabHalf singleSlab, NEBBlockSlabDouble doubleSlab, Boolean stacked)
    {
        super(block, singleSlab, doubleSlab);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int metadata = stack.getMetadata();
        return this.getUnlocalizedName() + "_" + String.valueOf(metadata);
    }
}
