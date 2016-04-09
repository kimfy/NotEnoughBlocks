package com.kimfy.notenoughblocks.common.file.json;

import com.google.gson.*;
import com.kimfy.notenoughblocks.common.block.IBlockProperties;
import com.kimfy.notenoughblocks.common.file.FileManager;
import com.kimfy.notenoughblocks.common.integration.Chisel;
import com.kimfy.notenoughblocks.common.util.Utilities;
import com.kimfy.notenoughblocks.common.util.block.*;
import lombok.Getter;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.JsonUtils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class BlockJson implements Serializable
{
    public BlockJson() {}

    protected String displayName     = "displayName";
    protected String unlocalizedName = null;

    protected Map<String, String> textures = new LinkedHashMap<>();
    protected List<Drop> drop              = null;
    protected List<String> lore            = null;
    protected List<String> oreDict         = null;
    protected Recipe recipe                = null;

    /* Block Specific Properties  */
    protected int maxStackSize           = 64;
    protected String creativeTab         = "buildingblocks";
    protected String shape               = "cube";
    protected float  resistance          = 10.0F;
    protected String stepSound           = "stone";
    protected boolean opaque             = true;
    protected boolean stained            = false;
    protected String toolType            = null;
    protected int harvestLevel           = 0;
    protected boolean beaconBase         = false;
    protected boolean silkTouch          = true;
    protected boolean enchAmplifier      = false;
    protected float slipperiness         = 0.6F;
    protected String sensitivity         = "everything";
    protected boolean canBlockGrass      = false;
    protected float particleGravity      = 1.0F;
    protected boolean enableStats        = true;
    protected String buttonType          = null; // Valid types are 'wooden' and 'stone'

    /* 1.9 made these metadata specific... */
    protected String material            = "rock"; // TODO: Add delegate methods in BlockAgent for these values
    protected float  hardness            = 1.5F;   //
    protected int lightOpacity           = 255;    //
    protected float lightLevel           = 0.0F;   //
    protected boolean neighborBrightness = false;  //
    protected boolean fullBlock          = true;   //
    protected boolean fullCube           = true;   //
    protected boolean transluscent       = false;  //
    protected boolean canProvidePower    = false;  //
    protected int mobility               = 0;      //

    /* Visual */
   protected int renderColor       = -1;
   protected boolean isSunflower   = false;
   protected boolean isDeadBush    = false;
   protected boolean needsColoring = false; // Used by Double Plants to determine their item color

    /** Parent block, used to inherit properties */
    protected BlockJson parent;

    protected Chisel chisel;

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
        }

        return trues == block.getTextureMap().size();
    }

    protected Shape realShape;

    public Shape getRealShape()
    {
        if (this.realShape == null)
        {
            this.realShape = Shape.get(this.shape);
        }
        return this.realShape;
    }

    protected Material realMaterial;

    public Material getRealMaterial()
    {
        if (this.realMaterial == null)
        {
            this.realMaterial = EnumMaterial.get(material).getMaterial();
        }
        return realMaterial;
    }

    protected SoundType realSoundType;

    public SoundType getRealSoundType()
    {
        if (this.realSoundType == null)
        {
            this.realSoundType = EnumSoundType.get(stepSound).getSoundType();
        }
        return this.realSoundType;
    }

    protected CreativeTabs realCreativeTab;

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

    protected static final List<String> sides = Arrays.asList("down", "up", "north", "south", "west", "east", "particle");

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

    public BlockJson textures(Map<String, String> textures)
    {
        this.textures = textures;
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
        String formattedIn = in.replace(" ", "");
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
                ", resistance=" + resistance +
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
                ", enableStats=" + enableStats +
                ", buttonType='" + buttonType + '\'' +
                '}';
    }

    public Chisel getChisel()
    {
        return chisel;
    }

    protected Category category;

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

    public BlockJson drop(List<Drop> drop)
    {
        this.drop = new ArrayList<>(drop.size());
        this.drop.addAll(drop);
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

    public List<Drop> getDrop()
    {
        if (this.drop == null)
        {
            this.drop = new ArrayList<>();
        }
        return this.drop;
    }

    /* ========== HELPER METHODS ========== */

    public static List<String> getDisplayNamesFromBlocks(List<BlockJson> blocks)
    {
        return blocks.stream().map(BlockJson::getDisplayName).collect(Collectors.toCollection(ArrayList<String>::new));
    }

    public static List<String> getDisplayNamesFromBlock(IBlockProperties block)
    {
        return getDisplayNamesFromBlocks(block.getData());
    }

    // Json -> BlockJson
    public static class Deserializer implements JsonDeserializer<BlockJson>
    {
        @Override
        public BlockJson deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            JsonObject json = element.getAsJsonObject();
            BlockJson block = new BlockJson();

            block.displayName        = JsonUtils.getString(json, "displayName", "Default Block Name");
            block.textures           = objectToMap(json, "textures", null);
            block.maxStackSize       = JsonUtils.getInt(json, "maxStackSize", 64);
            block.creativeTab        = JsonUtils.getString(json, "creativeTab", "buildingblocks");
            block.shape              = JsonUtils.getString(json, "shape", "cube");
            block.hardness           = JsonUtils.getFloat(json, "hardness", 1.5F);
            block.resistance         = JsonUtils.getFloat(json, "resistance", 10.0F);
            block.material           = JsonUtils.getString(json, "material", "rock");
            block.stepSound          = JsonUtils.getString(json, "stepSound", "stone");
            block.opaque             = JsonUtils.getBoolean(json, "opaque", true);
            block.stained            = JsonUtils.getBoolean(json, "stained", false);
            block.toolType           = JsonUtils.getString(json, "toolType", null);
            block.harvestLevel       = JsonUtils.getInt(json, "harvestLevel", 0);
            block.beaconBase         = JsonUtils.getBoolean(json, "beaconBase", false);
            block.silkTouch          = JsonUtils.getBoolean(json, "silkTouch", true);
            block.enchAmplifier      = JsonUtils.getBoolean(json, "enchAmplifier", false);
            block.slipperiness       = JsonUtils.getFloat(json, "slipperiness", 0.6F);
            block.sensitivity        = JsonUtils.getString(json, "sensitivity", "everything");
            block.canBlockGrass      = JsonUtils.getBoolean(json, "canBlockGrass", false);
            block.particleGravity    = JsonUtils.getFloat(json, "particleGravity", 1.0F);
            block.mobility           = JsonUtils.getInt(json, "mobility", 0);
            block.enableStats        = JsonUtils.getBoolean(json, "enableStats", true);
            block.neighborBrightness = JsonUtils.getBoolean(json, "neighborBrightness", false);
            block.lightLevel         = JsonUtils.getFloat(json, "lightLevel", 0.0F);
            block.lightOpacity       = JsonUtils.getInt(json, "lightOpacity", 255);
            block.buttonType         = JsonUtils.getString(json, "buttonType", null);
            /* Visual */
            block.renderColor        = JsonUtils.getInt(json, "renderColor", -1);
            block.isSunflower        = JsonUtils.getBoolean(json, "isSunflower", false);
            block.isDeadBush         = JsonUtils.getBoolean(json, "isDeadBush", false);
            block.needsColoring      = JsonUtils.getBoolean(json, "needsColoring", false);
            /* 1.9 */
            block.fullBlock          = JsonUtils.getBoolean(json, "fullBlock", true);
            block.fullCube           = JsonUtils.getBoolean(json, "fullCube", true);
            block.transluscent       = JsonUtils.getBoolean(json, "transluscent", false);
            block.canProvidePower    = JsonUtils.getBoolean(json, "canProvidePower", false);

            this.setBlockDrops(block, json);
            this.setBlockRecipe(block, json);
            // block.lore
            // block.oreDict

            return block;
        }

        private void setBlockDrops(BlockJson model, JsonObject json)
        {
            if (JsonUtils.hasField(json, "drop"))
            {
                JsonElement drop = json.get("drop");
                Drop.Deserializer.walk(model, drop);
            }
        }

        private void setBlockRecipe(BlockJson model, JsonObject json)
        {
            if (JsonUtils.hasField(json, "recipe"))
            {
                JsonElement recipe = json.get("recipe");
                model.recipe = Utilities.gson.fromJson(recipe, Recipe.class);
            }
        }

        private Map<String, String> objectToMap(JsonObject json, String memberName, Map fallback)
        {
            Set<Map.Entry<String, JsonElement>> jsonSet = json.get(memberName).getAsJsonObject().entrySet();
            Map<String, String> ret = new LinkedHashMap<>();

            for (Map.Entry<String, JsonElement> entry : jsonSet)
            {
                ret.put(entry.getKey(), entry.getValue().getAsString());
            }

            return ret;
        }
    }

    // BlockJson -> Json
    public static class Serializer implements JsonSerializer<BlockJson>
    {
        @Override
        public JsonElement serialize(BlockJson block, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject json = new JsonObject();

            addProperty(json, "displayName", block.getDisplayName(), "You forgot to name me! Bastard!");
            addTextures(json, block);
            addProperty(json, "maxStackSize", block.getMaxStackSize(), 64);
            addProperty(json, "creativeTab", block.getCreativeTab(), "buildingblocks");
            addProperty(json, "shape", block.getShape(), "cube");
            addProperty(json, "hardness", block.getHardness(), 1.5F);
            addProperty(json, "resistance", block.getResistance(), 10.0F);
            addProperty(json, "material", block.getMaterial(), "rock");
            addProperty(json, "stepSound", block.getStepSound(), "stone");
            addProperty(json, "opaque", block.isOpaque(), true);
            addProperty(json, "stained", block.isStained(), false);
            addProperty(json, "toolType", block.getToolType(), "");
            addProperty(json, "harvestLevel", block.getHarvestLevel(), 0);
            addProperty(json, "beaconBase", block.isBeaconBase(), false);
            addProperty(json, "silkTouch", block.isSilkTouch(), true);
            addProperty(json, "enchAmplifier", block.isEnchAmplifier(), false);
            addProperty(json, "slipperiness", block.getSlipperiness(), 0.6F);
            addProperty(json, "sensitivity", block.getSensitivity(), "everything");
            addProperty(json, "canBlockGrass", block.isCanBlockGrass(), false);
            addProperty(json, "particleGravity", block.getParticleGravity(), 1.0F);
            addProperty(json, "mobility", block.getMobility(), 0);
            addProperty(json, "enableStats", block.isEnableStats(), true);
            addProperty(json, "neighborBrightness", block.isNeighborBrightness(), false);
            addProperty(json, "lightLevel", block.getLightLevel(), 0.0F);
            addProperty(json, "lightOpacity", block.getLightOpacity(), 255);
            addProperty(json, "buttonType", block.getButtonType(), "");

            /* Visual Effects */
            addProperty(json, "renderColor", block.getRenderColor(), -1);
            addProperty(json, "isSunflower", block.isSunflower(), false);
            addProperty(json, "isDeadBush", block.isDeadBush(), false);
            addProperty(json, "needsColoring", block.needsColoring(), false);

            /* 1.9 */
            addProperty(json, "fullBlock", block.isFullBlock(), true);
            addProperty(json, "fullCube", block.isFullCube(), true);
            addProperty(json, "transluscent", block.isTransluscent(), false);
            addProperty(json, "canProvidePower", block.isCanProvidePower(), false);

            this.setBlockDrops(json, block);
            this.setBlockRecipe(json, block);
            // model.lore
            // model.oreDict

            return json;
        }

        private void setBlockDrops(JsonObject json, BlockJson block)
        {
            if (block.getDrop() != null && !(block.getDrop().isEmpty()))
            {
                JsonArray array = new JsonArray();

                for (Drop drop : block.getDrop())
                {
                    JsonElement serialized = Utilities.gson.toJsonTree(drop, Drop.class);
                    array.add(serialized);
                }
                json.add("drop", array);
            }
        }

        private void setBlockRecipe(JsonObject json, BlockJson block)
        {
            if (block.getRecipe() != null)
            {
                JsonElement serialized = Utilities.gson.toJsonTree(block.getRecipe(), Recipe.class);
                json.add("recipe", serialized);
            }
        }

        private void addProperty(JsonObject json, String property, String value, String defaultValue)
        {
            if (value == null || defaultValue == null || value.isEmpty() || defaultValue.isEmpty()) return;
            if (!value.equals(defaultValue))
            {
                json.add(property, new JsonPrimitive(value));
            }
        }

        private void addProperty(JsonObject json, String property, Boolean value, Boolean defaultValue)
        {
            if (value == null || defaultValue == null) return;
            if (!value.equals(defaultValue))
            {
                json.add(property, new JsonPrimitive(value));
            }
        }

        private void addProperty(JsonObject json, String property, Number value, Number defaultValue)
        {
            if (value == null || defaultValue == null) return;
            if (!value.equals(defaultValue))
            {
                json.add(property, new JsonPrimitive(value));
            }
        }

        private void addProperty(JsonObject json, String property, Character value, Character defaultValue)
        {
            if (value == null || defaultValue == null) return;
            if (!value.equals(defaultValue))
            {
                json.add(property, new JsonPrimitive(value));
            }
        }

        /**
         * This method converts {@link BlockJson#textures} into a {@link JsonElement}, I am unsure
         * whether or not this is a good approach. I am imagining it eats up a lot of memory and slows
         * down launch time significantly. I will investigate and perform tests at some point!!
         *
         * @param json The JsonObject to add the textures to
         * @param block The {@link BlockJson} to grab textures from. Does NOT use the getter as the map
         *              has already been formatted properly when the resource pack was made
         */
        private void addTextures(JsonObject json, BlockJson block)
        {
            json.add("textures", Utilities.gson.toJsonTree(block.textures));
        }
    }
}

