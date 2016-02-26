package com.kimfy.notenoughblocks.common.util.block;

import lombok.Getter;
import net.minecraft.block.BlockPressurePlate;

@Getter
public enum EnumSensitivity
{
    EVERYTHING(BlockPressurePlate.Sensitivity.EVERYTHING),
    MOBS(BlockPressurePlate.Sensitivity.MOBS),
    PLAYERS(MOBS.getSensitivity());

    private BlockPressurePlate.Sensitivity sensitivity;

    EnumSensitivity(BlockPressurePlate.Sensitivity sensitivity)
    {
        this.sensitivity = sensitivity;
    }
}
