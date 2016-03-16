package com.kimfy.notenoughblocks.common.util.block;

import com.kimfy.notenoughblocks.common.block.*;
import com.kimfy.notenoughblocks.common.item.*;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

@Getter
public enum Shape
{
    CUBE(16, NEBBlock.class, NEBItemBlock.class),
    STAIR(1, NEBBlockStair.class, NEBItemBlockStair.class),
    SLAB(8, NEBBlockSlab.class, NEBItemBlockSlab.class),
    GLASS(16, NEBBlockGlass.class, NEBItemBlock.class),
    WALL(16, NEBBlockWall.class, NEBItemBlock.class),
    FENCE(16, NEBBlockFence.class, NEBItemBlock.class),
    ANVIL(1, NEBBlockAnvil.class, NEBItemBlockAnvil.class),
    BREWING_STAND(16, NEBBlockBrewingStand.class, NEBItemBlock.class),
    PRESSURE_PLATE(1, NEBBlockPressurePlate.class, NEBItemBlock.class),
    BEACON(16, NEBBlockBeacon.class, NEBItemBlock.class),
    BED(1, NEBBlockBed.class, NEBItemBlockBed.class),
    BUSH(16, NEBBlockBush.class, NEBItemBlock.class), // Small grass, fern, dead bush
    BUTTON(1, NEBBlockButton.class, NEBItemBlock.class),
    CACTUS(16, null, null),
    CAKE(16, null, null),
    CARPET(16, NEBBlockCarpet.class, NEBItemBlock.class),
    CAULDRON(16, null, null),
    COCOA(16, null, null),
    CROP(4, null, null),
    DAYLIGHT_DETECTOR(1, null, null),
    DIRECTIONAL(1, NEBBlockDirectional.class, NEBItemBlock.class),
    DOOR(1, NEBBlockDoor.class, NEBItemBlockDoor.class),
    DOUBLE_PLANT(8, NEBBlockDoublePlant.class, NEBItemBlock.class),
    DRAGON_EGG(16, null, null),
    ENCHANTING_TABLE(16, null, null),
    FALLING(16, NEBBlockFalling.class, NEBItemBlock.class),
    FENCE_GATE(1, NEBBlockFenceGate.class, NEBItemBlock.class),
    FARMLAND(16, null, null),
    FLOWER(16, null, null),
    FLOWER_POT(16, null, null),
    GRASS(16, NEBBlockGrass.class, NEBItemBlock.class),
    ICE(16, CUBE.getBlockClass(), CUBE.getItemClass()),
    LADDER(16, null, null),
    LEAVES(16, null, null),
    LEVER(16, null, null),
    LILYPAD(16, null, null),
    LIQUID(16, null, null),
    MUSHROOM(16, null, null),
    MUSHROOM_BLOCK(3, null, null),
    PANE(16, NEBBlockPane.class, NEBItemBlock.class),
    BARS(16, NEBBlockPane.class, NEBItemBlock.class),
    RAIL(16, null, null),
    REDSTONE_LAMP(8, null, null),
    REDSTONE_TORCH(1, null, null),
    SUGAR_CANE(16, null, null),
    ROTATING(4, NEBBlockRotating.class, NEBItemBlock.class),
    SAPLING(16, null, null),
    SIGN(16, null, null),
    SKULL(16, null, null),
    SLIME_BLOCK(16, null, null),
    LAYER(1, NEBBlockLayer.class, NEBItemBlockLayer.class),
    TALLGRASS(8, null, null), // Double grass, double fern
    TORCH(1, null, null),
    TRAPDOOR(1, null, null),
    TRIPWIRE(16, null, null),
    TRIPWIRE_HOOK(16, null, null),
    VINE(4, null, null),
    WEB(16, null, null),
    WEIGHTED_PRESSURE_PLATE(16, null, null),

    /* 1.9 */
    CHORUS_PLANT(16, null, null),
    CHORUS_FLOWER(16, null, null),
    END_ROD(16, null, null),
    GRASS_PATH(16, null, null);

    private int maxSubBlocks;
    private Class<? extends Block> blockClass;
    private  Class<? extends ItemBlock> itemClass;

    Shape(int maxSubBlocks, Class<?> cls, Class<?> itemClass)
    {
        this.maxSubBlocks = maxSubBlocks;
        this.blockClass = (Class<? extends Block>) cls;
        this.itemClass = (Class<? extends ItemBlock>) itemClass;
    }

    public static Shape get(String shape)
    {
        for (Shape v : values())
        {
            if (v.name().equalsIgnoreCase(shape))
            {
                return v;
            }
        }
        throw new IllegalArgumentException("Shape: " + shape + " does not exist. Refer to the wiki for valid shapes!");
    }

    public boolean isMetadataBlock()
    {
        return this.maxSubBlocks > 1;
    }
}
