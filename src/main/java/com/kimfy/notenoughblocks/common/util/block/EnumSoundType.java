package com.kimfy.notenoughblocks.common.util.block;

import lombok.Getter;
import net.minecraft.block.SoundType;

@Getter
public enum EnumSoundType
{
    STONE(SoundType.STONE),
    WOOD(SoundType.WOOD),
    GRAVEL(SoundType.GROUND), // was soundTypeGravel
    GRASS(SoundType.GROUND), // was soundTypeGrass
    PLANT(SoundType.PLANT),
    PISTON(SoundType.STONE),
    METAL(SoundType.METAL),
    GLASS(SoundType.GLASS),
    CLOTH(SoundType.CLOTH),
    SAND(SoundType.SAND),
    SNOW(SoundType.SNOW),
    LADDER(SoundType.LADDER),
    ANVIL(SoundType.ANVIL);

    private SoundType soundType;

    EnumSoundType(SoundType soundType)
    {
        this.soundType = soundType;
    }

    public static EnumSoundType get(String stepSound)
    {
        for (EnumSoundType v : values())
        {
            if (v.name().equalsIgnoreCase(stepSound))
            {
                return v;
            }
        }
        throw new IllegalArgumentException("Step Sound (stepSound): " + stepSound + " does not exist. Refer to the wiki for valid sound types!");
    }
}
