package com.kimfy.notenoughblocks.common.item;

import com.kimfy.notenoughblocks.common.block.NEBBlockSlab;
import com.kimfy.notenoughblocks.common.file.json.Json;
import com.kimfy.notenoughblocks.common.file.json.JsonProcessor;
import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.List;

public class NEBItemBlockSlab extends ItemSlab implements IItemProperties
{
    @Delegate(excludes = Excludes.class)
    private final ItemAgent<NEBItemBlockSlab> agent;

    public NEBItemBlockSlab(Block block)
    {
        super(block, null, null);
        this.agent = new ItemAgent<>(this);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        int metadata = stack.getMetadata();
        return this.getUnlocalizedName() + "_" + String.valueOf(metadata);
    }

    public void setSingleSlab(Block block)
    {
        try
        {
            ReflectionHelper.setPrivateValue(ItemSlab.class, this, block, "field_150949_c", "b" , "singleSlab");
        }
        catch (Exception e) {} // Using obf name
    }

    public void setDoubleSlab(Block block)
    {
        try
        {
            ReflectionHelper.setPrivateValue(ItemSlab.class, this, block, "field_179226_c", "c", "doubleSlab");
        }
        catch (Exception e) {} // Using obf name
    }

    /**
     * @see NEBBlockSlab#register(ResourceLocation)
     */
    public void register(ResourceLocation registryName)
    {
        ;
    }

    private interface Excludes
    {
        /**
         * We are excluding this method and adding a custom implementation because the Slab block has two blocks it
         * needs to register - which only adds clutter in
         * {@link JsonProcessor#registerBlocks(List, Json, int)}
         */
        void register(ResourceLocation registryName);
    }
}
