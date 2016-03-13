package com.kimfy.notenoughblocks.common.util.endercore.core.common.util;

import com.google.common.io.Files;
import com.kimfy.notenoughblocks.NotEnoughBlocks;
import com.kimfy.notenoughblocks.common.util.FileUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FileResourcePack;

import java.io.File;
import java.io.IOException;

public class ResourcePackAssembler
{
    private final File root;
    private final File finalResourcePack;
    private final String MC_META = "{\"pack\": {\"pack_format\": 1, \"description\": \"NotEnoughBlocks\"}}";

    private File packMcMeta;

    private boolean exists = false;

    public ResourcePackAssembler(String folderToZip, String name)
    {
        if (new File("resourcepacks/" + name + ".zip").exists())
        {
            exists = true;
            NotEnoughBlocks.logger.info("Resource pack: " + name + " already exists");
        }

        this.root = new File(folderToZip);

        this.finalResourcePack = new File(name + ".zip");
        if (!finalResourcePack.exists())
        {
            try
            {
                finalResourcePack.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        assemblePackMcMeta();
    }

    private void assemblePackMcMeta()
    {
        this.packMcMeta = new File(root.getAbsolutePath() + "/pack.mcmeta");
        FileUtilities.write(packMcMeta, MC_META);
    }

    // usage put(new File("textures/brick.png", "assets/notenoughblocks/textures/blocks"))
    public void put(File from, String to)
    {
        String fullDest = root.getAbsolutePath() + "/" + to; // destination has to include file extension
        try
        {
            Files.move(from, new File(fullDest));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // usage putAll(new File("textures"), "assets/notenoughblocks/textures/blocks")
    public void putAll(File dir, String destinationDirectory)
    {
        String destination = root.getAbsolutePath() + "/" + destinationDirectory;
        File destinationDir = new File(destination);
        if (!destinationDir.exists())
        {
            destinationDir.mkdirs();
        }

        for (File f : dir.listFiles())
        {
            put(f, destinationDirectory + "/" + f.getName());
        }
    }

    public void assembleAndInject()
    {
        if (exists) return;

        try
        {
            EnderFileUtils.zipFolderContents(root, finalResourcePack);
            File resourcePack = new File("resourcepacks/" + finalResourcePack.getName());
            Files.move(finalResourcePack, resourcePack);
            inject(resourcePack);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void inject(File resourcePack)
    {
        Minecraft.getMinecraft().defaultResourcePacks.add(new FileResourcePack(resourcePack));
    }
}
