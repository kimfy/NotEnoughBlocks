package com.kimfy.notenoughblocks.rewrite.item;

import com.kimfy.notenoughblocks.rewrite.block.NEBBlockDoor;
import com.kimfy.notenoughblocks.rewrite.block.NEBBlockRotating;
import com.kimfy.notenoughblocks.rewrite.client.renderer.RenderItemDoor;
import com.kimfy.notenoughblocks.rewrite.client.renderer.RenderItemReed;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class NEBItemBlockDoor extends ItemBlock implements ICustomItemRender
{
    @Getter 
    protected Block block;

    public NEBItemBlockDoor(Block block)
    {
        super(block);
        this.setMaxStackSize(64);
        this.block = block;
        ((NEBBlockDoor) block).setItem(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerItemRender()
    {
        MinecraftForgeClient.registerItemRenderer(this, (IItemRenderer) new RenderItemDoor());
    }
    
    public boolean placeBlock(World world, Block block, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z)
    {
        if (world.setBlock(x, y, z, block, 0, 4))
        {
            block.onBlockPlacedBy(world, x, y, z, entityPlayer, itemStack);
            block.onPostBlockPlaced(world, x, y, z, 0);

            return true;

        } else
        {
            return false;
        }
    }

    /**
     * Callback for item usage. If the item does something special on right
     * clicking, he will have one of those. Return True if something happen and
     * false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int metadata, float hX, float hY, float hZ)
    {
        if (metadata != 1)
        {
            return false;
        } else
        {
            ++y;

            if (player.canPlayerEdit(x, y, z, metadata, itemStack)
                    && player.canPlayerEdit(x, y + 1, z, metadata, itemStack))
            {
                if (!block.canPlaceBlockAt(world, x, y, z))
                {
                    return false;
                } else
                {
                    int i1 = MathHelper.floor_double((double) ((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D)
                            & 3;
                    placeDoorBlock(world, x, y, z, i1, block);
                    --itemStack.stackSize;
                    return true;
                }
            } else
            {
                return false;
            }
        }
    }

    public void placeDoorBlock(World world, int x, int y, int z, int p_150924_4_, Block block)
    {
        byte byte1 = 0;
        byte byte2 = 0;

        if (p_150924_4_ == 0)
        {
            byte2 = 1;
        }

        if (p_150924_4_ == 1)
        {
            byte1 = -1;
        }

        if (p_150924_4_ == 2)
        {
            byte2 = -1;
        }

        if (p_150924_4_ == 3)
        {
            byte1 = 1;
        }

        int i1 = (world.getBlock(x - byte1, y, z - byte2).isNormalCube() ? 1 : 0)
                + (world.getBlock(x - byte1, y + 1, z - byte2).isNormalCube() ? 1 : 0);
        int j1 = (world.getBlock(x + byte1, y, z + byte2).isNormalCube() ? 1 : 0)
                + (world.getBlock(x + byte1, y + 1, z + byte2).isNormalCube() ? 1 : 0);
        boolean flag = world.getBlock(x - byte1, y, z - byte2) == block
                || world.getBlock(x - byte1, y + 1, z - byte2) == block;
        boolean flag1 = world.getBlock(x + byte1, y, z + byte2) == block
                || world.getBlock(x + byte1, y + 1, z + byte2) == block;
        boolean flag2 = false;

        if (flag && !flag1)
        {
            flag2 = true;
        } else if (j1 > i1)
        {
            flag2 = true;
        }

        world.setBlock(x, y, z, block, p_150924_4_, 2);
        world.setBlock(x, y + 1, z, block, 8 | (flag2 ? 1 : 0), 2);
        world.notifyBlocksOfNeighborChange(x, y, z, block);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, block);
    }

    @Override
    public IIcon getIconFromDamage(int metadata)
    {
        return block.getIcon(2, metadata);
    }

    @Override
    public int getMetadata(int metadata)
    {
        return 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        return this.getUnlocalizedName() + "_" + 0;
    }
}
