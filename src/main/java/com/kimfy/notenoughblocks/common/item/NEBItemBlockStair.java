package com.kimfy.notenoughblocks.common.item;

import net.minecraft.block.Block;

public class NEBItemBlockStair extends NEBItemBlock
{
    public NEBItemBlockStair(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return 0;
    }
}
