package com.kimfy.notenoughblocks.common;

import com.kimfy.notenoughblocks.common.file.FileManager;
import com.kimfy.notenoughblocks.common.file.json.Blocks;
import com.kimfy.notenoughblocks.common.file.json.JsonProcessor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy
{
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Blocks.loadBlocks();

        FileManager fileManager = new FileManager();
            fileManager.makeDirectories();
            fileManager.findAndProcessResourcePacks();

        JsonProcessor jsonProcessor = new JsonProcessor();
            jsonProcessor.loadFiles();
            jsonProcessor.processData();

        JsonProcessor.injectLanguageMap();
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        //Chisel.loadNotEnoughBlocksIntegration();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
