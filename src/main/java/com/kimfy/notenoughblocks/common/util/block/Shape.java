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
    //STAINED_GLASS(16, null, null), - Removed in favor of the 'glass' shape. use 'stained' to set stainability in json
    WALL(16, NEBBlockWall.class, NEBItemBlock.class),
    FENCE(16, NEBBlockFence.class, NEBItemBlock.class),
    ANVIL(16, null, null),
    BREWING_STAND(16, null, null),
    PRESSURE_PLATE(1, null, null),
    BEACON(16, null, null),
    BED(1, null, null),
    BUSH(16, null, null),
    BUTTON(16, null, null),
    CACTUS(16, null, null),
    CAKE(16, null, null),
    CARPET(16, NEBBlockCarpet.class, NEBItemBlock.class),
    CAULDRON(16, null, null),
    COCOA(16, null, null),
    CROP(4, null, null),
    DAYLIGHT_DETECTOR(1, null, null),
    DEADBUSH(16, null, null), // {@link BlockDeadBush}. Dead Bush: This is the one that spawns in deserts
    DIRECTIONAL(5, null, null),
    DOOR(1, NEBBlockDoor.class, NEBItemBlockDoor.class),
    DOUBLE_PLANT(8, NEBBlockDoublePlant.class, NEBItemBlock.class),
    DRAGON_EGG(16, null, null),
    ENCHANTING_TABLE(16, null, null),
    FALLING(16, null, null),
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
    PANE(16, null, null),
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
    TALLGRASS(16, null, null), // {@link BlockTallGrass}. Shrub(unused), Tallgrass, Fern
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
}
