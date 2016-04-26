package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.MinecraftUtilities;
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
import net.minecraft.world.World;

import java.util.List;

public class NEBBlockBush extends BlockBush implements IBlockProperties
{
    protected final ModPropertyInteger VARIANT;

    @Delegate
    private final BlockAgent<NEBBlockBush> agent;

    public NEBBlockBush(Material materialIn, List<BlockJson> data)
    {
        super(materialIn);
        this.agent = new BlockAgent<>(this, data);
        int variants = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", variants);
        this.addBlockStateProperty(VARIANT);
        MinecraftUtilities.overwriteBlockState(this);
        this.setupStates();
    }

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState().withProperty(VARIANT, 0);
        this.setDefaultState(blockState);
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
        return new ItemStack(this, 1, this.getMetaFromState(state));
    }
}