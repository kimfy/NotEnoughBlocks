package com.kimfy.notenoughblocks.rewrite.client.renderer;

import com.kimfy.notenoughblocks.rewrite.block.NEBBlockGrass;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderBlockGrass implements ISimpleBlockRenderingHandler
{

    public static int renderBlockGrassId = RenderingRegistry.getNextAvailableRenderId();
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        if (modelId == renderBlockGrassId)
        {
            renderBlockAsItem(block, metadata, 1.0F, renderer);
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (modelId == renderBlockGrassId)
        {
            NEBBlockGrass blockGrass = (NEBBlockGrass) block;
            int blockColorMultiplier = blockGrass.colorMultiplier(world, x, y, z);
            float r = (float) (blockColorMultiplier >> 16 & 255) / 255.0F;
            float g = (float) (blockColorMultiplier >> 8 & 255) / 255.0F;
            float b = (float) (blockColorMultiplier & 255) / 255.0F;

            if(EntityRenderer.anaglyphEnable){
                float f3 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
                float f4 = (r * 30.0F + g * 70.0F) / 100.0F;
                float f5 = (r * 30.0F + b * 70.0F) / 100.0F;
                r = f3;
                g = f4;
                b = f5;
            }

            renderer.setRenderBounds(0f, 0f, 0f, 1f, 1f, 1f);
            return Minecraft.isAmbientOcclusionEnabled() && blockGrass.getLightValue() == 0 ? (renderer.partialRenderBounds ? this.renderStandardBlockWithAmbientOcclusionPartial(blockGrass, x, y, z, r, g, b, renderer) : this.renderStandardBlockWithAmbientOcclusion(blockGrass, x, y, z, r, g, b, renderer)) : this.renderStandardBlockWithColorMultiplier(blockGrass, x, y, z, r, g, b, renderer);
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
        return this.renderBlockGrassId;
    }
    
    public static boolean renderStandardBlockWithColorMultiplier(Block block, int x, int y, int z, float r, float g, float b, RenderBlocks renderer)
    {
        NEBBlockGrass blockGrass = (NEBBlockGrass) block;
        IBlockAccess blockAccess = renderer.blockAccess;
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        
        renderer.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
        float f3 = 0.5F;
        float f4 = 1.0F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        float f7 = f4 * r;
        float f8 = f4 * g;
        float f9 = f4 * b;
        float f10 = f3;
        float f11 = f5;
        float f12 = f6;
        float f13 = f3;
        float f14 = f5;
        float f15 = f6;
        float f16 = f3;
        float f17 = f5;
        float f18 = f6;

        if (!(block instanceof NEBBlockGrass)) // TODO: Remove all this
        {
            f10 = f3 * r;
            f11 = f5 * r;
            f12 = f6 * r;
            f13 = f3 * g;
            f14 = f5 * g;
            f15 = f6 * g;
            f16 = f3 * b;
            f17 = f5 * b;
            f18 = f6 * b;
        }

        int mixedBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        
        // Bottom
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y - 1, z, 0))
        {
            tessellator.setBrightness(renderer.renderMinY > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z));
            tessellator.setColorOpaque_F(f10, f13, f16);
            renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, blockAccess, x, y, z, 0));
            flag = true;
        }
        
        // Top
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y + 1, z, 1))
        {
            tessellator.setBrightness(renderer.renderMaxY < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z));
            tessellator.setColorOpaque_F(f7, f8, f9);
            renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, blockAccess, x, y, z, 1));
            flag = true;
        }

        IIcon iicon;
        
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y, z - 1, 2))
        {
            tessellator.setBrightness(renderer.renderMinZ > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            iicon = renderer.getBlockIcon(block, blockAccess, x, y, z, 2);
            renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f11 * r, f14 * g, f17 * b);
                renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y, z + 1, 3))
        {
            tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            iicon = renderer.getBlockIcon(block, blockAccess, x, y, z, 3);
            renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f11 * r, f14 * g, f17 * b);
                renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x - 1, y, z, 4))
        {
            tessellator.setBrightness(renderer.renderMinX > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z));
            tessellator.setColorOpaque_F(f12, f15, f18);
            iicon = renderer.getBlockIcon(block, blockAccess, x, y, z, 4);
            renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f12 * r, f15 * g, f18 * b);
                renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x + 1, y, z, 5))
        {
            tessellator.setBrightness(renderer.renderMaxX < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z));
            tessellator.setColorOpaque_F(f12, f15, f18);
            iicon = renderer.getBlockIcon(block, blockAccess, x, y, z, 5);
            renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                tessellator.setColorOpaque_F(f12 * r, f15 * g, f18 * b);
                renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }

        return flag;
    }
    
    public static boolean renderStandardBlockWithAmbientOcclusion(Block block, int x, int y, int z, float r, float g, float b, RenderBlocks renderer)
    {
        NEBBlockGrass blockGrass = (NEBBlockGrass) block;
        IBlockAccess blockAccess = renderer.blockAccess;
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        
        renderer.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        
        int l = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        if (renderer.getBlockIcon(block).getIconName().contains("_top"))
        {
            flag1 = false;
        }
        else if (renderer.hasOverrideBlockTexture())
        {
            flag1 = false;
        }

        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;
        
        /** TODO: Negative Y | Bottom face | Side 0 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y - 1, z, 0))
        {
            if (renderer.renderMinY <= 0.0D)
            {
                --y;
            }

            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
            renderer.aoLightValueScratchXYNN = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPN = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            flag2 = blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();

            if (!flag5 && !flag3)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
            }

            if (!flag4 && !flag3)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
            }

            if (!flag5 && !flag2)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
            }

            if (!flag4 && !flag2)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
            }

            if (renderer.renderMinY <= 0.0D)
            {
                ++y;
            }

            i1 = l;

            if (renderer.renderMinY <= 0.0D || !blockAccess.getBlock(x, y - 1, z).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
            }

            f7 = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
            f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.5F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.5F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.5F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIconFromSideAndMetadata(blockGrass, 0, metadata)); //renderer.getBlockIcon(block, blockAccess, x, y, z, 0));
            flag = true;
        }
        
        /** TODO: Positive Y | Top face | Side 1 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y + 1, z, 1))
        {
            if (renderer.renderMaxY >= 1.0D)
            {
                ++y;
            }

            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
            renderer.aoLightValueScratchXYNP = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            flag2 = blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();

            if (!flag5 && !flag3)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
            }

            if (!flag5 && !flag2)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
            }

            if (!flag4 && !flag3)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
            }

            if (!flag4 && !flag2)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
            }

            if (renderer.renderMaxY >= 1.0D)
            {
                --y;
            }

            i1 = l;

            if (renderer.renderMaxY >= 1.0D || !blockAccess.getBlock(x, y + 1, z).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
            }

            f7 = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
            f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
            f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b;
            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, renderer.getBlockIconFromSideAndMetadata(blockGrass, 1, metadata)); //renderer.getBlockIcon(block, blockAccess, x, y, z, 1));
            flag = true;
        }

        float f8;
        float f9;
        float f10;
        float f11;
        int j1;
        int k1;
        int l1;
        int i2;
        IIcon iicon;
        
        /** TODO: Negative Z | North/Front face | Side 2 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y, z - 1, 2))
        {
            if (renderer.renderMinZ <= 0.0D)
            {
                --z;
            }

            renderer.aoLightValueScratchXZNN = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
            flag2 = blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();

            if (!flag3 && !flag5)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
            }

            if (!flag3 && !flag4)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
            }

            if (!flag2 && !flag5)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
            }

            if (!flag2 && !flag4)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
            }

            if (renderer.renderMinZ <= 0.0D)
            {
                ++z;
            }

            i1 = l;

            if (renderer.renderMinZ <= 0.0D || !blockAccess.getBlock(x, y, z - 1).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
            }

            f7 = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            f9 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f10 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
            f3 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            f4 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            f5 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            f6 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMinX), renderer.renderMaxY * renderer.renderMinX, (1.0D - renderer.renderMaxY) * renderer.renderMinX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMaxX), renderer.renderMaxY * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMaxX), renderer.renderMinY * renderer.renderMaxX, (1.0D - renderer.renderMinY) * renderer.renderMaxX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMinX), renderer.renderMinY * renderer.renderMinX, (1.0D - renderer.renderMinY) * renderer.renderMinX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.8F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIconFromSideAndMetadata(blockGrass, 2, metadata); //renderer.getBlockIcon(block, blockAccess, x, y, z, 2);
            renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                renderer.colorRedTopLeft *= r;
                renderer.colorRedBottomLeft *= r;
                renderer.colorRedBottomRight *= r;
                renderer.colorRedTopRight *= r;
                renderer.colorGreenTopLeft *= g;
                renderer.colorGreenBottomLeft *= g;
                renderer.colorGreenBottomRight *= g;
                renderer.colorGreenTopRight *= g;
                renderer.colorBlueTopLeft *= b;
                renderer.colorBlueBottomLeft *= b;
                renderer.colorBlueBottomRight *= b;
                renderer.colorBlueTopRight *= b;
                renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata)); // BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }
        

        /** TODO: Positive Z | South/Back face | Side 3 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y, z + 1, 3))
        {
            if (renderer.renderMaxZ >= 1.0D)
            {
                ++z;
            }

            renderer.aoLightValueScratchXZNP = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
            flag2 = blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();

            if (!flag3 && !flag5)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
            }

            if (!flag3 && !flag4)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
            }

            if (!flag2 && !flag5)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
            }

            if (!flag2 && !flag4)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
            }

            if (renderer.renderMaxZ >= 1.0D)
            {
                --z;
            }

            i1 = l;
            
            if (renderer.renderMaxZ >= 1.0D || !blockAccess.getBlock(x, y, z + 1).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
            }

            f7 = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
            f9 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f10 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            f3 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            f4 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            f5 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            f6 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * renderer.renderMinX, renderer.renderMaxY * renderer.renderMinX);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * renderer.renderMinX, renderer.renderMinY * renderer.renderMinX);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * renderer.renderMaxX, renderer.renderMinY * renderer.renderMaxX);
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * renderer.renderMaxX, renderer.renderMaxY * renderer.renderMaxX);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.8F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIconFromSideAndMetadata(blockGrass, 3, metadata); // renderer.getBlockIcon(block, blockAccess, x, y, z, 3);
            renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                renderer.colorRedTopLeft *= r;
                renderer.colorRedBottomLeft *= r;
                renderer.colorRedBottomRight *= r;
                renderer.colorRedTopRight *= r;
                renderer.colorGreenTopLeft *= g;
                renderer.colorGreenBottomLeft *= g;
                renderer.colorGreenBottomRight *= g;
                renderer.colorGreenTopRight *= g;
                renderer.colorBlueTopLeft *= b;
                renderer.colorBlueBottomLeft *= b;
                renderer.colorBlueBottomRight *= b;
                renderer.colorBlueTopRight *= b;
                renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }
        
        /** TODO: Negative X | West/Left face | Side 4 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x - 1, y, z, 4))
        {
            if (renderer.renderMinX <= 0.0D)
            {
                --x;
            }

            renderer.aoLightValueScratchXYNN = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYNP = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
            flag2 = blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();

            if (!flag4 && !flag3)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
            }

            if (!flag5 && !flag3)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
            }

            if (!flag4 && !flag2)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
            }

            if (!flag5 && !flag2)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
            }

            if (renderer.renderMinX <= 0.0D)
            {
                ++x;
            }

            i1 = l;

            if (renderer.renderMinX <= 0.0D || !blockAccess.getBlock(x - 1, y, z).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
            }

            f7 = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
            f9 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
            f10 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
            f3 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMaxZ + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            f4 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMinZ + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            f5 = (float)((double)f9 * renderer.renderMinY * renderer.renderMinZ + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            f6 = (float)((double)f9 * renderer.renderMinY * renderer.renderMaxZ + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMaxZ, renderer.renderMaxY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMinZ, renderer.renderMaxY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMinZ, renderer.renderMinY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMaxZ, renderer.renderMinY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * renderer.renderMaxZ);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.6F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIconFromSideAndMetadata(blockGrass, 4, metadata); // renderer.getBlockIcon(block, blockAccess, x, y, z, 4);
            renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                renderer.colorRedTopLeft *= r;
                renderer.colorRedBottomLeft *= r;
                renderer.colorRedBottomRight *= r;
                renderer.colorRedTopRight *= r;
                renderer.colorGreenTopLeft *= g;
                renderer.colorGreenBottomLeft *= g;
                renderer.colorGreenBottomRight *= g;
                renderer.colorGreenTopRight *= g;
                renderer.colorBlueTopLeft *= b;
                renderer.colorBlueBottomLeft *= b;
                renderer.colorBlueBottomRight *= b;
                renderer.colorBlueTopRight *= b;
                renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }

        /** TODO: Positive X | East/Right face | Side 5 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x + 1, y, z, 5))
        {
            if (renderer.renderMaxX >= 1.0D)
            {
                ++x;
            }

            renderer.aoLightValueScratchXYPN = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
            flag2 = blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();

            if (!flag3 && !flag5)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
            }

            if (!flag3 && !flag4)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
            }

            if (!flag2 && !flag5)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
            }

            if (!flag2 && !flag4)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
            }

            if (renderer.renderMaxX >= 1.0D)
            {
                --x;
            }

            i1 = l;

            if (renderer.renderMaxX >= 1.0D || !blockAccess.getBlock(x + 1, y, z).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
            }

            f7 = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
            f9 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
            f10 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
            f11 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f3 = (float)((double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMinY * renderer.renderMaxZ);
            f4 = (float)((double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMinY * renderer.renderMinZ);
            f5 = (float)((double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMaxY * renderer.renderMinZ);
            f6 = (float)((double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMaxY * renderer.renderMaxZ);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMaxZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), renderer.renderMinY * (1.0D - renderer.renderMaxZ), renderer.renderMinY * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMinZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), renderer.renderMinY * (1.0D - renderer.renderMinZ), renderer.renderMinY * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMinZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), renderer.renderMaxY * (1.0D - renderer.renderMinZ), renderer.renderMaxY * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMaxZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * renderer.renderMaxZ);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.6F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIconFromSideAndMetadata(blockGrass, 5, metadata); // renderer.getBlockIcon(block, blockAccess, x, y, z, 5);
            renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                renderer.colorRedTopLeft *= r;
                renderer.colorRedBottomLeft *= r;
                renderer.colorRedBottomRight *= r;
                renderer.colorRedTopRight *= r;
                renderer.colorGreenTopLeft *= g;
                renderer.colorGreenBottomLeft *= g;
                renderer.colorGreenBottomRight *= g;
                renderer.colorGreenTopRight *= g;
                renderer.colorBlueTopLeft *= b;
                renderer.colorBlueBottomLeft *= b;
                renderer.colorBlueBottomRight *= b;
                renderer.colorBlueTopRight *= b;
                renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }

        renderer.enableAO = false;
        return flag;
    }
    
    public static boolean renderStandardBlockWithAmbientOcclusionPartial(Block block, int x, int y, int z, float r, float g, float b, RenderBlocks renderer)
    {
        NEBBlockGrass blockGrass = (NEBBlockGrass) block;
        IBlockAccess blockAccess = renderer.blockAccess;
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        
        renderer.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        if (renderer.getBlockIcon(block).getIconName().contains("_top"))
        {
            flag1 = false;
        }
        else if (renderer.hasOverrideBlockTexture())
        {
            flag1 = false;
        }

        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;
        

        /** TODO: Negative Y | Bottom face | Side 0 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y - 1, z, 0))
        {
            if (renderer.renderMinY <= 0.0D)
            {
                --y;
            }

            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
            renderer.aoLightValueScratchXYNN = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPN = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            flag2 = blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();

            if (!flag5 && !flag3)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
            }

            if (!flag4 && !flag3)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
            }

            if (!flag5 && !flag2)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
            }

            if (!flag4 && !flag2)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
            }

            if (renderer.renderMinY <= 0.0D)
            {
                ++y;
            }

            i1 = l;

            if (renderer.renderMinY <= 0.0D || !blockAccess.getBlock(x, y - 1, z).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
            }

            f7 = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
            f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.5F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.5F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.5F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata)); //renderer.getBlockIcon(block, blockAccess, x, y, z, 0));
            flag = true;
        }

        /** TODO: Positive Y | Top face | Side 1 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y + 1, z, 1))
        {
            if (renderer.renderMaxY >= 1.0D)
            {
                ++y;
            }

            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
            renderer.aoLightValueScratchXYNP = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            flag2 = blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();

            if (!flag5 && !flag3)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z - 1);
            }

            if (!flag5 && !flag2)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z - 1);
            }

            if (!flag4 && !flag3)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z + 1);
            }

            if (!flag4 && !flag2)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z + 1);
            }

            if (renderer.renderMaxY >= 1.0D)
            {
                --y;
            }

            i1 = l;

            if (renderer.renderMaxY >= 1.0D || !blockAccess.getBlock(x, y + 1, z).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
            }

            f7 = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
            f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
            f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b;
            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata)); // renderer.getBlockIcon(block, blockAccess, x, y, z, 1));
            flag = true;
        }

        float f8;
        float f9;
        float f10;
        float f11;
        int j1;
        int k1;
        int l1;
        int i2;
        IIcon iicon;

        /** TODO: Negative Z | North/Front face | Side 2 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y, z - 1, 2))
        {
            if (renderer.renderMinZ <= 0.0D)
            {
                --z;
            }

            renderer.aoLightValueScratchXZNN = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
            flag2 = blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();

            if (!flag3 && !flag5)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
            }

            if (!flag3 && !flag4)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
            }

            if (!flag2 && !flag5)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
            }

            if (!flag2 && !flag4)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
            }

            if (renderer.renderMinZ <= 0.0D)
            {
                ++z;
            }

            i1 = l;

            if (renderer.renderMinZ <= 0.0D || !blockAccess.getBlock(x, y, z - 1).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
            }

            f7 = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            f9 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f10 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
            f3 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            f4 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            f5 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            f6 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMinX), renderer.renderMaxY * renderer.renderMinX, (1.0D - renderer.renderMaxY) * renderer.renderMinX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMaxX), renderer.renderMaxY * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMaxX), renderer.renderMinY * renderer.renderMaxX, (1.0D - renderer.renderMinY) * renderer.renderMaxX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMinX), renderer.renderMinY * renderer.renderMinX, (1.0D - renderer.renderMinY) * renderer.renderMinX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.8F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIconFromSideAndMetadata(block, 2, metadata); // renderer.getBlockIcon(block, blockAccess, x, y, z, 2);
            renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                renderer.colorRedTopLeft *= r;
                renderer.colorRedBottomLeft *= r;
                renderer.colorRedBottomRight *= r;
                renderer.colorRedTopRight *= r;
                renderer.colorGreenTopLeft *= g;
                renderer.colorGreenBottomLeft *= g;
                renderer.colorGreenBottomRight *= g;
                renderer.colorGreenTopRight *= g;
                renderer.colorBlueTopLeft *= b;
                renderer.colorBlueBottomLeft *= b;
                renderer.colorBlueBottomRight *= b;
                renderer.colorBlueTopRight *= b;
                renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }

        /** TODO: Positive Z | South/Back face | Side 3 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x, y, z + 1, 3))
        {
            if (renderer.renderMaxZ >= 1.0D)
            {
                ++z;
            }

            renderer.aoLightValueScratchXZNP = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
            flag2 = blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();

            if (!flag3 && !flag5)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y - 1, z);
            }

            if (!flag3 && !flag4)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x - 1, y + 1, z);
            }

            if (!flag2 && !flag5)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y - 1, z);
            }

            if (!flag2 && !flag4)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x + 1, y + 1, z);
            }

            if (renderer.renderMaxZ >= 1.0D)
            {
                --z;
            }

            i1 = l;

            if (renderer.renderMaxZ >= 1.0D || !blockAccess.getBlock(x, y, z + 1).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
            }

            f7 = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
            f9 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f10 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            f3 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            f4 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            f5 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            f6 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * renderer.renderMinX, renderer.renderMaxY * renderer.renderMinX);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * renderer.renderMinX, renderer.renderMinY * renderer.renderMinX);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * renderer.renderMaxX, renderer.renderMinY * renderer.renderMaxX);
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * renderer.renderMaxX, renderer.renderMaxY * renderer.renderMaxX);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.8F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIconFromSideAndMetadata(block, 3, metadata); //renderer.getBlockIcon(block, blockAccess, x, y, z, 3);
            renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                renderer.colorRedTopLeft *= r;
                renderer.colorRedBottomLeft *= r;
                renderer.colorRedBottomRight *= r;
                renderer.colorRedTopRight *= r;
                renderer.colorGreenTopLeft *= g;
                renderer.colorGreenBottomLeft *= g;
                renderer.colorGreenBottomRight *= g;
                renderer.colorGreenTopRight *= g;
                renderer.colorBlueTopLeft *= b;
                renderer.colorBlueBottomLeft *= b;
                renderer.colorBlueBottomRight *= b;
                renderer.colorBlueTopRight *= b;
                renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }

        /** TODO: Negative X | West/Left face | Side 4 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x - 1, y, z, 4))
        {
            if (renderer.renderMinX <= 0.0D)
            {
                --x;
            }

            renderer.aoLightValueScratchXYNN = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYNP = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
            flag2 = blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();

            if (!flag4 && !flag3)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
            }

            if (!flag5 && !flag3)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
            }

            if (!flag4 && !flag2)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
            }

            if (!flag5 && !flag2)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
            }

            if (renderer.renderMinX <= 0.0D)
            {
                ++x;
            }

            i1 = l;

            if (renderer.renderMinX <= 0.0D || !blockAccess.getBlock(x - 1, y, z).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z);
            }

            f7 = blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
            f9 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
            f10 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
            f3 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMaxZ + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            f4 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMinZ + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            f5 = (float)((double)f9 * renderer.renderMinY * renderer.renderMinZ + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            f6 = (float)((double)f9 * renderer.renderMinY * renderer.renderMaxZ + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMaxZ, renderer.renderMaxY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMinZ, renderer.renderMaxY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMinZ, renderer.renderMinY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMaxZ, renderer.renderMinY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * renderer.renderMaxZ);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.6F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIconFromSideAndMetadata(block, 4, metadata); // renderer.getBlockIcon(block, blockAccess, x, y, z, 4);
            renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("grass_side") && !renderer.hasOverrideBlockTexture())
            {
                renderer.colorRedTopLeft *= r;
                renderer.colorRedBottomLeft *= r;
                renderer.colorRedBottomRight *= r;
                renderer.colorRedTopRight *= r;
                renderer.colorGreenTopLeft *= g;
                renderer.colorGreenBottomLeft *= g;
                renderer.colorGreenBottomRight *= g;
                renderer.colorGreenTopRight *= g;
                renderer.colorBlueTopLeft *= b;
                renderer.colorBlueBottomLeft *= b;
                renderer.colorBlueBottomRight *= b;
                renderer.colorBlueTopRight *= b;
                renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }

        /** TODO: Positive X | East/Right face | Side 5 **/
        if (renderer.renderAllFaces || block.shouldSideBeRendered(blockAccess, x + 1, y, z, 5))
        {
            if (renderer.renderMaxX >= 1.0D)
            {
                ++x;
            }

            renderer.aoLightValueScratchXYPN = blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z);
            flag2 = blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            flag3 = blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            flag4 = blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();

            if (!flag3 && !flag5)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z - 1);
            }

            if (!flag3 && !flag4)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z + 1);
            }

            if (!flag2 && !flag5)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z - 1);
            }

            if (!flag2 && !flag4)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z + 1);
            }

            if (renderer.renderMaxX >= 1.0D)
            {
                --x;
            }

            i1 = l;

            if (renderer.renderMaxX >= 1.0D || !blockAccess.getBlock(x + 1, y, z).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z);
            }

            f7 = blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
            f9 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
            f10 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
            f11 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f3 = (float)((double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMinY * renderer.renderMaxZ);
            f4 = (float)((double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMinY * renderer.renderMinZ);
            f5 = (float)((double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMaxY * renderer.renderMinZ);
            f6 = (float)((double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMaxY * renderer.renderMaxZ);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMaxZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), renderer.renderMinY * (1.0D - renderer.renderMaxZ), renderer.renderMinY * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMinZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), renderer.renderMinY * (1.0D - renderer.renderMinZ), renderer.renderMinY * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMinZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), renderer.renderMaxY * (1.0D - renderer.renderMinZ), renderer.renderMaxY * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMaxZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * renderer.renderMaxZ);

            if (flag1)
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = r * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = g * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = b * 0.6F;
            }
            else
            {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIconFromSideAndMetadata(block, 5, metadata); // renderer.getBlockIcon(block, blockAccess, x, y, z, 5);
            renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().contains("_side") && !renderer.hasOverrideBlockTexture())
            {
                renderer.colorRedTopLeft *= r;
                renderer.colorRedBottomLeft *= r;
                renderer.colorRedBottomRight *= r;
                renderer.colorRedTopRight *= r;
                renderer.colorGreenTopLeft *= g;
                renderer.colorGreenBottomLeft *= g;
                renderer.colorGreenBottomRight *= g;
                renderer.colorGreenTopRight *= g;
                renderer.colorBlueTopLeft *= b;
                renderer.colorBlueBottomLeft *= b;
                renderer.colorBlueBottomRight *= b;
                renderer.colorBlueTopRight *= b;
                renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, blockGrass.getIconSideOverlay(metadata));
            }

            flag = true;
        }

        renderer.enableAO = false;
        return flag;
    }
    
    public static void renderBlockAsItem(Block block, int metadata, float colorMultiplier, RenderBlocks renderer)
    {
        NEBBlockGrass blockGrass = (NEBBlockGrass) block;
        Tessellator tessellator = Tessellator.instance;
        
        boolean isBlockGrass = block instanceof NEBBlockGrass;

        int renderType;
        float r, g, b;

        if (renderer.useInventoryTint)
        {
            renderType = block.getRenderColor(metadata);

            if (isBlockGrass)
            {
                renderType = 16777215;
            }

            r = (float)(renderType >> 16 & 255) / 255.0F;
            g = (float)(renderType >> 8 & 255) / 255.0F;
            b = (float)(renderType & 255) / 255.0F;
            GL11.glColor4f(r * colorMultiplier, g * colorMultiplier, b * colorMultiplier, 1.0F);
        }

        renderType = block.getRenderType();
        renderer.setRenderBoundsFromBlock(block);
        int renderColor;

            if (renderType == 16)
            {
                metadata = 1;
            }

            block.setBlockBoundsForItemRender();
            renderer.setRenderBoundsFromBlock(block);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();

            if (isBlockGrass && renderer.useInventoryTint)
            {
                renderColor = block.getRenderColor(metadata);
                g = (float)(renderColor >> 16 & 255) / 255.0F;
                b = (float)(renderColor >> 8 & 255) / 255.0F;
                float f4 = (float)(renderColor & 255) / 255.0F;
                GL11.glColor4f(g * colorMultiplier, b * colorMultiplier, f4 * colorMultiplier, 1.0F);
            }

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
            tessellator.draw();

            if (isBlockGrass && renderer.useInventoryTint)
            {
                GL11.glColor4f(colorMultiplier, colorMultiplier, colorMultiplier, 1.0F);
            }

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
}
