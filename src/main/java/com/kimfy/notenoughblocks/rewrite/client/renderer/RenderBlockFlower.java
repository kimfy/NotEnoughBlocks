package com.kimfy.notenoughblocks.rewrite.client.renderer;

import com.kimfy.notenoughblocks.rewrite.block.NEBBlockFlower;
import com.kimfy.notenoughblocks.rewrite.block.NEBBlockTallGrass;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBlockFlower implements ISimpleBlockRenderingHandler
{

    public static int renderBlockFlowerId = RenderingRegistry.getNextAvailableRenderId();
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        if (modelId == this.renderBlockFlowerId)
        {
        	
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if(modelId == this.renderBlockFlowerId)
        {
            return this.renderCrossedSquares(block, x, y, z, renderer);
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
        return this.renderBlockFlowerId;
    }

    /**
     * Renders any block requiring crossed squares such as reeds, flowers, and mushrooms
     */
    public boolean renderCrossedSquares(Block block, int x, int y, int z, RenderBlocks renderer)
    {
        IBlockAccess blockAccess = renderer.blockAccess;
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x, y, z));
        int l = block.colorMultiplier(blockAccess, x, y, z);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        tessellator.setColorOpaque_F(f, f1, f2);
        double d1 = (double)x;
        double d2 = (double)y;
        double d0 = (double)z;
        long i1;

        if (block instanceof NEBBlockTallGrass) // Blocks.tallgrass)
        {
            i1 = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
            i1 = i1 * i1 * 42317861L + i1 * 11L;
            d1 += ((double)((float)(i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
            d2 += ((double)((float)(i1 >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
            d0 += ((double)((float)(i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
        }
        else if (block instanceof NEBBlockFlower)//(block == Blocks.red_flower || block == Blocks.yellow_flower)
        {
            i1 = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
            i1 = i1 * i1 * 42317861L + i1 * 11L;
            d1 += ((double)((float)(i1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.3D;
            d0 += ((double)((float)(i1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.3D;
        }

        IIcon iicon = renderer.getBlockIconFromSideAndMetadata(block, 0, blockAccess.getBlockMetadata(x, y, z));
        renderer.drawCrossedSquares(iicon, d1, d2, d0, 1.0F);
        return true;
    }
}
