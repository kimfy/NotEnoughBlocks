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
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class NEBBlockRotating extends Block implements IBlockProperties
{
    public NEBBlockRotating(Material material, List<JsonBlock> data)
    {
        super(material);
        this.data = data;
    }

    /* ========== Properties Begin ========== */
    @SideOnly(Side.CLIENT)
    private IIcon[][] icons;
    private String[][] textures;

    private List<JsonBlock> data;
    /** Returns an int of how many sub blocks there are of this block **/
    @Getter
    private int subBlocks = -1;
    private boolean isBeaconBase = false;
    private boolean isOpaque = false;
    private boolean isStained = false;
    private boolean isSilkTouchable = true;
    private int silkTouchMetadata = -1;

    @Override
    public int damageDropped(int metadata)
    {
        return metadata & 3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return this.getIcon(side, blockAccess.getBlockMetadata(x, y, z));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        int pos = meta & 12;
        int orientation = meta & 3;

        if (!(pos == 0 && (side == 1 || side == 0) || pos == 4
                && (side == 5 || side == 4) || pos == 8
                && (side == 2 || side == 3)))
        {
            return icons[meta & 3][2];
        }
        else
        {
            return icons[meta & 3][0];
        }
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
                this.icons[i][j] = iconRegister.registerIcon(Constants.MOD_ID
                        + ":" + textures[i][j]);
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
        for (int i = 0; i < this.subBlocks; i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public void setBeaconBaseAble(boolean isBeaconBase)
    {
        this.isBeaconBase = isBeaconBase;
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z,
            int beaconX, int beaconY, int beaconZ)
    {
        return this.isBeaconBase;
    }

    @Override
    public void setBlockOpaqueness(boolean isOpaque)
    {
        this.isOpaque = isOpaque;
        this.opaque = isOpaque;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return this.opaque;
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
    public boolean canSilkHarvest(World world, EntityPlayer player, int x,
            int y, int z, int metadata)
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

    /* Exclusive Properties */

    public int getRenderType()
    {
        return 31;
    }

    public int onBlockPlaced(World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ, int meta)
    {

        int type = meta & 3;
        byte orientation = 0;

        switch (side)
        {
        case 0:
        case 1:
            orientation = 0;
            break;

        case 2:
        case 3:
            orientation = 8;
            break;

        case 4:
        case 5:
            orientation = 4;
        }

        return type | orientation;
    }

    public int getTypeFromMeta(int meta)
    {
        return meta & 3;
    }

    /**
     * Returns an item stack containing a single instance of the current block
     * type. 'i' is the block's subtype/damage and is ignored for blocks which
     * do not support subtypes. Blocks which cannot be harvested should return
     * null.
     */
    public ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1,
                this.getTypeFromMeta(meta));
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
