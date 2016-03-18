package com.kimfy.notenoughblocks.rewrite.block;

import com.kimfy.notenoughblocks.rewrite.client.renderer.RenderBlockDoor;
import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import com.kimfy.notenoughblocks.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class NEBBlockDoor extends BlockDoor implements IBlockProperties
{
    
    public NEBBlockDoor(Material material, List<JsonBlock> data)
    {
        super(material);
        this.data = data;
    }
    
    @Override
    public int getRenderType()
    {
        return RenderBlockDoor.renderBlockDoorId;
    }
    
    @Setter
    public Item item;
    
    @Override
    public Item getItem(World world, int x, int y, int z)
    {
        return this.item;
    }
    
    @Override
    public Item getItemDropped(int metadata, Random rand, int p_149650_3_)
    {
        return this.item;
    }
    
    /* ========== Properties Begin ========== */
    @SideOnly(Side.CLIENT)
    private IIcon[][] icons; // 0 = upperIcons[], 1 = lowerIcons[]
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
        return 0;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        if (side != 1 && side != 0)
        {
            int i1 = this.func_150012_g(blockAccess, x, y, z);
            int j1 = i1 & 3;
            boolean flag = (i1 & 4) != 0;
            boolean flag1 = false;
            boolean flag2 = (i1 & 8) != 0;

            if (flag)
            {
                if (j1 == 0 && side == 2)
                {
                    flag1 = !flag1;
                }
                else if (j1 == 1 && side == 5)
                {
                    flag1 = !flag1;
                }
                else if (j1 == 2 && side == 3)
                {
                    flag1 = !flag1;
                }
                else if (j1 == 3 && side == 4)
                {
                    flag1 = !flag1;
                }
            }
            else
            {
                if (j1 == 0 && side == 5)
                {
                    flag1 = !flag1;
                }
                else if (j1 == 1 && side == 3)
                {
                    flag1 = !flag1;
                }
                else if (j1 == 2 && side == 4)
                {
                    flag1 = !flag1;
                }
                else if (j1 == 3 && side == 2)
                {
                    flag1 = !flag1;
                }

                if ((i1 & 16) != 0)
                {
                    flag1 = !flag1;
                }
            }

            return flag2 ? this.icons[0][flag1 ? 2 : 0] : this.icons[0][flag1 ? 3 : 1];
        }
        else
        {
            return this.icons[0][1];
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (meta == 8)
        {
            return icons[0][0];
        } else {
            return icons[0][1];
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[this.textures.length][4];

        for (int i = 0; i < this.textures.length; i++)
        {
            this.icons[i][0] = iconRegister.registerIcon(Constants.MOD_ID + ":" + textures[i][0]); // upperIcons
            this.icons[i][1] = iconRegister.registerIcon(Constants.MOD_ID + ":" + textures[i][1]); // lowerIcons
            this.icons[i][2] = new IconFlipped(icons[i][0], true, false); // upperIconFlipped
            this.icons[i][3] = new IconFlipped(icons[i][1], true, false); // lowerIconFlipped
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
        this.opaque = false;
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
        this.lightOpacity = 0;
        this.setLightLevel(this.lightOpacity);
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
