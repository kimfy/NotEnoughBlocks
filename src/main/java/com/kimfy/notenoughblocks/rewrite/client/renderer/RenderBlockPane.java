package com.kimfy.notenoughblocks.rewrite.client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class RenderBlockPane implements ISimpleBlockRenderingHandler
{
    
    public static int blockPaneRenderId = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        if (modelId == blockPaneRenderId)
        {
            renderer.setRenderBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F); // Credit: https://github.com/SlimeKnights/TinkersConstruct/blob/ac202c3a17be7491b583ad9d21ee39b7b54a51ad/src/main/java/tconstruct/smeltery/model/PaneRender.java#L18
            this.renderStandardInventoryBlock(block, metadata, renderer);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (modelId == this.blockPaneRenderId)
        {
            return renderBlockPane((BlockPane) block, x, y, z, renderer);
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

    public boolean renderBlockPane(BlockPane block, int x, int y, int z, RenderBlocks renderer)
    {
        IBlockAccess blockAccess = renderer.blockAccess;
        int worldHeight = blockAccess.getHeight();
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
        IIcon iconFront;
        IIcon iconSide;
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        iconSide = renderer.getBlockIconFromSideAndMetadata(block, 0, metadata);
        iconFront = renderer.getBlockIconFromSideAndMetadata(block, 1, metadata);

        double d21 = (double)iconFront.getMinU();
        double d0 = (double)iconFront.getInterpolatedU(8.0D);
        double d1 = (double)iconFront.getMaxU();
        double d2 = (double)iconFront.getMinV();
        double d3 = (double)iconFront.getMaxV();
        double d4 = (double)iconSide.getInterpolatedU(7.0D);
        double d5 = (double)iconSide.getInterpolatedU(9.0D);
        double d6 = (double)iconSide.getMinV();
        double d7 = (double)iconSide.getInterpolatedV(8.0D);
        double d8 = (double)iconSide.getMaxV();
        double d9 = (double)x;
        double d10 = (double)x + 0.5D;
        double d11 = (double)(x + 1);
        double d12 = (double)z;
        double d13 = (double)z + 0.5D;
        double d14 = (double)(z + 1);
        double d15 = (double)x + 0.5D - 0.0625D;
        double d16 = (double)x + 0.5D + 0.0625D;
        double d17 = (double)z + 0.5D - 0.0625D;
        double d18 = (double)z + 0.5D + 0.0625D;
        boolean flag  = block.canPaneConnectTo(blockAccess, x, y, z - 1, NORTH);
        boolean flag1 = block.canPaneConnectTo(blockAccess, x, y, z + 1, SOUTH);
        boolean flag2 = block.canPaneConnectTo(blockAccess, x - 1, y, z, WEST );
        boolean flag3 = block.canPaneConnectTo(blockAccess, x + 1, y, z, EAST );
        boolean flag4 = block.shouldSideBeRendered(blockAccess, x, y + 1, z, 1);
        boolean flag5 = block.shouldSideBeRendered(blockAccess, x, y - 1, z, 0);
        double d19 = 0.01D;
        double d20 = 0.005D;

        if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1))
        {
            if (flag2 && !flag3)
            {
                tessellator.addVertexWithUV(d9, (double)(y + 1), d13, d21, d2);
                tessellator.addVertexWithUV(d9, (double)(y + 0), d13, d21, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d0, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d0, d2);
                tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d21, d2);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d21, d3);
                tessellator.addVertexWithUV(d9, (double)(y + 0), d13, d0, d3);
                tessellator.addVertexWithUV(d9, (double)(y + 1), d13, d0, d2);

                if (!flag1 && !flag)
                {
                    tessellator.addVertexWithUV(d10, (double)(y + 1), d18, d4, d6);
                    tessellator.addVertexWithUV(d10, (double)(y + 0), d18, d4, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 0), d17, d5, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 1), d17, d5, d6);
                    tessellator.addVertexWithUV(d10, (double)(y + 1), d17, d4, d6);
                    tessellator.addVertexWithUV(d10, (double)(y + 0), d17, d4, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 0), d18, d5, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 1), d18, d5, d6);
                }

                if (flag4 || y < worldHeight - 1 && blockAccess.isAirBlock(x - 1, y + 1, z))
                {
                    tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d8);
                    tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d7);
                }

                if (flag5 || y > 1 && blockAccess.isAirBlock(x - 1, y - 1, z))
                {
                    tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d8);
                    tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d8);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d7);
                }
            }
            else if (!flag2 && flag3)
            {
                tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d0, d2);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d0, d3);
                tessellator.addVertexWithUV(d11, (double)(y + 0), d13, d1, d3);
                tessellator.addVertexWithUV(d11, (double)(y + 1), d13, d1, d2);
                tessellator.addVertexWithUV(d11, (double)(y + 1), d13, d0, d2);
                tessellator.addVertexWithUV(d11, (double)(y + 0), d13, d0, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d1, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d1, d2);

                if (!flag1 && !flag)
                {
                    tessellator.addVertexWithUV(d10, (double)(y + 1), d17, d4, d6);
                    tessellator.addVertexWithUV(d10, (double)(y + 0), d17, d4, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 0), d18, d5, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 1), d18, d5, d6);
                    tessellator.addVertexWithUV(d10, (double)(y + 1), d18, d4, d6);
                    tessellator.addVertexWithUV(d10, (double)(y + 0), d18, d4, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 0), d17, d5, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 1), d17, d5, d6);
                }

                if (flag4 || y < worldHeight - 1 && blockAccess.isAirBlock(x + 1, y + 1, z))
                {
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d6);
                    tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d6);
                    tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d6);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d6);
                }

                if (flag5 || y > 1 && blockAccess.isAirBlock(x + 1, y - 1, z))
                {
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d6);
                    tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d6);
                    tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d6);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d6);
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(d9, (double)(y + 1), d13, d21, d2);
            tessellator.addVertexWithUV(d9, (double)(y + 0), d13, d21, d3);
            tessellator.addVertexWithUV(d11, (double)(y + 0), d13, d1, d3);
            tessellator.addVertexWithUV(d11, (double)(y + 1), d13, d1, d2);
            tessellator.addVertexWithUV(d11, (double)(y + 1), d13, d21, d2);
            tessellator.addVertexWithUV(d11, (double)(y + 0), d13, d21, d3);
            tessellator.addVertexWithUV(d9, (double)(y + 0), d13, d1, d3);
            tessellator.addVertexWithUV(d9, (double)(y + 1), d13, d1, d2);

            if (flag4)
            {
                tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d8);
                tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d6);
                tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d6);
                tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d8);
                tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d8);
                tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d6);
                tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d6);
                tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d8);
            }
            else
            {
                if (y < worldHeight - 1 && blockAccess.isAirBlock(x - 1, y + 1, z))
                {
                    tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d8);
                    tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d9, (double)(y + 1) + 0.01D, d17, d4, d8);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d7);
                }

                if (y < worldHeight - 1 && blockAccess.isAirBlock(x + 1, y + 1, z))
                {
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d6);
                    tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d6);
                    tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d18, d5, d6);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, (double)(y + 1) + 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d11, (double)(y + 1) + 0.01D, d17, d4, d6);
                }
            }

            if (flag5)
            {
                tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d8);
                tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d6);
                tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d6);
                tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d8);
                tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d8);
                tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d6);
                tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d6);
                tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d8);
            }
            else
            {
                if (y > 1 && blockAccess.isAirBlock(x - 1, y - 1, z))
                {
                    tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d8);
                    tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d9, (double)y - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d9, (double)y - 0.01D, d17, d4, d8);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d7);
                }

                if (y > 1 && blockAccess.isAirBlock(x + 1, y - 1, z))
                {
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d6);
                    tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d6);
                    tessellator.addVertexWithUV(d11, (double)y - 0.01D, d18, d5, d6);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d10, (double)y - 0.01D, d17, d4, d7);
                    tessellator.addVertexWithUV(d11, (double)y - 0.01D, d17, d4, d6);
                }
            }
        }

        if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1))
        {
            if (flag && !flag1)
            {
                tessellator.addVertexWithUV(d10, (double)(y + 1), d12, d21, d2);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d12, d21, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d0, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d0, d2);
                tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d21, d2);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d21, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d12, d0, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 1), d12, d0, d2);

                if (!flag3 && !flag2)
                {
                    tessellator.addVertexWithUV(d15, (double)(y + 1), d13, d4, d6);
                    tessellator.addVertexWithUV(d15, (double)(y + 0), d13, d4, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 0), d13, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 1), d13, d5, d6);
                    tessellator.addVertexWithUV(d16, (double)(y + 1), d13, d4, d6);
                    tessellator.addVertexWithUV(d16, (double)(y + 0), d13, d4, d8);
                    tessellator.addVertexWithUV(d15, (double)(y + 0), d13, d5, d8);
                    tessellator.addVertexWithUV(d15, (double)(y + 1), d13, d5, d6);
                }

                if (flag4 || y < worldHeight - 1 && blockAccess.isAirBlock(x, y + 1, z - 1))
                {
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d5, d6);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d4, d7);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d4, d6);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d5, d6);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d4, d7);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d4, d6);
                }

                if (flag5 || y > 1 && blockAccess.isAirBlock(x, y - 1, z - 1))
                {
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d5, d6);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d4, d7);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d4, d6);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d5, d6);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d4, d7);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d4, d6);
                }
            }
            else if (!flag && flag1)
            {
                tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d0, d2);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d0, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d14, d1, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 1), d14, d1, d2);
                tessellator.addVertexWithUV(d10, (double)(y + 1), d14, d0, d2);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d14, d0, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 0), d13, d1, d3);
                tessellator.addVertexWithUV(d10, (double)(y + 1), d13, d1, d2);

                if (!flag3 && !flag2)
                {
                    tessellator.addVertexWithUV(d16, (double)(y + 1), d13, d4, d6);
                    tessellator.addVertexWithUV(d16, (double)(y + 0), d13, d4, d8);
                    tessellator.addVertexWithUV(d15, (double)(y + 0), d13, d5, d8);
                    tessellator.addVertexWithUV(d15, (double)(y + 1), d13, d5, d6);
                    tessellator.addVertexWithUV(d15, (double)(y + 1), d13, d4, d6);
                    tessellator.addVertexWithUV(d15, (double)(y + 0), d13, d4, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 0), d13, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 1), d13, d5, d6);
                }

                if (flag4 || y < worldHeight - 1 && blockAccess.isAirBlock(x, y + 1, z + 1))
                {
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d4, d7);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d7);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d4, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d7);
                }

                if (flag5 || y > 1 && blockAccess.isAirBlock(x, y - 1, z + 1))
                {
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d4, d7);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d8);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d7);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d4, d8);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d7);
                }
            }
        }
        else
        {
            tessellator.addVertexWithUV(d10, (double)(y + 1), d14, d21, d2);
            tessellator.addVertexWithUV(d10, (double)(y + 0), d14, d21, d3);
            tessellator.addVertexWithUV(d10, (double)(y + 0), d12, d1, d3);
            tessellator.addVertexWithUV(d10, (double)(y + 1), d12, d1, d2);
            tessellator.addVertexWithUV(d10, (double)(y + 1), d12, d21, d2);
            tessellator.addVertexWithUV(d10, (double)(y + 0), d12, d21, d3);
            tessellator.addVertexWithUV(d10, (double)(y + 0), d14, d1, d3);
            tessellator.addVertexWithUV(d10, (double)(y + 1), d14, d1, d2);

            if (flag4)
            {
                tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d8);
                tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d5, d6);
                tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d4, d6);
                tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d8);
                tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d5, d8);
                tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d6);
                tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d6);
                tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d4, d8);
            }
            else
            {
                if (y < worldHeight - 1 && blockAccess.isAirBlock(x, y + 1, z - 1))
                {
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d5, d6);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d4, d7);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d4, d6);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d5, d6);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d12, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d12, d4, d7);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d4, d6);
                }

                if (y < worldHeight - 1 && blockAccess.isAirBlock(x, y + 1, z + 1))
                {
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d4, d7);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d14, d4, d7);
                    tessellator.addVertexWithUV(d15, (double)(y + 1) + 0.005D, d13, d4, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d13, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)(y + 1) + 0.005D, d14, d5, d7);
                }
            }

            if (flag5)
            {
                tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d8);
                tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d5, d6);
                tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d4, d6);
                tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d8);
                tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d5, d8);
                tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d6);
                tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d6);
                tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d4, d8);
            }
            else
            {
                if (y > 1 && blockAccess.isAirBlock(x, y - 1, z - 1))
                {
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d5, d6);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d4, d7);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d4, d6);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d5, d6);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d12, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d12, d4, d7);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d4, d6);
                }

                if (y > 1 && blockAccess.isAirBlock(x, y - 1, z + 1))
                {
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d4, d7);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d8);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d5, d7);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d14, d4, d7);
                    tessellator.addVertexWithUV(d15, (double)y - 0.005D, d13, d4, d8);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d13, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)y - 0.005D, d14, d5, d7);
                }
            }
        }

        return true;
    }
    
    // Credit: https://github.com/SlimeKnights/TinkersConstruct/blob/ac202c3a17be7491b583ad9d21ee39b7b54a51ad/src/main/java/tconstruct/util/ItemHelper.java#L42
    // Just done some texture rotations to make it work for my setup
    public static void renderStandardInventoryBlock(Block block, int metadata, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;
        
        /* BOTTOM: 0 */
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        renderer.uvRotateBottom = 1;
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(block.getIcon(0, metadata)));
        tessellator.draw();
        renderer.uvRotateBottom = 0;
        
        /* TOP: 1 */
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.uvRotateTop = 1;
        //tessellator.addTranslation(0.5F, 0.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(block.getIcon(0, metadata)));// block.getIcon(1, meta));
        tessellator.draw();
        renderer.uvRotateTop = 0;
        
        /* NORTH: 2 */
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(block.getIcon(2, metadata)));
        tessellator.draw();
        
        /* SOUTH: 3 */
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(block.getIcon(3, metadata)));
        tessellator.draw();
        
        /* WEST: 4 */
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        renderer.uvRotateWest = 1;
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(block.getIcon(0, metadata)));
        tessellator.draw();
        renderer.uvRotateWest = 0;
        
        /* EAST: 5 */
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.uvRotateEast = 1;
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getIconSafe(block.getIcon(0, metadata)));
        tessellator.draw();
        renderer.uvRotateEast = 0;
        
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
    
}
