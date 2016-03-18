package com.kimfy.notenoughblocks.rewrite.client.renderer;

import com.kimfy.notenoughblocks.rewrite.block.NEBBlockPane;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class RenderBlockStainedGlassPane implements ISimpleBlockRenderingHandler
{
    
    public static int blockPaneRenderId = RenderingRegistry.getNextAvailableRenderId();
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        NEBBlockPane b = (NEBBlockPane) block;
        
        if (modelId == this.blockPaneRenderId)
        {            
            renderer.setRenderBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F); // Credit: https://github.com/SlimeKnights/TinkersConstruct/blob/ac202c3a17be7491b583ad9d21ee39b7b54a51ad/src/main/java/tconstruct/smeltery/model/PaneRender.java#L18
            RenderBlockPane.renderStandardInventoryBlock(block, metadata, renderer);
        }
        
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        NEBBlockPane b = (NEBBlockPane) block;
        
        if (modelId == this.blockPaneRenderId)
        {
            return renderBlockStainedGlassPane(b, x, y, z, renderer);
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
        return this.blockPaneRenderId;
    }
    
    public boolean renderBlockStainedGlassPane(Block block, int x, int y, int z, RenderBlocks renderer)
    {
        IBlockAccess blockAccess = renderer.blockAccess;
        int worldHeight = blockAccess.getHeight();
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x, y, z));
        int colorMultiplier = block.colorMultiplier(blockAccess, x, y, z);
        
        float f = (float)(colorMultiplier >> 16 & 255) / 255.0F;
        float f1 = (float)(colorMultiplier >> 8 & 255) / 255.0F;
        float f2 = (float)(colorMultiplier & 255) / 255.0F;

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
        boolean isStained = ((NEBBlockPane)block).isBlockStained();
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        
        IIcon iconFront;
        IIcon iconSide;
        iconSide = renderer.getBlockIconFromSideAndMetadata(block, 0, metadata);
        iconFront = renderer.getBlockIconFromSideAndMetadata(block, 1, metadata);

        double d22 = (double)iconFront.getMinU();
        double d0 = (double)iconFront.getInterpolatedU(7.0D);
        double d1 = (double)iconFront.getInterpolatedU(9.0D);
        double d2 = (double)iconFront.getMaxU();
        double d3 = (double)iconFront.getMinV();
        double d4 = (double)iconFront.getMaxV();
        double d5 = (double)iconSide.getInterpolatedU(7.0D);
        double d6 = (double)iconSide.getInterpolatedU(9.0D);
        double d7 = (double)iconSide.getMinV();
        double d8 = (double)iconSide.getMaxV();
        double d9 = (double)iconSide.getInterpolatedV(7.0D);
        double d10 = (double)iconSide.getInterpolatedV(9.0D);
        double d11 = (double)x;
        double d12 = (double)(x + 1);
        double d13 = (double)z;
        double d14 = (double)(z + 1);
        double d15 = (double)x + 0.5D - 0.0625D;
        double d16 = (double)x + 0.5D + 0.0625D;
        double d17 = (double)z + 0.5D - 0.0625D;
        double d18 = (double)z + 0.5D + 0.0625D;
        boolean flag  = ((NEBBlockPane)block).canPaneConnectTo(blockAccess, x, y, z - 1, NORTH);
        boolean flag1 = ((NEBBlockPane)block).canPaneConnectTo(blockAccess, x, y, z + 1, SOUTH);
        boolean flag2 = ((NEBBlockPane)block).canPaneConnectTo(blockAccess, x - 1, y, z, WEST );
        boolean flag3 = ((NEBBlockPane)block).canPaneConnectTo(blockAccess, x + 1, y, z, EAST );
        double d19 = 0.001D;
        double d20 = 0.999D;
        double d21 = 0.001D;
        boolean flag4 = !flag && !flag1 && !flag2 && !flag3;

        if (!flag2 && !flag4)
        {
            if (!flag && !flag1)
            {
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d0, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d1, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d1, d3);
            }
        }
        else if (flag2 && flag3)
        {
            if (!flag)
            {
                tessellator.addVertexWithUV(d12, (double)y + 0.999D, d17, d2, d3);
                tessellator.addVertexWithUV(d12, (double)y + 0.001D, d17, d2, d4);
                tessellator.addVertexWithUV(d11, (double)y + 0.001D, d17, d22, d4);
                tessellator.addVertexWithUV(d11, (double)y + 0.999D, d17, d22, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d0, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d11, (double)y + 0.001D, d17, d22, d4);
                tessellator.addVertexWithUV(d11, (double)y + 0.999D, d17, d22, d3);
                tessellator.addVertexWithUV(d12, (double)y + 0.999D, d17, d2, d3);
                tessellator.addVertexWithUV(d12, (double)y + 0.001D, d17, d2, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d1, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d1, d3);
            }

            if (!flag1)
            {
                tessellator.addVertexWithUV(d11, (double)y + 0.999D, d18, d22, d3);
                tessellator.addVertexWithUV(d11, (double)y + 0.001D, d18, d22, d4);
                tessellator.addVertexWithUV(d12, (double)y + 0.001D, d18, d2, d4);
                tessellator.addVertexWithUV(d12, (double)y + 0.999D, d18, d2, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d11, (double)y + 0.999D, d18, d22, d3);
                tessellator.addVertexWithUV(d11, (double)y + 0.001D, d18, d22, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d0, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d0, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d1, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d1, d4);
                tessellator.addVertexWithUV(d12, (double)y + 0.001D, d18, d2, d4);
                tessellator.addVertexWithUV(d12, (double)y + 0.999D, d18, d2, d3);
            }

            tessellator.addVertexWithUV(d11, (double)y + 0.999D, d18, d6, d7);
            tessellator.addVertexWithUV(d12, (double)y + 0.999D, d18, d6, d8);
            tessellator.addVertexWithUV(d12, (double)y + 0.999D, d17, d5, d8);
            tessellator.addVertexWithUV(d11, (double)y + 0.999D, d17, d5, d7);
            tessellator.addVertexWithUV(d12, (double)y + 0.001D, d18, d5, d8);
            tessellator.addVertexWithUV(d11, (double)y + 0.001D, d18, d5, d7);
            tessellator.addVertexWithUV(d11, (double)y + 0.001D, d17, d6, d7);
            tessellator.addVertexWithUV(d12, (double)y + 0.001D, d17, d6, d8);
        }
        else
        {
            if (!flag && !flag4)
            {
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d1, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d1, d4);
                tessellator.addVertexWithUV(d11, (double)y + 0.001D, d17, d22, d4);
                tessellator.addVertexWithUV(d11, (double)y + 0.999D, d17, d22, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d0, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d11, (double)y + 0.001D, d17, d22, d4);
                tessellator.addVertexWithUV(d11, (double)y + 0.999D, d17, d22, d3);
            }

            if (!flag1 && !flag4)
            {
                tessellator.addVertexWithUV(d11, (double)y + 0.999D, d18, d22, d3);
                tessellator.addVertexWithUV(d11, (double)y + 0.001D, d18, d22, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d1, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d1, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d11, (double)y + 0.999D, d18, d22, d3);
                tessellator.addVertexWithUV(d11, (double)y + 0.001D, d18, d22, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d0, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d0, d3);
            }

            tessellator.addVertexWithUV(d11, (double)y + 0.999D, d18, d6, d7);
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d6, d9);
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d5, d9);
            tessellator.addVertexWithUV(d11, (double)y + 0.999D, d17, d5, d7);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d5, d9);
            tessellator.addVertexWithUV(d11, (double)y + 0.001D, d18, d5, d7);
            tessellator.addVertexWithUV(d11, (double)y + 0.001D, d17, d6, d7);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d6, d9);
        }

        if ((flag3 || flag4) && !flag2)
        {
            if (!flag1 && !flag4)
            {
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d0, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d0, d4);
                tessellator.addVertexWithUV(d12, (double)y + 0.001D, d18, d2, d4);
                tessellator.addVertexWithUV(d12, (double)y + 0.999D, d18, d2, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d1, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d1, d4);
                tessellator.addVertexWithUV(d12, (double)y + 0.001D, d18, d2, d4);
                tessellator.addVertexWithUV(d12, (double)y + 0.999D, d18, d2, d3);
            }

            if (!flag && !flag4)
            {
                tessellator.addVertexWithUV(d12, (double)y + 0.999D, d17, d2, d3);
                tessellator.addVertexWithUV(d12, (double)y + 0.001D, d17, d2, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d0, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d12, (double)y + 0.999D, d17, d2, d3);
                tessellator.addVertexWithUV(d12, (double)y + 0.001D, d17, d2, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d1, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d1, d3);
            }

            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d6, d10);
            tessellator.addVertexWithUV(d12, (double)y + 0.999D, d18, d6, d7);
            tessellator.addVertexWithUV(d12, (double)y + 0.999D, d17, d5, d7);
            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d5, d10);
            tessellator.addVertexWithUV(d12, (double)y + 0.001D, d18, d5, d8);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d5, d10);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d6, d10);
            tessellator.addVertexWithUV(d12, (double)y + 0.001D, d17, d6, d8);
        }
        else if (!flag3 && !flag && !flag1)
        {
            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d0, d3);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d0, d4);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d1, d4);
            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d1, d3);
        }

        if (!flag && !flag4)
        {
            if (!flag3 && !flag2)
            {
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d1, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d1, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d0, d3);
            }
        }
        else if (flag && flag1)
        {
            if (!flag2)
            {
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d13, d22, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d13, d22, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d14, d2, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d14, d2, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d13, d22, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d13, d22, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d0, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d1, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d1, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d14, d2, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d14, d2, d3);
            }

            if (!flag3)
            {
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d14, d2, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d14, d2, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d13, d22, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d13, d22, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d0, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d13, d22, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d13, d22, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d14, d2, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d14, d2, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d1, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d1, d3);
            }

            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d13, d6, d7);
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d13, d5, d7);
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d14, d5, d8);
            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d14, d6, d8);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d13, d5, d7);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d13, d6, d7);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d14, d6, d8);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d14, d5, d8);
        }
        else
        {
            if (!flag2 && !flag4)
            {
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d13, d22, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d13, d22, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d1, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d1, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d13, d22, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d13, d22, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d0, d3);
            }

            if (!flag3 && !flag4)
            {
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d1, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d1, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d13, d22, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d13, d22, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d0, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d13, d22, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d13, d22, d3);
            }

            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d13, d6, d7);
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d13, d5, d7);
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d5, d9);
            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d6, d9);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d13, d5, d7);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d13, d6, d7);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d6, d9);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d5, d9);
        }

        if ((flag1 || flag4) && !flag)
        {
            if (!flag2 && !flag4)
            {
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d0, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d14, d2, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d14, d2, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d1, d3);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d1, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.001D, d14, d2, d4);
                tessellator.addVertexWithUV(d15, (double)y + 0.999D, d14, d2, d3);
            }

            if (!flag3 && !flag4)
            {
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d14, d2, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d14, d2, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d0, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d0, d3);
            }
            else
            {
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d14, d2, d3);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d14, d2, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d1, d4);
                tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d1, d3);
            }

            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d6, d10);
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d5, d10);
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d14, d5, d8);
            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d14, d6, d8);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d5, d10);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d6, d10);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d14, d6, d8);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d14, d5, d8);
        }
        else if (!flag1 && !flag3 && !flag2)
        {
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d0, d3);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d0, d4);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d1, d4);
            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d1, d3);
        }

        tessellator.addVertexWithUV(d16, (double)y + 0.999D, d17, d6, d9);
        tessellator.addVertexWithUV(d15, (double)y + 0.999D, d17, d5, d9);
        tessellator.addVertexWithUV(d15, (double)y + 0.999D, d18, d5, d10);
        tessellator.addVertexWithUV(d16, (double)y + 0.999D, d18, d6, d10);
        tessellator.addVertexWithUV(d15, (double)y + 0.001D, d17, d5, d9);
        tessellator.addVertexWithUV(d16, (double)y + 0.001D, d17, d6, d9);
        tessellator.addVertexWithUV(d16, (double)y + 0.001D, d18, d6, d10);
        tessellator.addVertexWithUV(d15, (double)y + 0.001D, d18, d5, d10);

        if (flag4)
        {
            tessellator.addVertexWithUV(d11, (double)y + 0.999D, d17, d0, d3);
            tessellator.addVertexWithUV(d11, (double)y + 0.001D, d17, d0, d4);
            tessellator.addVertexWithUV(d11, (double)y + 0.001D, d18, d1, d4);
            tessellator.addVertexWithUV(d11, (double)y + 0.999D, d18, d1, d3);
            tessellator.addVertexWithUV(d12, (double)y + 0.999D, d18, d0, d3);
            tessellator.addVertexWithUV(d12, (double)y + 0.001D, d18, d0, d4);
            tessellator.addVertexWithUV(d12, (double)y + 0.001D, d17, d1, d4);
            tessellator.addVertexWithUV(d12, (double)y + 0.999D, d17, d1, d3);
            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d13, d1, d3);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d13, d1, d4);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d13, d0, d4);
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d13, d0, d3);
            tessellator.addVertexWithUV(d15, (double)y + 0.999D, d14, d0, d3);
            tessellator.addVertexWithUV(d15, (double)y + 0.001D, d14, d0, d4);
            tessellator.addVertexWithUV(d16, (double)y + 0.001D, d14, d1, d4);
            tessellator.addVertexWithUV(d16, (double)y + 0.999D, d14, d1, d3);
        }

        return true;
    }
}
