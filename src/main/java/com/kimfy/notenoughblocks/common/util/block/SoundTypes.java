package com.kimfy.notenoughblocks.common.util.block;

import com.kimfy.notenoughblocks.common.util.Log;
import lombok.Getter;
import net.minecraft.block.SoundType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class SoundTypes
{
    public static Map<String, SoundTypes> soundTypes = new HashMap<>();

    public static final SoundTypes WOOD = new SoundTypes("wood", SoundType.WOOD);
    public static final SoundTypes GROUND = new SoundTypes("ground", SoundType.GROUND);
    public static final SoundTypes PLANT = new SoundTypes("plant", SoundType.PLANT);
    public static final SoundTypes STONE = new SoundTypes("stone", SoundType.STONE);
    public static final SoundTypes METAL = new SoundTypes("metal", SoundType.METAL);
    public static final SoundTypes GLASS = new SoundTypes("glass", SoundType.GLASS);
    public static final SoundTypes CLOTH = new SoundTypes("cloth", SoundType.CLOTH);
    public static final SoundTypes SAND = new SoundTypes("sand", SoundType.SAND);
    public static final SoundTypes SNOW = new SoundTypes("snow", SoundType.SNOW);
    public static final SoundTypes LADDER = new SoundTypes("ladder", SoundType.LADDER);
    public static final SoundTypes ANVIL = new SoundTypes("anvil", SoundType.ANVIL);
    public static final SoundTypes SLIME = new SoundTypes("slime", SoundType.SLIME);

    private final String name;
    private final SoundType soundType;

    public SoundTypes(String name, SoundType soundType)
    {
        this.name = name;
        this.soundType = soundType;
        this.register();
    }

    private void register()
    {
        soundTypes.put(this.getName(), this);
    }

    public static SoundTypes get(final String soundType)
    {
        String name = soundType.toLowerCase();
        if (soundTypes.containsKey(name))
        {
            return soundTypes.get(name);
        }
        else
        {
            Log.error("SoundType {} does not exist", soundType);
            return STONE;
        }
    }

    @Override
    public String toString()
    {
        return this.getName();
    }

    /**
     * Returns the name of the given {@link SoundTypes}
     *
     * @param soundType The soundType to convert
     * @return The {@link SoundTypes#toString()} of the given soundType or {@link SoundTypes#STONE#toString()}
     */
    public static String toString(SoundType soundType)
    {
        Optional<SoundTypes> result = soundTypes.values().stream().filter(sType -> sType.getSoundType() == soundType).findFirst();
        return result.orElse(SoundTypes.STONE).toString();
    }
}
