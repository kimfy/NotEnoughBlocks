package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.MinecraftUtilities;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.List;

public class NEBBlockFence extends BlockFence implements IBlockProperties
{
    private final ModPropertyInteger VARIANT;

    @Delegate
    private final BlockAgent<NEBBlockFence> agent;

    public NEBBlockFence(Material material, List<BlockJson> data)
    {
        super(material, MapColor.adobeColor);
        this.agent = new BlockAgent<>(this, data);
        int variants = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", variants);
        this.addBlockStateProperties(new IProperty[]{NORTH, EAST, SOUTH, WEST, VARIANT});
        MinecraftUtilities.overwriteBlockState(this);
        this.setupStates();
    }

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState()
                .withProperty(NORTH, false)
                .withProperty(EAST, false)
                .withProperty(SOUTH, false)
                .withProperty(WEST, false)
                .withProperty(VARIANT, 0);
        this.setDefaultState(blockState);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return Blocks.oak_fence.getBlockState();
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
}