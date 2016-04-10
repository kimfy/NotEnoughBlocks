package com.kimfy.notenoughblocks.common.util;

import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtilities
{
    private static final List<Object> blockRegistry = new ArrayList<>();

    public static List<Object> getBlockRegistryAsList()
    {
        if (blockRegistry.isEmpty())
        {
            Block.blockRegistry.forEach(blockRegistry::add);
        }
        return blockRegistry;
    }

    public static boolean notNull(Object o)
    {
        return o != null;
    }
}
