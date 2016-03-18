package com.kimfy.notenoughblocks.rewrite.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class NEBItemBlockSnow extends ItemBlock
{
    protected Block block;
    
    public NEBItemBlockSnow(Block block)
    {
        super(block);
        this.block = block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!player.canPlayerEdit(x, y, z, p_77648_7_, stack))
        {
            return false;
        }
        else
        {
            Block block = world.getBlock(x, y, z);

            if (block == this.block)
            {
                int i1 = world.getBlockMetadata(x, y, z);
                int j1 = i1 & 7;

                if (j1 <= 6 && world.checkNoEntityCollision(this.block.getCollisionBoundingBoxFromPool(world, x, y, z)) && world.setBlockMetadataWithNotify(x, y, z, j1 + 1 | i1 & -8, 2))
                {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.block.stepSound.func_150496_b(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getPitch() * 0.8F);
                    --stack.stackSize;
                    return true;
                }
            }
            
            block = world.getBlock(x, y - 1, z);
            
            if(this.block.canPlaceBlockAt(world, x, y, z))
            {
                int meta = world.getBlockMetadata(x, y, z);
                
                if (world.checkNoEntityCollision(this.block.getCollisionBoundingBoxFromPool(world, x, y, z)) && world.setBlock(x, y + 1, z, this.block, 0, 2))
                {
                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.block.stepSound.func_150496_b(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getPitch() * 0.8F);
                    --stack.stackSize;
                    return true;
                } 
            }
            
            return super.onItemUse(stack, player, world, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
        }
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
