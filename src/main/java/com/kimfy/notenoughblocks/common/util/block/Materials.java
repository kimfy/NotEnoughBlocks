package com.kimfy.notenoughblocks.common.util.block;

import com.kimfy.notenoughblocks.common.util.Log;
import lombok.Getter;
import net.minecraft.block.material.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public class Materials
{
    public static Map<String, Materials> materials = new HashMap<>();

    public static final Materials AIR = new Materials("air", Material.air);
    public static final Materials GRASS = new Materials("grass", Material.grass);
    public static final Materials GROUND = new Materials("ground", Material.ground);
    public static final Materials WOOD = new Materials("wood", Material.wood);
    public static final Materials ROCK = new Materials("rock", Material.rock);
    public static final Materials IRON = new Materials("iron", Material.iron);
    public static final Materials ANVIL = new Materials("anvil", Material.anvil);
    public static final Materials WATER = new Materials("water", Material.water);
    public static final Materials LAVA = new Materials("lava", Material.lava);
    public static final Materials LEAVES = new Materials("leaves", Material.leaves);
    public static final Materials PLANTS = new Materials("plants", Material.leaves);
    public static final Materials VINE = new Materials("vine", Material.vine);
    public static final Materials SPONGE = new Materials("sponge", Material.sponge);
    public static final Materials CLOTH = new Materials("cloth", Material.cloth);
    public static final Materials FIRE = new Materials("fire", Material.fire);
    public static final Materials SAND = new Materials("sand", Material.sand);
    public static final Materials CIRCUITS = new Materials("circuits", Material.circuits);
    public static final Materials CARPET = new Materials("carpet", Material.carpet);
    public static final Materials GLASS = new Materials("glass", Material.glass);
    public static final Materials REDSTONE_LIGHT = new Materials("redstone_light", Material.redstoneLight);
    public static final Materials TNT = new Materials("tnt", Material.tnt);
    public static final Materials CORAL = new Materials("coral", Material.coral);
    public static final Materials ICE = new Materials("ice", Material.ice);
    public static final Materials PACKED_ICE = new Materials("packed_ice", Material.packedIce);
    public static final Materials SNOW = new Materials("snow", Material.snow);
    public static final Materials CRAFTED_SNOW = new Materials("crafted_snow", Material.craftedSnow);
    public static final Materials CACTUS = new Materials("cactus", Material.cactus);
    public static final Materials CLAY = new Materials("clay", Material.clay);
    public static final Materials GOURD = new Materials("gourd", Material.gourd);
    public static final Materials DRAGON_EGG = new Materials("dragon_egg", Material.dragonEgg);
    public static final Materials PORTAL = new Materials("portal", Material.portal);
    public static final Materials CAKE = new Materials("cake", Material.cake);
    public static final Materials WEB = new Materials("web", Material.web);
    public static final Materials PISTON = new Materials("piston", Material.piston);

    private String name;
    private Material material;

    private Materials(String name, Material material)
    {
        this.name = name;
        this.material = material;
        this.register();
    }

    private void register()
    {
        materials.put(this.name, this);
    }

    public static Materials get(final String material)
    {
        String name = material.toLowerCase();
        if (materials.containsKey(name))
        {
            return materials.get(name);
        }
        else
        {
            Log.error("Material {} does not exist");
            return ROCK;
        }
    }

    @Override
    public String toString()
    {
        return this.getName();
    }

    /**
     * Returns the name of the given {@link Material}
     *
     * @param material The material to convert
     * @return The {@link Materials#toString()} of the given material or {@link Materials#ROCK#toString()}
     */
    public static String toString(Material material)
    {
        Optional<Materials> result = materials.values().stream().filter(mats -> mats.getMaterial() == material).findFirst();
        return result.orElse(Materials.ROCK).toString();
    }
}