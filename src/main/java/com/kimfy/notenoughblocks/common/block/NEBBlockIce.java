package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class NEBBlockIce extends NEBBlock implements IBlockProperties
{
    public NEBBlockIce(Material material, List<BlockJson> data)
    {
        super(material, data);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer()
    {
        return Blocks.ice.getBlockLayer();
    }

    //@SideOnly(Side.CLIENT)
    //public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    //{
    //    IBlockState iblockstate = worldIn.getBlockState(pos);
    //    Block block = iblockstate.getBlock();
//
    //    if (this == Blocks.glass || this == Blocks.stained_glass)
    //    {
    //        if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate)
    //        {
    //            return true;
    //        }
//
    //        if (block == this)
    //        {
    //            return false;
    //        }
    //    }
//
    //    return !this.ignoreSimilarity && block == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    //}
}
