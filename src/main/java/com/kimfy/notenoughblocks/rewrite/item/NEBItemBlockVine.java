package com.kimfy.notenoughblocks.rewrite.item;

import com.kimfy.notenoughblocks.rewrite.block.IBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class NEBItemBlockVine extends ItemBlock
{
    protected final Block block;

    public NEBItemBlockVine(Block block)
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

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int metadata)
    {
        return ((IBlockProperties) block).getData().get(0).getRenderColor();
    }
}
