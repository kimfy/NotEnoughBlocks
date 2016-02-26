package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Look into ASM'ing {@link net.minecraft.block.BlockBeacon#updateColorAsync} to allow
 * stained blocks here to change the color of it. The way it could be done is get the
 * color from the block out of it's display name or a new field in BlockJson(color)
 * maybe? This could be a wanted feature at some point, not a clue. Would be hilarious
 * if no one ever uses this mod and I'm just writing all this code for nothing, lol.
 *
 * - kimfy
 */
public class NEBBlockGlass extends BlockGlass implements IBlockProperties
{
    private final ModPropertyInteger PROPERTY_METADATA;
    private final BlockState BLOCK_STATE_REAL;

    @Delegate(excludes = Excludes.class)
    private final BlockAgent<NEBBlockGlass> agent;

    public NEBBlockGlass(Material material, List<BlockJson> data)
    {
        super(material, false);
        this.agent = new BlockAgent<>(this);

        int blockCount = data.size();
        this.PROPERTY_METADATA = ModPropertyInteger.create("metadata", blockCount);
        this.BLOCK_STATE_REAL = createRealBlockState(PROPERTY_METADATA);
        this.setupStates();
    }


    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState().withProperty(PROPERTY_METADATA, 0);
        blockState = blockState.withProperty(PROPERTY_METADATA, 0);
        this.setDefaultState(blockState);
    }

    @Override
    public BlockState getBlockState()
    {
        return this.BLOCK_STATE_REAL;
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
        return getDefaultState().withProperty(PROPERTY_METADATA, metadata);
    }

    @Override
    public int getMetaFromState(IBlockState blockState)
    {
        return blockState.getValue(PROPERTY_METADATA);
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

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return this.isStained() ? EnumWorldBlockLayer.TRANSLUCENT : EnumWorldBlockLayer.CUTOUT;
    }
}
