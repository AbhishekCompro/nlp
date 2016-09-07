package com.cmp.utils;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

public class JSONFileWriter {
	
	public void writeJsonToFile(JSONObject jsonObj, String jsonFilename){
		FileWriter file = null;
		
				try {
					file = new FileWriter(jsonFilename);
					file.write(jsonObj.toJSONString());
//					System.out.println("Successfully Copied JSON Object to File..." + jsonFilename);
//					System.out.println("\nJSON Object: " + jsonObj);
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally{
					try {
						file.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	}
}
