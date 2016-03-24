package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

import java.util.List;

public class NEBBlockSlabDouble extends NEBBlockSlab
{
    public NEBBlockSlabDouble(Material materialIn, List<BlockJson> data)
    {
        super(materialIn, data);
    }

    public boolean isDouble()
    {
        return true;
    }
}
