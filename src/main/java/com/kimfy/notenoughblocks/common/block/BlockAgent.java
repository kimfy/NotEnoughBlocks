package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class BlockAgent<T extends Block & IBlockProperties> implements IBlockProperties
{
    T block;
    List<BlockJson> data;
    int blockCount;

    public BlockAgent(T block)
    {
        this.block = block;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        for (int i = 0; i < blockCount; i++)
        {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }

    /**
     * Return true from this function if the player with silk touch can harvest this block directly, and not it's normal drops.
     *
     * @param world The world
     * @param pos Block position in world
     * @param state current block state
     * @param player The player doing the harvesting
     * @return True if the block can be directly harvested using silk touch
     */
    //public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    //{
    //    return block.canSilkHarvest(world, pos, state, player); // TODO Implement this
    //}

    @Getter
    private boolean isBeaconBase;
    @Getter
    private boolean isOpaque;
    @Getter
    private boolean isStained;
    /** The index represents the metadata */
    private List<Boolean> isSilkTouch = new ArrayList<>(16);
    private int     lightOpacity;
    private float   slipperiness;

    @Override
    public void setBeaconBaseable(boolean isBeaconBase)
    {
        this.isBeaconBase = isBeaconBase;
    }

    @Override
    public void setBlockOpaqueness(boolean isOpaque)
    {
        this.isOpaque = isOpaque;
    }

    @Override
    public void setBlockStainable(boolean isStained)
    {
        this.isStained = isStained;
    }

    @Override
    public void isSilkTouchable(boolean isSilkTouch, int metadata)
    {
        this.isSilkTouch.add(metadata, isSilkTouch);
    }

    @Override
    public void setBlockLightOpacity(int lightOpacity)
    {
        this.lightOpacity = lightOpacity;
    }

    @Override
    public void setSlipperiness(float slipperiness)
    {
        this.slipperiness = slipperiness;
        block.slipperiness = slipperiness;
    }

    @Override
    public void setData(List<BlockJson> data)
    {
        this.blockCount = data.size();
        this.data = data;
    }

    @Override
    public List<BlockJson> getData()
    {
        return this.data;
    }
}
