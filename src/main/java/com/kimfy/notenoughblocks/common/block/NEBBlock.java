package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.block.EnumSoundType;
import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class NEBBlock  extends Block implements IBlockProperties
{
    private final ModPropertyInteger VARIANT;
    private final BlockState BLOCKSTATE_REAL;

    @Delegate(excludes = Excludes.class)
    private final BlockAgent<NEBBlock> agent;

    public NEBBlock(Material material, List<BlockJson> data)
    {
        super(material);
        this.agent = new BlockAgent<>(this);

        int blockCount = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", blockCount);
        this.BLOCKSTATE_REAL = createRealBlockState(VARIANT);
        this.setupStates();
    }

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState().withProperty(VARIANT, 0);
        blockState = blockState.withProperty(VARIANT, 0);
        this.setDefaultState(blockState);
    }

    @Override
    public BlockState getBlockState()
    {
        return this.BLOCKSTATE_REAL;
    }

    private BlockState createRealBlockState(ModPropertyInteger property)
    {
        return new BlockState(this, new IProperty[]{ property });
    }

    @Override
    protected BlockState createBlockState()
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
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    /**
     * Methods that should not be forwarded
     * to the delegate/agent when called
     */
    private interface Excludes
    {
        int damageDropped(IBlockState blockState);
        ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player);
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
        block.setStepSound(EnumSoundType.get(modelBlock.stepSound).getSoundType());

        return block;
    }
}