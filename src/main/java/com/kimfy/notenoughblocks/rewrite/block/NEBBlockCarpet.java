package com.kimfy.notenoughblocks.rewrite.block;

import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import com.kimfy.notenoughblocks.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class NEBBlockCarpet extends Block implements IBlockProperties
{
    public NEBBlockCarpet(Material material, List<JsonBlock> data)
    {
        super(material);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.setTickRandomly(true);
        this.func_150089_b(0);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        byte b0 = 0;
        float f = 0.0625F;
        return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)((float)y + (float)b0 * f), (double)z + this.maxZ);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public void setBlockBoundsForItemRender()
    {
        this.func_150089_b(0);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z)
    {
        this.func_150089_b(blockAccess.getBlockMetadata(x, y, z));
    }

    protected void func_150089_b(int p_150089_1_)
    {
        byte b0 = 0;
        float f = (float)(1 * (1 + b0)) / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        this.func_150090_e(world, x, y, z);
    }

    private boolean func_150090_e(World world, int x, int y, int z)
    {
        if (!this.canBlockStay(world, x, y, z))
        {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        return !world.isAirBlock(x, y - 1, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return side == 1 ? true : super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }
    
    /* ========== Properties Begin ========== */
    @SideOnly(Side.CLIENT)
    private IIcon[][] icons;
    private String[][] textures;
    
    private List<JsonBlock> data;
    /** Returns an int of how many sub blocks there are of this block **/
    @Getter
    private int     subBlocks           = -1;
    private boolean isBeaconBase        = false;
    private boolean isOpaque            = false;
    private boolean isStained           = false;
    private boolean isSilkTouchable     = true;
    private int     silkTouchMetadata   = -1;
    
    @Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return this.getIcon(side, blockAccess.getBlockMetadata(x, y, z));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return meta <= this.subBlocks ? icons[meta][side] : icons[0][side];
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[this.textures.length][6];
        
        for (int i = 0; i < this.textures.length; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                this.icons[i][j] = iconRegister.registerIcon(Constants.MOD_ID + ":" + textures[i][j]);
            }
        }
    }

    @Override
    public void registerTextures(String[][] textures)
    {
        this.textures = textures;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < this.subBlocks; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
    
    @Override
    public void setBeaconBaseAble(boolean isBeaconBase)
    {
        this.isBeaconBase = isBeaconBase;
    }
    
    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
    {
        return this.isBeaconBase;
    }

    @Override
    public void setBlockOpaqueness(boolean isOpaque)
    {
        ;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isBlockStained()
    {
        return this.isStained;
    }
    
    @Override
    public void setBlockStainable(boolean isStained)
    {
        this.isStained = isStained;
    }
    
    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
        return silkTouchData.get(this) == metadata;
    }

    @Override
    public void setBlockLightOpacity(int lightOpacity)
    {
        this.lightOpacity = lightOpacity;
        this.setLightLevel(lightOpacity);
    }

    @Override
    public void setSubBlocks(int subBlocks)
    {
        this.subBlocks = subBlocks;
    }
    
    @Override
    public List<JsonBlock> getData()
    {
        return this.data;
    }
    
    @Override
    public void setData(List<JsonBlock> data)
    {
        this.data = data;
    }
}