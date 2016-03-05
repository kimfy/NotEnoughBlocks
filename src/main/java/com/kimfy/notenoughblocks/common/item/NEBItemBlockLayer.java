package com.kimfy.notenoughblocks.common.item;

import com.kimfy.notenoughblocks.common.block.NEBBlockLayer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class NEBItemBlockLayer extends ItemBlock
{
    private final Block block;

    public NEBItemBlockLayer(Block block)
    {
        super(block);
        this.block = block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
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

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!playerIn.canPlayerEdit(pos, side, stack))
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            BlockPos blockpos = pos;

            if ((side != EnumFacing.UP || block != this.block) && !block.isReplaceable(worldIn, pos))
            {
                blockpos = pos.offset(side);
                iblockstate = worldIn.getBlockState(blockpos);
                block = iblockstate.getBlock();
            }

            if (block == this.block)
            {
                int blockLayer = iblockstate.getValue(NEBBlockLayer.LAYERS);

                if (blockLayer <= 7)
                {
                    IBlockState newBlockState = iblockstate.withProperty(NEBBlockLayer.LAYERS, blockLayer + 1);
                    AxisAlignedBB axisalignedbb = this.block.getCollisionBoundingBox(worldIn, blockpos, newBlockState);

                    if (axisalignedbb != null && worldIn.checkNoEntityCollision(axisalignedbb) && worldIn.setBlockState(blockpos, newBlockState, 2))
                    {
                        worldIn.playSoundEffect((double)((float)blockpos.getX() + 0.5F), (double)((float)blockpos.getY() + 0.5F), (double)((float)blockpos.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
                        --stack.stackSize;
                        return true;
                    }
                }
            }
            return super.onItemUse(stack, playerIn, worldIn, blockpos, side, hitX, hitY, hitZ);
        }
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
        IBlockState state = world.getBlockState(pos);
        return (state.getBlock() != this.block || ((Integer)state.getValue(NEBBlockLayer.LAYERS)) > 7) ? super.canPlaceBlockOnSide(world, pos, side, player, stack) : true;
    }
}