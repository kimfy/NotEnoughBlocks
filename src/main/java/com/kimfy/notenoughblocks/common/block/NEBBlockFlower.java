package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import net.minecraft.block.material.Material;

import java.util.List;

public class NEBBlockFlower extends NEBBlockBush implements IBlockProperties
{
    public NEBBlockFlower(Material material, List<BlockJson> data)
    {
        super(material, data);
    }
}
