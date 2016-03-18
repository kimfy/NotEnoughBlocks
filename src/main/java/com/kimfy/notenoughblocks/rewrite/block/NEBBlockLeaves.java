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
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class NEBBlockLeaves extends Block implements IBlockProperties
{
    public NEBBlockLeaves(Material material, List<JsonBlock> data)
    {
        super(material);
        this.data = data;
    }
    
    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        double d0 = 0.5D;
        double d1 = 1.0D;
        return ColorizerFoliage.getFoliageColor(d0, d1);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }
    
    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = -1; k1 <= 1; ++k1)
        {
            for (int l1 = -1; l1 <= 1; ++l1)
            {
                int i2 = p_149720_1_.getBiomeGenForCoords(p_149720_2_ + l1, p_149720_4_ + k1).getBiomeFoliageColor(p_149720_2_ + l1, p_149720_3_, p_149720_4_ + k1);
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }
    
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_)
    {
        if (p_149734_1_.canLightningStrikeAt(p_149734_2_, p_149734_3_ + 1, p_149734_4_) && !World.doesBlockHaveSolidTopSurface(p_149734_1_, p_149734_2_, p_149734_3_ - 1, p_149734_4_) && p_149734_5_.nextInt(15) == 1)
        {
            double d0 = (double)((float)p_149734_2_ + p_149734_5_.nextFloat());
            double d1 = (double)p_149734_3_ - 0.05D;
            double d2 = (double)((float)p_149734_4_ + p_149734_5_.nextFloat());
            p_149734_1_.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public boolean isLeaves(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }
    
    /* ========== Debug ========== */
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote && player.isSneaking()) {
            int metadata = world.getBlockMetadata(x, y, z);
            
            player.addChatMessage(new ChatComponentText("===================="));
            player.addChatMessage(new ChatComponentText("Display name: " + this.getLocalizedName()));
            player.addChatMessage(new ChatComponentText("Sub blocks: "   + this.subBlocks));
            player.addChatMessage(new ChatComponentText("Metadata: "     + metadata));
            player.addChatMessage(new ChatComponentText("Side: "         + side));
            player.addChatMessage(new ChatComponentText("Texture: "      + this.textures[metadata][side]));
            player.addChatMessage(new ChatComponentText("Silk Touch: "      + this.canSilkHarvest()));
            player.addChatMessage(new ChatComponentText("===================="));
            return false;
        }
        return false;
    }
    
    /* ========== Properties Begin ========== */
    @SideOnly(Side.CLIENT)
    private IIcon[][] icons;
    @SideOnly(Side.CLIENT)
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
        return Blocks.leaves.isOpaqueCube() ? icons[meta][1] : icons[meta][0];
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
    @SideOnly(Side.CLIENT)
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
        return Blocks.leaves.isOpaqueCube();
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
        this.lightOpacity = 1;
        this.setLightLevel(1);
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
