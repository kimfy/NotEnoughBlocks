package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;

import java.util.List;

public class NEBBlockPressurePlate extends BlockPressurePlate implements IBlockProperties
{
    @Delegate
    private final BlockAgent<NEBBlockPressurePlate> agent;

    public NEBBlockPressurePlate(Material materialIn, List<BlockJson> data)
    {
        super(materialIn, data.get(0).getSensitivity());
        this.agent = new BlockAgent<>(this, data);
    }
}
