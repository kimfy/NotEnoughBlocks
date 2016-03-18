package com.kimfy.notenoughblocks.rewrite.client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderBlockFence implements ISimpleBlockRenderingHandler
{
    public static int renderBlockFenceId = RenderingRegistry.getNextAvailableRenderId();
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        if (modelId == renderBlockFenceId)
        {
            renderBlockAsItem(block, metadata, 1.0f, renderer);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (modelId == renderBlockFenceId)
        {
            return renderBlockFence((BlockFence)block, x, y, z, renderer);
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return this.renderBlockFenceId;
    }
    
    public static boolean renderBlockFence(BlockFence block, int x, int y, int z, RenderBlocks renderer)
    {
        IBlockAccess blockAccess = renderer.blockAccess;
        boolean flag = false;
        float f = 0.375F;
        float f1 = 0.625F;
        renderer.setRenderBounds((double)f, 0.0D, (double)f, (double)f1, 1.0D, (double)f1);
        renderer.renderStandardBlock(block, x, y, z);
        flag = true;
        boolean flag1 = false;
        boolean flag2 = false;

        if (block.canConnectFenceTo(blockAccess, x - 1, y, z) || block.canConnectFenceTo(blockAccess, x + 1, y, z))
        {
            flag1 = true;
        }

        if (block.canConnectFenceTo(blockAccess, x, y, z - 1) || block.canConnectFenceTo(blockAccess, x, y, z + 1))
        {
            flag2 = true;
        }

        boolean flag3 = block.canConnectFenceTo(blockAccess, x - 1, y, z);
        boolean flag4 = block.canConnectFenceTo(blockAccess, x + 1, y, z);
        boolean flag5 = block.canConnectFenceTo(blockAccess, x, y, z - 1);
        boolean flag6 = block.canConnectFenceTo(blockAccess, x, y, z + 1);

        if (!flag1 && !flag2)
        {
            flag1 = true;
        }

        f = 0.4375F;
        f1 = 0.5625F;
        float f2 = 0.75F;
        float f3 = 0.9375F;
        float f4 = flag3 ? 0.0F : f;
        float f5 = flag4 ? 1.0F : f1;
        float f6 = flag5 ? 0.0F : f;
        float f7 = flag6 ? 1.0F : f1;
        renderer.field_152631_f = true;

        if (flag1)
        {
            renderer.setRenderBounds((double)f4, (double)f2, (double)f, (double)f5, (double)f3, (double)f1);
            renderer.renderStandardBlock(block, x, y, z);
            flag = true;
        }

        if (flag2)
        {
            renderer.setRenderBounds((double)f, (double)f2, (double)f6, (double)f1, (double)f3, (double)f7);
            renderer.renderStandardBlock(block, x, y, z);
            flag = true;
        }

        f2 = 0.375F;
        f3 = 0.5625F;

        if (flag1)
        {
            renderer.setRenderBounds((double)f4, (double)f2, (double)f, (double)f5, (double)f3, (double)f1);
            renderer.renderStandardBlock(block, x, y, z);
            flag = true;
        }

        if (flag2)
        {
            renderer.setRenderBounds((double)f, (double)f2, (double)f6, (double)f1, (double)f3, (double)f7);
            renderer.renderStandardBlock(block, x, y, z);
            flag = true;
        }

        renderer.field_152631_f = false;
        block.setBlockBoundsBasedOnState(renderer.blockAccess, x, y, z);
        
        return flag;
    }
    
    public static void renderBlockAsItem(Block block, int metadata, float colorMultiplier, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;

        int renderType, k;
        float r, g, b;

        if (renderer.useInventoryTint)
        {
            renderType = block.getRenderColor(metadata);

            r = (float)(renderType >> 16 & 255) / 255.0F;
            g = (float)(renderType >> 8 & 255) / 255.0F;
            b = (float)(renderType & 255) / 255.0F;
            GL11.glColor4f(r * colorMultiplier, g * colorMultiplier, b * colorMultiplier, 1.0F);
        }

        renderType = block.getRenderType();
        renderer.setRenderBoundsFromBlock(block);
        
        if (renderType == renderBlockFenceId)
        {
            for (k = 0; k < 4; ++k)
            {
                g = 0.125F;

                if (k == 0)
                {
                    renderer.setRenderBounds((double)(0.5F - g), 0.0D, 0.0D, (double)(0.5F + g), 1.0D, (double)(g * 2.0F));
                }

                if (k == 1)
                {
                    renderer.setRenderBounds((double)(0.5F - g), 0.0D, (double)(1.0F - g * 2.0F), (double)(0.5F + g), 1.0D, 1.0D);
                }

                g = 0.0625F;

                if (k == 2)
                {
                    renderer.setRenderBounds((double)(0.5F - g), (double)(1.0F - g * 3.0F), (double)(-g * 2.0F), (double)(0.5F + g), (double)(1.0F - g), (double)(1.0F + g * 2.0F));
                }

                if (k == 3)
                {
                    renderer.setRenderBounds((double)(0.5F - g), (double)(0.5F - g * 3.0F), (double)(-g * 2.0F), (double)(0.5F + g), (double)(0.5F - g), (double)(1.0F + g * 2.0F));
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1.0F);
                renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        }
    }
}
