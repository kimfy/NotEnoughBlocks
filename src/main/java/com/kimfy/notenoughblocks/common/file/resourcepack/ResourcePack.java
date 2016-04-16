package com.kimfy.notenoughblocks.common.file.resourcepack;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.kimfy.notenoughblocks.common.file.FileManager;
import com.kimfy.notenoughblocks.common.file.json.BlockJson;
import com.kimfy.notenoughblocks.common.file.json.Blocks;
import com.kimfy.notenoughblocks.common.util.Constants;
import com.kimfy.notenoughblocks.common.util.FileUtilities;
import com.kimfy.notenoughblocks.common.util.Log;
import com.kimfy.notenoughblocks.common.util.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResourcePack
{
    public File file;
    public String name;

    public ResourcePack(File file, String name)
    {
        this.file = file;
        this.name = name;
    }

    private FolderResourcePack resourcePack;

    private static final String PACK_MC_META = "{\n\t\"pack\": {\n\t\t\"pack_format\": 1,\n\t\t\"description\": \"Resources for " + Constants.MOD_NAME + "\"\n\t}\n}";

    public ResourcePack(String folder, String name)
    {
        this.resourcePack = new FolderResourcePack(new File(folder));
        this.name = name;
        injectPackMcMeta();
        injectResourcePack();
    }

    private void injectPackMcMeta()
    {
        FileUtilities.write(new File("resourcepacks/" + name + "/pack.mcmeta"), PACK_MC_META);
    }

    @SuppressWarnings("unchecked")
    private void injectResourcePack()
    {
        final String DEOBFUSCATED_NAME = "defaultResourcePacks";
        final String OBFUSCATED_NAME   = "field_110449_ao";

        try
        {
            ((List<IResourcePack>)ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), DEOBFUSCATED_NAME, OBFUSCATED_NAME)).add(resourcePack);
        }
        catch (ReflectionHelper.UnableToAccessFieldException e)
        {
            Log.error("[NotEnoughBlocks] Error when injecting resource pack. Did the deobfuscated name {} or the obfuscated name {} change?", DEOBFUSCATED_NAME, OBFUSCATED_NAME,  e);
        }

        Minecraft.getMinecraft().refreshResources();
    }

    private List<BlockJson> blocks = new ArrayList<>();

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

            block.getTextures().clear();
            block.getTextures().putAll(tempMap);
            //block.textures.clear();
            //block.textures.putAll(tempMap);

            if (BlockJson.exists(block))
            {
                tempBlockList.add(block);
            }
            else
            {
                Log.info("Block: " + block.getDisplayName() + " cannot be created from resource pack as not all the required textures for it was found. Textures needed for block to be created: " + block.getTextureMap());
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
        Log.info("Moving from " + temp.getAbsolutePath() + " to " + destination);

        if (temp.exists())
        {
            for (File file : temp.listFiles())
            {
                if (file.isFile())
                {
                    try
                    {
                        String textureName = name + "_" + file.getName();
                        FileManager.textures.add(textureName.replace(".png", ""));
                        File destinationFile = new File(destination + textureName/*name + "_" + file.getName()*/);
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
            Log.info("Resource Pack " + name + " has been extracted, but the folder does not exist. REPORT THIS!");
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
                    Gson gson = Utilities.GSON;

                    Map<String, List<BlockJson>> resourcePackMap = new LinkedHashMap<>(1);
                    resourcePackMap.put("blocks", blocks);

                    FileWriter fileWriter = new FileWriter(temp);
                    fileWriter.write(gson.toJson(resourcePackMap));
                    fileWriter.flush();
                    fileWriter.close();
                }
                else
                {
                    Log.error("Something went wrong when creating json file for " + name);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
