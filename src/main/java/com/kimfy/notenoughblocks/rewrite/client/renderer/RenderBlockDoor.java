package com.kimfy.notenoughblocks.rewrite.client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderBlockDoor implements ISimpleBlockRenderingHandler
{
    public static int renderBlockDoorId = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        ;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (modelId == this.renderBlockDoorId)
        {
            return renderBlockDoor(block, x, y, z, renderer);
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
        return this.renderBlockDoorId;
    }
    
    public static boolean renderBlockDoor(Block block, int x, int y, int z, RenderBlocks renderer)
    {
        IBlockAccess blockAccess = renderer.blockAccess;
        Tessellator tessellator = Tessellator.instance;
        int metadata = blockAccess.getBlockMetadata(x, y, z);

        if ((metadata & 8) != 0)
        {
            if (blockAccess.getBlock(x, y - 1, z) != block)
            {
                return false;
            }
        }
        else if (blockAccess.getBlock(x, y + 1, z) != block)
        {
            return false;
        }

        boolean flag = false;
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        int mixedBrightness = block.getMixedBrightnessForBlock(blockAccess, x, y, z);
        
        tessellator.setBrightness(renderer.renderMinY > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x, y - 1, z));
        tessellator.setColorOpaque_F(f, f, f);
        renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, blockAccess, x, y, z, 0));
        flag = true;
        
        tessellator.setBrightness(renderer.renderMaxY < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x, y + 1, z));
        tessellator.setColorOpaque_F(f1, f1, f1);
        renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, blockAccess, x, y, z, 1));
        flag = true;
        
        tessellator.setBrightness(renderer.renderMinZ > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x, y, z - 1));
        tessellator.setColorOpaque_F(f2, f2, f2);
        IIcon iicon = renderer.getBlockIcon(block, blockAccess, x, y, z, 2);
        renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, iicon);
        flag = true;
        
        renderer.flipTexture = false;
        tessellator.setBrightness(renderer.renderMaxZ < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x, y, z + 1));
        tessellator.setColorOpaque_F(f2, f2, f2);
        iicon = renderer.getBlockIcon(block, blockAccess, x, y, z, 3);
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, iicon);
        flag = true;
        
        renderer.flipTexture = false;
        tessellator.setBrightness(renderer.renderMinX > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x - 1, y, z));
        tessellator.setColorOpaque_F(f3, f3, f3);
        iicon = renderer.getBlockIcon(block, blockAccess, x, y, z, 4);
        renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, iicon);
        flag = true;
        
        renderer.flipTexture = false;
        tessellator.setBrightness(renderer.renderMaxX < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(blockAccess, x + 1, y, z));
        tessellator.setColorOpaque_F(f3, f3, f3);
        iicon = renderer.getBlockIcon(block, blockAccess, x, y, z, 5);
        renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, iicon);
        flag = true;
        
        renderer.flipTexture = false;
        return flag;
    }
}
