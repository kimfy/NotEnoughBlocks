package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public abstract class NEBBlockSlab extends BlockSlab implements IBlockProperties
{
    private final ModPropertyInteger VARIANT;

    @Delegate
    private final BlockAgent<NEBBlockSlab> agent;

    public NEBBlockSlab(Material materialIn, List<BlockJson> data)
    {
        super(materialIn);
        this.agent = new BlockAgent<>(this, data);

        int count = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", count);
        this.blockStateReal = createRealBlockState();

        this.setupStates();
    }

    public void setupStates()
    {
        IBlockState iblockstate = getBlockState().getBaseState();

        if (this.isDouble())
        {
            iblockstate = iblockstate.withProperty(VARIANT, 0);
        }
        else
        {
            iblockstate = iblockstate.withProperty(HALF, EnumBlockHalf.BOTTOM).withProperty(VARIANT, 0);
        }

        this.setDefaultState(iblockstate);
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        return this.getUnlocalizedName();
    }

    @Override
    public IProperty<?> getVariantProperty()
    {
        return VARIANT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, meta & 7);

        if (!this.isDouble())
        {
            iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockstate;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | (state.getValue(VARIANT));

        if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
        {
            i |= 8;
        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        //return this.isDouble() ? new BlockState(this, new IProperty[] {BlockStoneSlab.SEAMLESS, VARIANT}) : new BlockState(this, new IProperty[] {HALF, VARIANT});
        return Blocks.air.getBlockState();
    }

    private BlockStateContainer createRealBlockState()
    {
        //return this.isDouble() ? new BlockState(this, new IProperty[] {BlockStoneSlab.SEAMLESS, VARIANT}) : new BlockState(this, new IProperty[] {HALF, VARIANT});
        return this.isDouble() ? new BlockStateContainer(this, VARIANT): new BlockStateContainer(this, HALF, VARIANT);
    }

    private BlockStateContainer blockStateReal;

    @Override
    public BlockStateContainer getBlockState()
    {
        return blockStateReal;
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iBlockState = this.getStateFromMeta(meta).withProperty(VARIANT, meta & 7);
        if (!this.isDouble())
        {
            iBlockState = (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iBlockState.withProperty(HALF, EnumBlockHalf.BOTTOM) : iBlockState.withProperty(HALF, EnumBlockHalf.TOP));
        }
        return iBlockState;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        //return getMetaFromState(state);
        return state.getValue(VARIANT);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack)
    {
        return stack.getMetadata() & 7;
    }
}