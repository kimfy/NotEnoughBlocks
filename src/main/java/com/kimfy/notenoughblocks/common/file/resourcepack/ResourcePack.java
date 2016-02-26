package com.kimfy.notenoughblocks.common.file.resourcepack;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kimfy.notenoughblocks.NotEnoughBlocks;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.file.json.Blocks;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.FileUtilities;
import com.kimfy.notenoughblocks.common.util.Utilities;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResourcePack
{
    public final File file;
    public final String name;

    public ResourcePack(File file, String name)
    {
        this.file = file;
        this.name = name;
    }

    private List<BlockJson> blocks = new ArrayList<>();
    private static Logger logger = NotEnoughBlocks.logger;

    public void setBlocks()
    {
        List<BlockJson> temp = Utilities.deepCloneList(Blocks.blockList);
        List<BlockJson> tempBlockList = new ArrayList<>();

        for (BlockJson block : temp)
        {
            Map<String, String> textureMap = block.getTextureMap();
            Map<String, String> tempMap = new LinkedHashMap<>();

            for (Map.Entry<String, String> entry : textureMap.entrySet())
            {
                String side = entry.getKey();
                String texture = entry.getValue();

                tempMap.put(side, this.name + "_" + texture);
            }

            block.textures.clear();
            block.textures.putAll(tempMap);

            if (BlockJson.exists(block))
            {
                tempBlockList.add(block);
            }
            else
            {
                logger.info("Block " + block.getDisplayName() + " does not exist with textures " + block.getTextureMap());
            }
        }
        this.blocks.addAll(tempBlockList);
    }

    public void extract()
    {
        FileUtilities.extractZip(file, Constants.PATH_MOD_CONFIG_RESOURCE_PACKS + name + "_extracted");
    }

    public void move()
    {
        File temp = new File(Constants.PATH_MOD_CONFIG_RESOURCE_PACKS + name + "_extracted/assets/minecraft/textures/blocks/");
        String destination = Constants.PATH_MOD_TEXTURES_BLOCKS;
        logger.info("Moving from " + temp.getAbsolutePath() + " to " + destination);

        if (temp.exists())
        {
            for (File file : temp.listFiles())
            {
                if (file.isFile())
                {
                    try
                    {
                        File destinationFile = new File(destination + name + "_" + file.getName());
                        Files.move(file, destinationFile);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            try
            {
                FileUtils.deleteDirectory(new File(Constants.PATH_MOD_CONFIG_RESOURCE_PACKS + name + "_extracted"));
                this.selfDestruct();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            logger.info("Resource Pack " + name + " has been extracted, but the folder does not exist. REPORT THIS!");
        }
    }

    /**
     * Appends '.disabled' at the end of the filename
     */
    private void selfDestruct()
    {
        try
        {
            String destinationPath = file.getAbsolutePath() + ".disabled";
            File destinationFile = new File(destinationPath);
            Files.move(file, destinationFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void writeJson()
    {
        String destination = Constants.PATH_MOD_CONFIG_JSON + name + ".json";
        File temp = new File(destination);

        if (!temp.exists())
        {
            try
            {
                if (temp.createNewFile())
                {
                    /* Write to it */
                    Map<String, List<BlockJson>> blockMap = new LinkedHashMap<>();
                    blockMap.put("blocks", blocks);
                    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

                    try
                    {
                        FileWriter fileWriter = new FileWriter(temp);
                        String toWrite = gson.toJson(blockMap);
                        fileWriter.write(toWrite);
                        fileWriter.flush();
                        fileWriter.close();
                    }
                    catch (IOException e)
                    {
                        logger.error("Something went wrong when writing to json file for $name");
                        e.printStackTrace();
                    }
                }
                else
                {
                    logger.error("Something went wrong when creating json file for " + name);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
