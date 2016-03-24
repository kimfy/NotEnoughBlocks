package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.material.Material;

import java.util.List;

public class NEBBlockFlower extends NEBBlockBush implements IBlockProperties
{
    @Delegate
    private final BlockAgent<NEBBlockFlower> agent;

    public NEBBlockFlower(Material material, List<BlockJson> data)
    {
        super(material, data);
        this.agent = new BlockAgent<>(this, data);
    }
}
