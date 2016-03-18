package com.kimfy.notenoughblocks.rewrite.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class NEBItemBlockTorch extends ItemBlock
{
    protected final Block block;

    public NEBItemBlockTorch(Block block)
    {
        super(block);
        this.block = block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public IIcon getIconFromDamage(int metadata)
    {
        return block.getIcon(2, 0);
    }

    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        return this.getUnlocalizedName() + "_" + 0;
    }
}
