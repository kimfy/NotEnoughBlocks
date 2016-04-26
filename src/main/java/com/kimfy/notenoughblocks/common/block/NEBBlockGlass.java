package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.MinecraftUtilities;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

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
    private final ModPropertyInteger VARIANT;

    @Delegate
    private final BlockAgent<NEBBlockGlass> agent;

    public NEBBlockGlass(Material material, List<BlockJson> data)
    {
        super(material, false);
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
        return Blocks.glass.getBlockState();
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

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return this.isStained() ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random)
    {
        return 1;
    }
}
