package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class NEBBlockCauldron extends BlockCauldron implements IBlockProperties
{
    private final Material blockMaterial;

    @Delegate
    private final BlockAgent<NEBBlockCauldron> agent;

    public NEBBlockCauldron(Material material, List<BlockJson> data)
    {
        super();
        this.blockMaterial = material;
        this.agent = new BlockAgent<>(this, data);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(this);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            ItemStack itemstack = playerIn.inventory.getCurrentItem();

            if (itemstack == null)
            {
                boolean isSneaking = playerIn.isSneaking();
                int waterLevel = state.getValue(LEVEL);
                this.setWaterLevel(worldIn, pos, state, (isSneaking ? ((waterLevel == 0) ? 3 : (waterLevel - 1)) : ((waterLevel == 3) ? 0 : (waterLevel + 1))));
                return true;
            }
            return false;
        }
    }
}