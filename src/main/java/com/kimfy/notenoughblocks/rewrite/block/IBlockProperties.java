package com.kimfy.notenoughblocks.rewrite.block;

import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface IBlockProperties
{   
    void setBeaconBaseAble(boolean isBeaconBase);
    
    void setBlockOpaqueness(boolean isOpaque);
    
    void setBlockStainable(boolean isStained);
    
    boolean isBlockStained();

    /**
     * Todo: This needs to go away, what was I even thinking...
     */
    Map<Block, Integer> silkTouchData= new HashMap<Block, Integer>(4096);
    
    default void setBlockSilkTouchable(Block block, int metadata)
    {
        silkTouchData.put(block, metadata);
    }

    void registerTextures(String[][] textures);
    
    void setBlockLightOpacity(int lightOpacity);
    
    void setSubBlocks(int subBlocks);
    
    void setData(List<JsonBlock> data);

    /**
     * Used to retrieve the JsonBlock object for this block.
     * How to use:
     * ((IBlockProperties) block).getData().get(metadata)
     * @return The list of JsonBlock objects attached to this block
     */
    List<JsonBlock> getData();
}
