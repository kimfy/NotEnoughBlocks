package com.kimfy.notenoughblocks.common.util.block;

import com.kimfy.notenoughblocks.common.block.*;
import com.kimfy.notenoughblocks.common.item.*;
import com.kimfy.notenoughblocks.common.util.Log;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class Shape
{
    public static Map<String, Shape> shapes = new HashMap<>();

    public static final Shape CUBE = new Shape("cube", 16, NEBBlock.class, NEBItemBlock.class);
    public static final Shape STAIR = new Shape("stair", 1, NEBBlockStair.class, NEBItemBlockStair.class).useNeighborBrightness(true);
    public static final Shape SLAB = new Shape("slab", 8, NEBBlockSlab.class, NEBItemBlockSlab.class).setLightOpacity(255).useNeighborBrightness(true);
    public static final Shape GLASS = new Shape("glass", 16, NEBBlockGlass.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape WALL = new Shape("wall", 16, NEBBlockWall.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape FENCE = new Shape("fence", 16, NEBBlockFence.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape ANVIL = new Shape("anvil", 1, NEBBlockAnvil.class, NEBItemBlockAnvil.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape BREWING_STAND = new Shape("brewing_stand", 16, NEBBlockBrewingStand.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape PRESSURE_PLATE = new Shape("pressure_plate", 1, NEBBlockPressurePlate.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape BEACON = new Shape("beacon", 16, NEBBlockBeacon.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape BED = new Shape("bed", 1, NEBBlockBed.class, NEBItemBlockBed.class);
    public static final Shape BUSH = new Shape("bush", 16, NEBBlockBush.class, NEBItemBlock.class).setLightOpacity(0).setTranslucency(true).useNeighborBrightness(true);
    public static final Shape BUTTON = new Shape("button", 1, NEBBlockButton.class, NEBItemBlock.class).setLightOpacity(0).setTranslucency(true).useNeighborBrightness(true);
    public static final Shape CACTUS = new Shape("cactus", 16, null, null);
    public static final Shape CAKE = new Shape("cake", 16, null, null);
    public static final Shape CARPET = new Shape("carpet", 16, NEBBlockCarpet.class, NEBItemBlock.class).setLightOpacity(0).setTranslucency(true).useNeighborBrightness(true);
    public static final Shape CAULDRON = new Shape("cauldron", 1, NEBBlockCauldron.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape COCOA = new Shape("cocoa", 16, null, null);
    public static final Shape CROP = new Shape("crop", 4, null, null);
    public static final Shape DAYLIGHT_DETECTOR = new Shape("daylight_detector", 1, null, null);
    public static final Shape DIRECTIONAL = new Shape("directional", 1, NEBBlockDirectional.class, NEBItemBlock.class);
    public static final Shape DOOR = new Shape("door", 1, NEBBlockDoor.class, NEBItemBlockDoor.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape DOUBLE_PLANT = new Shape("double_plant", 8, NEBBlockDoublePlant.class, NEBItemBlock.class).setLightOpacity(0).setTranslucency(true).useNeighborBrightness(true);
    public static final Shape DRAGON_EGG = new Shape("dragon_egg", 16, null, null);
    public static final Shape ENCHANTING_TABLE = new Shape("enchanting_table", 16, null, null);
    public static final Shape FALLING = new Shape("falling", 16, NEBBlockFalling.class, NEBItemBlock.class);
    public static final Shape FENCE_GATE = new Shape("fence_gate", 1, NEBBlockFenceGate.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape FARMLAND = new Shape("farmland", 16, null, null);
    public static final Shape FLOWER = new Shape("flower", 16, NEBBlockFlower.class, NEBItemBlock.class).setLightOpacity(0).setTranslucency(true).useNeighborBrightness(true);
    public static final Shape FLOWER_POT = new Shape("flower_pot", 16, null, null);
    public static final Shape GRASS = new Shape("grass", 16, NEBBlockGrass.class, NEBItemBlock.class);
    public static final Shape ICE = new Shape("ice", 16, CUBE.getBlockClass(), CUBE.getItemClass()).setLightOpacity(3).setTranslucency(false);
    public static final Shape LADDER = new Shape("ladder", 16, null, null);
    public static final Shape LEAVES = new Shape("leaves", 16, null, null).setLightOpacity(1);
    public static final Shape LEVER = new Shape("lever", 16, null, null);
    public static final Shape LILYPAD = new Shape("lilypad", 16, null, null);
    public static final Shape LIQUID = new Shape("liquid", 16, null, null);
    public static final Shape MUSHROOM = new Shape("mushroom", 16, null, null);
    public static final Shape MUSHROOM_BLOCK = new Shape("mushroom_block", 3, null, null);
    public static final Shape PANE = new Shape("pane", 16, NEBBlockPane.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape BARS = new Shape("bars", 16, NEBBlockPane.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape RAIL = new Shape("rail", 16, null, null);
    public static final Shape REDSTONE_LAMP = new Shape("redstone_lamp", 8, null, null);
    public static final Shape REDSTONE_TORCH = new Shape("redstone_torch", 1, null, null);
    public static final Shape SUGAR_CANE = new Shape("sugar_cane", 16, null, null);
    public static final Shape ROTATING = new Shape("rotating", 4, NEBBlockRotating.class, NEBItemBlock.class);
    public static final Shape SAPLING = new Shape("sapling", 16, null, null);
    public static final Shape SIGN = new Shape("sign", 16, null, null);
    public static final Shape SKULL = new Shape("skull", 16, null, null);
    public static final Shape SLIME_BLOCK = new Shape("slime_block", 16, null, null);
    public static final Shape LAYER = new Shape("layer", 1, NEBBlockLayer.class, NEBItemBlockLayer.class).setLightOpacity(0);
    public static final Shape TORCH = new Shape("torch", 1, null, null);
    public static final Shape TRAPDOOR = new Shape("trapdoor", 1, NEBBlockTrapDoor.class, NEBItemBlock.class).setLightOpacity(0).useNeighborBrightness(true);
    public static final Shape TRIPWIRE = new Shape("tripwire", 16, null, null);
    public static final Shape TRIPWIRE_HOOK = new Shape("tripwire_hook", 16, null, null);
    public static final Shape VINE = new Shape("vine", 4, null, null);
    public static final Shape WEB = new Shape("web", 16, NEBBlockWeb.class, NEBItemBlock.class).setLightOpacity(1);
    public static final Shape WEIGHTED_PRESSURE_PLATE = new Shape("weighted_pressure_plate", 16, null, null);

    /* 1.9 */
    public static final Shape CHORUS_PLANT = new Shape("chorus_plant", 16, null, null);
    public static final Shape CHORUS_FLOWER = new Shape("chorus_flower", 16, null, null);
    public static final Shape END_ROD = new Shape("end_rod", 16, null, null);
    public static final Shape GRASS_PATH = new Shape("grass_path", 16, null, null);

    private int lightOpacity = 255;
    private boolean translucent = false;
    private boolean useNeighborBrightness = false;

    @Getter private final String name;
    @Getter private final int maxSubBlocks;
    @Getter private final Class<? extends Block> blockClass;
    @Getter private final Class<? extends Item> itemClass;

    public Shape(String name, int maxSubBlocks, Class<? extends Block> blockClass, Class<? extends Item> itemClass)
    {
        this.name = name;
        this.maxSubBlocks = maxSubBlocks;
        this.blockClass = blockClass;
        this.itemClass = itemClass;
        this.register();
    }

    private void register()
    {
        shapes.put(this.getName(), this);
    }

    public static Shape get(String shape)
    {
        if (shapes.containsKey(shape))
        {
            return shapes.get(shape);
        }
        else
        {
            Log.error("Shape {} does not exist", shape);
            return CUBE;
        }
    }

    public boolean isMetadataBlock()
    {
        return this.maxSubBlocks > 1;
    }

    @Override
    public String toString()
    {
        return this.getName();
    }

    private Shape setLightOpacity(int lightOpacity)
    {
        this.lightOpacity = lightOpacity;
        return this;
    }

    private Shape setTranslucency(boolean translucent)
    {
        this.translucent = translucent;
        return this;
    }

    private Shape useNeighborBrightness(boolean useNeighborBrightness)
    {
        this.useNeighborBrightness = useNeighborBrightness;
        return this;
    }

    public int getLightOpacity()
    {
        return this.lightOpacity;
    }

    public boolean isTranslucent()
    {
        return this.translucent;
    }

    public boolean useNeighborBrightness()
    {
        return this.useNeighborBrightness;
    }
}