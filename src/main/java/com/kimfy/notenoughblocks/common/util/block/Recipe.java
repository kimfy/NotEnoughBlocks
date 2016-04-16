package com.kimfy.notenoughblocks.common.util.block;

import com.google.gson.*;
import com.kimfy.notenoughblocks.common.util.IRegisterable;
import com.kimfy.notenoughblocks.common.util.MinecraftUtilities;
import com.kimfy.notenoughblocks.common.util.Registrar;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the recipe specified in JSON. Here are all the allowed
 * formats you can use: //TODO Move to the docs
 * Remember, a recipe must be unique, it cannot be the same as another item's recipe.
 *
 * <pre>
 *     // Primitive
 *     {
 *         "recipe": "minecraft:item:meta" // Drops 1 of the block, cannot have an amount param
 *     }
 *
 *     // Array
 *     {
 *          // Shaped recipe
 *         "recipe": [
 *              {
 *                  "amount": 8 // How many should the craft return. MUST be inside an object.
 *              },
 *              // Shaped recipe
 *              // Look at the below as the crafting grid in game, each array represents the row in the crafting table
 *              // The following would produce a shaped recipe. {@code null} MUST be put on empty slots
 *              [ null, "cobblestone", null ], // Row 1
 *              [ null, "stick", null ],       // Row 2
 *              [ null, "stick", null ]        // Row 3
 *         ]
 *
 *         // Shapeless recipe
 *         // Shapeless recipes are made with just one array, you can still give it an options object.
 *         // Since shapeless recipes do not care about an item's position in the crafting grid, you can
 *         // fill up an entire array from 1-9 with items.
 *         "recipe": [
 *              [ "torch", "crafting_table", "iron_ingot" ], // Array representing the item, do not put any {@code null}'s in here
 *              { "amount": 4 }
 *         ]
 *
 *         // The output would be 4 of the block
 *     }
 * </pre>
 */
public class Recipe implements IRegisterable
{
    private ItemStack output;
    private int outputAmount = 1;
    private final int width;
    private final int height;
    private final ItemStack[] ingredients;

    /** Shaped recipe */
    public Recipe(int width, int height, ItemStack[] ingredients, int outputAmount)
    {
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.outputAmount = outputAmount;
        Registrar.register(this);
    }

    public Recipe(int width, int height, ItemStack[] ingredients)
    {
        this(width, height, ingredients, 1);
    }

    /** Shapeless recipe */
    public Recipe(ItemStack[] ingredients, int outputAmount)
    {
        this(0, 0, ingredients, outputAmount);
    }

    public Recipe(ItemStack[] ingredients)
    {
        this(ingredients, 1);
    }

    public Recipe setOutput(Block block, int metadata)
    {
        this.output = new ItemStack(Item.getItemFromBlock(block), this.outputAmount, metadata);
        return this;
    }

    public ItemStack[] getIngredients()
    {
        return this.ingredients;
    }

    public ItemStack getResult()
    {
        return this.output;
    }

    public boolean isShapeless()
    {
        return height == 0;
    }

    public void register()
    {
        IRecipe recipe = this.isShapeless() ?
                new ShapelessRecipes(this.getResult(), Arrays.asList(this.ingredients)) :
                new ShapedRecipes(this.width, this.height, this.ingredients, this.getResult());
        GameRegistry.addRecipe(recipe);
    }

    // JSON -> Recipe
    public static class Deserializer implements JsonDeserializer<Recipe>
    {
        @Override
        public Recipe deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            Recipe recipe;
            int width, height;
            List<ItemStack> ingredients;

            if (src.isJsonArray())
            {
                ingredients = new LinkedList<>();
                JsonArray jsonRecipe = src.getAsJsonArray();

                JsonObject options = hasOptions(jsonRecipe);
                int output = options != null ? JsonUtils.getInt(options, "amount", 1) : 1;
                JsonArray temp = getFirstArray(jsonRecipe);

                width =  temp != null ? temp.size() : 3; // jsonRecipe.get(0).getAsJsonArray().size(); // get the size of the first array
                height = getNumberOfArrays(jsonRecipe); // jsonRecipe.size();

                if (!this.isShapeless(jsonRecipe))
                {
                    for (JsonElement row : jsonRecipe)
                    {
                        if (row.isJsonArray())
                        {
                            for (JsonElement slot : row.getAsJsonArray())
                            {
                                ingredients.add(slot.isJsonNull() ? null : MinecraftUtilities.strToItemStack(slot.getAsString())); // Only primitives allowed
                            }
                        }
                    }
                    recipe = new Recipe(width, height, ingredients.toArray(new ItemStack[ingredients.size()]), output); // TODO: OutputSize
                }
                else
                {
                    for (JsonElement row : jsonRecipe)
                    {
                        if (row.isJsonArray())
                        {
                            for (JsonElement slot : row.getAsJsonArray())
                            {
                                ingredients.add(slot.isJsonNull() ? null : MinecraftUtilities.strToItemStack(slot.getAsString()));
                            }
                        }
                    }
                    recipe = new Recipe(ingredients.toArray(new ItemStack[ingredients.size()]), output); // TODO: OutputSize
                }
                return recipe;
            }
            else if (src.isJsonPrimitive()) // No output allowed on primitives
            {
                ingredients = new ArrayList<>(1);
                ingredients.add(0, MinecraftUtilities.strToItemStack(src.getAsString()));
                return new Recipe(ingredients.toArray(new ItemStack[ingredients.size()]));
            }
            return null;
        }

        /**
         * Loops through the given array and checks if there's a {@link JsonObject}
         * present. If there is, return it, else, return null;
         *
         * @param array The json array to check
         * @return A {@link JsonObject} if one is found
         */
        private JsonObject hasOptions(JsonArray array)
        {
            for (JsonElement e : array)
            {
                if (e.isJsonObject())
                {
                    return e.getAsJsonObject();
                }
            }
            return null;
        }

        /**
         * Returns the amount of arrays found in this array
         *
         * @param array The array to check
         * @return The amount of arrays found
         */
        private int getNumberOfArrays(JsonArray array)
        {
            int found = 0;

            for (JsonElement e : array)
            {
                if (e.isJsonArray())
                {
                    found++;
                }
            }
            return found;
        }

        /**
         * Returns the first JsonArray it finds
         *
         * @param array The array to check
         * @return The first array in this array, otherwise null
         */
        private JsonArray getFirstArray(JsonArray array)
        {
            for (JsonElement e : array)
            {
                if (e.isJsonArray())
                {
                    return e.getAsJsonArray();
                }
            }
            return null;
        }

        /**
         * Returns true if there is only one array in the given array
         *
         * @param array The json array to check
         * @return True if this array only contains one array
         */
        private boolean isShapeless(JsonArray array)
        {
            int arrays = 0;
            for (JsonElement el : array)
            {
                if (el.isJsonArray())
                {
                    arrays++;
                }
            }
            return arrays == 1;
        }
    }

    // Recipe -> JSON
    public static class Serializer implements JsonSerializer<Recipe>
    {
        @Override
        public JsonElement serialize(Recipe src, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonArray recipe = new JsonArray();

            if (src.outputAmount > 1) // output amount is always set when it's over 1
            {
                JsonPrimitive outputAmount = new JsonPrimitive(src.outputAmount);
                JsonObject options = new JsonObject();
                options.add("amount", outputAmount);
                recipe.add(options);
            }

            if (src.isShapeless())
            {
                JsonArray row = new JsonArray();
                for (ItemStack stack : src.getIngredients())
                {
                    row.add(new JsonPrimitive(MinecraftUtilities.itemStackToString(stack)));
                }
                recipe.add(row);
            }
            else
            {
                // Split ingredients array into three arrays
                for (int i = 0; i < src.getIngredients().length; i += 2)
                {
                    // [ .. .. .. | .. .. .. | .. .. .. ] -> [ .. .. .. ], [ .. .. .. ], [ .. .. .. ]
                    int startIndex = i % 2 == 0 ? (3*i)/2 : (3*i+1)/2;
                    int endIndex = startIndex + 3;
                    if (startIndex == src.getIngredients().length) break;

                    List<ItemStack> row = Arrays.asList(src.getIngredients()).subList(startIndex, endIndex);
                    JsonArray jsonRow = new JsonArray();
                    row.forEach(stack -> jsonRow.add(stack == null ? JsonNull.INSTANCE : new JsonPrimitive(MinecraftUtilities.itemStackToString(stack))));
                    recipe.add(jsonRow);
                }
            }
            return recipe;
        }
    }
}
