package com.kimfy.notenoughblocks.rewrite;

import com.kimfy.notenoughblocks.rewrite.file.FileProcessor;
import com.kimfy.notenoughblocks.rewrite.json.JsonBlocks;
import com.kimfy.notenoughblocks.rewrite.json.JsonProcessor;

public class NEB
{
    public static void initiate()
    {
        // Initializes the blockRegistry
        JsonBlocks.loadBlocks();

        // Process resourcepacks, extracting, moving and storing
        FileProcessor.initialize();

        // Initializes the JSONProcessor class.
        JsonProcessor.initialize();
    }
}
