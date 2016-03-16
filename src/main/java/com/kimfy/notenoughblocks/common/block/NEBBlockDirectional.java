package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.material.Material;

import java.util.List;

public class NEBBlockDirectional extends BlockPumpkin implements IBlockProperties
{
    private final Material blockMaterial;

    @Delegate
    private final BlockAgent<NEBBlockDirectional> agent;

    public NEBBlockDirectional(Material material, List<BlockJson> data)
    {
        super();
        this.blockMaterial = material;
        this.agent = new BlockAgent<>(this, data);
    }

    @Override
    public Material getMaterial()
    {
        return this.blockMaterial;
    }
}
