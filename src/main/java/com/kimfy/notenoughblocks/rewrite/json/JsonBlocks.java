package com.kimfy.notenoughblocks.rewrite.json;

import com.kimfy.notenoughblocks.handler.ConfigHandler;
import com.kimfy.notenoughblocks.util.Constants;
import net.minecraft.world.ColorizerFoliage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class JsonBlocks
{
    private static final Logger logger = LogManager.getLogger(Constants.MOD_NAME + ":JsonBlocks");
    /**
     * Holds a reference to every single block in the registry.<br>
     *     Note: To copy do deep copy
     */
    public static List<JsonBlock> blockRegistry = new ArrayList<>();
    /**
     * Static reference to {@link ColorizerFoliage#getFoliageColorBasic()}
     * had to do this because I fucked up on proxies and I cannot access
     * a Side.CLIENT class from Side.SERVER
     */
    public static final int foliageColorBasic = 4764952;
    
    public static void loadBlocks()
    {
        JsonBlock tmp, rock, dirt, sandstone, rockSlab, lamp, sponge, woodFence, woodFenceGate, woodDoor = null;
        
        /** ========== 1.7 ========== */
        blockRegistry.add(rock = new JsonBlock().setDisplayName("Stone").setHardness(1.5F).setResistance(10.0F).setBlockTexture("stone"));
        blockRegistry.add(new JsonBlock().setDisplayName("Cobblestone").setParent(rock).setBlockTexture("cobblestone"));
        blockRegistry.add(new JsonBlock().setDisplayName("Mossy Cobblestone").setParent(rock).setBlockTexture("mossy_cobblestone"));
        blockRegistry.add(new JsonBlock().setDisplayName("Grass Block").setShape("grass").setMaterial("grass").setStepSound("grass").setCanBlockGrass(true).setBlockTextures(new String[] {"dirt", "grass_top", "grass_side", "grass_side_overlay", "grass_side_snowed"}));
        blockRegistry.add(dirt = new JsonBlock().setDisplayName("Dirt").setMaterial("ground").setStepSound("gravel").setBlockTexture("dirt"));
        blockRegistry.add(new JsonBlock().setDisplayName("Podzol").setParent(dirt).setBlockTextures(new String[] {"dirt", "dirt_podzol_top", "dirt_podzol_side"}));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Oak Wood Planks").setMaterial("wood").setStepSound("wood").setBlockTexture("planks_oak"));
        blockRegistry.add(new JsonBlock().setDisplayName("Spruce Wood Planks").setParent(tmp).setBlockTexture("planks_spruce"));
        blockRegistry.add(new JsonBlock().setDisplayName("Birch Wood Planks").setParent(tmp).setBlockTexture("planks_birch"));
        blockRegistry.add(new JsonBlock().setDisplayName("Jungle Wood Planks").setParent(tmp).setBlockTexture("planks_jungle"));
        blockRegistry.add(new JsonBlock().setDisplayName("Acacia Wood Planks").setParent(tmp).setBlockTexture("planks_acacia"));
        blockRegistry.add(new JsonBlock().setDisplayName("Dark Oak Wood Planks").setParent(tmp).setBlockTexture("planks_big_oak"));
        // Oak Sapling
        // Spruce Sapling
        // Birch Sapling
        // Jungle Sapling
        // Acacia Sapling
        // Dark Oak Sapling
        blockRegistry.add(new JsonBlock().setDisplayName("Bedrock").setBlockTexture("bedrock"));
        // Water
        // Lava
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Sand").setShape("falling").setMaterial("sand").setStepSound("sand").setBlockTexture("sand"));
        blockRegistry.add(new JsonBlock().setDisplayName("Red Sand").setParent(tmp).setShape("falling").setMaterial("sand").setStepSound("sand").setBlockTexture("red_sand"));
        blockRegistry.add(new JsonBlock().setDisplayName("Gravel").setShape("falling").setMaterial("ground").setStepSound("gravel").setBlockTexture("gravel"));
        blockRegistry.add(new JsonBlock().setDisplayName("Gold Ore").setBlockTexture("gold_ore"));
        blockRegistry.add(new JsonBlock().setDisplayName("Iron Ore").setBlockTexture("iron_ore"));
        blockRegistry.add(new JsonBlock().setDisplayName("Coal Ore").setBlockTexture("coal_ore"));
        blockRegistry.add(new JsonBlock().setDisplayName("Lapis Lazuli Ore").setBlockTexture("lapis_ore"));
        blockRegistry.add(new JsonBlock().setDisplayName("Redstone Ore").setBlockTexture("redstone_ore"));
        blockRegistry.add(new JsonBlock().setDisplayName("Diamond Ore").setBlockTexture("diamond_ore"));
        blockRegistry.add(new JsonBlock().setDisplayName("Emerald Ore").setBlockTexture("emerald_ore"));
        blockRegistry.add(new JsonBlock().setDisplayName("Quartz Ore").setBlockTexture("quartz_ore"));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Oak Wood").setShape("rotating").setMaterial("wood").setStepSound("wood").setBlockTextures(new String[] {"log_oak_top", "log_oak_top", "log_oak"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Spruce Wood").setParent(tmp).setBlockTextures(new String[] {"log_spruce_top", "log_spruce_top", "log_spruce"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Birch Wood").setParent(tmp).setBlockTextures(new String[] {"log_birch_top", "log_birch_top", "log_birch"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Jungle Wood").setParent(tmp).setBlockTextures(new String[] {"log_jungle_top", "log_jungle_top", "log_jungle"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Acacia Wood").setParent(tmp).setBlockTextures(new String[] {"log_acacia_top", "log_acacia_top", "log_acacia"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Dark Oak Wood").setParent(tmp).setBlockTextures(new String[] {"log_big_oak_top", "log_big_oak_top", "log_big_oak"}));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Oak Leaves").setShape("leaves").setMaterial("leaves").setHardness(0.2F).setLightOpacity(1).setStepSound("grass").setBlockTextures(new String[] {"leaves_oak", "leaves_oak_opaque"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Spruce Leaves").setParent(tmp).setBlockTextures(new String[] {"leaves_spruce", "leaves_spruce_opaque"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Birch Leaves").setParent(tmp).setBlockTextures(new String[] {"leaves_birch", "leaves_birch_opaque"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Jungle Leaves").setParent(tmp).setBlockTextures(new String[] {"leaves_jungle", "leaves_jungle_opaque"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Acacia Leaves").setParent(tmp).setBlockTextures(new String[] {"leaves_acacia", "leaves_acacia_opaque"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Dark Oak Leaves").setParent(tmp).setBlockTextures(new String[] {"leaves_big_oak", "leaves_big_oak_opaque"}));
        blockRegistry.add(sponge = new JsonBlock().setDisplayName("Sponge").setStepSound("grass").setMaterial("sponge").setBlockTexture("sponge"));
        blockRegistry.add(new JsonBlock().setDisplayName("Glass").setShape("glass").setLightOpacity(0).setMaterial("glass").setStepSound("glass").setOpaque(false).setHardness(0.3F).setBlockTexture("glass"));
        blockRegistry.add(new JsonBlock().setDisplayName("Lapis Lazuli Block").setHardness(3.0F).setBlockTexture("lapis_block"));
        // Dispenser
        blockRegistry.add(sandstone = new JsonBlock().setDisplayName("Sandstone").setHardness(0.8F).setBlockTextures(new String[] {"sandstone_bottom", "sandstone_top", "sandstone_normal"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Chiseled Sandstone").setParent(sandstone).setBlockTextures(new String[] {"sandstone_bottom", "sandstone_top", "sandstone_carved"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Smooth Sandstone").setParent(sandstone).setBlockTextures(new String[] {"sandstone_bottom", "sandstone_top", "sandstone_smooth"}));
        // Note Block
        blockRegistry.add(new JsonBlock().setDisplayName("Bed").setHardness(0.2F).setStepSound("cloth").setMaterial("cloth").setEnableStats(false).setShape("bed").setBlockTextures(new String[] {"bed_feet_top", "bed_head_top", "bed_feet_end", "bed_head_end", "bed_feet_side", "bed_head_side", "planks_oak"}));
        // Powered Rail
        // Detector Rail
        // Sticky Piston
        blockRegistry.add(new JsonBlock().setDisplayName("Cobweb").setShape("web").setLightOpacity(1).setHardness(4.0F).setBlockTexture("web"));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Dead Bush").setStepSound("grass").setShape("tallgrass").setHardness(0.0F).isDeadBush(true).setBlockTexture("deadbush"));
        blockRegistry.add(new JsonBlock().setDisplayName("Grass").setParent(tmp).setRenderColor(foliageColorBasic).setBlockTexture("tallgrass"));
        blockRegistry.add(new JsonBlock().setDisplayName("Fern").setParent(tmp).setRenderColor(foliageColorBasic).setBlockTexture("fern"));
        // Dead Shrub
        // Piston
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Wool").setMaterial("cloth").setStepSound("cloth").setHardness(0.8F).setBlockTexture("wool_colored_white"));
        blockRegistry.add(new JsonBlock().setDisplayName("Orange Wool").setParent(tmp).setBlockTexture("wool_colored_orange"));
        blockRegistry.add(new JsonBlock().setDisplayName("Magenta Wool").setParent(tmp).setBlockTexture("wool_colored_magenta"));
        blockRegistry.add(new JsonBlock().setDisplayName("Light Blue Wool").setParent(tmp).setBlockTexture("wool_colored_light_blue"));
        blockRegistry.add(new JsonBlock().setDisplayName("Yellow Wool").setParent(tmp).setBlockTexture("wool_colored_yellow"));
        blockRegistry.add(new JsonBlock().setDisplayName("Lime Wool").setParent(tmp).setBlockTexture("wool_colored_lime"));
        blockRegistry.add(new JsonBlock().setDisplayName("Pink Wool").setParent(tmp).setBlockTexture("wool_colored_pink"));
        blockRegistry.add(new JsonBlock().setDisplayName("Gray Wool").setParent(tmp).setBlockTexture("wool_colored_gray"));
        blockRegistry.add(new JsonBlock().setDisplayName("Light Gray Wool").setParent(tmp).setBlockTexture("wool_colored_silver"));
        blockRegistry.add(new JsonBlock().setDisplayName("Cyan Wool").setParent(tmp).setBlockTexture("wool_colored_cyan"));
        blockRegistry.add(new JsonBlock().setDisplayName("Purple Wool").setParent(tmp).setBlockTexture("wool_colored_purple"));
        blockRegistry.add(new JsonBlock().setDisplayName("Blue Wool").setParent(tmp).setBlockTexture("wool_colored_blue"));
        blockRegistry.add(new JsonBlock().setDisplayName("Brown Wool").setParent(tmp).setBlockTexture("wool_colored_brown"));
        blockRegistry.add(new JsonBlock().setDisplayName("Green Wool").setParent(tmp).setBlockTexture("wool_colored_green"));
        blockRegistry.add(new JsonBlock().setDisplayName("Red Wool").setParent(tmp).setBlockTexture("wool_colored_red"));
        blockRegistry.add(new JsonBlock().setDisplayName("Black Wool").setParent(tmp).setBlockTexture("wool_colored_black"));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Dandelion").setShape("flower").setHardness(0.0F).setStepSound("grass").setBlockTexture("flower_dandelion"));
        blockRegistry.add(new JsonBlock().setDisplayName("Poppy").setParent(tmp).setBlockTexture("flower_rose"));
        blockRegistry.add(new JsonBlock().setDisplayName("Blue Orchid").setParent(tmp).setBlockTexture("flower_blue_orchid"));
        blockRegistry.add(new JsonBlock().setDisplayName("Allium").setParent(tmp).setBlockTexture("flower_allium"));
        blockRegistry.add(new JsonBlock().setDisplayName("Azure Bluet").setParent(tmp).setBlockTexture("flower_houstonia"));
        blockRegistry.add(new JsonBlock().setDisplayName("Red Tulip").setParent(tmp).setBlockTexture("flower_tulip_red"));
        blockRegistry.add(new JsonBlock().setDisplayName("Orange Tulip").setParent(tmp).setBlockTexture("flower_tulip_orange"));
        blockRegistry.add(new JsonBlock().setDisplayName("White Tulip").setParent(tmp).setBlockTexture("flower_white_tulip"));
        blockRegistry.add(new JsonBlock().setDisplayName("Pink Tulip").setParent(tmp).setBlockTexture("flower_tulip_pink"));
        blockRegistry.add(new JsonBlock().setDisplayName("Oxeye Daisy").setParent(tmp).setBlockTexture("flower_oxeye_daisy"));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Brown Mushroom").setShape("mushroom").setHardness(0.0F).setStepSound("grass").setLightLevel(0.125F).setBlockTexture("mushroom_brown"));
        blockRegistry.add(new JsonBlock().setDisplayName("Red Mushroom").setParent(tmp).setLightLevel(0.0F).setBlockTexture("mushroom_red"));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Block of Gold").setHardness(3.0F).setResistance(10.0F).setStepSound("metal").setBeaconBase(true).setBlockTexture("gold_block"));
        blockRegistry.add(new JsonBlock().setDisplayName("Block of Iron").setParent(tmp).setBlockTexture("iron_block"));
        blockRegistry.add(new JsonBlock().setDisplayName("Block of Diamond").setParent(tmp).setBlockTexture("diamond_block"));
        blockRegistry.add(new JsonBlock().setDisplayName("Block of Emerald").setParent(tmp).setBlockTexture("emerald_block"));
        blockRegistry.add(new JsonBlock().setDisplayName("Block of Redstone").setParent(tmp).setBlockTexture("redstone_block"));
        blockRegistry.add(new JsonBlock().setDisplayName("Block of Coal").setParent(tmp).setStepSound("piston").setBlockTexture("coal_block"));
        blockRegistry.add(rockSlab = new JsonBlock().setDisplayName("Stone Slab").setShape("slab").setHardness(2.0F).setResistance(10.0F).setStepSound("piston").setBlockTextures(new String[] {"stone_slab_top", "stone_slab_top", "stone_slab_side"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Sandstone Slab").setParent(rockSlab).setBlockTextures(new String[] {"sandstone_bottom", "sandstone_top", "sandstone_normal"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Cobblestone Slab").setParent(rockSlab).setBlockTexture("cobblestone"));
        blockRegistry.add(new JsonBlock().setDisplayName("Bricks Slab").setParent(rockSlab).setBlockTexture("brick"));
        blockRegistry.add(new JsonBlock().setDisplayName("Stone Bricks Slab").setParent(rockSlab).setBlockTexture("stonebrick"));
        blockRegistry.add(new JsonBlock().setDisplayName("Nether Brick Slab").setParent(rockSlab).setBlockTexture("netherbrick"));
        blockRegistry.add(new JsonBlock().setDisplayName("Quartz Slab").setParent(rockSlab).setBlockTextures(new String[] {"quartz_block_bottom", "quartz_block_top", "quartz_block_side"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Bricks").setHardness(2.0F).setResistance(10.0F).setStepSound("piston").setBlockTexture("brick"));
        blockRegistry.add(new JsonBlock().setDisplayName("TNT").setHardness(0.0F).setStepSound("grass").setBlockTexture("tnt"));
        blockRegistry.add(new JsonBlock().setDisplayName("Bookshelf").setHardness(1.5F).setStepSound("wood").setBlockTextures(new String[] {"planks_oak", "planks_oak", "bookshelf"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Obsidian").setHardness(50.0F).setResistance(2000.0F).setStepSound("piston").setBlockTexture("obsidian"));
        blockRegistry.add(new JsonBlock().setDisplayName("Torch").setShape("torch").setHardness(0.0F).setMaterial("circuits").setLightLevel(0.9375F).setStepSound("wood").setBlockTexture("torch_on"));
        // Fire
        // Mob Spawner
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Oak Wood Stairs").setShape("stair").setMaterial("wood").setHardness(2.0F).setResistance(10.0F).setStepSound("wood").setBlockTexture("planks_oak"));
        blockRegistry.add(new JsonBlock().setDisplayName("Spruce Wood Stairs").setParent(tmp).setBlockTexture("planks_spruce"));
        blockRegistry.add(new JsonBlock().setDisplayName("Birch Wood Stairs").setParent(tmp).setBlockTexture("planks_birch"));
        blockRegistry.add(new JsonBlock().setDisplayName("Jungle Wood Stairs").setParent(tmp).setBlockTexture("planks_jungle"));
        blockRegistry.add(new JsonBlock().setDisplayName("Acacia Wood Stairs").setParent(tmp).setBlockTexture("planks_acacia"));
        blockRegistry.add(new JsonBlock().setDisplayName("Dark Oak Wood Stairs").setParent(tmp).setBlockTexture("planks_big_oak"));
        // Chest
        // Redstone Wire
        // Crafting Table
        // Wheat
        blockRegistry.add(new JsonBlock().setDisplayName("Farmland").setShape("farmland").setHardness(0.6F).setStepSound("gravel").setBlockTextures(new String[] {"dirt", "farmland_dry", "farmland_wet"})); // TODO: Implement
        // Furnace
        blockRegistry.add(new JsonBlock().setDisplayName("Sign").setShape("sign").setHardness(1.0F).setStepSound("wood").setBlockTexture("planks_oak")); // TODO: Implement
        blockRegistry.add(woodDoor = new JsonBlock().setDisplayName("Wooden Door").setShape("door").setMaterial("wood").setHardness(3.0F).setStepSound("wood").setEnableStats(false).setBlockTextures(new String[] {"door_wood_upper", "door_wood_lower"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Ladder").setShape("ladder").setHardness(0.4F).setStepSound("ladder").setBlockTexture("ladder")); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Rail").setShape("rail").setHardness(0.7F).setStepSound("metal").setBlockTextures(new String[] {"rail_normal", "rail_normal_turned"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Cobblestone Stairs").setShape("stair").setHardness(2.0F).setResistance(10.0F).setStepSound("piston").setBlockTexture("cobblestone"));
        blockRegistry.add(new JsonBlock().setDisplayName("Lever").setShape("lever").setHardness(0.5F).setStepSound("wood").setBlockTexture("lever")); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Stone Pressure Plate").setShape("pressure_plate").setSensitivity("mobs").setMaterial("rock").setHardness(0.5F).setStepSound("piston").setBlockTexture("stone"));
        blockRegistry.add(new JsonBlock().setDisplayName("Iron Door").setShape("door").setMaterial("iron").setHardness(5.0F).setStepSound("metal").setEnableStats(false).setBlockTextures(new String[] {"door_iron_upper", "door_iron_lower"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Wooden Pressure Plate").setShape("pressure_plate").setSensitivity("everything").setMaterial("wood").setHardness(0.5F).setStepSound("wood").setBlockTexture("planks_oak"));
        blockRegistry.add(new JsonBlock().setDisplayName("Redstone Torch").setShape("redstone_torch").setHardness(0.0F).setMaterial("wood").setStepSound("wood").setBlockTextures(new String[] {"redstone_torch_on", "redstone_torch_off"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Stone Button").setShape("button").setHardness(0.5F).setStepSound("piston").setBlockTexture("stone")); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Snow").setShape("snow_layer").setMaterial("snow").setHardness(0.1F).setStepSound("snow").setLightOpacity(0).setBlockTexture("snow"));
        blockRegistry.add(new JsonBlock().setDisplayName("Ice").setMaterial("ice").setSlipperiness(0.98F).setHardness(0.5F).setStepSound("glass").setBlockTexture("ice"));
        blockRegistry.add(new JsonBlock().setDisplayName("Snow Block").setMaterial("crafted_snow").setHardness(0.2F).setStepSound("snow").setBlockTexture("snow"));
        blockRegistry.add(new JsonBlock().setDisplayName("Cactus").setShape("cactus").setMaterial("cactus").setHardness(0.4F).setStepSound("cloth").setBlockTextures(new String[] {"cactus_bottom", "cactus_top", "cactus_side"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Clay").setMaterial("clay").setHardness(0.6F).setStepSound("gravel").setBlockTexture("clay"));
        blockRegistry.add(new JsonBlock().setDisplayName("Sugar Cane").setShape("sugar_cane").setMaterial("plants").setHardness(0.0F).setStepSound("grass").setEnableStats(false).setRenderColor(16777215).setBlockTexture("reeds"));
        blockRegistry.add(new JsonBlock().setDisplayName("Jukebox").setMaterial("wood").setHardness(2.0F).setResistance(10.0F).setStepSound("piston").setBlockTexture("jukebox")); // TODO: Implement
        blockRegistry.add(woodFence = new JsonBlock().setDisplayName("Wooden Fence").setShape("fence").setMaterial("wood").setHardness(2.0F).setResistance(5.0F).setStepSound("wood").setBlockTexture("planks_oak"));
        blockRegistry.add(new JsonBlock().setDisplayName("Pumpkin").setShape("directional").setMaterial("gourd").setHardness(1.0F).setStepSound("wood").setBlockTextures(new String[] {"pumpkin_top", "pumpkin_top", "pumpkin_face_off", "pumpkin_side"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Netherrack").setHardness(0.4F).setStepSound("piston").setBlockTexture("netherrack"));
        blockRegistry.add(new JsonBlock().setDisplayName("Soul Sand").setMaterial("sand").setHardness(0.5F).setStepSound("sand").setBlockTexture("soul_sand"));
        // Portal Block
        blockRegistry.add(lamp = new JsonBlock().setDisplayName("Glowstone").setMaterial("glass").setHardness(0.3F).setStepSound("glass").setLightLevel(1.0F).setBlockTexture("glowstone"));
        blockRegistry.add(new JsonBlock().setDisplayName("Jack-O-Lantern").setShape("directional").setMaterial("gourd").setHardness(1.0F).setStepSound("wood").setBlockTextures(new String[] {"pumpkin_top", "pumpkin_top", "pumpkin_face_on", "pumpkin_side"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Cake").setShape("cake").setMaterial("cake").setHardness(0.5F).setStepSound("cloth").setBlockTextures(new String[] {"cake_bottom", "cake_top", "cake_side", "cake_inner"})); // TODO: Implement
        // Redstone Repeater
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("White Stained Glass").setShape("stained_glass").setMaterial("glass").setHardness(0.3F).setStepSound("glass").setBlockTexture("glass_white"));
        blockRegistry.add(new JsonBlock().setDisplayName("Orange Stained Glass").setParent(tmp).setBlockTexture("glass_orange"));
        blockRegistry.add(new JsonBlock().setDisplayName("Magenta Stained Glass").setParent(tmp).setBlockTexture("glass_magenta"));
        blockRegistry.add(new JsonBlock().setDisplayName("Light Blue Stained Glass").setParent(tmp).setBlockTexture("glass_light_blue"));
        blockRegistry.add(new JsonBlock().setDisplayName("Yellow Stained Glass").setParent(tmp).setBlockTexture("glass_yellow"));
        blockRegistry.add(new JsonBlock().setDisplayName("Lime Stained Glass").setParent(tmp).setBlockTexture("glass_lime"));
        blockRegistry.add(new JsonBlock().setDisplayName("Pink Stained Glass").setParent(tmp).setBlockTexture("glass_pink"));
        blockRegistry.add(new JsonBlock().setDisplayName("Gray Stained Glass").setParent(tmp).setBlockTexture("glass_gray"));
        blockRegistry.add(new JsonBlock().setDisplayName("Light Gray Stained Glass").setParent(tmp).setBlockTexture("glass_silver"));
        blockRegistry.add(new JsonBlock().setDisplayName("Cyan Stained Glass").setParent(tmp).setBlockTexture("glass_cyan"));
        blockRegistry.add(new JsonBlock().setDisplayName("Purple Stained Glass").setParent(tmp).setBlockTexture("glass_purple"));
        blockRegistry.add(new JsonBlock().setDisplayName("Blue Stained Glass").setParent(tmp).setBlockTexture("glass_blue"));
        blockRegistry.add(new JsonBlock().setDisplayName("Brown Stained Glass").setParent(tmp).setBlockTexture("glass_brown"));
        blockRegistry.add(new JsonBlock().setDisplayName("Green Stained Glass").setParent(tmp).setBlockTexture("glass_green"));
        blockRegistry.add(new JsonBlock().setDisplayName("Red Stained Glass").setParent(tmp).setBlockTexture("glass_red"));
        blockRegistry.add(new JsonBlock().setDisplayName("Black Stained Glass").setParent(tmp).setBlockTexture("glass_black"));
        blockRegistry.add(new JsonBlock().setDisplayName("Trapdoor").setShape("trapdoor").setMaterial("wood").setHardness(3.0F).setStepSound("wood").setEnableStats(false).setBlockTexture("trapdoor"));
        // Stone Monster Egg
        // Cobblestone Monster Egg
        // Stone Brick Monster Egg
        // Mossy Stone Brick Monster Egg
        // Cracked Stone Brick Monster Egg
        // Chiseled Stone Brick Monster Egg
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Stone Bricks").setHardness(1.5F).setResistance(10.0F).setStepSound("piston").setBlockTexture("stonebrick"));
        blockRegistry.add(new JsonBlock().setDisplayName("Mossy Stone Bricks").setParent(tmp).setBlockTexture("stonebrick_mossy"));
        blockRegistry.add(new JsonBlock().setDisplayName("Cracked Stone Bricks").setParent(tmp).setBlockTexture("stonebrick_cracked"));
        blockRegistry.add(new JsonBlock().setDisplayName("Chiseled Stone Bricks").setParent(tmp).setBlockTexture("stonebrick_carved"));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Brown Mushroom Block").setShape("mushroom_block").setMaterial("wood").setHardness(0.2F).setStepSound("wood").setBlockTextures(new String[] {"mushroom_block_skin_stem", "mushroom_block_skin_brown", "mushroom_block_inside"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Red Mushroom Block").setParent(tmp).setBlockTextures(new String[] {"mushroom_block_skin_stem", "mushroom_block_skin_red", "mushroom_block_inside"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Iron Bars").setShape("pane").setMaterial("iron").setHardness(5.0F).setResistance(10.0F).setStepSound("metal").setBlockTexture("iron_bars"));
        blockRegistry.add(new JsonBlock().setDisplayName("Glass Pane").setShape("pane").setMaterial("glass").setHardness(0.3F).setStepSound("glass").setBlockTextures(new String[] {"glass_pane_top", "glass"}));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("White Stained Glass Pane").setShape("pane").setStained(true).setMaterial("glass").setHardness(0.3F).setStepSound("glass").setBlockTextures(new String[] {"glass_pane_top_white", "glass_white"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Orange Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_orange", "glass_orange"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Magenta Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_magenta", "glass_magenta"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Light Blue Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_light_blue", "glass_light_blue"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Yellow Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_yellow", "glass_yellow"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Lime Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_lime", "glass_lime"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Pink Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_pink", "glass_pink"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Gray Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_gray", "glass_gray"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Light Gray Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_silver", "glass_silver"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Cyan Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_cyan", "glass_cyan"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Purple Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_purple", "glass_purple"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Blue Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_blue", "glass_blue"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Brown Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_brown", "glass_brown"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Green Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_green", "glass_green"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Red Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_red", "glass_red"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Black Stained Glass Pane").setParent(tmp).setBlockTextures(new String[] {"glass_pane_top_black", "glass_black"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Melon").setMaterial("gourd").setHardness(1.0F).setStepSound("wood").setBlockTextures(new String[] {"melon_top", "melon_top", "melon_side"}));
        // Pumpkin Vine
        // Melon Vine
        blockRegistry.add(new JsonBlock().setDisplayName("Vine").setShape("vine").setMaterial("vine").setHardness(0.2F).setStepSound("grass").setRenderColor(foliageColorBasic).setBlockTexture("vine"));
        blockRegistry.add(woodFenceGate = new JsonBlock().setDisplayName("Fence Gate").setShape("fence_gate").setMaterial("wood").setHardness(2.0F).setResistance(5.0F).setStepSound("wood").setBlockTexture("planks_oak"));
        blockRegistry.add(new JsonBlock().setDisplayName("Brick Stairs").setShape("stair").setStepSound("piston").setBlockTexture("brick"));
        blockRegistry.add(new JsonBlock().setDisplayName("Stone Brick Stair").setShape("stair").setStepSound("piston").setBlockTexture("stonebrick"));
        blockRegistry.add(new JsonBlock().setDisplayName("Mycelium").setMaterial("grass").setHardness(0.6F).setStepSound("grass").setBlockTextures(new String[] {"dirt", "mycelium_top", "mycelium_side"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Lily Pad").setShape("lilypad").setMaterial("plants").setHardness(0.0F).setStepSound("grass").setRenderColor(4764952 /*TODO: Default this to use Blocks.lilypad.getRenderColor(...)*/).setBlockTexture("waterlily"));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Nether Brick").setHardness(2.0F).setResistance(10.0F).setStepSound("piston").setBlockTexture("nether_brick"));
        blockRegistry.add(new JsonBlock().setDisplayName("Nether Brick Fence").setParent(tmp).setShape("fence").setBlockTexture("nether_brick"));
        blockRegistry.add(new JsonBlock().setDisplayName("Nether Brick Slab").setParent(tmp).setShape("slab").setBlockTexture("nether_brick"));
        blockRegistry.add(new JsonBlock().setDisplayName("Nether Brick Stairs").setParent(tmp).setShape("stair").setBlockTexture("nether_brick"));
        // Nether Wart
        blockRegistry.add(new JsonBlock().setDisplayName("Enchanting Table").setShape("enchanting_table").setHardness(5.0F).setResistance(2000.0F).setBlockTextures(new String[] {"enchanting_table_bottom", "enchanting_table_top", "enchanting_table_side"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Brewing Stand").setShape("brewing_stand").setMaterial("iron").setHardness(0.5F).setLightLevel(0.125F).setBlockTextures(new String[] {"brewing_stand_base", "brewing_stand"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Cauldron").setShape("cauldron").setMaterial("iron").setHardness(2.0F).setBlockTextures(new String[] {"cauldron_bottom", "cauldron_top", "cauldron_inner", "cauldron_side"})); // TODO: Implement
        // End Portal
        // End Portal Frame
        blockRegistry.add(new JsonBlock().setDisplayName("End Stone").setHardness(3.0F).setResistance(15.0F).setStepSound("piston").setBlockTexture("end_stone"));
        blockRegistry.add(new JsonBlock().setDisplayName("Dragon Egg").setShape("dragon_egg").setHardness(3.0F).setResistance(15.0F).setStepSound("piston").setLightLevel(0.125F).setBlockTexture("dragon_egg")); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Redstone Lamp").setShape("redstone_lamp").setMaterial("redstone_light").setHardness(0.3F).setStepSound("glass").setBlockTextures(new String[] {"redstone_lamp_off", "redstone_lamp_on"}));
        /* Wooden Slabs */
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Oak Wood Slab").setShape("slab").setMaterial("wood").setHardness(2.0F).setResistance(5.0F).setStepSound("wood").setBlockTexture("planks_oak"));
        blockRegistry.add(new JsonBlock().setDisplayName("Spruce Wood Slab").setParent(tmp).setBlockTexture("planks_spruce"));
        blockRegistry.add(new JsonBlock().setDisplayName("Birch Wood Slab").setParent(tmp).setBlockTexture("planks_birch"));
        blockRegistry.add(new JsonBlock().setDisplayName("Jungle Wood Slab").setParent(tmp).setBlockTexture("planks_jungle"));
        blockRegistry.add(new JsonBlock().setDisplayName("Acacia Wood Slab").setParent(tmp).setBlockTexture("planks_acacia"));
        blockRegistry.add(new JsonBlock().setDisplayName("Dark Oak Wood Slab").setParent(tmp).setBlockTexture("planks_big_oak"));
        blockRegistry.add(new JsonBlock().setDisplayName("Cocoa Beans").setShape("cocoa").setMaterial("plants").setHardness(0.2F).setResistance(5.0F).setStepSound("wood").setBlockTextures(new String[] {"cocoa_stage_0", "cocoa_stage_1", "cocoa_stage_2"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Sandstone Stairs").setShape("stair").setHardness(0.8F).setStepSound("piston").setBlockTextures(new String[] {"sandstone_bottom", "sandstone_top", "sandstone_normal"}));
        // Ender Chest
        blockRegistry.add(new JsonBlock().setDisplayName("Tripwire Hook").setShape("tripwire_hook").setMaterial("circuits").setBlockTexture("trip_wire_source")); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Tripwire").setShape("tripwire").setMaterial("circuits").setBlockTexture("trip_wire"));
        // Command Block
        blockRegistry.add(new JsonBlock().setDisplayName("Beacon").setShape("beacon").setMaterial("glass").setHardness(3.0F).setLightLevel(1.0F).setBlockTextures(new String[] {"glass", "obsidian", "beacon"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Cobblestone Wall").setShape("wall").setBlockTexture("cobblestone"));
        blockRegistry.add(new JsonBlock().setDisplayName("Mossy Cobblestone Wall").setShape("wall").setBlockTexture("cobblestone_mossy"));
        blockRegistry.add(new JsonBlock().setDisplayName("Flower Pot").setShape("flower_pot").setMaterial("circuits").setHardness(0.0F).setStepSound("stone").setBlockTexture("flower_pot")); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Carrots").setShape("crop").setHardness(0.0F).setStepSound("grass").setBlockTextures(new String[] {"carrots_stage_0", "carrots_stage_1", "carrots_stage_2", "carrots_stage_3"})); // TODO: Implement
        blockRegistry.add(new JsonBlock().setDisplayName("Potatoes").setShape("crop").setHardness(0.0F).setStepSound("grass").setBlockTextures(new String[] {"potatoes_stage_0", "potatoes_stage_1", "potatoes_stage_2", "potatoes_stage_3"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Wooden Button").setShape("button").setMaterial("circuits").setHardness(0.5F).setStepSound("wood").setBlockTexture("planks_oak"));
        // Skulls. All logic lies in their item and we're not saving any Item textures. Maybe I'll implement that by copying over the respective textures.
        blockRegistry.add(new JsonBlock().setDisplayName("Anvil").setShape("anvil").setMaterial("anvil").setHardness(5.0F).setStepSound("anvil").setResistance(2000.0F).setBlockTextures(new String[] {"anvil_base", "anvil_top_damaged_0", "anvil_top_damaged_1", "anvil_top_damaged_2"}));
        // Trapped Chest
        blockRegistry.add(new JsonBlock().setDisplayName("Light Weighted Pressure Plate").setShape("weighted_pressure_plate").setMaterial("iron").setHardness(0.5F).setStepSound("wood").setBlockTexture("iron_block"));
        blockRegistry.add(new JsonBlock().setDisplayName("Heavy Weighted Pressure Plate").setShape("weighted_pressure_plate").setMaterial("iron").setHardness(0.5F).setStepSound("wood").setBlockTexture("gold_block"));
        // Redstone Comparator
        blockRegistry.add(new JsonBlock().setDisplayName("Daylight Detector").setShape("daylight_detector").setMaterial("wood").setHardness(0.2F).setStepSound("wood").setBlockTextures(new String[] {"daylight_detector_side", "daylight_detector_top"}));
        // Hopper
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Quartz Block").setHardness(0.8F).setStepSound("piston").setBlockTextures(new String[] {"quartz_block_bottom", "quartz_block_top", "quartz_block_side"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Chiseled Quartz Block").setParent(tmp).setBlockTextures(new String[] {"quartz_block_chiseled_top", "quartz_block_chiseled_side"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Quartz Stairs").setShape("stair").setHardness(2.0F).setStepSound("piston").setBlockTextures(new String[] {"quartz_block_bottom", "quartz_block_top", "quartz_block_side"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Pillar Quartz Block").setParent(tmp).setShape("rotating").setBlockTextures(new String[] {"quartz_block_lines_top", "quartz_block_lines_top", "quartz_block_lines"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Activator Rail").setShape("rail").setMaterial("circuits").setHardness(0.7F).setStepSound("metal").setBlockTextures(new String[] {"rail_activator", "rail_activator_powered"}));
        // Dropper
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("White Stained Clay").setHardness(1.25F).setResistance(7.0F).setStepSound("piston").setBlockTexture("hardened_clay_stained_white"));
        blockRegistry.add(new JsonBlock().setDisplayName("Orange Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_orange"));
        blockRegistry.add(new JsonBlock().setDisplayName("Magenta Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_magenta"));
        blockRegistry.add(new JsonBlock().setDisplayName("Light Blue Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_light_blue"));
        blockRegistry.add(new JsonBlock().setDisplayName("Yellow Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_yellow"));
        blockRegistry.add(new JsonBlock().setDisplayName("Lime Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_lime"));
        blockRegistry.add(new JsonBlock().setDisplayName("Pink Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_pink"));
        blockRegistry.add(new JsonBlock().setDisplayName("Gray Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_gray"));
        blockRegistry.add(new JsonBlock().setDisplayName("Light Gray Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_silver"));
        blockRegistry.add(new JsonBlock().setDisplayName("Cyan Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_cyan"));
        blockRegistry.add(new JsonBlock().setDisplayName("Purple Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_purple"));
        blockRegistry.add(new JsonBlock().setDisplayName("Blue Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_blue"));
        blockRegistry.add(new JsonBlock().setDisplayName("Brown Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_brown"));
        blockRegistry.add(new JsonBlock().setDisplayName("Green Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_green"));
        blockRegistry.add(new JsonBlock().setDisplayName("Red Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_red"));
        blockRegistry.add(new JsonBlock().setDisplayName("Black Stained Clay").setParent(tmp).setBlockTexture("hardened_clay_stained_black"));
        blockRegistry.add(new JsonBlock().setDisplayName("Hay Bale").setShape("rotating").setMaterial("grass").setHardness(0.5F).setStepSound("grass").setBlockTextures(new String[] {"hay_block_top", "hay_block_top", "hay_block_side"}));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Carpet").setShape("carpet").setMaterial("carpet").setStepSound("cloth").setHardness(0.1F).setLightOpacity(0).setBlockTexture("wool_colored_white"));
        blockRegistry.add(new JsonBlock().setDisplayName("Orange Carpet").setParent(tmp).setBlockTexture("wool_colored_orange"));
        blockRegistry.add(new JsonBlock().setDisplayName("Magenta Carpet").setParent(tmp).setBlockTexture("wool_colored_magenta"));
        blockRegistry.add(new JsonBlock().setDisplayName("Light Blue Carpet").setParent(tmp).setBlockTexture("wool_colored_light_blue"));
        blockRegistry.add(new JsonBlock().setDisplayName("Yellow Carpet").setParent(tmp).setBlockTexture("wool_colored_yellow"));
        blockRegistry.add(new JsonBlock().setDisplayName("Lime Carpet").setParent(tmp).setBlockTexture("wool_colored_lime"));
        blockRegistry.add(new JsonBlock().setDisplayName("Pink Carpet").setParent(tmp).setBlockTexture("wool_colored_pink"));
        blockRegistry.add(new JsonBlock().setDisplayName("Gray Carpet").setParent(tmp).setBlockTexture("wool_colored_gray"));
        blockRegistry.add(new JsonBlock().setDisplayName("Light Gray Carpet").setParent(tmp).setBlockTexture("wool_colored_silver"));
        blockRegistry.add(new JsonBlock().setDisplayName("Cyan Carpet").setParent(tmp).setBlockTexture("wool_colored_cyan"));
        blockRegistry.add(new JsonBlock().setDisplayName("Purple Carpet").setParent(tmp).setBlockTexture("wool_colored_purple"));
        blockRegistry.add(new JsonBlock().setDisplayName("Blue Carpet").setParent(tmp).setBlockTexture("wool_colored_blue"));
        blockRegistry.add(new JsonBlock().setDisplayName("Brown Carpet").setParent(tmp).setBlockTexture("wool_colored_brown"));
        blockRegistry.add(new JsonBlock().setDisplayName("Green Carpet").setParent(tmp).setBlockTexture("wool_colored_green"));
        blockRegistry.add(new JsonBlock().setDisplayName("Red Carpet").setParent(tmp).setBlockTexture("wool_colored_red"));
        blockRegistry.add(new JsonBlock().setDisplayName("Black Carpet").setParent(tmp).setBlockTexture("wool_colored_black"));
        blockRegistry.add(new JsonBlock().setDisplayName("Hardened Clay").setBlockTexture("hardened_clay"));
        blockRegistry.add(new JsonBlock().setDisplayName("Packed Ice").setMaterial("packed_ice").setHardness(0.5F).setSlipperiness(0.98F).setStepSound("glass").setBlockTexture("ice_packed"));
        blockRegistry.add(tmp = new JsonBlock().setDisplayName("Sunflower").setShape("double_plant").setMaterial("plants").setHardness(0.0F).setStepSound("grass").setBlockTextures(new String[] {"double_plant_sunflower_top", "double_plant_sunflower_bottom", "double_plant_sunflower_front", "double_plant_sunflower_back"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Lilac").setParent(tmp).setBlockTextures(new String[] {"double_plant_syringa_top", "double_plant_syringa_bottom"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Double Tallgrass").setParent(tmp).setRenderColor(foliageColorBasic).setBlockTextures(new String[] {"double_plant_grass_top", "double_plant_grass_bottom"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Large Fern").setParent(tmp).setRenderColor(foliageColorBasic).setBlockTextures(new String[] {"double_plant_fern_top", "double_plant_fern_bottom"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Rose Bush").setParent(tmp).setBlockTextures(new String[] {"double_plant_rose_top", "double_plant_rose_bottom"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Peony").setParent(tmp).setBlockTextures(new String[] {"double_plant_paeonia_top", "double_plant_paeonia_bottom"}));
        
        /** ========== 1.8 ========== **/
        blockRegistry.add(new JsonBlock().setDisplayName("Granite").setParent(rock).setBlockTexture("granite"));
        blockRegistry.add(new JsonBlock().setDisplayName("Polished Granite").setParent(rock).setBlockTexture("polished_granite"));
        blockRegistry.add(new JsonBlock().setDisplayName("Diorite").setParent(rock).setBlockTexture("diorite"));
        blockRegistry.add(new JsonBlock().setDisplayName("Polished Diorite").setParent(rock).setBlockTexture("polished_diorite"));
        blockRegistry.add(new JsonBlock().setDisplayName("Andesite").setParent(rock).setBlockTexture("andesite"));
        blockRegistry.add(new JsonBlock().setDisplayName("Polished Andesite").setParent(rock).setBlockTexture("polished_andesite"));
        blockRegistry.add(new JsonBlock().setDisplayName("Coarse Dirt").setParent(dirt).setBlockTexture("coarse_dirt"));
        blockRegistry.add(new JsonBlock().setDisplayName("Prismarine").setParent(rock).setBlockTexture("prismarine_rough"));
        blockRegistry.add(new JsonBlock().setDisplayName("Prismarine Bricks").setParent(rock).setBlockTexture("prismarine_bricks"));
        blockRegistry.add(new JsonBlock().setDisplayName("Dark Prismarine").setParent(rock).setBlockTexture("prismarine_dark"));
        blockRegistry.add(new JsonBlock().setDisplayName("Red Sandstone").setParent(sandstone).setBlockTextures(new String[] {"red_sandstone_bottom", "red_sandstone_top", "red_sandstone_normal"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Chiseled Red Sandstone").setParent(sandstone).setBlockTextures(new String[] {"red_sandstone_bottom", "red_sandstone_top", "red_sandstone_carved"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Smooth Red Sandstone").setParent(sandstone).setBlockTextures(new String[] {"red_sandstone_bottom", "red_sandstone_top", "red_sandstone_smooth"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Red Sandstone Slab").setParent(rockSlab).setBlockTextures(new String[] {"red_sandstone_bottom", "red_sandstone_top", "red_sandstone_normal"}));
        // Banner
        blockRegistry.add(new JsonBlock().setDisplayName("Red Sandstone Stairs").setShape("stair").setHardness(0.8F).setResistance(10.0F).setStepSound("piston").setBlockTextures(new String[] {"red_sandstone_bottom", "red_sandstone_top", "red_sandstone_normal"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Iron Trapdoor").setShape("trapdoor").setMaterial("iron").setHardness(5.0F).setResistance(25.0F).setBlockTexture("iron_block"));
        blockRegistry.add(new JsonBlock().setDisplayName("Sea Lantern").setParent(lamp).setBlockTexture("sea_lantern"));
        blockRegistry.add(new JsonBlock().setDisplayName("Wet Sponge").setParent(sponge).setBlockTexture("sponge_wet"));
        blockRegistry.add(new JsonBlock().setDisplayName("Slime Block").setShape("slime_block").setHardness(0.0F).setResistance(0.0F).setBlockTexture("slime"));
        // Barrier Block
        blockRegistry.add(new JsonBlock().setDisplayName("Spruce Wood Fence").setParent(woodFence).setBlockTexture("planks_spruce"));
        blockRegistry.add(new JsonBlock().setDisplayName("Birch Wood Fence").setParent(woodFence).setBlockTexture("planks_birch"));
        blockRegistry.add(new JsonBlock().setDisplayName("Jungle Wood Fence").setParent(woodFence).setBlockTexture("planks_jungle"));
        blockRegistry.add(new JsonBlock().setDisplayName("Acacia Wood Fence").setParent(woodFence).setBlockTexture("planks_acacia"));
        blockRegistry.add(new JsonBlock().setDisplayName("Dark Oak Wood Fence").setParent(woodFence).setBlockTexture("planks_big_oak"));
        blockRegistry.add(new JsonBlock().setDisplayName("Spruce Wood Fence Gate").setParent(woodFenceGate).setBlockTexture("planks_spruce"));
        blockRegistry.add(new JsonBlock().setDisplayName("Birch Wood Fence Gate").setParent(woodFenceGate).setBlockTexture("planks_birch"));
        blockRegistry.add(new JsonBlock().setDisplayName("Jungle Wood Fence Gate").setParent(woodFenceGate).setBlockTexture("planks_jungle"));
        blockRegistry.add(new JsonBlock().setDisplayName("Acacia Wood Fence Gate").setParent(woodFenceGate).setBlockTexture("planks_acacia"));
        blockRegistry.add(new JsonBlock().setDisplayName("Dark Oak Wood Fence Gate").setParent(woodFenceGate).setBlockTexture("planks_big_oak"));
        blockRegistry.add(new JsonBlock().setDisplayName("Spruce Wood Door").setParent(woodDoor).setBlockTextures(new String[] {"door_spruce_upper", "door_spruce_lower"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Birch Wood Door").setParent(woodDoor).setBlockTextures(new String[] {"door_birch_upper", "door_birch_lower"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Jungle Wood Door").setParent(woodDoor).setBlockTextures(new String[] {"door_jungle_upper", "door_jungle_lower"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Acacia Wood Door").setParent(woodDoor).setBlockTextures(new String[] {"door_acacia_upper", "door_acacia_lower"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Dark Oak Wood Door").setParent(woodDoor).setBlockTextures(new String[] {"door_dark_oak_upper", "door_dark_oak_lower"}));
        
        /** ========== 1.9 ========== */
        blockRegistry.add(new JsonBlock().setDisplayName("End Rod").setShape("end_rod").setMaterial("wood").setHardness(0.0F).setBlockTexture("end_rod"));
        blockRegistry.add(new JsonBlock().setDisplayName("End Stone Bricks").setResistance(0.8F).setBlockTexture("end_bricks"));
        blockRegistry.add(new JsonBlock().setDisplayName("Chorus Plant").setShape("chorus_plant").setBlockTexture("chorus_plant"));
        blockRegistry.add(new JsonBlock().setDisplayName("Chorus Flower").setShape("chorus_plant").setBlockTextures(new String[] {"chorus_flower", "chorus_flower_dead"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Purpur Block").setParent(rock).setBlockTexture("purpur_block"));
        blockRegistry.add(new JsonBlock().setDisplayName("Purpur Pillar").setParent(rock).setShape("rotating").setBlockTextures(new String[] {"purpur_pillar_top", "purpur_pillar_top", "purpur_pillar"}));
        blockRegistry.add(new JsonBlock().setDisplayName("Purpur Slab").setParent(rockSlab).setShape("slab").setBlockTexture("purpur_block"));
        blockRegistry.add(new JsonBlock().setDisplayName("Purpur Stairs").setShape("stair").setHardness(2.0F).setResistance(10.0F).setStepSound("piston").setBlockTexture("purpur_block"));
        blockRegistry.add(new JsonBlock().setDisplayName("Grass Path").setShape("grass_path").setBlockTextures(new String[] {"dirt", "grass_path_top", "grass_path_side"}));
        // End Gateway
        // Structure Block
        
        // http://www.minecraftinfo.com/idlist.htm

        for (JsonBlock block : blockRegistry)
        {
            block.setCategory();
            block.setNextId();
        }
        blockRegistry.removeIf(block -> ConfigHandler.getBoolean(block.getShape().name(), true) == false);
    }
}
