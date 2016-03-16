package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockButton;
import net.minecraft.block.material.Material;

import java.util.List;

public class NEBBlockButton extends BlockButton implements IBlockProperties
{
    private final Material blockMaterial;

    @Delegate
    private final BlockAgent<NEBBlockButton> agent;

    public NEBBlockButton(Material material, List<BlockJson> data)
    {
        super(data.get(0).isButtonWooden());
        this.blockMaterial = material;
        this.agent = new BlockAgent<>(this, data);
    }

    @Override
    public Material getMaterial()
    {
        return this.blockMaterial;
    }
}
