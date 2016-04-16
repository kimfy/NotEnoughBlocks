package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

import java.util.List;

// TODO: Fix block light
public class NEBBlockStair extends BlockStairs implements IBlockProperties
{
    @Delegate
    private final BlockAgent<NEBBlockStair> agent;

    public NEBBlockStair(Material material, List<BlockJson> data)
    {
        super(new NEBBlock.Builder().setMaterial(material).setData(data).build().getDefaultState() /*NEBBlock.buildModBlock(material, data).getDefaultState()*/);
        this.agent = new BlockAgent<>(this, data);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }
}
