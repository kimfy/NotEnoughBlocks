package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.List;

public class NEBBlockWall extends BlockWall implements IBlockProperties
{
    private final Material blockMaterial;
    private final ModPropertyInteger VARIANT;
    private final BlockStateContainer BLOCKSTATE_REAL;

    @Delegate
    private final BlockAgent<NEBBlockWall> agent;

    public NEBBlockWall(Material material, List<BlockJson> data)
    {
        super(Blocks.stone);
        this.blockMaterial = material;
        this.agent = new BlockAgent<>(this, data);

        int blockCount = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", blockCount);
        this.BLOCKSTATE_REAL = createRealBlockState();
        this.setupStates();
    }

    //@Override
    //public Material getMaterial()
    //{
    //    return this.blockMaterial;
    //}

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState()
                .withProperty(UP, false)
                .withProperty(NORTH, false)
                .withProperty(EAST, false)
                .withProperty(SOUTH, false)
                .withProperty(WEST, false)
                .withProperty(VARIANT, 0);
        this.setDefaultState(blockState);
    }

    @Override
    public BlockStateContainer getBlockState()
    {
        return this.BLOCKSTATE_REAL;
    }

    private BlockStateContainer createRealBlockState()
    {
        return new BlockStateContainer(this, new IProperty[]{ UP, NORTH, EAST, SOUTH, WEST, VARIANT});
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return Blocks.cobblestone_wall.getBlockState();
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

    ///**
    // * Get the actual Block state of this Block at the given position. This applies properties not visible in the
    // * metadata, such as fence connections.
    // */
    //public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    //{
    //    return state.withProperty(UP, !worldIn.isAirBlock(pos.up())).withProperty(NORTH, this.canConnectTo(worldIn, pos.north())).withProperty(EAST, this.canConnectTo(worldIn, pos.east())).withProperty(SOUTH, this.canConnectTo(worldIn, pos.south())).withProperty(WEST, this.canConnectTo(worldIn, pos.west())).withProperty(VARIANT, state.getValue(VARIANT));
    //}
}