package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;

import java.util.List;

public class NEBBlockTrapDoor extends BlockTrapDoor implements IBlockProperties
{
    @Delegate
    private final BlockAgent<NEBBlockTrapDoor> agent;

    public NEBBlockTrapDoor(Material material, List<BlockJson> data)
    {
        super(material);
        this.agent = new BlockAgent<>(this, data);
    }
}