package com.kimfy.notenoughblocks.handler;

import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import com.kimfy.notenoughblocks.util.Constants;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class ConfigHandler
{
    public static Configuration config = new Configuration(new File("config/" + Constants.MOD_ID + "/" + Constants.MOD_ID + ".cfg"));
    public static final String SHAPES = "shapes";

    public static void load()
    {
        config.addCustomCategoryComment(SHAPES, "Enable or disable shapes that can be generated. This does not apply to custom made json files or already generated json files.");

        for (JsonBlock.Shape shape : JsonBlock.Shape.values())
        {
            config.get(SHAPES, shape.name(), true).getBoolean(true);
        }

        if (config.hasChanged())
        {
            config.save();
        }
    }

    public static boolean getBoolean(String name, boolean default_)
    {
        Property property = config.get(SHAPES, name, default_);
        return property.getBoolean(default_);
    }
}
