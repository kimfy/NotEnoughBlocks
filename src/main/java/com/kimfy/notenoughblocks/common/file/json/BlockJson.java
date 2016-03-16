package com.kimfy.notenoughblocks.common.file.json;

import com.kimfy.notenoughblocks.common.file.FileManager;
import com.kimfy.notenoughblocks.common.integration.Chisel;
import com.kimfy.notenoughblocks.common.util.block.EnumCreativeTab;
import com.kimfy.notenoughblocks.common.util.block.EnumMaterial;
import com.kimfy.notenoughblocks.common.util.block.EnumSoundType;
import com.kimfy.notenoughblocks.common.util.block.Shape;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public class BlockJson implements Serializable
{
    public String displayName               = "displayName";
    public transient String unlocalizedName = null;

    public Map<String, String> textures = new LinkedHashMap<>();
    public String drop                  = null;
    public List<String> drops           = null;
    public String lore                  = null;
    public List<String> lores           = null;
    public String oreDict               = null;
    public List<String> oreDicts        = null;

    /* Block Specific Properties  */
    public int maxStackSize           = 64;
    public String creativeTab         = "buildingblocks";
    public String shape               = "cube";
    public float  hardness            = 1.5F;
    public float  resistance          = 10.0F;
    public String material            = "rock";
    public String stepSound           = "stone";
    public boolean opaque             = true;
    public boolean stained            = false;
    public String toolType            = null;
    public int harvestLevel           = 0;
    public boolean beaconBase         = false;
    public boolean silkTouch          = true;
    public boolean enchAmplifier      = false;
    public float slipperiness         = 0.6F;
    public String sensitivity         = "everything";
    public boolean canBlockGrass      = false;
    public float particleGravity      = 1.0F;
    public int mobility               = 0;
    public boolean enableStats        = true;
    public boolean neighborBrightness = false;
    public float lightLevel           = 0.0F;
    public int lightOpacity           = 255;
    public String buttonType          = null; // Valid types are 'wooden' and 'stone'

    /* Visual */
    public int renderColor       = -1;
    public boolean isSunflower   = false;
    public boolean isDeadBush    = false;
    public boolean needsColoring = false; // Used by Double Plants to determine their item color

    /** Parent block, used to inherit properties */
    private transient BlockJson parent;

    private Chisel chisel;

    public static boolean exists(BlockJson block)
    {
        if (block == null)
        {
            throw new IllegalArgumentException("BlockJson cannot be null");
        }

        int trues = 0;

        for (String texture : block.getTextureMap().values())
        {
            if (FileManager.textures.contains(texture))
            {
                trues++;
            }
            //String filePath = Constants.PATH_MOD_TEXTURES_BLOCKS + texture + ".png";
            //File file = new File(filePath);

            //if (file.exists())
            //{
            //    trues++;
            //}
        }

        return trues == block.getTextureMap().size();
    }

    private transient Shape realShape;

    public Shape getRealShape()
    {
        if (this.realShape == null)
        {
            this.realShape = Shape.get(this.shape);
        }
        return this.realShape;
    }

    private transient Material realMaterial;

    public Material getRealMaterial()
    {
        if (this.realMaterial == null)
        {
            this.realMaterial = EnumMaterial.get(material).getMaterial();
        }
        return realMaterial;
    }

    private transient Block.SoundType realSoundType;

    public Block.SoundType getRealSoundType()
    {
        if (this.realSoundType == null)
        {
            this.realSoundType = EnumSoundType.get(stepSound).getSoundType();
        }
        return this.realSoundType;
    }

    private transient CreativeTabs realCreativeTab;

    public CreativeTabs getRealCreativeTab()
    {
        if (this.realCreativeTab == null)
        {
            this.realCreativeTab = EnumCreativeTab.get(creativeTab).getCreativeTab();
        }
        return this.realCreativeTab;
    }

    public BlockPressurePlate.Sensitivity getRealSensitivity()
    {
        String tmp = sensitivity.toUpperCase();
        return BlockPressurePlate.Sensitivity.valueOf(tmp);
    }

    private static final List<String> sides = Arrays.asList("down", "up", "north", "south", "west", "east", "particle");

    public Map<String, String> getTextureMap()
    {
        Function<String, Boolean> notUpNotDown = (input) -> !input.equals("down") && !input.equals("up");

        /*
         * Key 'all' assumes this key is the only one
         * present. Removes itself from the texture
         * map once the value is saved
         */
        if (textures.containsKey("all"))
        {
            String val = textures.get("all");
            textures.remove("all");

            sides.forEach(side -> textures.put(side, val));
        }

        /*
         * Key 'allSides' assumes only the keys 'down'
         * and 'up' exists. Populates every side but
         * those with the value of 'allSides'. Also
         * removes itself from the texture map once
         * the value has been saved.
         */
        if (textures.containsKey("allSides"))
        {
            String texture = textures.get("allSides");
            textures.remove("allSides");

            sides.stream()
                    .filter(notUpNotDown::apply)
                    .forEach(side -> {
                        if (!textures.containsKey(side))
                             textures.put(side, texture);
                    });
        }

        /*
         * Key 'particle' is not found, put the first element
         * from the texture map as particle.
         */
        if (!textures.containsKey("particle"))
        {
            if (textures.size() == 0) return textures;
            List<String> keys = Arrays.asList(textures.keySet().toArray(new String[textures.keySet().size()]));
            textures.put("particle", textures.get(keys.get(0)));
        }

        return textures;
    }

    public BlockJson textures(String textures)
    {
        this.textures = format(textures);
        return this;
    }

    /**
     * Turns a String of a key-value look into a map. Allows for
     * nested variables referencing keys. However, you cannot reference
     * a variable before it would appear in a map.
     * Examples:
     * <pre>
     *     // Returns: {particle=stone, down=planks_oak, up=planks_oak, side=planks_oak, allSides=planks_oak, random=planks_oak}
     *     format("particle: stone, down: planks_oak, up: #down, side: #up, allSides: #side, random: #allSides");
     *
     *     // Returns: {particle=#allSides, down=planks_oak, up=planks_oak, side=planks_oak, allSides=planks_oak, random=planks_oak}
     *     format("particle: #random, down: planks_oak, up: #down, side: #up, allSides: #side, random: #allSides");
     * </pre>
     *
     * @param in A string whose formatted in a way that is acceptable, refer to the javadocs
     * @return A map which is constructed as a Key = key and Value = value, refer to the javadocs
     */
    private static Map<String, String> format(String in)
    {
        Function<String, String> rmWhitespace = (String s) -> s.replace(" ", "");

        String formattedIn = rmWhitespace.apply(in);
        Map<String, String> temp = new LinkedHashMap<>();

        String[] split = formattedIn.split(",");
        for (String i : split)
        {
            temp.put(i.split(":")[0], i.split(":")[1]);
        }

        for (Map.Entry<String, String> entry : temp.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();

            temp.put(key, value);
        }
        return temp;
    }


    @Override
    public String toString()
    {
        return "BlockJson{" +
                "maxStackSize=" + maxStackSize +
                ", creativeTab='" + creativeTab + '\'' +
                ", shape='" + shape + '\'' +
                ", hardness=" + hardness +
                ", resistance=" + resistance +
                ", material='" + material + '\'' +
                ", stepSound='" + stepSound + '\'' +
                ", opaque=" + opaque +
                ", stained=" + stained +
                ", toolType='" + toolType + '\'' +
                ", harvestLevel=" + harvestLevel +
                ", beaconBase=" + beaconBase +
                ", silkTouch=" + silkTouch +
                ", enchAmplifier=" + enchAmplifier +
                ", slipperiness=" + slipperiness +
                ", sensitivity='" + sensitivity + '\'' +
                ", canBlockGrass=" + canBlockGrass +
                ", particleGravity=" + particleGravity +
                ", mobility=" + mobility +
                ", enableStats=" + enableStats +
                ", neighborBrightness=" + neighborBrightness +
                ", lightLevel=" + lightLevel +
                ", lightOpacity=" + lightOpacity +
                ", buttonType=" + buttonType +
                '}';
    }

    public Chisel getChisel()
    {
        return chisel;
    }

    private transient Category category;

    public Category getCategory()
    {
        if (category == null)
        {
            this.category = new Category(this);
        }
        return category;
    }

    /* ========== Builder methods ========== */
    /*
     * Builder methods have been made because
     * using Lombok's @Data annotation didn't
     * allow me to use default values for fields.
     * When reading JSON files generated from
     * resource packs, it would simply set the
     * empty/missing key/value pairs to null.
     */

    public BlockJson displayName(String displayName)
    {
        this.displayName = displayName;
        return this;
    }

    public BlockJson isDeadBush(boolean deadBush)
    {
        isDeadBush = deadBush;
        return this;
    }

    public BlockJson drop(String drop)
    {
        this.drop = drop;
        return this;
    }

    public BlockJson drops(List<String> drops)
    {
        this.drops = drops;
        return this;
    }

    public BlockJson lore(String lore)
    {
        this.lore = lore;
        return this;
    }

    public BlockJson lores(List<String> lores)
    {
        this.lores = lores;
        return this;
    }

    public BlockJson oreDict(String oreDict)
    {
        this.oreDict = oreDict;
        return this;
    }

    public BlockJson oreDicts(List<String> oreDicts)
    {
        this.oreDicts = oreDicts;
        return this;
    }

    public BlockJson maxStackSize(int maxStackSize)
    {
        this.maxStackSize = maxStackSize;
        return this;
    }

    public BlockJson creativeTab(String creativeTab)
    {
        this.creativeTab = creativeTab;
        return this;
    }

    public BlockJson shape(String shape)
    {
        this.shape = shape;
        return this;
    }

    public BlockJson hardness(float hardness)
    {
        this.hardness = hardness;
        return this;
    }

    public BlockJson resistance(float resistance)
    {
        this.resistance = resistance;
        return this;
    }

    public BlockJson material(String material)
    {
        this.material = material;
        return this;
    }

    public BlockJson stepSound(String stepSound)
    {
        this.stepSound = stepSound;
        return this;
    }

    public BlockJson opaque(boolean opaque)
    {
        this.opaque = opaque;
        return this;
    }

    public BlockJson stained(boolean stained)
    {
        this.stained = stained;
        return this;
    }

    public BlockJson toolType(String toolType)
    {
        this.toolType = toolType;
        return this;
    }

    public BlockJson harvestLevel(int harvestLevel)
    {
        this.harvestLevel = harvestLevel;
        return this;
    }

    public BlockJson beaconBase(boolean beaconBase)
    {
        this.beaconBase = beaconBase;
        return this;
    }

    public BlockJson silkTouch(boolean silkTouch)
    {
        this.silkTouch = silkTouch;
        return this;
    }

    public BlockJson enchAmplifier(boolean enchAmplifier)
    {
        this.enchAmplifier = enchAmplifier;
        return this;
    }

    public BlockJson slipperiness(float slipperiness)
    {
        this.slipperiness = slipperiness;
        return this;
    }

    public BlockJson sensitivity(String sensitivity)
    {
        this.sensitivity = sensitivity;
        return this;
    }

    public BlockJson canBlockGrass(boolean canBlockGrass)
    {
        this.canBlockGrass = canBlockGrass;
        return this;
    }

    public BlockJson particleGravity(float particleGravity)
    {
        this.particleGravity = particleGravity;
        return this;
    }

    public BlockJson mobility(int mobility)
    {
        this.mobility = mobility;
        return this;
    }

    public BlockJson enableStats(boolean enableStats)
    {
        this.enableStats = enableStats;
        return this;
    }

    public BlockJson neighborBrightness(boolean neighborBrightness)
    {
        this.neighborBrightness = neighborBrightness;
        return this;
    }

    public BlockJson lightLevel(float lightLevel)
    {
        this.lightLevel = lightLevel;
        return this;
    }

    public BlockJson lightOpacity(int lightOpacity)
    {
        this.lightOpacity = lightOpacity;
        return this;
    }

    public BlockJson renderColor(int renderColor)
    {
        this.renderColor = renderColor;
        return this;
    }

    public BlockJson sunflower(boolean sunflower)
    {
        isSunflower = sunflower;
        return this;
    }

    public BlockJson parent(BlockJson parent)
    {
        this.parent = parent;
        shape = parent.getShape();
        hardness = parent.getHardness();
        resistance = parent.getResistance();
        stepSound = parent.getStepSound();
        material = parent.getMaterial();
        creativeTab = parent.getCreativeTab();
        opaque = parent.isOpaque();
        stained = parent.isStained();
        toolType = parent.getToolType();
        harvestLevel = parent.getHarvestLevel();
        beaconBase = parent.isBeaconBase();
        silkTouch = parent.isSilkTouch();
        enchAmplifier = parent.isEnchAmplifier();
        maxStackSize = parent.getMaxStackSize();
        slipperiness = parent.getSlipperiness();
        lightLevel = parent.getLightLevel();
        lightOpacity = parent.getLightOpacity();
        canBlockGrass = parent.isCanBlockGrass();
        particleGravity = parent.getParticleGravity();
        mobility = parent.getMobility();
        enableStats = parent.isEnableStats();
        neighborBrightness = parent.isNeighborBrightness();
        return this;
    }

    public BlockJson unlocalizedName(String unlocalizedName)
    {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    public BlockJson buttonType(String buttonType)
    {
        this.buttonType = buttonType;
        return this;
    }

    public BlockJson needsColoring(boolean needsColoring)
    {
        this.needsColoring = needsColoring;
        return this;
    }

    /* ========== Getters ========== */

    public String getShape()
    {
        if (shape.equalsIgnoreCase("ice"))
        {
            return "cube";
        }

        return shape;
    }

    public String getButtonType()
    {
        return buttonType == null ? "wooden" : buttonType;
    }

    public boolean isButtonWooden()
    {
        return this.getButtonType().toUpperCase().equals("WOODEN");
    }

    public boolean needsColoring()
    {
        return needsColoring;
    }
}

