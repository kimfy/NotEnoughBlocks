package com.kimfy.notenoughblocks.common.util;

import com.kimfy.notenoughblocks.NotEnoughBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtilities
{
    private static Logger logger = NotEnoughBlocks.logger;

    /**
     * @author Ilias Tsagklis
     * @link http://examples.javacodegeeks.com/core-java/util/zip/extract-zip-file-with-subdirectories/
     * @param zip The zip file to extract
     * @param zipPath The destination folder to extract this zip to
     * @return The folder it extracted to
     */
    public static File extractZip(File zip, String zipPath) {
        // String zipPath = zip.getParent() + "/extracted";
        File temp = new File(zipPath);
        temp.mkdir();

        ZipFile zipFile = null;

        try {
            zipFile = new ZipFile(zip);

            // get an enumeration of the ZIP file entries
            Enumeration<? extends ZipEntry> e = zipFile.entries();

            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();

                File destinationPath = new File(zipPath, entry.getName());

                // create parent directories
                destinationPath.getParentFile().mkdirs();

                // if the entry is a file extract it
                if (entry.isDirectory()) {
                    continue;
                } else {
                    //LOG.info("Extracting file: " + destinationPath);

                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

                    int b;
                    byte buffer[] = new byte[1024];

                    FileOutputStream fos = new FileOutputStream(destinationPath);

                    BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

                    while ((b = bis.read(buffer, 0, 1024)) != -1) {
                        bos.write(buffer, 0, b);
                    }

                    bos.close();
                    bis.close();
                }
            }
        } catch (IOException e) {
            logger.error("Error opening zip file" + e);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
                logger.error("Error while closing zip file" + e);
            }
        }
        return temp;
    }

    public static IResourceManager getResourceManager()
    {
        return Minecraft.getMinecraft().getResourceManager();
    }

    /**
     * DOES NOT WORK FOR FILES PUT IN RESOURCE LOADER FOLDER - ONLY FOR RESOURCE PACKS AND SRC/MAIN/RESOURCES
     *
     * Expected input: getFileAsString(new ResourceLocation("modid", "path/to/file.extension"));
     * Base directory would be assets/modid/ - if your file is located in assets/modid/json/file.json
     * you'd call getFileFromAssets(RL(modid, "json/file.json"));
     */
    public static String getFileFromAssets(ResourceLocation resourceLocation)
    {
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            IResource iResource = getResourceManager().getResource(resourceLocation);

            int ch;
            while ((ch = iResource.getInputStream().read()) != -1)
            {
                stringBuilder.append((char) ch);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String getFileFromAssets(String path)
    {
        return getFileFromAssets(new ResourceLocation(Constants.MOD_ID, path));
    }

    public static boolean exists(ResourceLocation resourceLocation)
    {
        try
        {
            getResourceManager().getResource(resourceLocation);
            return true;
        }
        catch (IOException e)
        {
            if (e instanceof FileNotFoundException)
            {
                return false;
            }
            e.printStackTrace();
        }
        return false;
    }

    public static boolean exists(String path)
    {
        return exists(new ResourceLocation(Constants.MOD_ID, path));
    }

    public static void write(File file, String content)
    {
        if (file.exists())
        {
            NotEnoughBlocks.logger.info("File: " + file.getName() + " already exists in: " + file.getPath());
            return;
        }

        try
        {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
