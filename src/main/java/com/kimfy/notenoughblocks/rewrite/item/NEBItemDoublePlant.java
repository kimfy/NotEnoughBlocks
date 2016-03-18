package com.kimfy.notenoughblocks.rewrite.item;

import com.kimfy.notenoughblocks.rewrite.block.NEBBlockDoublePlant;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;

public class NEBItemDoublePlant extends ItemBlock
{
    protected final Block block;

    public NEBItemDoublePlant(Block block)
    {
        super(block);
        this.block = block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    /**
     * Gets an icon index based on an item's damage value
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int metadata)
    {
        //return (metadata & 7) == 0 ? ((NEBBlockDoublePlant) block).sunflowerIcons[0][2] : ((NEBBlockDoublePlant) block).getIcon(0, metadata & 7); // This works, kinda...
        return (metadata & 7) == 0 ? ((NEBBlockDoublePlant) block).sunflowerIcons[0][2] : ((NEBBlockDoublePlant) block).func_149888_a(true, metadata & 7); // Derived from the above one
    }

    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int metadata = itemStack.getItemDamage();
        if (metadata > 5) { return "Please, someone who knows how the double plant works, help me give this a name!"; }
        return this.getUnlocalizedName() + "_" + (metadata % 6);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int p_82790_2_)
    {
        int metadata = BlockDoublePlant.func_149890_d(itemStack.getItemDamage());
        return metadata != 2 && metadata != 3 ? super.getColorFromItemStack(itemStack, p_82790_2_) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }
}
