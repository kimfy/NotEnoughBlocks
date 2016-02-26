package com.kimfy.notenoughblocks;

import com.google.common.base.Stopwatch;
import com.kimfy.notenoughblocks.common.ServerProxy;
import com.kimfy.notenoughblocks.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
    public static ServerProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Stopwatch watch = Stopwatch.createStarted();
        logger.info("Pre-Initialization started. This may take several minutes depending on how many resource packs we're processing");
        proxy.preInit(event);
        logger.info("Pre-Initialization ended after " + watch.elapsed(TimeUnit.MILLISECONDS) + "ms(" + watch.elapsed(TimeUnit.SECONDS) + "s)");
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        Stopwatch watch = Stopwatch.createStarted();
        logger.info("Initialization started.");
        proxy.init(event);
        logger.info("Initialization ended after " + watch.elapsed(TimeUnit.MILLISECONDS) + "ms(" + watch.elapsed(TimeUnit.SECONDS) + "s)");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        Stopwatch watch = Stopwatch.createStarted();
        logger.info("Post-Initialization started.");
        proxy.postInit(event);
        logger.info("Post-Initialization ended after " + watch.elapsed(TimeUnit.MILLISECONDS) + "ms(" + watch.elapsed(TimeUnit.SECONDS) + "s)");
    }
}