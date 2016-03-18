package com.kimfy.notenoughblocks.rewrite.block;

import com.kimfy.notenoughblocks.rewrite.client.renderer.RenderBlockGrass;
import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import com.kimfy.notenoughblocks.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class NEBBlockGrass extends Block implements IBlockProperties, IGrowable
{
    public NEBBlockGrass(Material material, List<JsonBlock> data)
    {
        super(material);
        this.setTickRandomly(true);
        this.canBlockGrass = true;
        this.data = data;
    }
    
    /* Exclusive Block Texturing */
    
    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        return side == 0 ? icons[metadata][0] : side == 1 ? icons[metadata][1] : icons[metadata][2];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        
        if (side == 1) // Top
        {
            return icons[metadata][1];
        }
        else if (side == 0)
        {
            return icons[metadata][0];
        }
        else
        {
            Material material = blockAccess.getBlock(x, y + 1, z).getMaterial();
            return material == Material.snow && material == Material.craftedSnow ? icons[metadata][4] : icons[metadata][2]; // TODO: Fix snowy texture
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconSideOverlay(int metadata) // TODO: Implement metadata in renderblocks
    {
        return icons[metadata][3];
    }
    
    /* Exclusive Block Properties */
    
    /**
     * Spreads to dirt blocks
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (!world.isRemote)
        {
            if (world.getBlockLightValue(x, y + 1, z) < 4 && world.getBlockLightOpacity(x, y + 1, z) > 2)
            {
                world.setBlock(x, y, z, Blocks.dirt);
            }
            else if (world.getBlockLightValue(x, y + 1, z) >= 9)
            {
                for (int l = 0; l < 4; ++l)
                {
                    int i1 = x + rand.nextInt(3) - 1;
                    int j1 = y + rand.nextInt(5) - 3;
                    int k1 = z + rand.nextInt(3) - 1;
                    Block block = world.getBlock(i1, j1 + 1, k1);
                    int metadata = world.getBlockMetadata(x, y, z);
                    
                    if (world.getBlock(i1, j1, k1) == Blocks.dirt && world.getBlockMetadata(i1, j1, k1) == 0 && world.getBlockLightValue(i1, j1 + 1, k1) >= 4 && world.getBlockLightOpacity(i1, j1 + 1, k1) <= 2)
                    {
                        world.setBlock(i1, j1, k1, this, metadata, 2);
                    }
                }
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        double d0 = 0.5D;
        double d1 = 1.0D;
        return ColorizerGrass.getGrassColor(d0, d1);
    }
    
    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_)
    {
        return this.getBlockColor();
    }
    
    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z)
    {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = -1; k1 <= 1; ++k1)
        {
            for (int l1 = -1; l1 <= 1; ++l1)
            {
                int i2 = blockAccess.getBiomeGenForCoords(x + l1, z + k1).getBiomeGrassColor(x + l1, y, z + k1);
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }
    
    @Override
    public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_, boolean p_149851_5_)
    {
        return true;
    }

    @Override
    public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_)
    {
        return true;
    }
    
    /* onBonemeal() */
    @Override
    public void func_149853_b(World world, Random rand, int x, int y, int z)
    {
        int l = 0;

        while (l < 128)
        {
            int blockX = x;
            int blockY = y + 1;
            int blockZ = z;
            int iterator = 0;

            while (true)
            {
                if (iterator < l / 16)
                {
                    blockX += rand.nextInt(3) - 1;
                    blockY += (rand.nextInt(3) - 1) * rand.nextInt(3) / 2;
                    blockZ += rand.nextInt(3) - 1;

                    if (world.getBlock(blockX, blockY - 1, blockZ) == this && !world.getBlock(blockX, blockY, blockZ).isNormalCube())
                    {
                        ++iterator;
                        continue;
                    }
                }
                else if (world.getBlock(blockX, blockY, blockZ).getMaterial() == Material.air)
                {
                    if (rand.nextInt(8) != 0)
                    {
                        if (Blocks.tallgrass.canBlockStay(world, blockX, blockY, blockZ))
                        {
                            world.setBlock(blockX, blockY, blockZ, Blocks.tallgrass, 1, 3);
                        }
                        /*
                        * TODO Implement grass growing
                        */
                    }
                    else
                    {
                        world.getBiomeGenForCoords(blockX, blockZ).plantFlower(world, rand, blockX, blockY, blockZ);
                    }
                }

                ++l;
                break;
            }
        }
    }
    
    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable)
    {
        Block plant = plantable.getPlant(world, x, y + 1, z);
        EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);
        
        switch(plantType)
        {
            case Cave:      return isSideSolid(world, x, y, z, direction.UP);
            case Plains:    return true;
            case Beach:
                boolean hasWater = (world.getBlock(x - 1, y, z    ).getMaterial() == Material.water ||
                                    world.getBlock(x + 1, y, z    ).getMaterial() == Material.water ||
                                    world.getBlock(x,     y, z - 1).getMaterial() == Material.water ||
                                    world.getBlock(x,     y, z + 1).getMaterial() == Material.water);
                return hasWater;
            default:
                break;
        }
        
        return super.canSustainPlant(world, x, y, z, direction, plantable);
    }
    
    /* ========== Properties Begin ========== */
    @SideOnly(Side.CLIENT)
    private IIcon[][] icons;        // [meta][0] = bottomIcon, [meta][1] = topIcon, [meta][2] = sideIcon, [meta][3] = sideOverlay, [meta][4] = snowed
    private String[][] textures;    // [meta][0] = bottomIcon, [meta][1] = topIcon, [meta][2] = sideIcon, [meta][3] = sideOverlay, [meta][4] = snowed
    
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
        return true;
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
    
    /* Render */
    @Override
    public int getRenderType()
    {
        return RenderBlockGrass.renderBlockGrassId;
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
