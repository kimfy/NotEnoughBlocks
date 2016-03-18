package com.kimfy.notenoughblocks.proxy;

import codechicken.nei.api.API;
import com.kimfy.notenoughblocks.rewrite.block.NEBBlockSlab;
import com.kimfy.notenoughblocks.rewrite.client.renderer.*;
import com.kimfy.notenoughblocks.rewrite.item.ICustomItemRender;
import com.kimfy.notenoughblocks.util.Constants;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;
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

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {   
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        registerRenders();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        hideFromNEI();
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders()
    {
        /* Blocks */
        RenderingRegistry.registerBlockHandler(new RenderBlockFence());
        RenderingRegistry.registerBlockHandler(new RenderBlockGrass());
        RenderingRegistry.registerBlockHandler(new RenderBlockStainedGlassPane());
        RenderingRegistry.registerBlockHandler(new RenderBlockPane());
        RenderingRegistry.registerBlockHandler(new RenderBlockDoor());
        RenderingRegistry.registerBlockHandler(new RenderBlockFlower());
        RenderingRegistry.registerBlockHandler(new RenderBlockDoublePlant());
        RenderingRegistry.registerBlockHandler(new RenderBlockLilyPad());

        /* Items */
        for (Object o : GameData.getItemRegistry())
        {
            if (o != null)
            {
                Item item = (Item) o;

                if (item instanceof ICustomItemRender)
                {
                    //NotEnoughBlocks.logger.info("Registering renderer for: " + item.getUnlocalizedName());
                    ((ICustomItemRender) item).registerItemRender();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void hideFromNEI()
    {
        /** Hide double slabs from NEI */
        if (Loader.isModLoaded("NotEnoughItems"))
        {
            for (Object o : GameData.getBlockRegistry())
            {
                Block block = (Block)o;
                String blockName = block.getLocalizedName();
                final Logger logger = LogManager.getLogger(Constants.MOD_NAME);

                if (block instanceof NEBBlockSlab)
                {
                    if (((NEBBlockSlab)block).doubleSlab)
                    {
                        for (int i = 0; i <= 15; i++)
                        {
                            API.hideItem(new ItemStack(block, 1, i));
                        }
                    }
                }
            }
        }
    }
}
