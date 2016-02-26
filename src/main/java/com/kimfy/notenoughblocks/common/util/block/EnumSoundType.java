package com.kimfy.notenoughblocks.common.util.block;

import lombok.Getter;
import net.minecraft.block.Block;

@Getter
public enum EnumSoundType
{
    STONE(Block.soundTypeStone),
    WOOD(Block.soundTypeWood),
    GRAVEL(Block.soundTypeGravel),
    GRASS(Block.soundTypeGrass),
    PISTON(Block.soundTypePiston),
    METAL(Block.soundTypeMetal),
    GLASS(Block.soundTypeGlass),
    CLOTH(Block.soundTypeCloth),
    SAND(Block.soundTypeSand),
    SNOW(Block.soundTypeSnow),
    LADDER(Block.soundTypeLadder),
    ANVIL(Block.soundTypeAnvil);

    private Block.SoundType soundType;

    EnumSoundType(Block.SoundType soundType)
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
