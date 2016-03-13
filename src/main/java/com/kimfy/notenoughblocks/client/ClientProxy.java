package com.kimfy.notenoughblocks.client;

import com.kimfy.notenoughblocks.client.file.json.OneEight;
import com.kimfy.notenoughblocks.common.ServerProxy;
import com.kimfy.notenoughblocks.common.file.resourcepack.ResourcePack;
import com.kimfy.notenoughblocks.common.util.Constants;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {   
        super.preInit(event);
        OneEight.writeBlockStateFiles();
        OneEight.registerItemModels();
        this.registerResourcePack();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    private void registerResourcePack()
    {
        ResourcePack resourcePack = new ResourcePack("resourcepacks/" + Constants.MOD_NAME, Constants.MOD_NAME);
    }
}
