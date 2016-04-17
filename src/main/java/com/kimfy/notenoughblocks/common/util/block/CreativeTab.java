package com.kimfy.notenoughblocks.common.util.block;

import com.kimfy.notenoughblocks.common.util.Log;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class CreativeTab
{
    public static Map<String, CreativeTab> creativeTabs = new HashMap<>();

    public static final CreativeTab BLOCKS = new CreativeTab("blocks", CreativeTabs.tabBlock);
    public static final CreativeTab BUILDINGBLOCKS = new CreativeTab("buildingblocks", CreativeTabs.tabBlock);
    public static final CreativeTab DECORATIONS = new CreativeTab("decorations", CreativeTabs.tabDecorations);
    public static final CreativeTab REDSTONE = new CreativeTab("redstone", CreativeTabs.tabRedstone);
    public static final CreativeTab TRANSPORT = new CreativeTab("transport", CreativeTabs.tabTransport);
    public static final CreativeTab MISC = new CreativeTab("misc", CreativeTabs.tabMisc);
    public static final CreativeTab SEARCH = new CreativeTab("search", CreativeTabs.tabAllSearch);
    public static final CreativeTab FOOD = new CreativeTab("food", CreativeTabs.tabFood);
    public static final CreativeTab TOOLS = new CreativeTab("tools", CreativeTabs.tabTools);
    public static final CreativeTab COMBAT = new CreativeTab("combat", CreativeTabs.tabCombat);
    public static final CreativeTab BREWING = new CreativeTab("brewing", CreativeTabs.tabBrewing);
    public static final CreativeTab MATERIALS = new CreativeTab("materials", CreativeTabs.tabMaterials);
    public static final CreativeTab INVENTORY = new CreativeTab("inventory", CreativeTabs.tabInventory);

    private final String name;
    private final CreativeTabs creativeTab;

    public CreativeTab(String name, CreativeTabs creativeTab)
    {
        this.name = name;
        this.creativeTab = creativeTab;
        this.register();
    }

    private void register()
    {
        creativeTabs.put(this.getName(), this);
    }

    public static CreativeTab get(final String creativeTab)
    {
        String name = creativeTab.toLowerCase();
        if (creativeTabs.containsKey(name))
        {
            return creativeTabs.get(name);
        }
        else
        {
            Log.error("CreativeTab {} does not exist");
            return BUILDINGBLOCKS;
        }
    }

    @Override
    public String toString()
    {
        return this.getName();
    }

    /**
     * Returns the name of the given {@link CreativeTab}
     *
     * @param creativeTab The creativeTab to convert
     * @return The {@link Materials#toString()} of the given creativeTab or {@link CreativeTab#BUILDINGBLOCKS#toString()}
     */
    public static String toString(CreativeTabs creativeTab)
    {
        Optional<CreativeTab> result = creativeTabs.values().stream().filter(cTab -> cTab.getCreativeTab() == creativeTab).findFirst();
        return result.orElse(CreativeTab.BUILDINGBLOCKS).toString();
    }
}