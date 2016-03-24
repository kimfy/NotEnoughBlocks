package com.kimfy.notenoughblocks.client;

import com.kimfy.notenoughblocks.client.file.json.OneEight;
import com.kimfy.notenoughblocks.common.ServerProxy;
import com.kimfy.notenoughblocks.common.block.NEBBlockBed;
import com.kimfy.notenoughblocks.common.block.NEBBlockDoor;
import com.kimfy.notenoughblocks.common.block.NEBBlockDoublePlant;
import com.kimfy.notenoughblocks.common.block.NEBBlockFenceGate;
import com.kimfy.notenoughblocks.common.file.resourcepack.ResourcePack;
import com.kimfy.notenoughblocks.common.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {   
        super.preInit(event);
        OneEight.writeBlockStateFiles();
        OneEight.registerItemModels();
        this.ignoreBlockProperties();
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

    private void ignoreBlockProperties()
    {
        List<Block> blocks = OneEight.blocks;

        for (Block block : blocks)
        {
            if (block instanceof NEBBlockBed)         ignoreStateOnBlock(block, BlockBed.OCCUPIED);
            if (block instanceof NEBBlockDoor)        ignoreStateOnBlock(block, BlockDoor.POWERED);
            if (block instanceof NEBBlockDoublePlant) ignoreStateOnBlock(block, NEBBlockDoublePlant.FACING);
            if (block instanceof NEBBlockFenceGate)   ignoreStateOnBlock(block, BlockFenceGate.POWERED);
        }
    }

    private void ignoreStateOnBlock(Block block, IProperty toIgnore)
    {
        if (block != null)
        {
            ModelLoader.setCustomStateMapper(block, new StateMap.Builder().ignore(new IProperty[]{ toIgnore }).build());
        }
    }
}
