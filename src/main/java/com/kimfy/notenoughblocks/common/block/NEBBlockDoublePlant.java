package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TODO: Improve this class
 * Needs adding:
 * 1. Sunflowers are fucked - the top part isn't connected to the bottom
 */
public class NEBBlockDoublePlant extends BlockDoublePlant implements IBlockProperties, IGrowable, IShearable, IPlantable
{
    private final BlockStateContainer BLOCKSTATE_REAL;
    private final ModPropertyInteger VARIANT;

    @Delegate
    private final BlockAgent<NEBBlockDoublePlant> agent;

    public NEBBlockDoublePlant(Material material, List<BlockJson> data)
    {
        super();
        this.agent = new BlockAgent<>(this, data);

        int blockCount = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", blockCount);
        this.BLOCKSTATE_REAL = createRealBlockState();
        this.setupStates();
    }

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState()
                .withProperty(VARIANT, 0)
                .withProperty(HALF, EnumBlockHalf.LOWER)
                .withProperty(FACING, EnumFacing.NORTH);
        this.setDefaultState(blockState);
    }

    @Override
    public BlockStateContainer getBlockState()
    {
        return this.BLOCKSTATE_REAL;
    }

    private BlockStateContainer createRealBlockState()
    {
        return new BlockStateContainer(this, VARIANT, HALF, FACING);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return Blocks.double_plant.getBlockState();
    }

    /* ========== BlockDoublePlant ========== */


    public int getVariant(IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == this)
        {
            iblockstate = this.getActualState(iblockstate, worldIn, pos);
            return iblockstate.getValue(VARIANT);
        }
        else
        {
            return 0;
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(VARIANT);
    }

    /**
     * Whether this IGrowable can grow
     */
    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        //BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = this.getVariant(worldIn, pos);
        int variant = this.getVariant(worldIn, pos);
        //return blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN;
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        spawnAsEntity(worldIn, pos, new ItemStack(this, 1, getVariant(worldIn, pos)));
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER) : this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(VARIANT, (meta & 7));//BlockDoublePlant.EnumPlantType.byMetadata(meta & 7));
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.down());

            if (iblockstate.getBlock() == this)
            {
                state = state.withProperty(VARIANT, iblockstate.getValue(VARIANT));
            }
        }

        return state;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER ? 8 | (state.getValue(FACING)).getHorizontalIndex() : state.getValue(VARIANT);//((BlockDoublePlant.EnumPlantType)state.getValue(VARIANT)).getMeta();
    }

    // TODO: BlockJson#isShearable
    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.LOWER;
    }

    /**
     TODO: BlockJson#isShearable
     */
    @Override
    public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        //java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        //BlockDoublePlant.EnumPlantType type = (BlockDoublePlant.EnumPlantType)world.getBlockState(pos).getValue(VARIANT);
        //if (type == BlockDoublePlant.EnumPlantType.FERN) ret.add(new ItemStack(Blocks.tallgrass, 2, BlockTallGrass.EnumType.FERN.getMeta()));
        //if (type == BlockDoublePlant.EnumPlantType.GRASS) ret.add(new ItemStack(Blocks.tallgrass, 2, BlockTallGrass.EnumType.GRASS.getMeta()));
        List<ItemStack> ret = new ArrayList<>();
        return ret;
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        //Forge: Break both parts on the client to prevent the top part flickering as default type for a few frames.
        if (state.getBlock() ==  this && state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.LOWER && world.getBlockState(pos.up()).getBlock() == this)
            world.setBlockToAir(pos.up());
        return world.setBlockToAir(pos);
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(this);
    }
}
