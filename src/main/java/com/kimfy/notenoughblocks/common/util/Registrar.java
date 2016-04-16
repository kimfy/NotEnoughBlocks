package com.kimfy.notenoughblocks.common.util;

import java.util.LinkedList;
import java.util.List;

public class Registrar
{
    /**
     * Contains everything that needs to be registered in the mod
     */
    private static List<IRegisterable> modRegistry = new LinkedList<>();

    public static void load()
    {
        if (modRegistry.isEmpty())
        {
            Log.info("There are no objects to register");
        }
        else
        {
            modRegistry.forEach(IRegisterable::register);
        }
    }

    public static void register(IRegisterable object)
    {
        modRegistry.add(object);
    }
}
