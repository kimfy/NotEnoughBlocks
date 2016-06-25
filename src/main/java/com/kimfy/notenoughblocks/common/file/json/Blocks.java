package com.kimfy.notenoughblocks.common.file.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.FileUtilities;
import net.minecraft.util.ResourceLocation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Blocks
{
    public static List<BlockJson> blockList = new ArrayList<>();
    
    public static void loadBlocks()
    {

        ResourceLocation blocksFile = new ResourceLocation(Constants.MOD_ID, "json/blocks.json");
        InputStream stream = FileUtilities.getFile(blocksFile);
        if (stream != null)
        {
            Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, new ModJsonDeserializer()).disableHtmlEscaping().create();
            Map<String, List<BlockJson>> blockMap = gson.fromJson(FileUtilities.getContent(stream), Map.class);
            blockList = blockMap.get("blocks");
        }
        else
        {
            throw new NullPointerException("assets/notenoughblocks/json/blocks.json was not found. Report this to the mod author!");
        }
    }
}
