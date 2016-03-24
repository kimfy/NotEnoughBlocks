package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class NEBBlockBush extends BlockBush implements IBlockProperties
{
    protected final ModPropertyInteger VARIANT;
    private final BlockStateContainer BLOCKSTATE_REAL;

    @Delegate
    private final BlockAgent<NEBBlockBush> agent;

    public NEBBlockBush(Material materialIn, List<BlockJson> data)
    {
        super(materialIn);
        float f = 0.4F;
        //this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
        this.agent = new BlockAgent<>(this, data);

        int blockCount = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", blockCount);
        this.BLOCKSTATE_REAL = createRealBlockState();
        this.setupStates();
    }

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState().withProperty(VARIANT, 0);
        this.setDefaultState(blockState);
    }

    @Override
    public BlockStateContainer getBlockState()
    {
        return this.BLOCKSTATE_REAL;
    }

    private BlockStateContainer createRealBlockState()
    {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return Blocks.tallgrass.getBlockState();
    }

    @Override
    public IBlockState getStateFromMeta(int metadata)
    {
        return getDefaultState().withProperty(VARIANT, metadata);
    }

    @Override
    public int getMetaFromState(IBlockState blockState)
    {
        return blockState.getValue(VARIANT);
    }

    @Override
    public int damageDropped(IBlockState blockState)
    {
        return getMetaFromState(blockState);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    /* BlockTallGrass */

    //@SideOnly(Side.CLIENT)
    //public int getBlockColor()
    //{
    //    return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    //}
//
    //@SideOnly(Side.CLIENT)
    //public int getRenderColor(IBlockState state)
    //{
    //    if (state.getBlock() != this)
    //    {
    //        return super.getRenderColor(state);
    //    }
    //    else
    //    {
    //        return getData().get(state.getValue(VARIANT)).needsColoring() ? ColorizerGrass.getGrassColor(0.5D, 1.0D) : 16777215;
    //    }
    //}
//
    //@SideOnly(Side.CLIENT)
    //public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    //{
    //    return worldIn.getBiomeGenForCoords(pos).getGrassColorAtPos(pos);
    //}
}
