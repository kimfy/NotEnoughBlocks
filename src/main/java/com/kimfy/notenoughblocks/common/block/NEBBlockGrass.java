package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.MinecraftUtilities;
import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class NEBBlockGrass extends Block implements IGrowable, IBlockProperties
{
    private final ModPropertyInteger VARIANT;

    @Delegate
    private final BlockAgent<NEBBlockGrass> agent;

    public NEBBlockGrass(Material material, List<BlockJson> data)
    {
        super(material);
        this.setTickRandomly(true);
        this.agent = new BlockAgent<>(this, data);

        int variants = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", variants);
        this.addBlockStateProperties(new IProperty[]{VARIANT, BlockGrass.SNOWY});
        MinecraftUtilities.overwriteBlockState(this);
        this.setupStates();
    }

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState().withProperty(BlockGrass.SNOWY, false).withProperty(VARIANT, 0);
        this.setDefaultState(blockState);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return Blocks.grass.getBlockState();
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
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    // BlockGrass
    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return state.withProperty(BlockGrass.SNOWY, block == Blocks.snow || block == Blocks.snow_layer);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        Blocks.grass.updateTick(worldIn, pos, state, rand);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    // Implements IGrowable
    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        Blocks.grass.grow(worldIn, rand, pos, state);
    }
}