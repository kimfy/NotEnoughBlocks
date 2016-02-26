package com.kimfy.notenoughblocks.common.item;

import com.kimfy.notenoughblocks.common.block.NEBBlockGrass;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NEBItemBlock extends ItemBlock
{
    private final boolean isColoredBlock;

    public NEBItemBlock(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.isColoredBlock = block instanceof NEBBlockGrass;
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int metadata = stack.getMetadata();
        return this.getUnlocalizedName() + "_" + String.valueOf(metadata);
    }

    /*
        Colored item (Grass Block)
     */

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        return isColoredBlock ? block.getRenderColor(block.getStateFromMeta(stack.getMetadata())) : super.getColorFromItemStack(stack, renderPass);
        //return this.coloredBlock.getRenderColor(this.coloredBlock.getStateFromMeta(stack.getMetadata()));
    }
}
