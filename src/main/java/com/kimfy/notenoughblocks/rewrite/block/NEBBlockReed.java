package com.kimfy.notenoughblocks.rewrite.block;

import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import com.kimfy.notenoughblocks.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockReed;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class NEBBlockReed extends BlockReed implements IBlockProperties, IPlantable
{
    protected final Material material;
    
    public NEBBlockReed(Material material, List<JsonBlock> data)
    {
        super();
        this.material = material;
        this.setTickRandomly(true);
    }
    
    @Override
    public Material getMaterial()
    {
        return this.material;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (world.getBlock(x, y - 1, z) == this || this.func_150170_e(world, x, y, z))
        {
            if (world.isAirBlock(x, y + 1, z))
            {
                int l;

                for (l = 1; world.getBlock(x, y - l, z) == this; ++l)
                {
                    ;
                }

                if (l < 3)
                {
                    int i1 = world.getBlockMetadata(x, y, z);

                    if (i1 == 15)
                    {
                        world.setBlock(x, y + 1, z, this);
                        world.setBlockMetadataWithNotify(x, y, z, 0, 4);
                    }
                    else
                    {
                        world.setBlockMetadataWithNotify(x, y, z, i1 + 1, 4);
                    }
                }
            }
        }
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable)
    {
        Block plant = plantable.getPlant(world, x, y + 1, z);
        EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);

        if (plant == this)
        {
            return true;
        }
        return false;
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(this);
    }

    @Override
    public Item getItem(World world, int x, int y, int z)
    {
        return Item.getItemFromBlock(world.getBlock(x, y, z));
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Beach;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z)
    {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z);
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
        return 0;
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
        return meta <= this.subBlocks - 1 ? icons[meta][side] : icons[0][side];
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
