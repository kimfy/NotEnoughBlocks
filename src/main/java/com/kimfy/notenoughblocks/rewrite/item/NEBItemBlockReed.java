package com.kimfy.notenoughblocks.rewrite.item;

import com.kimfy.notenoughblocks.rewrite.block.IBlockProperties;
import com.kimfy.notenoughblocks.rewrite.client.renderer.RenderItemReed;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class NEBItemBlockReed extends ItemBlock implements IBlock, ICustomItemRender
{
    protected final Block block;
    
    public NEBItemBlockReed(Block block)
    {
        super(block);
        this.block = block;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerItemRender()
    {
        MinecraftForgeClient.registerItemRenderer(this, (IItemRenderer) new RenderItemReed());
    }

    @Override
    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        return ((IBlockProperties) block).getData().get(0).getRenderColor();
    }

    @Override
    public String getUnlocalizedName(ItemStack is)
    {
        return this.getUnlocalizedName() + "_" + 0;
    }

    @Override
    public IIcon getIconFromDamage(int metadata)
    {
        return block.getIcon(2, 0);
    }

    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    @Override
    public Block getBlock()
    {
        return this.block;
    }
    
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        Block block = world.getBlock(x, y, z);

        if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1)
        {
            p_77648_7_ = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush)
        {
            if (p_77648_7_ == 0)
            {
                --y;
            }

            if (p_77648_7_ == 1)
            {
                ++y;
            }

            if (p_77648_7_ == 2)
            {
                --z;
            }

            if (p_77648_7_ == 3)
            {
                ++z;
            }

            if (p_77648_7_ == 4)
            {
                --x;
            }

            if (p_77648_7_ == 5)
            {
                ++x;
            }
        }

        if (!player.canPlayerEdit(x, y, z, p_77648_7_, itemStack))
        {
            return false;
        }
        else if (itemStack.stackSize == 0)
        {
            return false;
        }
        else
        {
            if (world.canPlaceEntityOnSide(this.block, x, y, z, false, p_77648_7_, (Entity)null, itemStack))
            {
                int i1 = this.block.onBlockPlaced(world, x, y, z, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, 0);

                if (world.setBlock(x, y, z, this.block, i1, 3))
                {
                    if (world.getBlock(x, y, z) == this.block)
                    {
                        this.block.onBlockPlacedBy(world, x, y, z, player, itemStack);
                        this.block.onPostBlockPlaced(world, x, y, z, i1);
                    }

                    world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.block.stepSound.func_150496_b(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getPitch() * 0.8F);
                    --itemStack.stackSize;
                }
            }

            return true;
        }
    }
    
    @Override
    protected String getIconString()
    {
        return "apple"; /* Stops Forge from complaining about missing item textures */
    }
}
