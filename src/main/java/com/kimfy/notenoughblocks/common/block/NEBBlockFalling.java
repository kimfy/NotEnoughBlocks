package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class NEBBlockFalling extends BlockFalling implements IBlockProperties
{
    private final ModPropertyInteger VARIANT;
    private final BlockStateContainer BLOCKSTATE_REAL;

    @Delegate
    private final BlockAgent<NEBBlockFalling> agent;

    public NEBBlockFalling(Material material, List<BlockJson> data)
    {
        super(material);
        this.agent = new BlockAgent<>(this, data);

        int blockCount = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", blockCount);
        this.BLOCKSTATE_REAL = createRealBlockState();
        this.setupStates();
    }

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState().withProperty(VARIANT, 0);
        blockState = blockState.withProperty(VARIANT, 0);
        this.setDefaultState(blockState);
    }

    @Override
    public BlockStateContainer getBlockState()
    {
        return this.BLOCKSTATE_REAL;
    }

    private BlockStateContainer createRealBlockState()
    {
        return new BlockStateContainer(this, new IProperty[]{ VARIANT });
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return Blocks.air.getBlockState();
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
}