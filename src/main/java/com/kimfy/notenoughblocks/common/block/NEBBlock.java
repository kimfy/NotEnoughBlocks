package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.block.EnumMaterial;
import com.kimfy.notenoughblocks.common.util.block.EnumSoundType;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class NEBBlock extends Block implements IBlockProperties
{
    public final ModPropertyInteger VARIANT;
    private final BlockStateContainer BLOCKSTATE_REAL;
    private final Shape blockShape;

    @Delegate
    private final BlockAgent<NEBBlock> agent;

    public NEBBlock(Material material, List<BlockJson> data)
    {
        super(material);
        this.agent = new BlockAgent<>(this, data);
        this.blockShape = agent.getModelBlock().getRealShape();

        int blockCount = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", blockCount);
        this.BLOCKSTATE_REAL = createRealBlockState();
        this.setupStates();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            playerIn.addChatComponentMessage(new TextComponentString("Material: " + EnumMaterial.toString(state.getMaterial()) ));
        }
        return false;
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

    /**
     * Used in stairs creation
     */
    protected static Block buildModBlock(Material material, List<BlockJson> data)
    {
        NEBBlock block = new NEBBlock(material, data);
        BlockJson modelBlock = data.get(0);
        block.setHardness(modelBlock.hardness);
        block.setResistance(modelBlock.resistance);
        block.setBlockSoundType(EnumSoundType.get(modelBlock.stepSound).getSoundType());

        return block;
    }

    public Shape getBlockShape()
    {
        return this.blockShape;
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

    @Override
    public boolean isFullyOpaque(IBlockState state)
    {
        if (getBlockShape() == Shape.ICE)
        {
            return false;
        }
        return super.isFullyOpaque(state);
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
}