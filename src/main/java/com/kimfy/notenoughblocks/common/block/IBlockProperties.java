package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;

import java.util.List;

public interface IBlockProperties
{   
    void setBeaconBaseable(boolean isBeaconBase);
    
    void setBlockStainable(boolean isStained);

    void setSlipperiness(float slipperiness);

    List<BlockJson> getData();

    BlockAgent getBlockAgent();

    void setBlockMaterial(Material material);

    void setBlockSoundType(SoundType soundType);

    IProperty<?>[] getBlockStateProperties();

    void addBlockStateProperty(IProperty<?> property);

    void addBlockStateProperties(IProperty<?>... property);

    Shape getShape();

    void setUseNeighborBrightness(boolean useNeighborBrightness);

    void setTranslucency(boolean translucent);
}