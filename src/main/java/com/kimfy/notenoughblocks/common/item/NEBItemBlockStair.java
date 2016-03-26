package com.kimfy.notenoughblocks.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

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

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return this.getUnlocalizedName();
    }
}
