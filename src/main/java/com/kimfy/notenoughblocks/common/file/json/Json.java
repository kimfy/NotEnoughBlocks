package com.kimfy.notenoughblocks.common.file.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kimfy.notenoughblocks.NotEnoughBlocks;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Json
{
    private File file;
    private List<BlockJson> blocks = new ArrayList<>();
	private String name;
    
    public Json(File file, String name)
    {
        this.file = file;
        this.name = name;
    }
    
    /**
     * Reads in the JSON file associated with this Object. 
     * There's some basic error handling in there. If I had a custom
     * serializer I'd put in more error handling but that'll have to
     * come at a later stage.
     */
    @SuppressWarnings("ALL")
    public void read()
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Map.class, new ModJsonDeserializer());
        Gson gson = gsonBuilder.create();

        try
        {
            FileReader fileReader = new FileReader(file);
            Map json = gson.<Map>fromJson(fileReader, Map.class);
            blocks = (List<BlockJson>) json.get("blocks");
        }
        catch (Exception e)
        {
            NotEnoughBlocks.logger.error("Something went really wrong when parsing {}!", file.getName(), e);
        }
        return;
    }
}
