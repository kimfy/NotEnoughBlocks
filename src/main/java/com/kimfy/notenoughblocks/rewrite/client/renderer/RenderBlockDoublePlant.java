package com.kimfy.notenoughblocks.rewrite.client.renderer;

import com.kimfy.notenoughblocks.rewrite.block.NEBBlockDoublePlant;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBlockDoublePlant implements ISimpleBlockRenderingHandler
{
    public static int renderBlockDoublePlantId = RenderingRegistry.getNextAvailableRenderId();
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if(modelId == this.renderBlockDoublePlantId)
        {
            return renderBlockDoublePlant((NEBBlockDoublePlant)block, x, y, z, renderer);
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return this.renderBlockDoublePlantId;
    }
    
    public boolean renderBlockDoublePlant(NEBBlockDoublePlant block, int x, int y, int z, RenderBlocks renderer)
    {
        IBlockAccess blockAccess = renderer.blockAccess;
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x, y, z));
        int colorMultiplier = block.colorMultiplier(blockAccess, x, y, z);
        float r = (float)(colorMultiplier >> 16 & 255) / 255.0F;
        float g = (float)(colorMultiplier >> 8 & 255) / 255.0F;
        float b = (float)(colorMultiplier & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float f4 = (r * 30.0F + g * 70.0F) / 100.0F;
            float f5 = (r * 30.0F + b * 70.0F) / 100.0F;
            r = f3;
            g = f4;
            b = f5;
        }

        tessellator.setColorOpaque_F(r, g, b);
        long j1 = (long)(x * 3129871) ^ (long)z * 116129781L;
        j1 = j1 * j1 * 42317861L + j1 * 11L;
        double d19 = (double)x;
        double d0 = (double)y;
        double d1 = (double)z;
        d19 += ((double)((float)(j1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.3D;
        d1 += ((double)((float)(j1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.3D;
        int meta = blockAccess.getBlockMetadata(x, y, z);
        boolean flag = false;
        boolean bottom = NEBBlockDoublePlant.func_149887_c(meta);
        int metadata;

        if (bottom)
        {
            if (blockAccess.getBlock(x, y - 1, z) != block)
            {
                return false;
            }

            metadata = NEBBlockDoublePlant.func_149890_d(blockAccess.getBlockMetadata(x, y - 1, z));
        }
        else
        {
            metadata = NEBBlockDoublePlant.func_149890_d(meta);
        }

        IIcon iicon = block.func_149888_a(bottom, metadata);
        renderer.drawCrossedSquares(iicon, d19, d0, d1, 1.0F);

        if (bottom && iicon.getIconName().contains("sunflower"))// metadata == 0)
        {
            IIcon botIcon = block.sunflowerIcons[0][2];
            double d2 = Math.cos((double)j1 * 0.8D) * Math.PI * 0.1D;
            double d3 = Math.cos(d2);
            double d4 = Math.sin(d2);
            double d5 = (double)botIcon.getMinU();
            double d6 = (double)botIcon.getMinV();
            double d7 = (double)botIcon.getMaxU();
            double d8 = (double)botIcon.getMaxV();
            double d9 = 0.3D;
            double d10 = -0.05D;
            double d11 = 0.5D + 0.3D * d3 - 0.5D * d4;
            double d12 = 0.5D + 0.5D * d3 + 0.3D * d4;
            double d13 = 0.5D + 0.3D * d3 + 0.5D * d4;
            double d14 = 0.5D + -0.5D * d3 + 0.3D * d4;
            double d15 = 0.5D + -0.05D * d3 + 0.5D * d4;
            double d16 = 0.5D + -0.5D * d3 + -0.05D * d4;
            double d17 = 0.5D + -0.05D * d3 - 0.5D * d4;
            double d18 = 0.5D + 0.5D * d3 + -0.05D * d4;
            tessellator.addVertexWithUV(d19 + d15, d0 + 1.0D, d1 + d16, d5, d8);
            tessellator.addVertexWithUV(d19 + d17, d0 + 1.0D, d1 + d18, d7, d8);
            tessellator.addVertexWithUV(d19 + d11, d0 + 0.0D, d1 + d12, d7, d6);
            tessellator.addVertexWithUV(d19 + d13, d0 + 0.0D, d1 + d14, d5, d6);
            IIcon topIcon = block.sunflowerIcons[0][3];
            d5 = (double)topIcon.getMinU();
            d6 = (double)topIcon.getMinV();
            d7 = (double)topIcon.getMaxU();
            d8 = (double)topIcon.getMaxV();
            tessellator.addVertexWithUV(d19 + d17, d0 + 1.0D, d1 + d18, d5, d8);
            tessellator.addVertexWithUV(d19 + d15, d0 + 1.0D, d1 + d16, d7, d8);
            tessellator.addVertexWithUV(d19 + d13, d0 + 0.0D, d1 + d14, d7, d6);
            tessellator.addVertexWithUV(d19 + d11, d0 + 0.0D, d1 + d12, d5, d6);
        }

        return true;
    }
}
