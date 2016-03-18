package com.kimfy.notenoughblocks.rewrite.block;

import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import com.kimfy.notenoughblocks.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class NEBBlockSlab extends BlockSlab implements IBlockProperties
{
    public boolean doubleSlab;
    private final String name;
    
    public NEBBlockSlab(boolean doubleSlab, Material material, String name, List<JsonBlock> data)
    {
        super(doubleSlab, material);
        this.useNeighborBrightness = true;
        this.doubleSlab = doubleSlab;
        this.name = name;
        this.data = data;
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 7);
    }
    
    @Override
    public ItemStack createStackedBlock(int metadata)
    {
        return new ItemStack(this);
    }
    
    @Override
    public String func_150002_b(int metadata)
    {
        return this.name;
    }
    
    /* ========== Properties Begin ========== */
    @SideOnly(Side.CLIENT)
    private IIcon[][] icons;
    private String[][] textures;
    
    private List<JsonBlock> data;
    /** Returns an int of how many sub blocks there are of this block **/
    @Getter 
    public int     subBlocks           = -1;
    private boolean isBeaconBase        = false;
    private boolean isOpaque            = false;
    private boolean isStained           = false;
    private boolean isSilkTouchable     = true;
    private int     silkTouchMetadata   = -1;
    
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
        /*
        * NEI complained about some shit so I had to do this
        * Can't wait till this bites me in the ass
        */
        return icons[(meta & 7) > icons.length - 1 ? 0 : meta & 7][side];
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
        if (!this.doubleSlab) {
            for (int i = 0; i < this.subBlocks; i++) {
                list.add(new ItemStack(item, 1, i));
            }
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
        this.isOpaque = isOpaque;
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
        return silkTouchData.get(this) == metadata % 8 ? true : false;
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
