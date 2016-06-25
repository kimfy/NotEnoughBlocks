package com.kimfy.notenoughblocks.common.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

public class ItemAgent<T extends Item & IItemProperties> implements IItemProperties
{
    private T item;

    ItemAgent(@Nonnull T item) { this.item = item; }

    public ItemAgent<T> getItemAgent()
    {
        return this;
    }

    public Item getItem()
    {
        return this.item;
    }

    @Override
    public void register(ResourceLocation registryName)
    {
        GameRegistry.register(item, registryName);
    }
}