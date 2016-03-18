package com.kimfy.notenoughblocks.proxy;

import com.kimfy.notenoughblocks.rewrite.integration.Chisel;
import com.kimfy.notenoughblocks.util.Constants;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonProxy
{
    static Logger logger = LogManager.getLogger(Constants.MOD_NAME + ":CommonProxy");
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ;
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //Chisel.loadMinecraftIntegration();
        Chisel.loadNotEnoughBlocksIntegration();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ;
    }
}
