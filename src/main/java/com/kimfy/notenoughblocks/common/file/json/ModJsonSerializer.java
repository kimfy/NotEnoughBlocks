package com.kimfy.notenoughblocks.common.file.json;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ModJsonSerializer implements JsonSerializer<BlockJson>
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

        return json;
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
    private static Gson gson = new Gson();

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
        json.add("textures", gson.toJsonTree(block.textures));
    }
}
