package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;

import java.util.List;

public class NEBBlockFenceGate extends BlockFenceGate implements IBlockProperties
{
    private final Material blockMaterial;

    @Delegate
    private final BlockAgent<NEBBlockFenceGate> agent;

    public NEBBlockFenceGate(Material material, List<BlockJson> data)
    {
        super(BlockPlanks.EnumType.OAK);
        this.blockMaterial = material;
        this.agent = new BlockAgent<>(this, data);
    }

    @Override
    public Material getMaterial()
    {
        return this.blockMaterial;
    }
}
