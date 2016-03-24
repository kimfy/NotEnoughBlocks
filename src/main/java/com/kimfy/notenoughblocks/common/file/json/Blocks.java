package com.kimfy.notenoughblocks.common.file.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Since I am supposed to be completely data driven, there is actually no need for all the blocks
 * to be specified/hardcoded like this.
 *
 * TODO: Export the blocklist to a JSON file, put it in assets/json/blocks
 */
public class Blocks
{
    public static List<BlockJson> blockList = new ArrayList<>();
    
    public static void loadBlocks()
    {
        BlockJson tmp, rock, dirt, sandstone, rockSlab, lamp, sponge, woodFence, woodFenceGate, woodDoor;

        blockList.add(rock = new BlockJson().displayName("Stone").hardness(1.5F).resistance(10.0F).textures("all: stone"));
        blockList.add(new BlockJson().displayName("Cobblestone").parent(rock).textures("all: cobblestone"));
        blockList.add(new BlockJson().displayName("Mossy Cobblestone").parent(rock).textures("all: cobblestone_mossy"));
        blockList.add(new BlockJson().displayName("Grass Block").shape("grass").material("grass").stepSound("plant").canBlockGrass(true).textures("particle: dirt, down: dirt, up: grass_top, allSides: grass_side, overlay: grass_side_overlay, snowed: grass_side_snowed"));
        blockList.add(dirt = new BlockJson().displayName("Dirt").material("ground").stepSound("gravel").textures("all: dirt"));
        blockList.add(new BlockJson().displayName("Podzol").parent(dirt).textures("particle: dirt, down: dirt, up: dirt_podzol_top, allSides: dirt_podzol_side"));
        blockList.add(tmp = new BlockJson().displayName("Oak Wood Planks").material("wood").stepSound("wood").textures("all: planks_oak"));
        blockList.add(new BlockJson().displayName("Spruce Wood Planks").parent(tmp).textures("all: planks_spruce"));
        blockList.add(new BlockJson().displayName("Birch Wood Planks").parent(tmp).textures("all: planks_birch"));
        blockList.add(new BlockJson().displayName("Jungle Wood Planks").parent(tmp).textures("all: planks_jungle"));
        blockList.add(new BlockJson().displayName("Acacia Wood Planks").parent(tmp).textures("all: planks_acacia"));
        blockList.add(new BlockJson().displayName("Dark Oak Wood Planks").parent(tmp).textures("all: planks_big_oak"));
        // Oak Sapling
        // Spruce Sapling
        // Birch Sapling
        // Jungle Sapling
        // Acacia Sapling
        // Dark Oak Sapling
        blockList.add(new BlockJson().displayName("Bedrock").textures("all: bedrock"));
        // Water
        // Lava
        blockList.add(tmp = new BlockJson().displayName("Sand").shape("falling").material("sand").stepSound("sand").textures("all: sand"));
        blockList.add(new BlockJson().displayName("Red Sand").parent(tmp).shape("falling").material("sand").stepSound("sand").textures("all: red_sand"));
        blockList.add(new BlockJson().displayName("Gravel").shape("falling").material("ground").stepSound("gravel").textures("all: gravel"));
        blockList.add(new BlockJson().displayName("Gold Ore").textures("all: gold_ore"));
        blockList.add(new BlockJson().displayName("Iron Ore").textures("all: iron_ore"));
        blockList.add(new BlockJson().displayName("Coal Ore").textures("all: coal_ore"));
        blockList.add(new BlockJson().displayName("Lapis Lazuli Ore").textures("all: lapis_ore"));
        blockList.add(new BlockJson().displayName("Redstone Ore").textures("all: redstone_ore"));
        blockList.add(new BlockJson().displayName("Diamond Ore").textures("all: diamond_ore"));
        blockList.add(new BlockJson().displayName("Emerald Ore").textures("all: emerald_ore"));
        blockList.add(new BlockJson().displayName("Quartz Ore").textures("all: quartz_ore"));
        blockList.add(tmp = new BlockJson().displayName("Oak Wood").shape("rotating").material("wood").stepSound("wood").textures("end: log_oak_top, side: log_oak"));
        blockList.add(new BlockJson().displayName("Spruce Wood").parent(tmp).textures("end: log_spruce_top, side: log_spruce"));
        blockList.add(new BlockJson().displayName("Birch Wood").parent(tmp).textures("end: log_birch_top, side: log_birch"));
        blockList.add(new BlockJson().displayName("Jungle Wood").parent(tmp).textures("end: log_jungle_top, side: log_jungle"));
        blockList.add(new BlockJson().displayName("Acacia Wood").parent(tmp).textures("end: log_acacia_top, side: log_acacia"));
        blockList.add(new BlockJson().displayName("Dark Oak Wood").parent(tmp).textures("end: log_big_oak_top, side: log_big_oak"));
        blockList.add(tmp = new BlockJson().displayName("Oak Leaves").shape("leaves").material("leaves").hardness(0.2F).lightOpacity(1).stepSound("grass").textures("all: leaves_oak"));
        blockList.add(new BlockJson().displayName("Spruce Leaves").parent(tmp).textures("all: leaves_spruce"));
        blockList.add(new BlockJson().displayName("Birch Leaves").parent(tmp).textures("all: leaves_birch"));
        blockList.add(new BlockJson().displayName("Jungle Leaves").parent(tmp).textures("all: leaves_jungle"));
        blockList.add(new BlockJson().displayName("Acacia Leaves").parent(tmp).textures("all: leaves_acacia"));
        blockList.add(new BlockJson().displayName("Dark Oak Leaves").parent(tmp).textures("all: leaves_big_oak"));
        blockList.add(sponge = new BlockJson().displayName("Sponge").stepSound("grass").material("sponge").textures("all: sponge"));
        blockList.add(new BlockJson().displayName("Glass").shape("glass").lightOpacity(0).material("glass").stepSound("glass").opaque(false).hardness(0.3F).textures("all: glass"));
        blockList.add(new BlockJson().displayName("Lapis Lazuli Block").hardness(3.0F).textures("all: lapis_block"));
        // Dispenser
        blockList.add(sandstone = new BlockJson().displayName("Sandstone").hardness(0.8F).textures("down: sandstone_bottom, up: sandstone_top, allSides: sandstone_normal"));
        blockList.add(new BlockJson().displayName("Chiseled Sandstone").parent(sandstone).textures("down: sandstone_bottom, up: sandstone_top, allSides: sandstone_carved"));
        blockList.add(new BlockJson().displayName("Smooth Sandstone").parent(sandstone).textures("down: sandstone_bottom, up: sandstone_top, allSides: sandstone_smooth"));
        // Note Block
        blockList.add(new BlockJson().displayName("Bed").hardness(0.2F).stepSound("cloth").material("cloth").enableStats(false).shape("bed").textures("headParticle: bed_head_top, headTop: bed_head_top, headBase: planks_oak, headEnd: bed_head_end, headSide: bed_head_side, feetParticle: bed_feet_top, feetTop: bed_feet_top, feetBase: planks_oak, feetEnd: bed_feet_end, feetSide: bed_feet_side"));
        // Powered Rail
        // Detector Rail
        // Sticky Piston
        blockList.add(new BlockJson().displayName("Cobweb").shape("web").lightOpacity(1).hardness(4.0F).textures("cross: web"));
        blockList.add(tmp = new BlockJson().displayName("Dead Bush").stepSound("grass").shape("bush").hardness(0.0F).isDeadBush(true).textures("cross: deadbush"));
        blockList.add(new BlockJson().displayName("Grass").parent(tmp).needsColoring(true).textures("cross: tallgrass"));
        blockList.add(new BlockJson().displayName("Fern").parent(tmp).needsColoring(true).textures("cross: fern"));
        // Dead Shrub
        // Piston
        blockList.add(tmp = new BlockJson().displayName("Wool").material("cloth").stepSound("cloth").hardness(0.8F).textures("all: wool_colored_white"));
        blockList.add(new BlockJson().displayName("Orange Wool").parent(tmp).textures("all: wool_colored_orange"));
        blockList.add(new BlockJson().displayName("Magenta Wool").parent(tmp).textures("all: wool_colored_magenta"));
        blockList.add(new BlockJson().displayName("Light Blue Wool").parent(tmp).textures("all: wool_colored_light_blue"));
        blockList.add(new BlockJson().displayName("Yellow Wool").parent(tmp).textures("all: wool_colored_yellow"));
        blockList.add(new BlockJson().displayName("Lime Wool").parent(tmp).textures("all: wool_colored_lime"));
        blockList.add(new BlockJson().displayName("Pink Wool").parent(tmp).textures("all: wool_colored_pink"));
        blockList.add(new BlockJson().displayName("Gray Wool").parent(tmp).textures("all: wool_colored_gray"));
        blockList.add(new BlockJson().displayName("Light Gray Wool").parent(tmp).textures("all: wool_colored_silver"));
        blockList.add(new BlockJson().displayName("Cyan Wool").parent(tmp).textures("all: wool_colored_cyan"));
        blockList.add(new BlockJson().displayName("Purple Wool").parent(tmp).textures("all: wool_colored_purple"));
        blockList.add(new BlockJson().displayName("Blue Wool").parent(tmp).textures("all: wool_colored_blue"));
        blockList.add(new BlockJson().displayName("Brown Wool").parent(tmp).textures("all: wool_colored_brown"));
        blockList.add(new BlockJson().displayName("Green Wool").parent(tmp).textures("all: wool_colored_green"));
        blockList.add(new BlockJson().displayName("Red Wool").parent(tmp).textures("all: wool_colored_red"));
        blockList.add(new BlockJson().displayName("Black Wool").parent(tmp).textures("all: wool_colored_black"));
        blockList.add(tmp = new BlockJson().displayName("Dandelion").shape("flower").hardness(0.0F).stepSound("plant").textures("cross: flower_dandelion"));
        blockList.add(new BlockJson().displayName("Poppy").parent(tmp).textures("cross: flower_rose"));
        blockList.add(new BlockJson().displayName("Blue Orchid").parent(tmp).textures("cross: flower_blue_orchid"));
        blockList.add(new BlockJson().displayName("Allium").parent(tmp).textures("cross: flower_allium"));
        blockList.add(new BlockJson().displayName("Azure Bluet").parent(tmp).textures("cross: flower_houstonia"));
        blockList.add(new BlockJson().displayName("Red Tulip").parent(tmp).textures("cross: flower_tulip_red"));
        blockList.add(new BlockJson().displayName("Orange Tulip").parent(tmp).textures("cross: flower_tulip_orange"));
        blockList.add(new BlockJson().displayName("White Tulip").parent(tmp).textures("cross: flower_tulip_white"));
        blockList.add(new BlockJson().displayName("Pink Tulip").parent(tmp).textures("cross: flower_tulip_pink"));
        blockList.add(new BlockJson().displayName("Oxeye Daisy").parent(tmp).textures("cross: flower_oxeye_daisy"));
        blockList.add(tmp = new BlockJson().displayName("Brown Mushroom").shape("mushroom").hardness(0.0F).stepSound("grass").lightLevel(0.125F).textures("all: mushroom_brown"));
        blockList.add(new BlockJson().displayName("Red Mushroom").parent(tmp).lightLevel(0.0F).textures("all: mushroom_red"));
        blockList.add(tmp = new BlockJson().displayName("Block of Gold").hardness(3.0F).resistance(10.0F).stepSound("metal").beaconBase(true).textures("all: gold_block"));
        blockList.add(new BlockJson().displayName("Block of Iron").parent(tmp).textures("all: iron_block"));
        blockList.add(new BlockJson().displayName("Block of Diamond").parent(tmp).textures("all: diamond_block"));
        blockList.add(new BlockJson().displayName("Block of Emerald").parent(tmp).textures("all: emerald_block"));
        blockList.add(new BlockJson().displayName("Block of Redstone").parent(tmp).beaconBase(false).textures("all: redstone_block"));
        blockList.add(new BlockJson().displayName("Block of Coal").parent(tmp).stepSound("piston").textures("all: coal_block"));
        blockList.add(rockSlab = new BlockJson().displayName("Stone Slab").shape("slab").hardness(2.0F).resistance(10.0F).stepSound("piston").textures("down: stone_slab_top, up: stone_slab_top, allSides: stone_slab_side"));
        blockList.add(new BlockJson().displayName("Sandstone Slab").parent(rockSlab).textures("down: sandstone_bottom, up: sandstone_top, allSides: sandstone_normal"));
        blockList.add(new BlockJson().displayName("Cobblestone Slab").parent(rockSlab).textures("all: cobblestone"));
        blockList.add(new BlockJson().displayName("Bricks Slab").parent(rockSlab).textures("all: brick"));
        blockList.add(new BlockJson().displayName("Stone Bricks Slab").parent(rockSlab).textures("all: stonebrick"));
        blockList.add(new BlockJson().displayName("Nether Brick Slab").parent(rockSlab).textures("all: nether_brick"));
        blockList.add(new BlockJson().displayName("Quartz Slab").parent(rockSlab).textures("down: quartz_block_bottom, up: quartz_block_top, allSides: quartz_block_side"));
        blockList.add(new BlockJson().displayName("Bricks").hardness(2.0F).resistance(10.0F).stepSound("piston").textures("all: brick"));
        blockList.add(new BlockJson().displayName("TNT").hardness(0.0F).material("tnt").stepSound("grass").textures("down: tnt_bottom, up: tnt_top, allSides: tnt_side"));
        blockList.add(new BlockJson().displayName("Bookshelf").hardness(1.5F).stepSound("wood").material("wood").textures("down: planks_oak, up: planks_oak, allSides: bookshelf"));
        blockList.add(new BlockJson().displayName("Obsidian").hardness(50.0F).resistance(2000.0F).stepSound("piston").textures("all: obsidian"));
        blockList.add(new BlockJson().displayName("Torch").shape("torch").hardness(0.0F).material("circuits").lightLevel(0.9375F).stepSound("wood").textures("on: torch_on"));
        // Fire
        // Mob Spawner
        blockList.add(tmp = new BlockJson().displayName("Oak Wood Stairs").shape("stair").material("wood").hardness(2.0F).resistance(10.0F).stepSound("wood").opaque(false).textures("all: planks_oak"));
        blockList.add(new BlockJson().displayName("Spruce Wood Stairs").parent(tmp).textures("all: planks_spruce"));
        blockList.add(new BlockJson().displayName("Birch Wood Stairs").parent(tmp).textures("all: planks_birch"));
        blockList.add(new BlockJson().displayName("Jungle Wood Stairs").parent(tmp).textures("all: planks_jungle"));
        blockList.add(new BlockJson().displayName("Acacia Wood Stairs").parent(tmp).textures("all: planks_acacia"));
        blockList.add(new BlockJson().displayName("Dark Oak Wood Stairs").parent(tmp).textures("all: planks_big_oak"));
        // Chest
        // Redstone Wire
        // Crafting Table
        // Wheat
        blockList.add(new BlockJson().displayName("Farmland").shape("farmland").hardness(0.6F).stepSound("gravel").textures("down: dirt, allSides: dirt, dryUp: farmland_dry, wetUp: farmland_wet")); // TODO: Implement
        // Furnace
        blockList.add(new BlockJson().displayName("Sign").shape("sign").hardness(1.0F).stepSound("wood").textures("all: planks_oak")); // TODO: Implement
        blockList.add(woodDoor = new BlockJson().displayName("Wooden Door").shape("door").material("wood").hardness(3.0F).stepSound("wood").enableStats(false).textures("bottom: door_wood_lower, top: door_wood_upper")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Ladder").shape("ladder").hardness(0.4F).stepSound("ladder").textures("all: ladder")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Rail").shape("rail").hardness(0.7F).stepSound("metal").textures("rail: rail_normal, turned: rail_normal_turned")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Cobblestone Stairs").shape("stair").hardness(2.0F).resistance(10.0F).stepSound("piston").opaque(false).textures("all: cobblestone"));
        blockList.add(new BlockJson().displayName("Lever").shape("lever").hardness(0.5F).stepSound("wood").textures("all: lever")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Stone Pressure Plate").shape("pressure_plate").sensitivity("mobs").material("rock").hardness(0.5F).stepSound("piston").textures("all: stone"));
        blockList.add(new BlockJson().displayName("Iron Door").shape("door").material("iron").hardness(5.0F).stepSound("metal").enableStats(false).textures("bottom: door_iron_lower, top: door_iron_upper"));
        blockList.add(new BlockJson().displayName("Wooden Pressure Plate").shape("pressure_plate").sensitivity("everything").material("wood").hardness(0.5F).stepSound("wood").textures("all: planks_oak"));
        blockList.add(new BlockJson().displayName("Redstone Torch").shape("redstone_torch").hardness(0.0F).material("wood").stepSound("wood").textures("on: redstone_torch_on, off: redstone_torch_off")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Stone Button").shape("button").hardness(0.5F).stepSound("piston").textures("all: stone")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Snow").shape("layer").material("snow").hardness(0.1F).stepSound("snow").lightOpacity(0).textures("all: snow"));
        blockList.add(new BlockJson().displayName("Ice").material("ice").slipperiness(0.98F).hardness(0.5F).stepSound("glass").textures("all: ice"));
        blockList.add(new BlockJson().displayName("Snow Block").material("crafted_snow").hardness(0.2F).stepSound("snow").textures("all: snow"));
        blockList.add(new BlockJson().displayName("Cactus").shape("cactus").material("cactus").hardness(0.4F).stepSound("cloth").textures("bottom: cactus_bottom, top: cactus_top, side: cactus_side")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Clay").material("clay").hardness(0.6F).stepSound("gravel").textures("all: clay"));
        blockList.add(new BlockJson().displayName("Sugar Cane").shape("sugar_cane").material("plants").hardness(0.0F).stepSound("grass").enableStats(false).renderColor(16777215).textures("all: reeds"));
        blockList.add(new BlockJson().displayName("Jukebox").material("wood").hardness(2.0F).resistance(10.0F).stepSound("piston").textures("down: jukebox_top, up: jukebox_top, allSides: jukebox_side")); // TODO: Implement
        blockList.add(woodFence = new BlockJson().displayName("Wooden Fence").shape("fence").material("wood").hardness(2.0F).resistance(5.0F).stepSound("wood").textures("all: planks_oak"));
        blockList.add(new BlockJson().displayName("Pumpkin").shape("directional").material("gourd").hardness(1.0F).stepSound("wood").textures("top: pumpkin_top, faceOff: pumpkin_face_off, face: pumpkin_face_on, side: pumpkin_side")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Netherrack").hardness(0.4F).stepSound("piston").textures("all: netherrack"));
        blockList.add(new BlockJson().displayName("Soul Sand").material("sand").hardness(0.5F).stepSound("sand").textures("all: soul_sand"));
        // Portal Block
        blockList.add(lamp = new BlockJson().displayName("Glowstone").material("glass").hardness(0.3F).stepSound("glass").lightLevel(1.0F).textures("all: glowstone"));
        blockList.add(new BlockJson().displayName("Jack-O-Lantern").shape("directional").material("gourd").hardness(1.0F).stepSound("wood").textures("top: pumpkin_top, face: pumpkin_face_on, side: pumpkin_side")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Cake").shape("cake").material("cake").hardness(0.5F).stepSound("cloth").textures("bottom: cake_bottom, top: cake_top, side: cake_side, inner: cake_inner")); // TODO: Implement
        // Redstone Repeater
        blockList.add(tmp = new BlockJson().displayName("White Stained Glass").shape("glass").stained(true).material("glass").hardness(0.3F).stepSound("glass").textures("all: glass_white"));
        blockList.add(new BlockJson().displayName("Orange Stained Glass").parent(tmp).textures("all: glass_orange"));
        blockList.add(new BlockJson().displayName("Magenta Stained Glass").parent(tmp).textures("all: glass_magenta"));
        blockList.add(new BlockJson().displayName("Light Blue Stained Glass").parent(tmp).textures("all: glass_light_blue"));
        blockList.add(new BlockJson().displayName("Yellow Stained Glass").parent(tmp).textures("all: glass_yellow"));
        blockList.add(new BlockJson().displayName("Lime Stained Glass").parent(tmp).textures("all: glass_lime"));
        blockList.add(new BlockJson().displayName("Pink Stained Glass").parent(tmp).textures("all: glass_pink"));
        blockList.add(new BlockJson().displayName("Gray Stained Glass").parent(tmp).textures("all: glass_gray"));
        blockList.add(new BlockJson().displayName("Light Gray Stained Glass").parent(tmp).textures("all: glass_silver"));
        blockList.add(new BlockJson().displayName("Cyan Stained Glass").parent(tmp).textures("all: glass_cyan"));
        blockList.add(new BlockJson().displayName("Purple Stained Glass").parent(tmp).textures("all: glass_purple"));
        blockList.add(new BlockJson().displayName("Blue Stained Glass").parent(tmp).textures("all: glass_blue"));
        blockList.add(new BlockJson().displayName("Brown Stained Glass").parent(tmp).textures("all: glass_brown"));
        blockList.add(new BlockJson().displayName("Green Stained Glass").parent(tmp).textures("all: glass_green"));
        blockList.add(new BlockJson().displayName("Red Stained Glass").parent(tmp).textures("all: glass_red"));
        blockList.add(new BlockJson().displayName("Black Stained Glass").parent(tmp).textures("all: glass_black"));
        blockList.add(new BlockJson().displayName("Trapdoor").shape("trapdoor").material("wood").hardness(3.0F).stepSound("wood").enableStats(false).textures("all: trapdoor"));
        // Stone Monster Egg
        // Cobblestone Monster Egg
        // Stone Brick Monster Egg
        // Mossy Stone Brick Monster Egg
        // Cracked Stone Brick Monster Egg
        // Chiseled Stone Brick Monster Egg
        blockList.add(tmp = new BlockJson().displayName("Stone Bricks").hardness(1.5F).resistance(10.0F).stepSound("piston").textures("all: stonebrick"));
        blockList.add(new BlockJson().displayName("Mossy Stone Bricks").parent(tmp).textures("all: stonebrick_mossy"));
        blockList.add(new BlockJson().displayName("Cracked Stone Bricks").parent(tmp).textures("all: stonebrick_cracked"));
        blockList.add(new BlockJson().displayName("Chiseled Stone Bricks").parent(tmp).textures("all: stonebrick_carved"));
        //blockList.add(tmp = new BlockJson().displayName("Brown Mushroom Block").shape("mushroom_block").material("wood").hardness(0.2F).stepSound("wood").textures({"mushroom_block_skin_stem", "mushroom_block_skin_brown", "mushroom_block_inside"})); // TODO: Implement
        //blockList.add(new BlockJson().displayName("Red Mushroom Block").parent(tmp).textures({"mushroom_block_skin_stem", "mushroom_block_skin_red", "mushroom_block_inside"})); // TODO: Implement
        blockList.add(new BlockJson().displayName("Iron Bars").shape("bars").material("iron").hardness(5.0F).resistance(10.0F).stepSound("metal").textures("edge: iron_bars, bars: iron_bars"));
        blockList.add(new BlockJson().displayName("Glass Pane").shape("pane").material("glass").hardness(0.3F).stepSound("glass").textures("edge: glass_pane_top, pane: glass"));
        blockList.add(tmp = new BlockJson().displayName("White Stained Glass Pane").shape("pane").stained(true).material("glass").hardness(0.3F).stepSound("glass").textures("edge: glass_pane_top_white, pane: glass_white"));
        blockList.add(new BlockJson().displayName("Orange Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_orange, pane: glass_orange"));
        blockList.add(new BlockJson().displayName("Magenta Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_magenta, pane: glass_magenta"));
        blockList.add(new BlockJson().displayName("Light Blue Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_light_blue, pane: glass_light_blue"));
        blockList.add(new BlockJson().displayName("Yellow Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_yellow, pane: glass_yellow"));
        blockList.add(new BlockJson().displayName("Lime Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_lime, pane: glass_lime"));
        blockList.add(new BlockJson().displayName("Pink Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_pink, pane: glass_pink"));
        blockList.add(new BlockJson().displayName("Gray Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_gray, pane: glass_gray"));
        blockList.add(new BlockJson().displayName("Light Gray Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_silver, pane: glass_silver"));
        blockList.add(new BlockJson().displayName("Cyan Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_cyan, pane: glass_cyan"));
        blockList.add(new BlockJson().displayName("Purple Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_purple, pane: glass_purple"));
        blockList.add(new BlockJson().displayName("Blue Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_blue, pane: glass_blue"));
        blockList.add(new BlockJson().displayName("Brown Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_brown, pane: glass_brown"));
        blockList.add(new BlockJson().displayName("Green Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_green, pane: glass_green"));
        blockList.add(new BlockJson().displayName("Red Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_red, pane: glass_red"));
        blockList.add(new BlockJson().displayName("Black Stained Glass Pane").parent(tmp).textures("edge: glass_pane_top_black, pane: glass_black"));
        blockList.add(new BlockJson().displayName("Melon").material("gourd").hardness(1.0F).stepSound("wood").textures("down: melon_top, up: melon_top, allSides: melon_side"));
        // Pumpkin Vine
        // Melon Vine
        blockList.add(new BlockJson().displayName("Vine").shape("vine").material("vine").hardness(0.2F).stepSound("grass").textures("all: vine"));
        blockList.add(woodFenceGate = new BlockJson().displayName("Fence Gate").shape("fence_gate").material("wood").hardness(2.0F).resistance(5.0F).stepSound("wood").textures("all: planks_oak"));
        blockList.add(new BlockJson().displayName("Brick Stairs").shape("stair").stepSound("piston").opaque(false).textures("all: brick"));
        blockList.add(new BlockJson().displayName("Stone Brick Stair").shape("stair").stepSound("piston").textures("all: stonebrick"));
        blockList.add(new BlockJson().displayName("Mycelium").material("grass").hardness(0.6F).stepSound("grass").textures("down: dirt, up: mycelium_top, allSides: mycelium_side"));
        blockList.add(new BlockJson().displayName("Lily Pad").shape("lilypad").material("plants").hardness(0.0F).stepSound("grass").renderColor(4764952 /*TODO: Default this to use Blocks.lilypad.getRenderColor(...)*/).textures("all: waterlily"));
        blockList.add(tmp = new BlockJson().displayName("Nether Brick").hardness(2.0F).resistance(10.0F).stepSound("piston").textures("all: nether_brick"));
        blockList.add(new BlockJson().displayName("Nether Brick Fence").parent(tmp).shape("fence").textures("all: nether_brick"));
        //blockList.add(new BlockJson().displayName("Nether Brick Slab").parent(tmp).shape("slab").textures("all: nether_brick"));
        blockList.add(new BlockJson().displayName("Nether Brick Stairs").parent(tmp).shape("stair").opaque(false).textures("all: nether_brick"));
        // Nether Wart
        blockList.add(new BlockJson().displayName("Enchanting Table").shape("enchanting_table").hardness(5.0F).resistance(2000.0F).textures("down: enchanting_table_bottom, up: enchanting_table_top, allSides: enchanting_table_side")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Brewing Stand").shape("brewing_stand").material("iron").hardness(0.5F).lightLevel(0.125F).textures("base: brewing_stand_base, side: brewing_stand")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Cauldron").shape("cauldron").material("iron").hardness(2.0F).textures("bottom: cauldron_bottom, top: cauldron_top, inside: cauldron_inner, allSides: cauldron_side, water: water_still")); // TODO: Implement
        // End Portal
        // End Portal Frame
        blockList.add(new BlockJson().displayName("End Stone").hardness(3.0F).resistance(15.0F).stepSound("piston").textures("all: end_stone"));
        blockList.add(new BlockJson().displayName("Dragon Egg").shape("dragon_egg").hardness(3.0F).resistance(15.0F).stepSound("piston").lightLevel(0.125F).textures("all: dragon_egg")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Redstone Lamp").shape("redstone_lamp").material("redstone_light").hardness(0.3F).stepSound("glass").textures("on: redstone_lamp_on, off: redstone_lamp_off"));
        /* Wooden Slabs */
        blockList.add(tmp = new BlockJson().displayName("Oak Wood Slab").shape("slab").material("wood").hardness(2.0F).resistance(5.0F).stepSound("wood").textures("all: planks_oak"));
        blockList.add(new BlockJson().displayName("Spruce Wood Slab").parent(tmp).textures("all: planks_spruce"));
        blockList.add(new BlockJson().displayName("Birch Wood Slab").parent(tmp).textures("all: planks_birch"));
        blockList.add(new BlockJson().displayName("Jungle Wood Slab").parent(tmp).textures("all: planks_jungle"));
        blockList.add(new BlockJson().displayName("Acacia Wood Slab").parent(tmp).textures("all: planks_acacia"));
        blockList.add(new BlockJson().displayName("Dark Oak Wood Slab").parent(tmp).textures("all: planks_big_oak"));
        blockList.add(new BlockJson().displayName("Cocoa Beans").shape("cocoa").material("plants").hardness(0.2F).resistance(5.0F).stepSound("wood").textures("stage_0: cocoa_stage_0, stage_1: cocoa_stage_1, stage_2: cocoa_stage_2")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Sandstone Stairs").shape("stair").hardness(0.8F).stepSound("piston").opaque(false).textures("down: sandstone_bottom, up: sandstone_top, allSides: sandstone_normal"));
        // Ender Chest
        blockList.add(new BlockJson().displayName("Tripwire Hook").shape("tripwire_hook").material("circuits").textures("all: trip_wire_source")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Tripwire").shape("tripwire").material("circuits").textures("all: trip_wire"));
        // Command Block
        blockList.add(new BlockJson().displayName("Beacon").shape("beacon").material("glass").hardness(3.0F).lightLevel(1.0F).textures("glass: glass, obsidian: obsidian, beacon: beacon")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Cobblestone Wall").shape("wall").textures("all: cobblestone"));
        blockList.add(new BlockJson().displayName("Mossy Cobblestone Wall").shape("wall").textures("all: cobblestone_mossy"));
        blockList.add(new BlockJson().displayName("Flower Pot").shape("flower_pot").material("circuits").hardness(0.0F).stepSound("stone").textures("all: flower_pot")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Carrots").shape("crop").hardness(0.0F).stepSound("grass").textures("stage_0: carrots_stage_0, stage_1: carrots_stage_1, stage_2: carrots_stage_2")); // TODO: Implement
        blockList.add(new BlockJson().displayName("Potatoes").shape("crop").hardness(0.0F).stepSound("grass").textures("stage_0: potatoes_stage_0, stage_1: potatoes_stage_1, stage_2: potatoes_stage_2"));
        blockList.add(new BlockJson().displayName("Wooden Button").shape("button").material("circuits").hardness(0.5F).stepSound("wood").textures("all: planks_oak"));
        // Skulls. All logic lies in their item and we're not saving any Item textures. Maybe I'll implement that by copying over the respective textures.
        blockList.add(new BlockJson().displayName("Anvil").shape("anvil").material("anvil").hardness(5.0F).stepSound("anvil").resistance(2000.0F).textures("base: anvil_base, undamaged: anvil_top_damaged_0, slightly_damaged: anvil_top_damaged_1, very_damaged: anvil_top_damaged_2"));
        // Trapped Chest
        blockList.add(new BlockJson().displayName("Light Weighted Pressure Plate").shape("weighted_pressure_plate").material("iron").hardness(0.5F).stepSound("wood").textures("all: iron_block"));
        blockList.add(new BlockJson().displayName("Heavy Weighted Pressure Plate").shape("weighted_pressure_plate").material("iron").hardness(0.5F).stepSound("wood").textures("all: gold_block"));
        // Redstone Comparator
        blockList.add(new BlockJson().displayName("Daylight Detector").shape("daylight_detector").material("wood").hardness(0.2F).stepSound("wood").textures("down: daylight_detector_side, up: daylight_detector_top, allSides: daylight_detector_side"));
        // Hopper
        blockList.add(tmp = new BlockJson().displayName("Quartz Block").hardness(0.8F).stepSound("piston").textures("down: quartz_block_bottom, up: quartz_block_top, allSides: quartz_block_side"));
        blockList.add(new BlockJson().displayName("Chiseled Quartz Block").parent(tmp).textures("down: quartz_block_chiseled_top, up: quartz_block_chiseled_top, allSides: quartz_block_chiseled"));
        blockList.add(new BlockJson().displayName("Quartz Stairs").shape("stair").hardness(2.0F).stepSound("piston").opaque(false).textures("down: quartz_block_bottom, up: quartz_block_top, allSides: quartz_block_side"));
        blockList.add(new BlockJson().displayName("Pillar Quartz Block").parent(tmp).shape("rotating").textures("end: quartz_block_lines_top, side: quartz_block_lines"));
        blockList.add(new BlockJson().displayName("Activator Rail").shape("rail").material("circuits").hardness(0.7F).stepSound("metal").textures("rail: rail_activator, powered: rail_activator_powered"));
        // Dropper
        blockList.add(tmp = new BlockJson().displayName("White Stained Clay").hardness(1.25F).resistance(7.0F).stepSound("piston").textures("all: hardened_clay_stained_white"));
        blockList.add(new BlockJson().displayName("Orange Stained Clay").parent(tmp).textures("all: hardened_clay_stained_orange"));
        blockList.add(new BlockJson().displayName("Magenta Stained Clay").parent(tmp).textures("all: hardened_clay_stained_magenta"));
        blockList.add(new BlockJson().displayName("Light Blue Stained Clay").parent(tmp).textures("all: hardened_clay_stained_light_blue"));
        blockList.add(new BlockJson().displayName("Yellow Stained Clay").parent(tmp).textures("all: hardened_clay_stained_yellow"));
        blockList.add(new BlockJson().displayName("Lime Stained Clay").parent(tmp).textures("all: hardened_clay_stained_lime"));
        blockList.add(new BlockJson().displayName("Pink Stained Clay").parent(tmp).textures("all: hardened_clay_stained_pink"));
        blockList.add(new BlockJson().displayName("Gray Stained Clay").parent(tmp).textures("all: hardened_clay_stained_gray"));
        blockList.add(new BlockJson().displayName("Light Gray Stained Clay").parent(tmp).textures("all: hardened_clay_stained_silver"));
        blockList.add(new BlockJson().displayName("Cyan Stained Clay").parent(tmp).textures("all: hardened_clay_stained_cyan"));
        blockList.add(new BlockJson().displayName("Purple Stained Clay").parent(tmp).textures("all: hardened_clay_stained_purple"));
        blockList.add(new BlockJson().displayName("Blue Stained Clay").parent(tmp).textures("all: hardened_clay_stained_blue"));
        blockList.add(new BlockJson().displayName("Brown Stained Clay").parent(tmp).textures("all: hardened_clay_stained_brown"));
        blockList.add(new BlockJson().displayName("Green Stained Clay").parent(tmp).textures("all: hardened_clay_stained_green"));
        blockList.add(new BlockJson().displayName("Red Stained Clay").parent(tmp).textures("all: hardened_clay_stained_red"));
        blockList.add(new BlockJson().displayName("Black Stained Clay").parent(tmp).textures("all: hardened_clay_stained_black"));
        blockList.add(new BlockJson().displayName("Hay Bale").shape("rotating").material("grass").hardness(0.5F).stepSound("grass").textures("end: hay_block_top, side: hay_block_side"));
        blockList.add(tmp = new BlockJson().displayName("Carpet").shape("carpet").material("carpet").stepSound("cloth").hardness(0.1F).lightOpacity(0).textures("all: wool_colored_white"));
        blockList.add(new BlockJson().displayName("Orange Carpet").parent(tmp).textures("all: wool_colored_orange"));
        blockList.add(new BlockJson().displayName("Magenta Carpet").parent(tmp).textures("all: wool_colored_magenta"));
        blockList.add(new BlockJson().displayName("Light Blue Carpet").parent(tmp).textures("all: wool_colored_light_blue"));
        blockList.add(new BlockJson().displayName("Yellow Carpet").parent(tmp).textures("all: wool_colored_yellow"));
        blockList.add(new BlockJson().displayName("Lime Carpet").parent(tmp).textures("all: wool_colored_lime"));
        blockList.add(new BlockJson().displayName("Pink Carpet").parent(tmp).textures("all: wool_colored_pink"));
        blockList.add(new BlockJson().displayName("Gray Carpet").parent(tmp).textures("all: wool_colored_gray"));
        blockList.add(new BlockJson().displayName("Light Gray Carpet").parent(tmp).textures("all: wool_colored_silver"));
        blockList.add(new BlockJson().displayName("Cyan Carpet").parent(tmp).textures("all: wool_colored_cyan"));
        blockList.add(new BlockJson().displayName("Purple Carpet").parent(tmp).textures("all: wool_colored_purple"));
        blockList.add(new BlockJson().displayName("Blue Carpet").parent(tmp).textures("all: wool_colored_blue"));
        blockList.add(new BlockJson().displayName("Brown Carpet").parent(tmp).textures("all: wool_colored_brown"));
        blockList.add(new BlockJson().displayName("Green Carpet").parent(tmp).textures("all: wool_colored_green"));
        blockList.add(new BlockJson().displayName("Red Carpet").parent(tmp).textures("all: wool_colored_red"));
        blockList.add(new BlockJson().displayName("Black Carpet").parent(tmp).textures("all: wool_colored_black"));
        blockList.add(new BlockJson().displayName("Hardened Clay").textures("all: hardened_clay"));
        blockList.add(new BlockJson().displayName("Packed Ice").material("packed_ice").hardness(0.5F).slipperiness(0.98F).stepSound("glass").textures("all: ice_packed"));
        blockList.add(tmp = new BlockJson().displayName("Sunflower").shape("double_plant").sunflower(true).material("plants").hardness(0.0F).stepSound("plant").textures("bottom: double_plant_sunflower_bottom, top: double_plant_sunflower_top, front: double_plant_sunflower_front, back: double_plant_sunflower_back"));
        blockList.add(new BlockJson().displayName("Lilac").parent(tmp).textures("bottom: double_plant_syringa_bottom, top: double_plant_syringa_top"));
        blockList.add(new BlockJson().displayName("Double Tallgrass").parent(tmp).needsColoring(true).textures("bottom: double_plant_grass_bottom, top: double_plant_grass_top"));
        blockList.add(new BlockJson().displayName("Large Fern").parent(tmp).needsColoring(true).textures("bottom: double_plant_fern_bottom, top: double_plant_fern_top"));
        blockList.add(new BlockJson().displayName("Rose Bush").parent(tmp).textures("bottom: double_plant_rose_bottom, top: double_plant_rose_top"));
        blockList.add(new BlockJson().displayName("Peony").parent(tmp).textures("bottom: double_plant_paeonia_bottom, top: double_plant_paeonia_top"));

        /** ========== 1.8 ========== **/
        blockList.add(new BlockJson().displayName("Granite").parent(rock).textures("all: granite"));
        blockList.add(new BlockJson().displayName("Polished Granite").parent(rock).textures("all: polished_granite"));
        blockList.add(new BlockJson().displayName("Diorite").parent(rock).textures("all: diorite"));
        blockList.add(new BlockJson().displayName("Polished Diorite").parent(rock).textures("all: polished_diorite"));
        blockList.add(new BlockJson().displayName("Andesite").parent(rock).textures("all: andesite"));
        blockList.add(new BlockJson().displayName("Polished Andesite").parent(rock).textures("all: polished_andesite"));
        blockList.add(new BlockJson().displayName("Coarse Dirt").parent(dirt).textures("all: coarse_dirt"));
        blockList.add(new BlockJson().displayName("Prismarine").parent(rock).textures("all: prismarine_rough"));
        blockList.add(new BlockJson().displayName("Prismarine Bricks").parent(rock).textures("all: prismarine_bricks"));
        blockList.add(new BlockJson().displayName("Dark Prismarine").parent(rock).textures("all: prismarine_dark"));
        blockList.add(new BlockJson().displayName("Red Sandstone").parent(sandstone).textures("down: red_sandstone_bottom, up: red_sandstone_top, allSides: red_sandstone_normal"));
        blockList.add(new BlockJson().displayName("Chiseled Red Sandstone").parent(sandstone).textures("down: red_sandstone_bottom, up: red_sandstone_top, allSides: red_sandstone_carved"));
        blockList.add(new BlockJson().displayName("Smooth Red Sandstone").parent(sandstone).textures("down: red_sandstone_bottom, up: red_sandstone_top, allSides: red_sandstone_smooth"));
        blockList.add(new BlockJson().displayName("Red Sandstone Slab").parent(rockSlab).textures("down: red_sandstone_bottom, up: red_sandstone_top, allSides: red_sandstone_normal"));
        // Banner
        blockList.add(new BlockJson().displayName("Red Sandstone Stairs").shape("stair").hardness(0.8F).resistance(10.0F).stepSound("piston").opaque(false).textures("down: red_sandstone_bottom, up: red_sandstone_top, allSides: red_sandstone_normal"));
        blockList.add(new BlockJson().displayName("Iron Trapdoor").shape("trapdoor").material("iron").hardness(5.0F).resistance(25.0F).textures("all: iron_block"));
        blockList.add(new BlockJson().displayName("Sea Lantern").parent(lamp).textures("all: sea_lantern"));
        blockList.add(new BlockJson().displayName("Wet Sponge").parent(sponge).textures("all: sponge_wet"));
        blockList.add(new BlockJson().displayName("Slime Block").shape("slime_block").hardness(0.0F).resistance(0.0F).textures("all: slime"));
        // Barrier Block
        blockList.add(new BlockJson().displayName("Spruce Wood Fence").parent(woodFence).textures("all: planks_spruce"));
        blockList.add(new BlockJson().displayName("Birch Wood Fence").parent(woodFence).textures("all: planks_birch"));
        blockList.add(new BlockJson().displayName("Jungle Wood Fence").parent(woodFence).textures("all: planks_jungle"));
        blockList.add(new BlockJson().displayName("Acacia Wood Fence").parent(woodFence).textures("all: planks_acacia"));
        blockList.add(new BlockJson().displayName("Dark Oak Wood Fence").parent(woodFence).textures("all: planks_big_oak"));
        blockList.add(new BlockJson().displayName("Spruce Wood Fence Gate").parent(woodFenceGate).textures("all: planks_spruce"));
        blockList.add(new BlockJson().displayName("Birch Wood Fence Gate").parent(woodFenceGate).textures("all: planks_birch"));
        blockList.add(new BlockJson().displayName("Jungle Wood Fence Gate").parent(woodFenceGate).textures("all: planks_jungle"));
        blockList.add(new BlockJson().displayName("Acacia Wood Fence Gate").parent(woodFenceGate).textures("all: planks_acacia"));
        blockList.add(new BlockJson().displayName("Dark Oak Wood Fence Gate").parent(woodFenceGate).textures("all: planks_big_oak"));
        blockList.add(new BlockJson().displayName("Spruce Wood Door").parent(woodDoor).textures("bottom: door_spruce_lower, top: door_spruce_upper"));
        blockList.add(new BlockJson().displayName("Birch Wood Door").parent(woodDoor).textures("bottom: door_birch_lower, top: door_birch_upper"));
        blockList.add(new BlockJson().displayName("Jungle Wood Door").parent(woodDoor).textures("bottom: door_jungle_lower, top: door_jungle_upper"));
        blockList.add(new BlockJson().displayName("Acacia Wood Door").parent(woodDoor).textures("bottom: door_acacia_lower, top: door_acacia_upper"));
        blockList.add(new BlockJson().displayName("Dark Oak Wood Door").parent(woodDoor).textures("bottom: door_dark_oak_lower, top: door_dark_oak_upper"));

        /** ========== 1.9 ========== */
        blockList.add(new BlockJson().displayName("End Rod").shape("end_rod").material("wood").hardness(0.0F).textures("all: end_rod"));
        blockList.add(new BlockJson().displayName("End Stone Bricks").resistance(0.8F).textures("all: end_bricks"));
        blockList.add(new BlockJson().displayName("Chorus Plant").shape("chorus_plant").textures("all: chorus_plant"));
        blockList.add(new BlockJson().displayName("Chorus Flower").shape("chorus_plant").textures("alive: chorus_flower, dead: chorus_flower_dead"));
        blockList.add(new BlockJson().displayName("Purpur Block").parent(rock).textures("all: purpur_block"));
        blockList.add(new BlockJson().displayName("Purpur Pillar").parent(rock).shape("rotating").textures("end: purpur_pillar_top, side: purpur_pillar"));
        blockList.add(new BlockJson().displayName("Purpur Slab").parent(rockSlab).shape("slab").textures("all: purpur_block"));
        blockList.add(new BlockJson().displayName("Purpur Stairs").shape("stair").hardness(2.0F).resistance(10.0F).stepSound("piston").opaque(false).textures("all: purpur_block"));
        blockList.add(new BlockJson().displayName("Grass Path").shape("grass_path").textures("down: dirt, up: grass_path_top, allSides: grass_path_side"));
        // End Gateway
        // Structure Block

        // http://www.minecraftinfo.com/idlist.htm
    }
}
