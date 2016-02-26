package com.kimfy.notenoughblocks.common.block.properties;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.properties.PropertyHelper;

import java.util.Collection;
import java.util.Set;

public class ModPropertyInteger extends PropertyHelper<Integer>
{
    private final ImmutableSet<Integer> allowedValues;

    protected ModPropertyInteger(String name, int subBlocks)
    {
        super(name, Integer.class);

        if (subBlocks > 16)
        {
            throw new IllegalArgumentException("Argument : subBlocks(" + subBlocks + ") is higher than the max value of 16");
        }
        else if (subBlocks < 0)
        {
            throw new IllegalArgumentException("Argument: subBlocks(" + subBlocks + ") is lower than the min value of 0");
        }

        Set<Integer> set = Sets.<Integer>newHashSet();

        for (int i = 0; i < subBlocks; i++)
        {
            set.add(Integer.valueOf(i));
        }

        this.allowedValues = ImmutableSet.copyOf(set);
    }

    @Override
    public Collection<Integer> getAllowedValues()
    {
        return this.allowedValues;
    }

    @Override
    public String getName(Integer value)
    {
        return value.toString();
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        else if (object != null && this.getClass() == object.getClass())
        {
            if (!super.equals(object))
            {
                return false;
            }
            else
            {
                ModPropertyInteger propertyinteger = (ModPropertyInteger) object;
                return this.allowedValues.equals(propertyinteger.allowedValues);
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        int i = super.hashCode();
        i = 31 * i + this.allowedValues.hashCode();
        return i;
    }

    public static ModPropertyInteger create(String name, int subBlocks)
    {
        return new ModPropertyInteger(name, subBlocks);
    }
}
