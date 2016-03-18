package com.kimfy.notenoughblocks;

import codechicken.nei.api.API;
import com.google.common.base.Stopwatch;
import com.kimfy.notenoughblocks.handler.ConfigHandler;
import com.kimfy.notenoughblocks.proxy.ClientProxy;
import com.kimfy.notenoughblocks.proxy.CommonProxy;
import com.kimfy.notenoughblocks.rewrite.NEB;
import com.kimfy.notenoughblocks.rewrite.block.NEBBlockSlab;
import com.kimfy.notenoughblocks.rewrite.client.renderer.*;
import com.kimfy.notenoughblocks.rewrite.item.ICustomItemRender;
import com.kimfy.notenoughblocks.rewrite.item.NEBItemBlockReed;
import com.kimfy.notenoughblocks.util.Constants;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

@Mod(name = Constants.MOD_NAME, modid = Constants.MOD_ID, version = Constants.MOD_VERSION)
public class NotEnoughBlocks
{
    @Mod.Instance
    public static NotEnoughBlocks instance;
    public static final Logger logger = LogManager.getLogger(Constants.MOD_NAME);

    @SidedProxy(clientSide = Constants.CLIENT_PROXY, serverSide = Constants.COMMON_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	Stopwatch watch = Stopwatch.createStarted();
        logger.info("Pre-Initialization started. This may take several minutes depending on how many resource packs we're processing");
        /*proxy.preInit(event);*/
        ConfigHandler.load();
        NEB.initiate();
        logger.info("Pre-Initialization ended after " + watch.elapsed(TimeUnit.MILLISECONDS) + "ms(" + watch.elapsed(TimeUnit.SECONDS) + "s)");
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Stopwatch watch = Stopwatch.createStarted();
        logger.info("Initialization started.");
        proxy.init(event);
        logger.info("Initialization ended after " + watch.elapsed(TimeUnit.MILLISECONDS) + "ms(" + watch.elapsed(TimeUnit.SECONDS) + "s)");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        Stopwatch watch = Stopwatch.createStarted();
        logger.info("Post-Initialization started.");
        proxy.postInit(event);
        logger.info("Post-Initialization ended after " + watch.elapsed(TimeUnit.MILLISECONDS) + "ms(" + watch.elapsed(TimeUnit.SECONDS) + "s)");
    }
}