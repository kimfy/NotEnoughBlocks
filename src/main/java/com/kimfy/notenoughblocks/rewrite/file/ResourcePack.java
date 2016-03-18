package com.kimfy.notenoughblocks.rewrite.file;

import com.google.common.io.Files;
import com.kimfy.notenoughblocks.rewrite.json.JsonBlock;
import com.kimfy.notenoughblocks.rewrite.json.JsonBlocks;
import com.kimfy.notenoughblocks.util.Constants;
import com.kimfy.notenoughblocks.util.Utilities;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResourcePack
{
    @Getter
    private File resourcePack;
    
    @Getter
    private String name;
    
    @Getter
    private String extension = ".zip";
    
    @Setter
    @Getter
    private String path;
    
    @Setter
    @Getter
    private List<JsonBlock> blocks = new ArrayList<JsonBlock>(JsonBlocks.blockRegistry.size());
    
    private static final Logger LOG = LogManager.getLogger(Constants.MOD_NAME + ":ResourcePack");
    
    public ResourcePack(File resourcePack, String name)
    {
        this.resourcePack = resourcePack;
        this.name = name;
        this.setPath(resourcePack.getAbsolutePath());
        this.setBlocks(Utilities.deepCloneList(JsonBlocks.blockRegistry));
    }

    public String getExtractedAbsolutePath()
    {
        return Constants.RESOURCE_PACKS_PATH + this.getName() + "_extracted/";
    }
    
    public boolean extract()
    {
        String destination = Constants.RESOURCE_PACKS_PATH + this.getName() + "_extracted";

        this.extractZip(resourcePack, destination);
        return true;
    }
    
    /**
     * Moves the textures in "resourcepackname_extracted" folder to it's correct
     * location {@link Constants#RESOURCE_LOADER_NEB_PATH}
     */
    public void move()
    {
        File extractedTexturesPath = new File(this.getExtractedAbsolutePath() + "assets/minecraft/textures/blocks/");
        String destination = Constants.RESOURCE_LOADER_NEB_PATH;
        LOG.info("Moving from: " + extractedTexturesPath.getAbsolutePath());
        
        // For every file in the extracted directory
        // Move to resources/modid/textures/blocks/
        // but prefix with this.name
        for (File f : extractedTexturesPath.listFiles()) {
            try {
                // LOG.info("Moving: " + f.getAbsolutePath());
                Files.move(f, new File(destination + this.getName() + "_" + f.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try {
            FileUtils.deleteDirectory(new File(this.getExtractedAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info("Done moving!");
    }
    
    public boolean mkJSON()
    {
        String destination = Constants.JSON_PATH + this.getName() + ".json";
        
        if (!new File(destination).exists()) {
            
            try {
                return new File(destination).createNewFile();
                
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
    
    public boolean hasJSON()
    {
    	return new File(Constants.JSON_PATH + this.getName() + ".json").exists();
    }
    
    public File getJSON()
    {
    	return new File(Constants.JSON_PATH + this.getName() + ".json");
    }
    
    /**
     * Changes the name of this resource pack to pack.zip.disabled
     * so we don't have to deal with it on every single launch.
     * @return
     */
    public boolean selfDestruct()
    {
        try {
            
            String dest = this.getPath() + ".disabled";
            File d = new File(dest);
            Files.move(resourcePack, d);
            return true;
            
        } catch (IOException | IllegalArgumentException e) {
            
            e.printStackTrace();
            
        }
        
        return false;
    }
    
    private int registrySize = 0;
    private List<JsonBlock> blockRegistry = new ArrayList<JsonBlock>();
    
    /**
     * Loops through the blockRegistry with the intention
     * of returning a List<> containing JsonBlock's. The
     * outcome depends if the entry in the blockRegistry
     * exists on disk. In shorter words; it checks all
     * entries if they have their textures.
     * <h3>Note:</h3>
     * This method also prefixes every entry with the
     * resource pack name so that we don't have to
     * worry about prefixing later on.
     * 
     * @return A List of JsonBlock's that exist on disk.
     */
    public List<JsonBlock> getExistingBlocks()
    {
        List<JsonBlock> prefixedBlockRegistry = new ArrayList<JsonBlock>();
        String[] textures;
        String[] tmpTextures;
        
        for (JsonBlock block : this.blocks)
        {
            
            textures = block.getBlockTextures();
            if (block.getShape() == JsonBlock.Shape.BED)
            {
                tmpTextures = new String[7];
                for (int i = 0; i < 7; i++)
                {
                    tmpTextures[i] = this.getName() + "_" + textures[i];
                    //LOG.info("Converted: " + textures[i] + " >>> " + tmpTextures[i]);
                }
            }
            else
            {
                tmpTextures = new String[6];
                for (int i = 0; i < 6; i++)
                {
                    tmpTextures[i] = this.getName() + "_" + textures[i];
                    //LOG.info("Converted: " + textures[i] + " >>> " + tmpTextures[i]);
                }
            }
            
            block.setBlockTextures(tmpTextures);
            
            // Perform a check on this block
            // to see if it's textures exists
            if (block.exists())
            {
                prefixedBlockRegistry.add(block);
            }
        }
        this.setBlocks(prefixedBlockRegistry);
        return prefixedBlockRegistry;
    }

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
            LOG.error("Error opening zip file" + e);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
                LOG.error("Error while closing zip file" + e);
            }
        }
        return temp;
    }
}
