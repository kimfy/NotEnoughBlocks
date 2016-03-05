package com.kimfy.notenoughblocks.common.block;

import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface IBlockProperties
{   
    void setBeaconBaseable(boolean isBeaconBase);

    @Deprecated
    void setBlockOpaqueness(boolean isOpaque);
    
    void setBlockStainable(boolean isStained);

    void isSilkTouchable(boolean isSilkTouch, int metadata);
    
    void setBlockLightOpacity(int lightOpacity);

    void setSlipperiness(float slipperiness);

    void setData(List<BlockJson> data);

    List<BlockJson> getData();
}
