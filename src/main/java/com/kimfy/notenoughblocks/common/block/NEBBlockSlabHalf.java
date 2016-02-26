package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import net.minecraft.block.material.Material;

import java.util.List;

public class NEBBlockSlabHalf extends NEBBlockSlab
{
    public NEBBlockSlabHalf(Material materialIn, List<BlockJson> data)
    {
        super(materialIn, data);
    }

    public boolean isDouble()
    {
        return false;
    }
}
