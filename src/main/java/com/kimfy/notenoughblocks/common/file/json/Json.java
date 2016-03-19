package com.kimfy.notenoughblocks.common.file.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kimfy.notenoughblocks.common.util.Constants;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Json
{
    private File file;
    @Getter
    private List<BlockJson> blocks = new ArrayList<>();
    private Gson gson = new Gson();
    @Getter
	private String name;
    public Logger logger = LogManager.getLogger(Constants.MOD_NAME);
    
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
    public void read()
    {
        try
        {
            FileReader fileReader = new FileReader(file);
            Map<String, List<BlockJson>> temp;
            Type type = new TypeToken<Map<String, List<BlockJson>>>(){}.getType();
            temp = gson.fromJson(fileReader, type);
            blocks = temp.get("blocks");

            //if (name.equals("Developer"))
            //{
            //    Gson builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            //    NotEnoughBlocks.logger.info("\nDeveloper.json: \n" + builder.toJson(temp, type));
            //}
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
