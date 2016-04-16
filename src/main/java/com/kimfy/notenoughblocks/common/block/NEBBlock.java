package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class NEBBlock extends Block implements IBlockProperties
{
    public final ModPropertyInteger VARIANT;
    private final BlockStateContainer BLOCKSTATE_REAL;
    private final Shape BLOCK_SHAPE;

    @Delegate
    private final BlockAgent<NEBBlock> agent;

    public NEBBlock(Material material, List<BlockJson> data)
    {
        super(material);
        this.agent = new BlockAgent<>(this, data);
        this.BLOCK_SHAPE = agent.getModelBlock().getRealShape();

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
        return blockState.getValue(VARIANT);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    public Shape getBlockShape()
    {
        return this.BLOCK_SHAPE;
    }

    /* ========== Layer / Render / Client ========== */

    @Override
    public BlockRenderLayer getBlockLayer()
    {
        if (getBlockShape() == Shape.ICE)
        {
            return BlockRenderLayer.TRANSLUCENT;
        }
        return super.getBlockLayer();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        Block block = state.getBlock();

        if (getBlockShape() == Shape.ICE)
        {
            if (block == this)
            {
                return false;
            }
        }
        return super.shouldSideBeRendered(state, worldIn, pos, side);
    }

    protected static class Builder
    {
        private Material material;
        private List<BlockJson> data;

        protected Builder()
        {
        }

        protected Block build()
        {
            NEBBlock block = new NEBBlock(this.material, this.data);
            BlockJson model = this.data.get(0);
            block.setHardness(model.getHardness());
            block.setResistance(model.getResistance());
            block.setBlockSoundType(model.getRealSoundType());
            return block;
        }

        protected Builder setMaterial(Material material)
        {
            this.material = material;
            return this;
        }

        protected Builder setData(List<BlockJson> data)
        {
            this.data = data;
            return this;
        }
    }
}