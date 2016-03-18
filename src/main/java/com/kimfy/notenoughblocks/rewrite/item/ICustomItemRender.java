package com.kimfy.notenoughblocks.rewrite.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface ICustomItemRender
{
    @SideOnly(Side.CLIENT)
    void registerItemRender();
}
