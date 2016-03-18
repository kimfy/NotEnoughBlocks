package com.kimfy.notenoughblocks.rewrite.json;

import com.kimfy.notenoughblocks.rewrite.block.*;
import com.kimfy.notenoughblocks.rewrite.integration.Chisel;
import com.kimfy.notenoughblocks.rewrite.integration.ForgeMultipart;
import com.kimfy.notenoughblocks.rewrite.integration.MineFactoryReloaded;
import com.kimfy.notenoughblocks.rewrite.integration.NotEnoughItems;
import com.kimfy.notenoughblocks.rewrite.item.*;
import com.kimfy.notenoughblocks.util.Constants;
import com.kimfy.notenoughblocks.util.Utilities;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.biome.BiomeGenBase;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * TODO: Store metadata
 */
@Getter
public class JsonBlock implements Serializable
{
    protected transient Category category = null;
    
    public JsonBlock() {}
    
    public JsonBlock(Block block) {}
    
    @Getter
    protected transient JsonBlock instance = this;
    
    protected String displayName       = "Name of the Block";
    /**
     * When the real block of this Object is registered
     * unlocalizedName is set to the name it was registered
     * with.
     *
     * Usages:
     *  Can retrieve block directly from the GameRegistry
     */
    protected String unlocalizedName   = null;
    protected String identifier        = null;
    protected String shape             = "normal"; // Global
    
    protected String[] textures        = null;
    protected String texture           = null;
    protected String[] drops           = null;
    protected String drop              = null;
    protected String[] multiLore       = null;
    protected String lore              = null;
    protected String[] oreDicts        = null;
    protected String oreDict           = null;
    
    
    protected float hardness           = 1.5F; // Global
    protected float resistance         = 10.0F; // Global
    protected String stepSound         = "stone"; // Global
    protected String material          = "rock";  // Global
    protected String creativeTab       = "buildingblocks";
    protected boolean opaque           = true; // Global
    protected boolean stained          = false; // Global
    protected String toolType          = null; // pickaxe, spade, axe
    protected int harvestLevel         = 0; // 0 - wood & gold, 1 - stone, 2 - iron, 3 - diamond
    protected boolean beaconBase       = false; // Global
    protected boolean silkTouch        = true; // Global
    protected boolean enchantAmplifier = false; // Bookshelfs are 1 - everything else is 0 // global
    protected int maxStackSize         = 64;
    protected float slipperiness       = 0.6F; // Global
    protected String sensitivity       = null; // Used for pressure plates. Values are: everything, mobs, players
    protected Boolean deadBush         = null;
    
    // Visual 
    protected float lightLevel         = 0.0F; // How much light is emitted from this block | Global
    protected int lightOpacity         = 255; // How much light this block stops from going through | Global
    protected int renderColor          = -1;
    
    // Not implemented or used features
    protected boolean canBlockGrass            = false; // Global
    protected float blockParticleGravity       = 1.0F; // Global
    protected int mobility                     = 0; // Global
    protected boolean enableStats              = true; // Global
    protected boolean useNeighborBrightness    = false; // Used for stairs // Global
    protected static double id = 0.0;

    /** World Generation */
    @Setter
    protected WorldGeneration worldGeneration = null;

    // Mod interactions
    protected ForgeMultipart fmp       = null;
    protected Chisel chisel            = null;
    protected MineFactoryReloaded mfr  = null;
    @Setter
    protected NotEnoughItems nei       = null;
    
    @Getter
    public enum Shape
    {
        NORMAL(16, NEBBlock.class, NEBItemBlock.class), 
        STAIR(1, NEBBlockStair.class, NEBItemBlockStair.class),
        SLAB(8, NEBBlockSlab.class, null),
        GLASS(16, NEBBlockGlass.class, NEBItemBlock.class),
        STAINED_GLASS(16, NEBBlockGlass.class, NEBItemBlock.class),
        WALL(16, NEBBlockWall.class, NEBItemBlock.class), 
        FENCE(16, NEBBlockFence.class, NEBItemBlock.class), 
        ANVIL(16, null, null),
        BREWING_STAND(16, null, null),
        PRESSURE_PLATE(1, NEBBlockPressurePlate.class, NEBItemBlockPressurePlate.class),
        BEACON(16, null, null),
        BED(1, NEBBlockBed.class, NEBItemBlockBed.class),
        BUSH(16, null, null), 
        BUTTON(16, null, null), 
        CACTUS(16, null, null),
        CAKE(16, null, null), 
        CARPET(16, NEBBlockCarpet.class, NEBItemBlock.class), 
        CAULDRON(16, null, null), 
        COCOA(16, null, null),
        CROP(4, null, null),
        DAYLIGHT_DETECTOR(1, NEBBlockDaylightDetector.class, NEBItemBlockDaylightDetector.class),
        DEADBUSH(16, null, null), // {@link BlockDeadBush}. Dead Bush: This is the one that spawns in deserts
        DIRECTIONAL(5, null, null),
        DOOR(1, NEBBlockDoor.class, NEBItemBlockDoor.class),
        DOUBLE_PLANT(8, NEBBlockDoublePlant.class, NEBItemDoublePlant.class),
        DRAGON_EGG(16, null, null),
        ENCHANTING_TABLE(16, null, null),
        FALLING(16, NEBBlockFalling.class, NEBItemBlock.class), 
        FENCE_GATE(1, NEBBlockFenceGate.class, NEBItemBlockFenceGate.class),
        FARMLAND(16, null, null),
        FLOWER(16, NEBBlockFlower.class, NEBItemBlock.class),
        FLOWER_POT(16, null, null),
        GRASS(16, NEBBlockGrass.class, NEBItemBlock.class), 
        ICE(16, null, null),
        LADDER(16, null, null),
        LEAVES(16, NEBBlockLeaves.class, NEBItemBlock.class),
        LEVER(16, null, null),
        LILYPAD(16, NEBBlockLilyPad.class, NEBItemBlockLilyPad.class),
        LIQUID(16, null, null),
        MUSHROOM(16, null, null),
        MUSHROOM_BLOCK(3, null, null),
        PANE(16, NEBBlockPane.class, NEBItemBlock.class),
        RAIL(16, null, null),
        REDSTONE_LAMP(8, null, null),
        REDSTONE_TORCH(1, NEBBlockTorch.class, NEBItemBlockTorch.class),
        SUGAR_CANE(16, NEBBlockReed.class, NEBItemBlockReed.class),
        ROTATING(4, NEBBlockRotating.class, NEBItemBlockRotating.class),
        SAPLING(16, null, null),
        SIGN(16, null, null),
        SKULL(16, null, null),
        SLIME_BLOCK(16, null, null),
        SNOW_LAYER(1, NEBBlockSnow.class, NEBItemBlockSnow.class),
        TALLGRASS(16, NEBBlockTallGrass.class, NEBItemBlock.class), // {@link BlockTallGrass}. Shrub(unused), Tallgrass, Fern
        TORCH(1, NEBBlockTorch.class, NEBItemBlockTorch.class),
        TRAPDOOR(4, NEBBlockTrapdoor.class, NEBItemBlockTrapdoor.class),
        TRIPWIRE(16, null, null),
        TRIPWIRE_HOOK(16, null, null),
        VINE(4, NEBBlockVine.class, NEBItemBlockVine.class),
        WEB(16, NEBBlockWeb.class, NEBItemBlock.class),
        WEIGHTED_PRESSURE_PLATE(16, null, NEBItemBlockPressurePlate.class),
        
        /* 1.9 */
        CHORUS_PLANT(16, null, null),
        CHORUS_FLOWER(16, null, null),
        END_ROD(16, null, null),
        GRASS_PATH(16, null, null);
        
        private int maxSubBlocks;
        private Class<? extends Block> blockClass;
        private Class<? extends ItemBlock> itemClass;
        
        Shape(int maxSubBlocks, Class<?> cls, Class<?> itemClass)
        {
        	this.maxSubBlocks = maxSubBlocks;
        	this.blockClass = (Class<? extends Block>) cls;
        	this.itemClass = (Class<? extends ItemBlock>) itemClass;
        }
    }
    
    public transient JsonBlock parent = null;
    
    public JsonBlock setParent(JsonBlock parent)
    {
        this.parent = parent;
        this.setShape(parent.getStringShape());
        this.setHardness(parent.getHardness());
        this.setResistance(parent.getResistance());
        this.setStepSound(parent.getStepSound());
        this.setMaterial(parent.getMaterial());
        this.setCreativeTab(parent.getCreativeTab());
        this.setOpaque(parent.isOpaque());
        this.setStained(parent.isStained());
        this.setToolType(parent.getToolType());
        this.setHarvestLevel(parent.getHarvestLevel());
        this.setBeaconBase(parent.isBeaconBase());
        this.setSilkTouch(parent.isSilkTouch());
        this.setEnchantAmplifier(parent.isEnchantAmplifier());
        this.setMaxStackSize(parent.getMaxStackSize());
        this.setSlipperiness(parent.getSlipperiness());
        this.setLightLevel(parent.getLightLevel());
        this.setLightOpacity(parent.getLightOpacity());
        this.setCanBlockGrass(parent.isCanBlockGrass());
        this.setBlockParticleGravity(parent.getBlockParticleGravity());
        this.setMobility(parent.getMobility());
        this.setEnableStats(parent.isEnableStats());
        this.setUseNeighborBrightness(parent.isUseNeighborBrightness());
        return this;
    }
    
    public Shape getShape()
    {
        return Shape.valueOf(this.shape.toUpperCase());
    }
    
    public String getStringShape()
    {
        return this.shape;
    }

    public JsonBlock setDisplayName(String displayName)
    {
        this.displayName = displayName;
        return this;
    }

    public JsonBlock setUnlocalizedName(String unlocalizedName)
    {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    public JsonBlock setIdentifier(String identifier)
    {
        this.identifier = identifier;
        return this;
    }

    public JsonBlock setShape(String shape)
    {
        this.shape = shape;
        return this;
    }

    public JsonBlock setTextures(String[] textures)
    {
        this.textures = textures;
        return this;
    }

    public JsonBlock setDrops(String[] drops)
    {
        this.drops = drops;
        return this;
    }

    public JsonBlock setLore(String[] lore)
    {
        this.multiLore = lore;
        return this;
    }

    public JsonBlock setOreDict(String[] oreDict)
    {
        this.oreDicts = oreDict;
        return this;
    }

    public JsonBlock setHardness(float hardness)
    {
        this.hardness = hardness;
        return this;
    }

    public JsonBlock setResistance(float resistance)
    {
        this.resistance = resistance;
        return this;
    }

    public JsonBlock setStepSound(String stepSound)
    {
        this.stepSound = stepSound;
        return this;
    }

    public JsonBlock setMaterial(String material)
    {
        this.material = material;
        return this;
    }

    public JsonBlock setCreativeTab(String creativeTab)
    {
        this.creativeTab = creativeTab;
        return this;
    }

    public JsonBlock setOpaque(boolean opaque)
    {
        this.opaque = opaque;
        return this;
    }

    public JsonBlock setStained(boolean stained)
    {
        this.stained = stained;
        return this;
    }

    public JsonBlock setToolType(String toolType)
    {
        this.toolType = toolType;
        return this;
    }

    public JsonBlock setHarvestLevel(int harvestLevel)
    {
        this.harvestLevel = harvestLevel;
        return this;
    }

    public JsonBlock setBeaconBase(boolean beaconBase)
    {
        this.beaconBase = beaconBase;
        return this;
    }

    public JsonBlock setSilkTouch(boolean silkTouch)
    {
        this.silkTouch = silkTouch;
        return this;
    }

    public JsonBlock setEnchantAmplifier(boolean enchantAmplifier)
    {
        this.enchantAmplifier = enchantAmplifier;
        return this;
    }

    public JsonBlock setMaxStackSize(int maxStackSize)
    {
        this.maxStackSize = maxStackSize;
        return this;
    }

    public JsonBlock setSlipperiness(float slipperiness)
    {
        this.slipperiness = slipperiness;
        return this;
    }

    public JsonBlock setLightLevel(float lightLevel)
    {
        this.lightLevel = lightLevel;
        return this;
    }

    public JsonBlock setLightOpacity(int lightOpacity)
    {
        this.lightOpacity = lightOpacity;
        return this;
    }

    public JsonBlock setCanBlockGrass(boolean canBlockGrass)
    {
        this.canBlockGrass = canBlockGrass;
        return this;
    }

    public JsonBlock setBlockParticleGravity(float blockParticleGravity)
    {
        this.blockParticleGravity = blockParticleGravity;
        return this;
    }

    public JsonBlock setMobility(int mobility)
    {
        this.mobility = mobility;
        return this;
    }

    public JsonBlock setEnableStats(boolean enableStats)
    {
        this.enableStats = enableStats;
        return this;
    }

    public JsonBlock setUseNeighborBrightness(boolean useNeighborBrightness)
    {
        this.useNeighborBrightness = useNeighborBrightness;
        return this;
    }

    public JsonBlock setId(double id)
    {
        this.id = id;
        return this;
    }
    
    public JsonBlock setCategory()
    {
        this.category = new Category(this);
        return this;
    }
    
    public Category getCategory()
    {
        if (this.category == null) this.category = new Category(this);
        return this.category;
    }
    
    public JsonBlock setNextId()
    {
        this.id++;
        return this;
    }
    
    public double getId()
    {
        return this.id;
    }
    
    public boolean isStained()
    {
        return this.getStringShape().toUpperCase().equals("STAINED_GLASS") ? true : this.stained;
    }
    
    public JsonBlock setSensitivity(String sensitivity)
    {
        this.sensitivity = sensitivity;
        return this;
    }
    
    public JsonBlock isDeadBush(boolean isDeadBush)
    {
        this.deadBush = isDeadBush;
        return this;
    }
    
    public JsonBlock setRenderColor(int renderColor)
    {
        this.renderColor = renderColor;
        return this;
    }
    
    public boolean isDeadBush()
    {
        return this.deadBush != null ? this.deadBush : false;
    }
    
    public JsonBlock setBlockTexture(String texture)
    {
        this.setBlockTextures(new String[] {texture});
        return this;
    }
    
    public JsonBlock setBlockTextures(String[] textures)
    {
        if (textures.length < 6) {
            
            this.textures = new String[6];
            
            int l = textures.length;
            
            for (int i = 0; i < l; i++) {
                this.textures[i] = textures[i];
            }
            String t = textures[l - 1];
            
            for (int i = l; i < 6; i++) {
                this.textures[i] = t;
            }
        } else {
            this.textures = textures;
        }
        
        return this;
    }
    
    public String[] getBlockTextures()
    {
    	if (textures == null) {
    		textures = new String[6];
    		
    		for (int i = 0; i < 6; i++) {
    			textures[i] = texture;
    		}
    		return textures;
    		
    	} else if (textures.length < 6 && textures != null) {
    		int l = textures.length;
    		String t = textures[l - 1];
    		String[] tmp = new String[6];
    		
    		for (int i = 0; i < l; i++) {
    			tmp[i] = textures[i];
    		}
    		
    		for (int i = l - 1; i < 6; i++) {
    			tmp[i] = t;
    		}
    		
    		return tmp;
    	}
    	return textures;
    }
    
    /**
     * Checks for if this block's textures exist.
     * @return true if textures was found
     */
    public boolean exists()
    {
        int c = 0;
        
        for (String blockTexture : this.getBlockTextures()) {
            
            if (new File(Constants.RESOURCE_LOADER_NEB_PATH + blockTexture + ".png").exists()) {
                
                c++;
                
            }
        }
        
        return this.getShape() != Shape.BED ? c == 6 : c == 7;
    }
    
    public Material getObjectMaterial()
    {
        return Utilities.EnumMaterial.valueOf(material.toUpperCase()).getMaterial();
    }
    
    /** TODO: Error handling **/
    public Block.SoundType getObjectSoundType()
    {
    	return Utilities.EnumSoundType.valueOf(stepSound.toUpperCase()).getSoundType();
    }
    
    /** TODO: Error handling **/
    public CreativeTabs getObjectCreativeTab()
    {
    	return Utilities.EnumCreativeTab.valueOf(creativeTab.toUpperCase()).getCreativeTab();
    }
    
    /** TODO: Error handling **/
    public BlockPressurePlate.Sensitivity getObjectSensitivity()
    {
        return Utilities.EnumSensitivity.valueOf(sensitivity.toUpperCase()).getSensitivity();
    }

    public JsonBlock setChiselGroup(String group)
    {
        chisel = new Chisel().setChiselGroup(group);
        return this;
    }

    public JsonBlock setChiselGroup(List<String> groups)
    {
        chisel = new Chisel().setChiselGroups(groups);
        return this;
    }

    /**
     * Stores `worldGeneration` object from JSON <br>
     *     <h3>Example:</h3>
     *     <p><i>Flower Generation - Only works on shapes: flower</i> <br>
     *     <code>"worldGeneration": {
     *          "flowerGeneration": [
     *              {
     *                  "biome": "plains",
     *                  "weight": 20
     *              }
     *          ]
     *     }</code>
     *     Sets this object to spawn as a flower when grass is bonemealed
     *     </p>
     */
    public static class WorldGeneration implements Serializable
    {
        protected List<Biome> flowerGeneration = null;

        public List<Biome> getFlowerGeneration()
        {
            return this.flowerGeneration;
        }

        public static class Biome implements Serializable
        {
            public Biome(String biome, int weight)
            {
                this.biome = biome;
                this.weight = weight;
            }

            String biome = null;
            int weight   = 10;

            public BiomeGenBase getBiome()
            {
                return Utilities.EnumBiome.valueOf(this.biome.toUpperCase()).getBiome();
            }

            public int getWeight()
            {
                return this.weight;
            }
        }
    }
}