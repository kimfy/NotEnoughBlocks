package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.block.properties.ModPropertyInteger;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class NEBBlockRotating extends BlockLog implements IBlockProperties
{
    private final ModPropertyInteger VARIANT;
    private final BlockState BLOCKSTATE_REAL;
    private final Material material;

    @Delegate
    private final BlockAgent<NEBBlockRotating> agent;

    public NEBBlockRotating(Material material, List<BlockJson> data)
    {
        super();
        this.material = material;
        this.agent = new BlockAgent<>(this, data);

        int blockCount = data.size();
        this.VARIANT = ModPropertyInteger.create("metadata", blockCount);
        this.BLOCKSTATE_REAL = createRealBlockState();
        this.setupStates();
    }

    @Override
    public Material getMaterial()
    {
        return material;
    }

    private void setupStates()
    {
        IBlockState blockState = getBlockState().getBaseState().withProperty(LOG_AXIS, EnumAxis.Y).withProperty(VARIANT, 0);
        this.setDefaultState(blockState);
    }

    @Override
    public BlockState getBlockState()
    {
        return this.BLOCKSTATE_REAL;
    }

    private BlockState createRealBlockState()
    {
        return new BlockState(this, new IProperty[]{ LOG_AXIS, VARIANT });
    }

    @Override
    protected BlockState createBlockState()
    {
        return Blocks.air.getBlockState();
    }


    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, meta & 3/*(meta & 3) + 4*//*BlockPlanks.EnumType.byMetadata((meta & 3) + 4)*/);

        switch (meta & 12)
        {
            case 0:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 4:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 8:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return iblockstate;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(VARIANT);

        switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS))
        {
            case X:
                i |= 4;
                break;
            case Z:
                i |= 8;
                break;
            case NONE:
                i |= 12;
        }

        return i;
    }
    protected ItemStack createStackedBlock(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, (state.getValue(VARIANT)));
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(IBlockState state)
    {
        return state.getValue(VARIANT);
    }
}
