package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.NotEnoughBlocks;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BlockAgent<T extends Block & IBlockProperties> implements IBlockProperties
{
    T block;
    List<BlockJson> data;
    int blockCount;
    private final Shape blockShape;

    public BlockAgent(T block, List<BlockJson> data)
    {
        this.block = block;
        this.blockCount = data.size();
        this.data = data;
        this.blockShape = getModelBlock().getRealShape();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
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
     * @param world  The world
     * @param pos    Block position in world
     * @param state  current block state
     * @param player The player doing the harvesting
     * @return True if the block can be directly harvested using silk touch
     */
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        //int metadata = world.getBlockState(pos).getBlock().getDamageValue(world, pos);
        int metadata = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        //return getData().get(metadata).isSilkTouch(); // This should be safe, as long as all damageDropped methods are valid and legal
        return true; // TODO: Fix for 1.9
    }

    /**
     * Determines if this block can be used as the base of a beacon.
     *
     * @param worldObj The current world
     * @param pos      Block position in world
     * @param beacon   Beacon position in world
     * @return True, to support the beacon, and make it active with this block.
     */
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon)
    {
        return getModelBlock().isBeaconBase();
    }

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
    @Deprecated
    public void setBlockOpaqueness(boolean isOpaque)
    {
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

    public BlockAgent getBlockAgent()
    {
        return this;
    }

    @Override
    public void setBlockMaterial(Material material)
    {
        try
        {
            Field field = ReflectionHelper.findField(Block.class, "blockMaterial", "field_149764_J");
            field.set(block, material);
        }
        catch (IllegalAccessException e)
        {
            NotEnoughBlocks.logger.error("Failed to set Material for Block {} with sub blocks {}", block, BlockJson.getDisplayNamesFromBlock(block), e);
        }
    }

    @Override
    public void setBlockSoundType(SoundType soundType)
    {
        try
        {
            Field field = ReflectionHelper.findField(Block.class, "blockSoundType", "field_149762_H");
            field.set(block, soundType);
        }
        catch (IllegalAccessException e)
        {
            NotEnoughBlocks.logger.error("Failed to set SoundType for Block {} with sub blocks {}", block, BlockJson.getDisplayNamesFromBlock(block), e);
        }
    }

    private BlockJson modelBlock;

    public BlockJson getModelBlock()
    {
        if (data == null)
        {
            throw new NullPointerException("BlockAgent#getModelBlock(): data is null");
        }
        if (data.isEmpty())
        {
            throw new IndexOutOfBoundsException("BlockAgent#getModelBlock(): data is empty");
        }

        if (modelBlock == null)
        {
            modelBlock = data.get(0);
        }

        return modelBlock;
    }
}
