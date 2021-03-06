package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.ServerProxy;
import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.file.json.Json;
import com.kimfy.notenoughblocks.common.item.NEBItemBlockSlab;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.Utilities;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public abstract class NEBBlockSlab extends BlockSlab implements IBlockProperties
{
    private final ModPropertyInteger VARIANT;

    @Delegate(excludes = Excludes.class)
    private final BlockAgent<NEBBlockSlab> agent;

    public NEBBlockSlab(Material materialIn, List<BlockJson> data)
    {
        super(materialIn);
        this.agent = new BlockAgent<>(this, data);

        int count = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", count);
        this.BLOCK_STATE_REAL = createRealBlockState();
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
        return this.isDouble() ? Blocks.DOUBLE_STONE_SLAB.getBlockState() : Blocks.STONE_SLAB.getBlockState();
    }

    private BlockStateContainer createRealBlockState()
    {
        //return this.isDouble() ? new BlockState(this, new IProperty[] {BlockStoneSlab.SEAMLESS, VARIANT}) : new BlockState(this, new IProperty[] {HALF, VARIANT});
        return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, new IProperty[] {HALF, VARIANT});
    }

    private final BlockStateContainer BLOCK_STATE_REAL;

    @Override
    public BlockStateContainer getBlockState()
    {
        return BLOCK_STATE_REAL;
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
        return state.getValue(VARIANT);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, this.damageDropped(state));
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack)
    {
        return stack.getMetadata() & 7;
    }

    /**
     * Registers both half and double slab with items. Register method in NEBItemBlockSlab is empty because I've put
     * the implementation here. I do not want to deal with this slab stuff anymore, it's horrendous and my system isn't,
     * the most expandable either, but I don't care, it's working, smile!
     */
    public void register(ResourceLocation registryName)
    {
        this.setRegistryName(registryName);
        NEBItemBlockSlab   itemSlabHalf           = new NEBItemBlockSlab(this);
        ResourceLocation   slabDoubleRegistryName = new ResourceLocation(Constants.MOD_ID, this.getRegistryName().getResourcePath().replace("_slab_", "_slab_double_"));
        NEBBlockSlabDouble blockSlabDouble        = new NEBBlockSlabDouble(this.blockMaterial, Utilities.deepCloneList(this.getData()));
        NEBItemBlockSlab   itemSlabDouble         = new NEBItemBlockSlab(blockSlabDouble);

        itemSlabHalf.setSingleSlab(this);
        itemSlabHalf.setDoubleSlab(blockSlabDouble);

        itemSlabDouble.setSingleSlab(this);
        itemSlabDouble.setDoubleSlab(blockSlabDouble);

        GameRegistry.register(this);
        GameRegistry.register(itemSlabHalf, this.getRegistryName());

        GameRegistry.register(blockSlabDouble, slabDoubleRegistryName);
        GameRegistry.register(itemSlabDouble, slabDoubleRegistryName);

        ServerProxy.JSON_PROCESSOR.setBlockProperties(this, this.getData(), this.getRegistryName());
        ServerProxy.JSON_PROCESSOR.setBlockProperties(blockSlabDouble, blockSlabDouble.getData(), blockSlabDouble.getRegistryName());
    }

    private interface Excludes
    {
        /**
         * We are excluding this method and adding a custom implementation because the Slab block has two blocks it
         * needs to register - which only adds clutter in
         * {@link com.kimfy.notenoughblocks.common.file.json.JsonProcessor#registerBlocks(List, Json, int)}
         */
        void register(ResourceLocation registryName);
    }
}