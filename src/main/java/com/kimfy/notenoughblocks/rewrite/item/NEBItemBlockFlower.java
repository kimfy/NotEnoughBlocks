package com.kimfy.notenoughblocks.rewrite.item;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class NEBItemBlockFlower extends NEBItemBlock implements IBlock
{
    protected final Block block;
    
    public NEBItemBlockFlower(Block block)
    {
        super(block);
        this.block = block;
        this.setHasSubtypes(true);
    }
    
    @Override
    public Block getBlock()
    {
        return this.block;
    }
    
    @Override
    public IIcon getIconFromDamage(int metadata)
    {
        return this.block.getIcon(0, metadata);
    }
}
