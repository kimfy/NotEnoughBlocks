package com.kimfy.notenoughblocks.rewrite.client.renderer;

import com.kimfy.notenoughblocks.rewrite.block.NEBBlockDoor;
import com.kimfy.notenoughblocks.rewrite.item.NEBItemBlockDoor;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderItemDoor implements IItemRenderer
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
                GL11.glScalef(0.75f, 0.75f, 0.75F);
                render(item, -0.5F, 0.0F, 0.0F, renderer);
                GL11.glPopMatrix();
                break;
            }
            case EQUIPPED:
            {
                GL11.glPushMatrix();
                GL11.glRotatef(150, -0.825F, 1.5F, 0.0F);
                render(item, -1.2F, -1.2F, -0.350F, renderer);
                GL11.glPopMatrix();
                break;
            }
            case EQUIPPED_FIRST_PERSON:
            {
                GL11.glPushMatrix();
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-2.0F, -1.450F, -0.250F);
                render(item, 1, 1, 1, renderer);
                GL11.glPopMatrix();
                break;
            }
            case INVENTORY:
            {
                GL11.glPushMatrix();
                GL11.glScaled(0.66, 0.66, 0.66);
                render(item, -0.35F, -0.9F, 0, renderer);
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
        
        renderItemDoor(stack, renderer, 3);
        GL11.glTranslatef(0, 1, 0);
        renderItemDoor(stack, renderer, 8);
    }

    private void renderItemDoor(ItemStack stack, RenderBlocks renderer, int metadata)
    {
        Tessellator tessellator = Tessellator.instance;
        NEBItemBlockDoor item = (NEBItemBlockDoor) stack.getItem();
        NEBBlockDoor block = (NEBBlockDoor) item.getBlock();
        
        tessellator.startDrawingQuads();
        
        renderer.setRenderBounds(0F, 0F, 0F, 1F, 1F, 0.2F);
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        
        renderer.setRenderBounds(0F, 0F, 0F, 1.0F, 1.0F, 0.2F);
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, 3));
        
        IIcon iicon = renderer.getBlockIconFromSideAndMetadata(block, 2, metadata);
        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.2F);
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, iicon);
        
        renderer.flipTexture = false;
        iicon = renderer.getBlockIconFromSideAndMetadata(block, 3, metadata);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, iicon);
        
        renderer.flipTexture = false;
        iicon = renderer.getBlockIconFromSideAndMetadata(block, 4, metadata);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, iicon);
        
        renderer.flipTexture = false;
        iicon = renderer.getBlockIconFromSideAndMetadata(block, 5, metadata);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, iicon);
        
        renderer.flipTexture = false;
        tessellator.draw();
    }
}
