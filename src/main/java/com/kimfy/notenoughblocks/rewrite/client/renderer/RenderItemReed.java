package com.kimfy.notenoughblocks.rewrite.client.renderer;

import com.kimfy.notenoughblocks.rewrite.item.IBlock;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderItemReed implements IItemRenderer
{

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        RenderBlocks renderer = (RenderBlocks) data[0];
        
        switch (type)
        {
            case ENTITY:
            {
                GL11.glPushMatrix();
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                render(item, 0.0F, 0.3F, 0.0F, renderer);
                GL11.glPopMatrix();
                break;
            }
            case EQUIPPED:
            {
                GL11.glPushMatrix();
                GL11.glScalef(0.8f, 0.8f, 0.8F);
                GL11.glRotatef(-45.0F, 0.0F, 1.0F, 0.0F);
                renderInHand(item, 0.6F, 0.0F, 0.0F, renderer);
                GL11.glPopMatrix();
                break;
            }
            case EQUIPPED_FIRST_PERSON:
            {
                GL11.glPushMatrix();
                GL11.glScaled(0.5F, 0.5F, 0.5F);
                GL11.glRotatef(22.5F, 0.0F, 1.0F, -0.25F);
                GL11.glTranslatef(-2.0F, 1.1F, -0.050F);
                render(item, 1, 0.8F, 1, renderer);
                GL11.glPopMatrix();
                break;
            }
            case INVENTORY:
            {
                GL11.glPushMatrix();
                GL11.glScaled(1F, 1F, 1F);
                GL11.glRotatef(22.5F, 0.0F, 1.0F, 0.0F);
                render(item, 0, 0, 0, renderer);
                GL11.glPopMatrix();
                break;
            }
            default:
            {
                break;
            }
        }
    }
    
    private void render(ItemStack stack, float x, float y, float z, RenderBlocks renderer)
    {
        GL11.glTranslatef(x, y, z);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        
        int metadata = stack.getItemDamage();
        Tessellator tessellator = Tessellator.instance;
        IBlock item = (IBlock) stack.getItem();
        Block block = item.getBlock();
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.6F, 0.0F);
        IIcon iicon = renderer.getBlockIconFromSideAndMetadata(block, 0, metadata);
        renderer.drawCrossedSquares(iicon, -0.5D, -0.5D, -0.5D, 1.0F);
        tessellator.draw();
    }
    
    /**
     * Renders the item but as a flat texture with no width or anything
     */
    private void renderInHand(ItemStack stack, float x, float y, float z, RenderBlocks renderer)
    {
        GL11.glTranslatef(x, y, z);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        
        int metadata = stack.getItemDamage();
        Tessellator tessellator = Tessellator.instance;
        IBlock item = (IBlock) stack.getItem();
        Block block = item.getBlock();
        
        tessellator.startDrawingQuads();
        IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 0, metadata);
        tessellator.setNormal(0.0F, 0.6F, 0.0F);
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.00);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
        tessellator.draw();
    }
}
