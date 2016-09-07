/**
 * Reads Baloo Task JSON.
 * Update structure of this json to sim5runner compatible format
 * Split baloo actions into sim5framework actions
 * Add splitted actions into task json
 * Export updated task json
 *
 * @author piyush
 * @param Folder containing Baloo Task JSONs
 * @return Final Task JSON
 */
package com.cmp.taskcreation;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.cmp.taskcreation.base.taskdata.TaskReader;
import com.cmp.utils.JSONFileWriter;
import com.cmp.utils.ReadFromFile;

public class Runner {
	

	/**
	 * 
	 * @param args
	 */
	public JSONObject getTaskJSONString(String TaskId){
		JSONObject taskOb =null;
		JSONObject response = new JSONObject();
		try {
			System.out.println("************\n\nCreating Task json for " + TaskId + "\n");
			/**
			 * Read baloo task json and get json loaded into jsonobject
			 */
			taskOb = new TaskReader().getTaskObject(TaskId);
			
			String str_msg="";
			
			JSONObject message = (JSONObject)taskOb.get("message");
        	if(message != null){
        		str_msg = (String)message.get("name");
        	}
        	
			if(taskOb != null && !str_msg.equalsIgnoreCase("NotFound")){
        		//Get Item list array in a task
    			JSONArray itemSet = (JSONArray)taskOb.get("items");
    			System.out.println("itemSet size: " + itemSet.size());
    			
    			System.out.println("Adding framework actions...");
    			
    			for(int itemNum=0; itemNum<itemSet.size();itemNum++)
    			{
    				System.out.println("Item:" + (itemNum+1));
    				JSONArray updatedMethodSet = new JSONArray();
    				
    				System.out.println("in item num: " + itemNum);
    				JSONObject itemOb = (JSONObject)itemSet.get(itemNum);
    				

    				// Get Method list array in an Item
    				JSONArray methodSet = (JSONArray)itemOb.get("methods");
    				System.out.println("methodSet size: " + methodSet.size());
    				
    				//Iterate item method
    				for(int methNum=0; (methNum<methodSet.size() && methNum<7);methNum++)
    				{
    					System.out.println("\tMethod:" + (methNum+1));
    					JSONObject methodOb = (JSONObject)methodSet.get(methNum);

    					/**
    					 * 1. Send current method object 
    					 * 2. Add baloo actions
    					 * 3. Divide each baloo action and add divided actions in SIMS IDE compatible format
    					 */
    					MethodUpdate updateBalooMethod = new MethodUpdate(methodOb);
    					methodOb = updateBalooMethod.addSplittedActions();
    					
    					// Add updated method to updated method set
    					updatedMethodSet.add(methodOb);
    				}
    				
    				itemOb.put("methods", updatedMethodSet);
    			}
    			response.put("status", "200");
        		response.put("type", "content/json");        		
        		response.put("message", "success");
        		response.put("content", taskOb);
        	}else{
        		response.put("status", "400");
        		response.put("type", "error");        		
        		response.put("message", message.get("message"));
        	}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return response;
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		
		String outputFileName = "outputJson.json";
		String taskListFilename = "tasklist.txt";
		String[] taskList = {"local"};
		
		try {
			
			/**
			 * Read tasklist.txt and get list of tasks to be converted to framework task jsons
			 */
			
			try{
				 String fileContent = new ReadFromFile().readFile(taskListFilename);
				 if(fileContent != null && fileContent.length() !=0)
				 { 
					 taskList = fileContent.split("\n");
				 }
			}
			catch(Exception e){
				System.err.println("Error in reading file - 'tasklist.txt'");
				e.printStackTrace();
				System.exit(0);
			}
			
			for(String taskID : taskList)
			{
				
				System.out.println("************\n\nCreating Task json for " + taskID + "\n");
				/**
				 * Read baloo task json and get json loaded into jsonobject
				 */
	            	JSONObject taskOb = new TaskReader().getTaskObject(taskID);
	            	
	    			//Get Item list array in a task
	    			JSONArray itemSet = (JSONArray)taskOb.get("items");
//	    			System.out.println("itemSet size: " + itemSet.size());
	    			
//	    			System.out.println("Adding framework actions...");
	    			
	    			for(int itemNum=0; itemNum<itemSet.size();itemNum++)
	    			{
	    				System.out.println("Item:" + (itemNum+1));
	    				JSONArray updatedMethodSet = new JSONArray();
	    				
//	    				System.out.println("in item num: " + itemNum);
	    				JSONObject itemOb = (JSONObject)itemSet.get(itemNum);
	    				

	    				// Get Method list array in an Item
	    				JSONArray methodSet = (JSONArray)itemOb.get("methods");
//	    				System.out.println("methodSet size: " + methodSet.size());
	    				
	    				//Iterate item method
	    				for(int methNum=0; (methNum<methodSet.size() && methNum<7);methNum++)
	    				{
	    					System.out.println("\tMethod:" + (methNum+1));
	    					JSONObject methodOb = (JSONObject)methodSet.get(methNum);

	    					/**
	    					 * 1. Send current method object 
	    					 * 2. Add baloo actions
	    					 * 3. Divide each baloo action and add divided actions in SIMS IDE compatible format
	    					 */
	    					MethodUpdate updateBalooMethod = new MethodUpdate(methodOb);
	    					methodOb = updateBalooMethod.addSplittedActions();
	    					
	    					// Add updated method to updated method set
	    					updatedMethodSet.add(methodOb);
	    				}
	    				
	    				itemOb.put("methods", updatedMethodSet);
	    			}
	    			
	        		// write json object to json file
	            	outputFileName = (String)taskOb.get("id");
	            	outputFileName = outputFileName.replaceAll("\\.", "_");
	            	outputFileName = outputFileName + "_" + (String)taskOb.get("scenario") + ".json";
	        		new JSONFileWriter().writeJsonToFile(taskOb, ("output/" + outputFileName));
	        		
	    			System.out.println("\nTask json file created in output folder !!\n");
	        		System.out.println("************\n\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
