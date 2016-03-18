package com.kimfy.notenoughblocks.rewrite.client.renderer;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderItemBlock implements IItemRenderer
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

        switch(type)
        {
            case INVENTORY:
            {
                GL11.glPushMatrix();
                GL11.glScalef(1.0F, 1.0F, 1.0F);
                //GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
                render(item, 0.0F, -0.2F, 0.0F, renderer);
                GL11.glPopMatrix();
                break;
            }
            default:
            {
                break;
            }
        }
        return;
    }

    public void render(ItemStack stack, float x, float y, float z, RenderBlocks renderer)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        Tessellator tessellator = Tessellator.instance;

        //NEBItemBlockBed item = (NEBItemBlockBed) itemStack.getItem();
        //NEBBlockBed block = (NEBBlockBed) item.getBlock();
        Block block = Block.getBlockFromItem(stack.getItem());
        int metadata = stack.getItemDamage();

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

        //tessellator.startDrawingQuads();

        RenderItem.getInstance().renderItemAndEffectIntoGUI(fontRenderer, textureManager, stack, 0, 0);

        //tessellator.draw();
        GL11.glPopMatrix();
    }
}
