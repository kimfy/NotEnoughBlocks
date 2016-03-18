package com.kimfy.notenoughblocks.rewrite.file;

import com.kimfy.notenoughblocks.rewrite.json.JsonWriter;
import com.kimfy.notenoughblocks.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used for finding, storing, extracting and moving
 * resource packs that needs processing. Use {@link FileProcessor#resourcePacks}
 * if you need access to the resource packs. Stored as {@link ResourcePack}
 */
public class FileProcessor
{
    private static final File BASE_DIR = new File(Constants.BASE_PATH);
    private static final File JSON_DIR = new File(Constants.JSON_PATH);
    private static final File RESOURCE_PACKS_DIR = new File(Constants.RESOURCE_PACKS_PATH);
    private static final File RESOURCE_LOADER_NEB_DIR = new File(Constants.RESOURCE_LOADER_NEB_PATH);

    /**
     * Stores every resource pack found in {@link FileProcessor#RESOURCE_PACKS_DIR}
     * and saves a reference to the File and checks {@link FileProcessor#JSON_DIR}
     * if the resource pack has a JSON file generated already. <br>
     * <h3>How it works:</h3>
     * <p>
     * JohnSmith.zip is in {@link FileProcessor#RESOURCE_PACKS_DIR}, NEB
     * checks to see if JohnSmith.json exists in {@link FileProcessor#JSON_DIR}.
     * If it does exist, set the V(Boolean) to true, else it sets it to false.</p>
     */
    public static List<ResourcePack> resourcePacks = new ArrayList<>();
    public static List<File> textures;

    private static final Logger logger = LogManager.getLogger(Constants.MOD_NAME + ":FileProcessor");

    public static void initialize()
    {
        // Create necessary directories if they don't already exist
        makeDirectories();

        // Find all resource packs and store them where appropriate
        findResourcePacks();

        // Process every resource pack - extract the ones that need to be
        // extracted
        processResourcePacks();

        // Now that we have found, stored, extracted moved and prefixed
        // every single resource pack that needed to be processed
        // we can move on to reading from the JSON directory
        // See {@link NEB}
    }

    private static void processResourcePacks()
    {
        if (!resourcePacks.isEmpty())
        {
            for (ResourcePack pack : resourcePacks)
            {
                if (pack.extract())
                {
                    pack.move();

                    FileProcessor.textures = Arrays.asList((new File(Constants.RESOURCE_LOADER_NEB_PATH).listFiles()));

                    pack.getExistingBlocks();

                    pack.mkJSON();

                    if (pack.selfDestruct())
                    {
                        JsonWriter writer = new JsonWriter();
                        writer.write(pack);
                    }
                }
            }
        }
        else
        {
            logger.info("No resource packs were found");
        }
    }

    /**
     * Scans {@link FileProcessor#RESOURCE_PACKS_DIR} for resource packs
     * and stores them in {@link FileProcessor#resourcePacks}
     */
    private static void findResourcePacks()
    {
        for (File file : RESOURCE_PACKS_DIR.listFiles())
        {
            if (isZip(file) && isValidFilename(file))
            {
                String fileName = file.getName().replace(".zip", "");

                ResourcePack resourcePack = new ResourcePack(file, fileName);

                resourcePacks.add(resourcePack);
                logger.info("Found and added: " + resourcePack.getName() + " to the resourcePacks registry.");
            }
            else if (!isValidFilename(file))
            {
                logger.error("File: " + file.getName());
                logger.error("The name of this resource pack is not valid. Make sure there are no spaces or colons in the filename.");
            }
        }
    }

    private static boolean isZip(File file)
    {
        return file.getName().endsWith(".zip");
    }

    /**
     * Performs a regex check on the filename - checking if there are any
     * spaces or colons in the filename.
     *
     * @param file the file to perform this action on
     * @return true if the filename does not contain any spaces( ) or colons(:)
     */
    private static boolean isValidFilename(File file)
    {
        String fileName = file.getName();
        Pattern regex = Pattern.compile("(?:[ :])");
        Matcher matcher = regex.matcher(fileName);
        return !matcher.find();
    }

    private static void makeDirectories()
    {
        try
        {
            if (!BASE_DIR.exists())
            {
                BASE_DIR.mkdirs();
            }
            if (!JSON_DIR.exists())
            {
                JSON_DIR.mkdirs();
            }
            if (!RESOURCE_PACKS_DIR.exists())
            {
                RESOURCE_PACKS_DIR.mkdirs();
            }
            if (!RESOURCE_LOADER_NEB_DIR.exists())
            {
                RESOURCE_LOADER_NEB_DIR.mkdirs();
            }
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }
    }
}
