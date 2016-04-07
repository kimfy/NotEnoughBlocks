package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.block.Drop;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BlockAgent<T extends Block & IBlockProperties> implements IBlockProperties
{
    private final T block;
    private final List<BlockJson> data;
    private final int blockCount;
    private final Shape blockShape;

    public BlockAgent(T block, List<BlockJson> data)
    {
        this.block = block;
        this.blockCount = data.size();
        this.data = data;
        this.blockShape = getModelBlock().getRealShape();
    }

    @Getter private boolean isBeaconBase;
    @Getter private boolean isOpaque;
    @Getter private boolean isStained;
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
    public List<BlockJson> getData()
    {
        return this.data;
    }

    public BlockAgent getBlockAgent()
    {
        return this;
    }

    private Material blockMaterial;

    @Override
    public void setBlockMaterial(Material material)
    {
        this.blockMaterial = material;
    }

    private SoundType soundType;

    @Override
    public void setBlockSoundType(SoundType soundType)
    {
        this.soundType = soundType;
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
            throw new RuntimeException("BlockAgent#getModelBlock(): data is empty");
        }

        if (modelBlock == null)
        {
            modelBlock = data.get(0);
        }

        return modelBlock;
    }

    /* ========== Helpers ========== */

    public BlockJson get(int metadata)
    {
        return data.get(metadata);
    }

    public int getBlockVariant(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return block.damageDropped(state);
    }

    /* ========== Block.java ========== */

    protected static java.util.Random RANDOM = new java.util.Random();

    public Material getMaterial(IBlockState state)
    {
        int metadata = block.damageDropped(state);
        return get(metadata).getRealMaterial();
    }

    public SoundType getStepSound()
    {
        return this.soundType;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
    {
        IntStream.range(0, blockCount).forEach(metadata -> list.add(new ItemStack(item, 1, metadata)));
    }

    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return get(block.damageDropped(state)).isSilkTouch();
    }

    public boolean isBeaconBase(IBlockAccess world, BlockPos pos, BlockPos beacon)
    {
        int metadata = getBlockVariant(world, pos);
        return this.get(metadata).isBeaconBase();
    }

    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        List<ItemStack> ret = new ArrayList<>();
        int metadata = state.getBlock().damageDropped(state);

        if (get(metadata).getDrop() != null)
        {
            BlockJson model = get(metadata);
            ret.addAll(model.getDrop().stream().map(Drop::getItemStack).collect(Collectors.toCollection(ArrayList<ItemStack>::new)));
        }
        else // Fallback to Block#getDrops()
        {
            Random rand = world instanceof World ? ((World)world).rand : RANDOM;

            int count = block.quantityDropped(state, fortune, rand);

            IntStream.range(0, count).forEach(i -> {
                Item item = block.getItemDropped(state, rand, fortune);
                if (item != null)
                {
                    ret.add(new ItemStack(item, 1, block.damageDropped(state)));
                }
            });
        }
        return ret;
    }
}