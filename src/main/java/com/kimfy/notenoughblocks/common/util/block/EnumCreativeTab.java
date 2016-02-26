package com.kimfy.notenoughblocks.common.util.block;

import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;

@Getter
public enum EnumCreativeTab
{
    BLOCKS(CreativeTabs.tabBlock),
    BUILDINGBLOCKS(CreativeTabs.tabBlock),
    DECORATIONS(CreativeTabs.tabDecorations),
    REDSTONE(CreativeTabs.tabRedstone),
    TRANSPORT(CreativeTabs.tabTransport),
    MISC(CreativeTabs.tabMisc),
    SEARCH(CreativeTabs.tabAllSearch),
    FOOD(CreativeTabs.tabFood),
    TOOLS(CreativeTabs.tabTools),
    COMBAT(CreativeTabs.tabCombat),
    BREWING(CreativeTabs.tabBrewing),
    MATERIALS(CreativeTabs.tabMaterials),
    INVENTORY(CreativeTabs.tabInventory);

    private CreativeTabs creativeTab;

    EnumCreativeTab(CreativeTabs creativeTab)
    {
        this.creativeTab = creativeTab;
    }

    public static EnumCreativeTab get(String creativeTab)
    {
        for (EnumCreativeTab v : values())
        {
            if (v.name().equalsIgnoreCase(creativeTab))
            {
                return v;
            }
        }
        throw new IllegalArgumentException("Creative Tab " + creativeTab + " does not exist. Refer to the wiki for valid creative tabs!");
    }
}
