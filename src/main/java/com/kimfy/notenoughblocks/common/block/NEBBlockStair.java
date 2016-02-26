package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;

import java.util.List;

/*
    TODO: Fix lightning
 */
public class NEBBlockStair extends BlockStairs implements IBlockProperties
{
    @Delegate
    private final BlockAgent<NEBBlockStair> agent;

    public NEBBlockStair(Material material, List<BlockJson> data)
    {
        super(NEBBlock.buildModBlock(material, data).getDefaultState());
        this.agent = new BlockAgent<>(this);
    }
}
