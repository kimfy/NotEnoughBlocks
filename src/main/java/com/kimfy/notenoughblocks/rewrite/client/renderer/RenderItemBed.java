package com.kimfy.notenoughblocks.rewrite.client.renderer;

import com.kimfy.notenoughblocks.rewrite.block.NEBBlockBed;
import com.kimfy.notenoughblocks.rewrite.item.NEBItemBlockBed;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Credit: ganymedes<br>
 * https://github.com/ganymedes01/Gany-s-Surface/blob/master/src/main/java/ganymedes01/ganyssurface/client/renderer/item/vanilla/ItemBedRenderer.java
 * <br>
 * I've made changes to fit my environment, obviously.
 */
public class RenderItemBed implements IItemRenderer
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
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data)
    {
        RenderBlocks renderer = (RenderBlocks) data[0];
        
        switch (type)
        {
            case ENTITY:
            {
                GL11.glPushMatrix();
                render(stack, -0.5F, -0.25F, -1.0F, renderer);
                GL11.glPopMatrix();
                break;
            }
            case EQUIPPED:
            {
                GL11.glPushMatrix();
                GL11.glScalef(0.66F, 0.66F, 0.66F);
                GL11.glRotatef(240F, 0.0F, 1.0F, 0.0F);
                render(stack, -.2F, .3F, -1.5F, renderer);
                GL11.glPopMatrix();
                break;
            }
            case EQUIPPED_FIRST_PERSON:
            {
                GL11.glPushMatrix();
                GL11.glScalef(0.66F, 0.66F, 0.66F);
                GL11.glRotatef(160, 0.0F, 1.0F, 0.0F);
                render(stack, -1F, 0.8F, -2F, renderer);
                GL11.glPopMatrix();
                break;
            }
            case INVENTORY:
            {
                GL11.glPushMatrix();
                GL11.glScalef(0.66F, 0.66F, 0.66F);
                GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                render(stack, -0.5F, -0.45F, -1.0F, renderer);
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
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        
        renderBed(stack, renderer, 0, false);
        GL11.glTranslatef(0, 0, 1);
        renderBed(stack, renderer, 8, true);
        
        GL11.glPopMatrix();
    }
    
    private void renderBed(ItemStack itemStack, RenderBlocks renderer, int metadata, boolean flag) {
        Tessellator tessellator = Tessellator.instance;

        NEBItemBlockBed item = (NEBItemBlockBed) itemStack.getItem();
        NEBBlockBed block = (NEBBlockBed) item.getBlock();
        
        tessellator.startDrawingQuads();

        // BOTTOM
        renderer.setRenderBounds(0, 3.0 / 16.0, 0, 1, 1, 1);
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));

        // TOP
        renderer.setRenderBoundsFromBlock(block);
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        if (flag)
            renderer.uvRotateTop = 1;
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        if (flag)
            renderer.uvRotateTop = 0;

        // BACK
        if (flag) {
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        } else {
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        }

        // SIDES
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));

        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.flipTexture = true;
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        renderer.flipTexture = false;
        tessellator.draw();
    }
}
