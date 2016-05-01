package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.MinecraftUtilities;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class NEBBlockPane extends BlockPane implements IBlockProperties
{
    private final boolean canDrop;
    private final ModPropertyInteger VARIANT;

    @Delegate
    private final BlockAgent<NEBBlockPane> agent;

    public NEBBlockPane(Material materialIn, List<BlockJson> data)
    {
        super(materialIn, true);
        this.canDrop = true; // What is this even
        this.agent = new BlockAgent<>(this, data);
        if (getModelBlock().getShape() == Shape.PANE)
        {
            this.isStainedCached = getModelBlock().isStained();
        }
        int variants = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", variants);
        this.addBlockStateProperties(new IProperty[] {NORTH, EAST, SOUTH, WEST, VARIANT});
        MinecraftUtilities.overwriteBlockState(this);
        this.setupStates();
    }

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState()
                .withProperty(VARIANT, 0)
                .withProperty(NORTH, false)
                .withProperty(EAST, false)
                .withProperty(SOUTH, false)
                .withProperty(WEST, false);
        this.setDefaultState(blockState);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return Blocks.glass_pane.getBlockState();
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

    /**
     * I have "cached" this value by setting it in the constructor instead of accessing
     * <code>
     *     this.getModelBlock().isStained()
     * </code>
     * every time NEBBlockPane#getBlockLayer is called. I honestly don't know if it's
     * faster or worth it to do this. If anyone with some knowledge could answer this
     * that'd be great.
     */
    private boolean isStainedCached;

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return isStainedCached ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT_MIPPED;
    }
}