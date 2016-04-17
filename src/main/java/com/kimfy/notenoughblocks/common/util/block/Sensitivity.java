package com.kimfy.notenoughblocks.common.util.block;

import com.kimfy.notenoughblocks.common.util.Log;
import lombok.Getter;
import net.minecraft.block.BlockPressurePlate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class Sensitivity
{
    public static Map<String, Sensitivity> sensitivities = new HashMap<>();

    public static final Sensitivity EVERYTHING = new Sensitivity("everything", BlockPressurePlate.Sensitivity.EVERYTHING);
    public static final Sensitivity MOBS = new Sensitivity("mobs", BlockPressurePlate.Sensitivity.MOBS);
    public static final Sensitivity PLAYERS = new Sensitivity("players", MOBS.getSensitivity());

    private final String name;
    private final BlockPressurePlate.Sensitivity sensitivity;

    public Sensitivity(String name, BlockPressurePlate.Sensitivity sensitivity)
    {
        this.name = name;
        this.sensitivity = sensitivity;
        this.register();
    }

    private void register()
    {
        sensitivities.put(this.getName(), this);
    }

    public static Sensitivity get(final String sensitivity)
    {
        String name = sensitivity.toLowerCase();
        if (sensitivities.containsKey(name))
        {
            return sensitivities.get(name);
        }
        else
        {
            Log.error("Sensitivity {} does not exist", sensitivity);
            return EVERYTHING;
        }
    }

    @Override
    public String toString()
    {
        return this.getName();
    }

    /**
     * Returns the name of the given {@link Sensitivity}
     *
     * @param sensitivity The sensitivity to convert
     * @return The {@link Materials#toString()} of the given sensitivity or {@link Sensitivity#EVERYTHING#toString()}
     */
    public static String toString(BlockPressurePlate.Sensitivity sensitivity)
    {
        Optional<Sensitivity> result = sensitivities.values().stream().filter(sens -> sens.getSensitivity() == sensitivity).findFirst();
        return result.orElse(Sensitivity.EVERYTHING).toString();
    }
}
