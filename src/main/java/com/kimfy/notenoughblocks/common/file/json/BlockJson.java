package com.kimfy.notenoughblocks.common.file.json;

import com.google.gson.*;
import com.kimfy.notenoughblocks.common.block.IBlockProperties;
import com.kimfy.notenoughblocks.common.file.FileManager;
import com.kimfy.notenoughblocks.common.util.JsonUtilities;
import com.kimfy.notenoughblocks.common.util.Utilities;
import com.kimfy.notenoughblocks.common.util.block.*;
import lombok.Getter;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class BlockJson
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
    protected CreativeTabs creativeTab   = CreativeTabs.BUILDING_BLOCKS;
    protected Shape shape                = Shape.CUBE;
    protected float  resistance          = 10.0F;
    protected SoundType stepSound        = SoundType.STONE;
    protected boolean opaque             = true;
    protected boolean stained            = false;
    protected String toolType            = null;
    protected int harvestLevel           = 0;
    protected boolean beaconBase         = false;
    protected boolean silkTouch          = true;
    protected boolean enchAmplifier      = false;
    protected float slipperiness         = 0.6F;
    protected BlockPressurePlate.Sensitivity sensitivity = BlockPressurePlate.Sensitivity.EVERYTHING;
    protected boolean canBlockGrass      = false;
    protected float particleGravity      = 1.0F;
    protected boolean enableStats        = true;
    protected String buttonType          = null;

    /* 1.9 made these metadata specific */
    protected Material material          = Material.ROCK;
    protected float  hardness            = 1.5F;
    protected int lightOpacity           = 255;    // Let shape handle this
    protected float lightLevel           = 0.0F;
    protected boolean neighborBrightness = false;  // Shape handles this
    protected boolean fullBlock          = true;   // Not accessible to end user
    protected boolean fullCube           = true;   // Not accessible to end user
    protected boolean translucent        = false;
    protected boolean canProvidePower    = false;
    protected int mobility               = 0;      // Not accessible to end user

    /* Visual */
    protected int renderColor       = -1;
    protected boolean isSunflower   = false;
    protected boolean isDeadBush    = false;
    protected boolean needsColoring = false; // Used by Double Plants to determine their item color

    /* Miscellaneous */
    protected SoundEvent buttonOnSound  = SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON;
    protected SoundEvent buttonOffSound = SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;

    protected Category category;

    /* ========== Setters ========== */

    public BlockJson setUnlocalizedName(String unlocalizedName)
    {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    /* ========== Getters ========== */

    public Category getCategory()
    {
        if (category == null)
        {
            this.category = new Category(this);
        }
        return category;
    }

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

    /* ========== Helpers ========== */

    @Override
    public String toString()
    {
        return "BlockJson{" +
                "maxStackSize=" + maxStackSize +
                ", creativeTab='" + CreativeTab.toString(this.getCreativeTab()) + '\'' +
                ", shape='" + shape + '\'' +
                ", resistance=" + resistance +
                ", stepSound='" + SoundTypes.toString(this.getStepSound()) + '\'' +
                ", opaque=" + opaque +
                ", stained=" + stained +
                ", toolType='" + toolType + '\'' +
                ", harvestLevel=" + harvestLevel +
                ", beaconBase=" + beaconBase +
                ", silkTouch=" + silkTouch +
                ", enchAmplifier=" + enchAmplifier +
                ", slipperiness=" + slipperiness +
                ", sensitivity='" + com.kimfy.notenoughblocks.common.util.block.Sensitivity.toString(this.getSensitivity()) + '\'' +
                ", canBlockGrass=" + canBlockGrass +
                ", particleGravity=" + particleGravity +
                ", enableStats=" + enableStats +
                ", buttonType='" + buttonType + '\'' +
                '}';
    }

    protected static final List<String> sides = Arrays.asList("down", "up", "north", "south", "west", "east", "particle");

    /**
     * Used if this {@code BlockJson} is coming from a resource pack.
     * Performs a check on all textures found in it's texture map against
     * {@link FileManager#textures}. If all of the textures are found in
     * that list, we return true.
     *
     * @param block The block to perform this check on
     * @return True if all textures in this block's texture map were found
     */
    public static boolean exists(BlockJson block)
    {
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

    /**
     * Returns a {@link List<String>} of the display names of each block
     * given in the parameter.
     *
     * @param blocks The {@link List<BlockJson>} to get display names for
     * @return A list of display names
     */
    public static List<String> getDisplayNamesFromBlocks(List<BlockJson> blocks)
    {
        return blocks.stream().map(BlockJson::getDisplayName).collect(Collectors.toCollection(ArrayList<String>::new));
    }

    /**
     * @see BlockJson#getDisplayNamesFromBlocks(List)
     */
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
            block.shape              = Shape.get(JsonUtilities.getString(json, "shape", "cube"));
            block.displayName        = JsonUtilities.getString(json, "displayName", "Default Block Name");
            block.textures           = objectToMap(json, "textures", null);
            block.maxStackSize       = JsonUtilities.getInt(json, "maxStackSize", 64);
            block.creativeTab        = CreativeTab.get(JsonUtilities.getString(json, "creativeTab", "buildingblocks")).getCreativeTab();
            block.hardness           = JsonUtilities.getFloat(json, "hardness", 1.5F);
            block.resistance         = JsonUtilities.getFloat(json, "resistance", 10.0F);
            block.material           = Materials.get(JsonUtilities.getString(json, "material", "rock")).getMaterial();
            block.stepSound          = SoundTypes.get(JsonUtilities.getString(json, "stepSound", "stone")).getSoundType();
            block.opaque             = JsonUtilities.getBoolean(json, "opaque", true);
            block.stained            = JsonUtilities.getBoolean(json, "stained", false);
            block.toolType           = JsonUtilities.getString(json, "toolType", null);
            block.harvestLevel       = JsonUtilities.getInt(json, "harvestLevel", 0);
            block.beaconBase         = JsonUtilities.getBoolean(json, "beaconBase", false);
            block.silkTouch          = JsonUtilities.getBoolean(json, "silkTouch", true);
            block.enchAmplifier      = JsonUtilities.getBoolean(json, "enchAmplifier", false);
            block.slipperiness       = JsonUtilities.getFloat(json, "slipperiness", 0.6F);
            block.sensitivity        = Sensitivity.get(JsonUtilities.getString(json, "sensitivity", "everything")).getSensitivity();
            block.canBlockGrass      = JsonUtilities.getBoolean(json, "canBlockGrass", false);
            block.particleGravity    = JsonUtilities.getFloat(json, "particleGravity", 1.0F);
            block.mobility           = JsonUtilities.getInt(json, "mobility", 0);
            block.enableStats        = JsonUtilities.getBoolean(json, "enableStats", true);
            block.neighborBrightness = JsonUtilities.getBoolean(json, "neighborBrightness", block.getShape().useNeighborBrightness());
            block.lightLevel         = JsonUtilities.getFloat(json, "lightLevel", 0.0F);
            block.lightOpacity       = JsonUtilities.getInt(json, "lightOpacity", block.getShape().getLightOpacity());
            block.buttonType         = JsonUtilities.getString(json, "buttonType", null);
            /* Visual */
            block.renderColor        = JsonUtilities.getInt(json, "renderColor", -1);
            block.isSunflower        = JsonUtilities.getBoolean(json, "isSunflower", false);
            block.isDeadBush         = JsonUtilities.getBoolean(json, "isDeadBush", false);
            block.needsColoring      = JsonUtilities.getBoolean(json, "needsColoring", false);
            /* 1.9 */
            block.fullBlock          = JsonUtilities.getBoolean(json, "fullBlock", true);
            block.fullCube           = JsonUtilities.getBoolean(json, "fullCube", true);
            block.translucent        = JsonUtilities.getBoolean(json, "translucent", block.getShape().isTranslucent());
            block.canProvidePower    = JsonUtilities.getBoolean(json, "canProvidePower", false);
            /* Miscellaneous */
            this.setButtonSoundType(block, json);

            this.setBlockDrops(block, json);
            this.setBlockRecipe(block, json);
            // block.lore
            // block.oreDict

            return block;
        }

        private void setButtonSoundType(BlockJson model, JsonObject json)
        {
            if (!JsonUtilities.hasField(json, "buttonOnSound") && !JsonUtilities.hasField(json, "buttonOffSound"))
                return;
            model.buttonOnSound  = ButtonSoundType.getSound(JsonUtilities.getString(json, "buttonOnSound"));
            model.buttonOffSound = ButtonSoundType.getSound(JsonUtilities.getString(json, "buttonOffSound"));
        }

        private void setBlockDrops(BlockJson model, JsonObject json)
        {
            if (JsonUtilities.hasField(json, "drop"))
            {
                JsonElement drop = json.get("drop");
                Drop.Deserializer.walk(model, drop);
            }
        }

        private void setBlockRecipe(BlockJson model, JsonObject json)
        {
            if (JsonUtilities.hasField(json, "recipe"))
            {
                JsonElement recipe = json.get("recipe");
                model.recipe = Utilities.GSON.fromJson(recipe, Recipe.class);
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

            addProperty(json, "displayName", block.getDisplayName(), "displayName");
            addTextures(json, block);
            addProperty(json, "maxStackSize", block.getMaxStackSize(), 64);
            addProperty(json, "creativeTab", CreativeTab.toString(block.getCreativeTab()), "buildingblocks");
            addProperty(json, "shape", block.getShape().getName(), "cube");
            addProperty(json, "hardness", block.getHardness(), 1.5F);
            addProperty(json, "resistance", block.getResistance(), 10.0F);
            addProperty(json, "material", Materials.toString(block.getMaterial()), "rock");
            addProperty(json, "stepSound", SoundTypes.toString(block.getStepSound()), "stone");
            addProperty(json, "opaque", block.isOpaque(), true);
            addProperty(json, "stained", block.isStained(), false);
            addProperty(json, "toolType", block.getToolType(), "");
            addProperty(json, "harvestLevel", block.getHarvestLevel(), 0);
            addProperty(json, "beaconBase", block.isBeaconBase(), false);
            addProperty(json, "silkTouch", block.isSilkTouch(), true);
            addProperty(json, "enchAmplifier", block.isEnchAmplifier(), false);
            addProperty(json, "slipperiness", block.getSlipperiness(), 0.6F);
            addProperty(json, "sensitivity", Sensitivity.toString(block.getSensitivity()), "everything");
            addProperty(json, "canBlockGrass", block.isCanBlockGrass(), false);
            addProperty(json, "particleGravity", block.getParticleGravity(), 1.0F);
            addProperty(json, "mobility", block.getMobility(), 0);
            addProperty(json, "enableStats", block.isEnableStats(), true);
            addProperty(json, "neighborBrightness", block.isNeighborBrightness(), block.getShape().useNeighborBrightness());
            addProperty(json, "lightLevel", block.getLightLevel(), 0.0F);
            addProperty(json, "lightOpacity", block.getLightOpacity(), block.getShape().getLightOpacity());
            addProperty(json, "buttonType", block.getButtonType(), "");

            /* Visual Effects */
            addProperty(json, "renderColor", block.getRenderColor(), -1);
            addProperty(json, "isSunflower", block.isSunflower(), false);
            addProperty(json, "isDeadBush", block.isDeadBush(), false);
            addProperty(json, "needsColoring", block.needsColoring(), false);

            /* 1.9 */
            addProperty(json, "fullBlock", block.isFullBlock(), true);
            addProperty(json, "fullCube", block.isFullCube(), true);
            addProperty(json, "translucent", block.isTranslucent(), block.getShape().isTranslucent());
            addProperty(json, "canProvidePower", block.isCanProvidePower(), false);

            /* Miscellaneous */
            if (block.getShape() == Shape.BUTTON)
                this.addButtonSoundType(json, block);

            this.setBlockDrops(json, block);
            this.setBlockRecipe(json, block);
            // model.lore
            // model.oreDict

            return json;
        }

        private void addButtonSoundType(JsonObject json, BlockJson block)
        {
            this.addProperty(json, "buttonOnSound", ButtonSoundType.toString(block.buttonOnSound), "wood");
            this.addProperty(json, "buttonOffSound", ButtonSoundType.toString(block.buttonOffSound), "wood");
        }

        private void setBlockDrops(JsonObject json, BlockJson block)
        {
            if (block.getDrop() != null && !(block.getDrop().isEmpty()))
            {
                JsonArray array = new JsonArray();

                for (Drop drop : block.getDrop())
                {
                    JsonElement serialized = Utilities.GSON.toJsonTree(drop, Drop.class);
                    array.add(serialized);
                }
                json.add("drop", array);
            }
        }

        private void setBlockRecipe(JsonObject json, BlockJson block)
        {
            if (block.getRecipe() != null)
            {
                JsonElement serialized = Utilities.GSON.toJsonTree(block.getRecipe(), Recipe.class);
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
            json.add("textures", Utilities.GSON.toJsonTree(block.textures));
        }
    }
}

