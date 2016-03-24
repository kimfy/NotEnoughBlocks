package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class NEBBlockLayer extends BlockSnow implements IBlockProperties
{
    private final BlockStateContainer BLOCKSTATE_REAL;
    public static final PropertyInteger LAYERS = BlockSnow.LAYERS;

    @Delegate
    private final BlockAgent<NEBBlockLayer> agent;

    public NEBBlockLayer(Material material, List<BlockJson> data)
    {
        super();
        this.agent = new BlockAgent<>(this, data);
        this.setTickRandomly(true);

        this.BLOCKSTATE_REAL = createRealBlockState();
        this.setupStates();
    }

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState().withProperty(LAYERS, 1);
        this.setDefaultState(blockState);
    }

    @Override
    public BlockStateContainer getBlockState()
    {
        return this.BLOCKSTATE_REAL;
    }

    private BlockStateContainer createRealBlockState()
    {
        return new BlockStateContainer(this, LAYERS);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return Blocks.snow_layer.getBlockState();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(LAYERS, (meta & 7) + 1);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LAYERS) - 1;
    }
}
