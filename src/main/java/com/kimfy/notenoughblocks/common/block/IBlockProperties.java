package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import java.util.List;


public interface IBlockProperties
{   
    void setBeaconBaseable(boolean isBeaconBase);

    @Deprecated
    void setBlockOpaqueness(boolean isOpaque);
    
    void setBlockStainable(boolean isStained);

    void isSilkTouchable(boolean isSilkTouch, int metadata);
    
    void setBlockLightOpacity(int lightOpacity);

    void setSlipperiness(float slipperiness);

    List<BlockJson> getData();

    BlockAgent getBlockAgent();

    void setBlockMaterial(Material material);

    void setBlockSoundType(SoundType soundType);
}