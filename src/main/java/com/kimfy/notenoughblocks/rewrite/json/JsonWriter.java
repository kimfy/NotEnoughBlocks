package com.kimfy.notenoughblocks.rewrite.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kimfy.notenoughblocks.rewrite.file.ResourcePack;
import com.kimfy.notenoughblocks.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonWriter
{
	
    private static final Logger LOG = LogManager.getLogger(Constants.MOD_NAME + ":JsonBlocks:Writer");
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private String output;
	
	public JsonWriter() {}
	
	public void write(ResourcePack pack)
	{
		File jsonFile = pack.getJSON();
		String toWrite = constructJSON(pack.getBlocks());
		
		try {

			FileWriter fileWriter = new FileWriter(jsonFile);
			
			fileWriter.write(toWrite);
			fileWriter.flush();
			fileWriter.close();
			/* jsonFile.setReadOnly(); */
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * TODO: This needs to be done in a not so fucked up way
	 * @param blocks A List of blocks to make a JSON file out of
	 * @return Json file content as a String
	 */
	public String constructJSON(List<JsonBlock> blocks)
	{
        this.gson  = new GsonBuilder().setPrettyPrinting().create();
		this.output = "{\n\"blocks\" : ";
		this.output = output.concat(gson.toJson(blocks));
		this.output = output.concat("\n}");
		return output;
	}
}
