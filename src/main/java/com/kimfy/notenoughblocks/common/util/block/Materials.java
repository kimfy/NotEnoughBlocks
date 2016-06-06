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

    public static final Materials AIR = new Materials("air", Material.AIR);
    public static final Materials GRASS = new Materials("grass", Material.GRASS);
    public static final Materials GROUND = new Materials("ground", Material.GROUND);
    public static final Materials WOOD = new Materials("wood", Material.WOOD);
    public static final Materials ROCK = new Materials("rock", Material.ROCK);
    public static final Materials IRON = new Materials("iron", Material.IRON);
    public static final Materials ANVIL = new Materials("anvil", Material.ANVIL);
    public static final Materials WATER = new Materials("water", Material.WATER);
    public static final Materials LAVA = new Materials("lava", Material.LAVA);
    public static final Materials LEAVES = new Materials("leaves", Material.LEAVES);
    public static final Materials PLANTS = new Materials("plants", Material.LEAVES);
    public static final Materials VINE = new Materials("vine", Material.VINE);
    public static final Materials SPONGE = new Materials("sponge", Material.SPONGE);
    public static final Materials CLOTH = new Materials("cloth", Material.CLOTH);
    public static final Materials FIRE = new Materials("fire", Material.FIRE);
    public static final Materials SAND = new Materials("sand", Material.SAND);
    public static final Materials CIRCUITS = new Materials("circuits", Material.CIRCUITS);
    public static final Materials CARPET = new Materials("carpet", Material.CARPET);
    public static final Materials GLASS = new Materials("glass", Material.GLASS);
    public static final Materials REDSTONE_LIGHT = new Materials("redstone_light", Material.REDSTONE_LIGHT);
    public static final Materials TNT = new Materials("tnt", Material.TNT);
    public static final Materials CORAL = new Materials("coral", Material.CORAL);
    public static final Materials ICE = new Materials("ice", Material.ICE);
    public static final Materials PACKED_ICE = new Materials("packed_ice", Material.PACKED_ICE);
    public static final Materials SNOW = new Materials("snow", Material.SNOW);
    public static final Materials CRAFTED_SNOW = new Materials("crafted_snow", Material.CRAFTED_SNOW);
    public static final Materials CACTUS = new Materials("cactus", Material.CACTUS);
    public static final Materials CLAY = new Materials("clay", Material.CLAY);
    public static final Materials GOURD = new Materials("gourd", Material.GOURD);
    public static final Materials DRAGON_EGG = new Materials("dragon_egg", Material.DRAGON_EGG);
    public static final Materials PORTAL = new Materials("portal", Material.PORTAL);
    public static final Materials CAKE = new Materials("cake", Material.CAKE);
    public static final Materials WEB = new Materials("web", Material.WEB);
    public static final Materials PISTON = new Materials("piston", Material.PISTON);

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

    public static Materials get(String material)
    {
        String name = material.toLowerCase();
        if (materials.containsKey(name))
        {
            return materials.get(name);
        }
        else
        {
            Log.error("Material {} does not exist", material);
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