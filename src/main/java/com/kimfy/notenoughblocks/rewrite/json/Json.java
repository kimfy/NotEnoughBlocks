package com.kimfy.notenoughblocks.rewrite.json;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.kimfy.notenoughblocks.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Json
{
    private File jsonFile;
    @Getter
    private POJO data;
    private Gson gson = new Gson();
    @Setter @Getter private String name;
    public Logger logger = LogManager.getLogger(Constants.MOD_NAME);
    
    public Json(File file)
    {
        this.jsonFile = file;
        this.read();
        this.name = file.getName().replace(".json", "");
    }
    
    /**
     * Reads in the JSON file associated with this Object. 
     * There's some basic error handling in there. If I had a custom
     * serializer I'd put in more error handling but that'll have to
     * come at a later stage.
     * @return
     */
    protected POJO read()
    {
    	try {
			this.data = gson.fromJson(new FileReader(jsonFile), POJO.class);
			return data;
		}
    	
    	catch (JsonSyntaxException e) 
    	{
    		StringBuilder msg = new StringBuilder();
    		msg.append("Error reading JSON file: " + this.getName() + ".\n");
    		msg.append("Your JSON file is most likely invalid. Go to `jsonlint.com` to validate your file.\n");
    		msg.append("If you continue to get this error after your JSON file was corrected, please contact\n");
    		msg.append("the mod author on either GitHub, Curse or MinecraftForum.");
    		logger.error(msg.toString());
    		logger.error(e.getCause().getMessage());
			e.printStackTrace();
		}
    	catch (JsonIOException e) 
    	{
			StringBuilder msg = new StringBuilder();
			msg.append("Error reading JSON file: " + this.getName() + ".\n");
			msg.append("There was a problem reading your JSON file.");
			msg.append("It either has no permission to open. Report to mod author");
			logger.error(msg.toString());
			logger.error(e.getCause().getMessage());
			e.printStackTrace();
		}
    	
    	catch (FileNotFoundException e) 
    	{
			StringBuilder msg = new StringBuilder();
			msg.append("Error reading JSON file: " + this.getName() + ".\n");
			msg.append("The file was not found. Try reloading the game. If that does not help, contact mod author");
			logger.error(msg.toString());
			logger.error(e.getCause().getMessage());
			e.printStackTrace();
    	}
    	return null;
    }
}
