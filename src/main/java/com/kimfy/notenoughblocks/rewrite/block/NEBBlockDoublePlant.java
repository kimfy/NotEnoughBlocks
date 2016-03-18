package com.kimfy.notenoughblocks.rewrite.block;

import com.kimfy.notenoughblocks.rewrite.client.renderer.RenderBlockDoublePlant;
import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import com.kimfy.notenoughblocks.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class NEBBlockDoublePlant extends BlockDoublePlant implements IBlockProperties
{
    public NEBBlockDoublePlant(Material material, List<JsonBlock> data)
    {
        super();
    }
    
    @Override
    public int getRenderType()
    {
        return RenderBlockDoublePlant.renderBlockDoublePlantId;
    }
    
    /* ========== Properties Begin ========== */
    @SideOnly(Side.CLIENT)
    private IIcon[][] icons;
    private String[][] textures;
    public IIcon[][] sunflowerIcons;
    
    private List<JsonBlock> data;
    /** Returns an int of how many sub blocks there are of this block **/
    @Getter
    public int     subBlocks           = -1;
    private boolean isBeaconBase        = false;
    private boolean isOpaque            = false;
    private boolean isStained           = false;
    private boolean isSilkTouchable     = true;
    private int     silkTouchMetadata   = -1;

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return this.getIcon(side, blockAccess.getBlockMetadata(x, y, z));
    }
    */

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return BlockDoublePlant.func_149887_c(meta) ? icons[meta & 7][side] : icons[0][1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon func_149888_a(boolean top, int metadata)
    {
        return top ? this.icons[metadata & 7][0] : this.icons[metadata & 7][1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[this.textures.length][6];

        for (int i = 0; i < this.textures.length; i++)
        {
            if(sunflowerIcons == null && textures[i][0].contains("sunflower"))
                sunflowerIcons = new IIcon[1][6];

            for (int j = 0; j < 6; j++)
            {
                if(textures[i][j].contains("sunflower"))
                {
                    sunflowerIcons[i][j] = iconRegister.registerIcon(Constants.MOD_ID + ":" + textures[i][j]);
                }
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
        ;
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
