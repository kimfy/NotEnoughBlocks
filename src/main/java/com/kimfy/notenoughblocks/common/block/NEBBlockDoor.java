package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class NEBBlockDoor extends BlockDoor implements IBlockProperties
{
    @Delegate
    private final BlockAgent<NEBBlockDoor> agent;

    public NEBBlockDoor(Material materialIn, List<BlockJson> data)
    {
        super(materialIn);
        this.agent = new BlockAgent<>(this, data);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(this);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? null : Item.getItemFromBlock(this);
    }
}
