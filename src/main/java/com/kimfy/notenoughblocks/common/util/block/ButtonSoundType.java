package com.kimfy.notenoughblocks.common.util.block;

import lombok.AllArgsConstructor;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;

@AllArgsConstructor
public enum ButtonSoundType
{
    WOOD_ON(SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON),
    WOOD_OFF(SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF),
    STONE_ON(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON),
    STONE_OFF(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF);

    private SoundEvent sound;
    private static final SoundEvent DEFAULT = WOOD_ON.sound;

    public static SoundEvent getSound(@Nullable String sound)
    {
        if (sound == null) return DEFAULT;
        SoundEvent ret = null;
        for (ButtonSoundType bst : values())
        {
            String n = bst.name().split("_")[0];
            if (sound.equalsIgnoreCase(n))
            {
                ret = bst.sound;
            }
        }
        return ret != null ? ret : DEFAULT;
    }

    public static String toString(SoundEvent soundEvent)
    {
        for (ButtonSoundType bst : values())
        {
            if (bst.sound.equals(soundEvent))
                return bst.name().split("_")[0].toLowerCase();
        }
        return "wood";
    }
}