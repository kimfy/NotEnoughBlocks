package com.kimfy.notenoughblocks.common.file.json;

public class Category
{
    private BlockJson block;

    public Category(BlockJson block)
    {
        this.block = block;
    }

    int getMaxBlocks()
    {
        return block.getRealShape().getMaxSubBlocks();
    }

    @Override
    public String toString()
    {
        return block.toString();
    }
}
