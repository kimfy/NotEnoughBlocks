package com.kimfy.notenoughblocks.common.file;

import com.kimfy.notenoughblocks.NotEnoughBlocks;
import com.kimfy.notenoughblocks.common.file.resourcepack.ResourcePack;
import com.kimfy.notenoughblocks.common.util.Constants;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManager
{
    public static List<String> textures = new ArrayList<>();
    public static Logger logger = NotEnoughBlocks.logger;
    public static File configFolder = new File(Constants.PATH_MOD_CONFIG_BASE);
    public static File configResourcePacksFolder = new File(Constants.PATH_MOD_CONFIG_RESOURCE_PACKS);
    public static File configJsonFolder = new File(Constants.PATH_MOD_CONFIG_JSON);
    public static File blockTexturesFolder = new File(Constants.PATH_MOD_TEXTURES_BLOCKS);

    public void makeDirectories()
    {
        if (!configResourcePacksFolder.exists() || !configJsonFolder.exists() || !blockTexturesFolder.exists())
        {
            configResourcePacksFolder.mkdirs();
            configJsonFolder.mkdirs();
            blockTexturesFolder.mkdirs();
        }
    }

    public void findAndProcessResourcePacks()
    {
        logger.info("Searching {} for resource packs", configResourcePacksFolder.getAbsolutePath());
        for (File file : configResourcePacksFolder.listFiles())
        {
            /* Only process files that end with .zip and are valid filenames.
             * If the test passes, we can be certain that the file we're
             * working with is an actual resource pack
             */
            boolean isFileNameValid = isValidFilename(file);
            if (isZipFile(file) && isFileNameValid)
            {
                String fileName = file.getName().replace(".zip", "");
                logger.info("Found resource pack: " + fileName);

                ResourcePack resourcePack = new ResourcePack(file, fileName);
                    resourcePack.extract();
                    resourcePack.move();
                    resourcePack.setBlocks();
                    resourcePack.writeJson();
            }
            else if (!isFileNameValid)
            {
                logger.error("Found resource pack \"{}\" but it's name is invalid. Use only characters/underscores/dashes/numbers.", file.getName());
            }
        }
    }

    /**
     * Performs a check on the file that it's filename does not contain
     * any whitespace or colons. They are invalid filenames.
     *
     * @param file The file to perform this action on
     * @return True if the given file's filename does not contain any whitespace( ) or colons(:)
     */
    private boolean isValidFilename(File file)
    {
        return !file.getName().replace(".zip", "").matches("(?s).*[: ].*");
    }

    private boolean isZipFile(File file)
    {
        return file.getName().endsWith(".zip");
    }
}
