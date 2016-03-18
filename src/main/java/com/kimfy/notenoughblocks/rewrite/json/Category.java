package com.kimfy.notenoughblocks.rewrite.json;

import com.kimfy.notenoughblocks.rewrite.json.JsonBlock.Shape;
import org.apache.commons.lang3.builder.CompareToBuilder;

public class Category
{
    private JsonBlock data;
    private static int id = 0;
    
    public Category(JsonBlock block)
    {
        this.data = block;
    }
    
    public int compare(Category toCompare)
    {
        JsonBlock a = data;
        JsonBlock b = toCompare.getJsonBlock();
        
        return new CompareToBuilder()
            .append(a.getStringShape(), b.getStringShape())
            .append(a.getHardness(), b.getHardness())
            .append(a.getResistance(), b.getResistance())
            .append(a.getStepSound(), b.getStepSound())
            .append(a.getMaterial(), b.getMaterial())
            .append(a.isOpaque(), b.isOpaque())
            .append(a.isStained(), b.isStained())
            .append(a.isBeaconBase(), b.isBeaconBase())
            .append(a.isSilkTouch(), b.isSilkTouch())
            .append(a.isEnchantAmplifier(), b.isEnchantAmplifier())
            .append(a.getSlipperiness(), b.getSlipperiness())
            .append(a.getLightLevel(), b.getLightLevel())
            .append(a.getLightOpacity(), b.getLightOpacity())
            .append(a.isCanBlockGrass(), b.isCanBlockGrass())
            .append(a.getBlockParticleGravity(), b.getBlockParticleGravity())
            .append(a.getMobility(), b.getMobility())
            .append(a.isEnableStats(), b.isEnableStats())
            .append(a.isUseNeighborBrightness(), b.isUseNeighborBrightness())
            .toComparison();
    }
    
    public JsonBlock getJsonBlock ()
    {
        return data;
    }
    
    public Category getInstance()
    {
        return this;
    }
    
    public int getNextAvailableId()
    {
        return id++;
    }
    
    @Override
    /**
     * Returns the Category as a String formatted like: <br>
     * STRING, INT, FLOAT
     */
    public String toString()
    {
        JsonBlock b = data;
        
        return new StringBuilder()
            .append(b.getStringShape())
            .append("|")
            .append(b.getHardness())
            .append("|")
            .append(b.getResistance())
            .append("|")
            .append(b.getStepSound())
            .append("|")
            .append(b.getMaterial())
            .append("|")
            .append(b.isOpaque())
            .append("|")
            .append(b.isStained())
            .append("|")
            .append(b.isBeaconBase())
            .append("|")
            .append(b.isSilkTouch())
            .append("|")
            .append(b.isEnchantAmplifier())
            .append("|")
            .append(b.getSlipperiness())
            .append("|")
            .append(b.getLightLevel())
            .append("|")
            .append(b.getLightOpacity())
            .append("|")
            .append(b.isCanBlockGrass())
            .append("|")
            .append(b.getBlockParticleGravity())
            .append("|")
            .append(b.getMobility())
            .append("|")
            .append(b.isEnableStats())
            .append("|")
            .append(b.isUseNeighborBrightness())
            .toString();
    }
    
    public Shape getShape()
    {
    	return data.getShape();
    }
    
    public int getMaxSubBlocks()
    {
    	return data.getShape().getMaxSubBlocks();
    }
}
