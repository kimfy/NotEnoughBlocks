package com.kimfy.notenoughblocks.common.util.block;

import com.google.gson.*;
import com.kimfy.notenoughblocks.common.util.MinecraftUtilities;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Recipe
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

    public Recipe setOutput(Block block, int metadata, int amount)
    {
        this.output = new ItemStack(Item.getItemFromBlock(block), amount, metadata);
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

    public void registerRecipe()
    {
        IRecipe recipe = this.isShapeless() ?
                new ShapelessRecipes(this.getResult(), Arrays.asList(this.ingredients)) :
                new ShapedRecipes(this.width, this.height, this.ingredients, this.getResult());
        GameRegistry.addRecipe(recipe);
    }

    /**
     * <pre>
     *     {
     *         "recipe": [
     *           [ "$c", "$c", "$c" ],
     *           [ "$c", "$c", "$c" ],
     *           [ "$c", "$c", "$c" ]
     *         ]
     *     }
     * </pre>
     */
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
                /*
                 * [
                 *       /---/---/---------width
                 *    [ "", "", "" ],  / - height
                 *    [ "", "", "" ], |
                 *    [ "", "", "" ]  |
                 * ]
                 */
                width = jsonRecipe.get(0).getAsJsonArray().size(); // get the size of the first array
                height = jsonRecipe.size();

                if (!this.isShapeless(jsonRecipe))
                {
                    for (JsonElement row : jsonRecipe)
                    {
                        for (JsonElement slot : row.getAsJsonArray())
                        {
                            ingredients.add(slot.isJsonNull() ? null : MinecraftUtilities.strToItemStack(slot.getAsString()));
                        }
                    }
                    recipe = new Recipe(width, height, ingredients.toArray(new ItemStack[ingredients.size()]), 1); // TODO: OutputSize
                }
                else
                {
                    for (JsonElement row : jsonRecipe)
                    {
                        for (JsonElement slot : row.getAsJsonArray())
                        {
                            ingredients.add(slot.isJsonNull() ? null : MinecraftUtilities.strToItemStack(slot.getAsString()));
                        }
                    }
                    recipe = new Recipe(ingredients.toArray(new ItemStack[ingredients.size()]), 1); // TODO: OutputSize
                }
                return recipe;
            }
            else if (src.isJsonPrimitive())
            {
                ingredients = new ArrayList<>(1);
                ingredients.add(0, MinecraftUtilities.strToItemStack(src.getAsString()));
                return new Recipe(ingredients.toArray(new ItemStack[ingredients.size()]));
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
