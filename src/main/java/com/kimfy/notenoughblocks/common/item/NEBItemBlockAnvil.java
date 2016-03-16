package com.kimfy.notenoughblocks.common.item;

import net.minecraft.block.Block;

public class NEBItemBlockAnvil extends NEBItemBlock
{
    public NEBItemBlockAnvil(Block block)
    {
        super(block);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage << 2;
    }
}
