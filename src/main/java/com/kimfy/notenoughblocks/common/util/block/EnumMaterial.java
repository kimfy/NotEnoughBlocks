package com.kimfy.notenoughblocks.common.util.block;

import lombok.Getter;
import net.minecraft.block.material.Material;

import java.util.Arrays;
import java.util.List;

@Getter
public enum EnumMaterial
{
    AIR(Material.air),
    GRASS(Material.grass),
    GROUND(Material.ground),
    WOOD(Material.wood),
    ROCK(Material.rock),
    IRON(Material.iron),
    ANVIL(Material.anvil),
    WATER(Material.water),
    LAVA(Material.lava),
    LEAVES(Material.leaves),
    PLANTS(Material.leaves),
    VINE(Material.vine),
    SPONGE(Material.sponge),
    CLOTH(Material.cloth),
    FIRE(Material.fire),
    SAND(Material.sand),
    CIRCUITS(Material.circuits),
    CARPET(Material.carpet),
    GLASS(Material.glass),
    REDSTONE_LIGHT(Material.redstoneLight),
    TNT(Material.tnt),
    CORAL(Material.coral),
    ICE(Material.ice),
    PACKED_ICE(Material.packedIce),
    SNOW(Material.snow),
    CRAFTED_SNOW(Material.craftedSnow),
    CACTUS(Material.cactus),
    CLAY(Material.clay),
    GOURD(Material.gourd),
    DRAGON_EGG(Material.dragonEgg),
    PORTAL(Material.portal),
    CAKE(Material.cake),
    WEB(Material.web),
    PISTON(Material.piston);

    private Material material;

    EnumMaterial(Material material)
    {
        this.material = material;
    }

    public static EnumMaterial get(String material)
    {
        for (EnumMaterial v : values())
        {
            if (v.name().equalsIgnoreCase(material))
            {
                return v;
            }
        }
        throw new IllegalArgumentException("Material: " + material + " does not exist. Refer to the wiki for valid materials!");
    }

    private transient static List<EnumMaterial> materials = Arrays.asList(EnumMaterial.values());

    public static String toString(Material material)
    {
        for (EnumMaterial e : EnumMaterial.values())
        {
            if (e.getMaterial() == material)
                return e.toString();
        }
        return ROCK.toString();
    }

    @Override
    public String toString()
    {
        return this.name();
    }
}
