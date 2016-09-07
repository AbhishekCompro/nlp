package com.cmp.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReader {
	
	private String jsonFilePath = "";
	
	public JSONReader(String jsonFilePath){
		this.jsonFilePath = jsonFilePath;
	}
	
  public JSONObject getJSONAsObject() {

      JSONParser jsonParser = new JSONParser();

      try {

    	  FileReader fileReader = new FileReader(jsonFilePath);
          JSONObject jsonFileAsObject = (JSONObject) jsonParser.parse(fileReader);
          
          return jsonFileAsObject;

      } catch (FileNotFoundException e) { 
          e.printStackTrace();

      } catch (IOException e) {
          e.printStackTrace();

      } catch (ParseException e) {
          e.printStackTrace();
      }
	return null;

  }



}
