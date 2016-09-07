package com.cmp.taskcreation.base.taskdata;

import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cmp.utils.GetHTTPResponse;

public class TaskReader {
	
	@SuppressWarnings("unchecked")
	public JSONObject getTaskObject(String taskID) throws IOException, ParseException {
		JSONObject jsonRoot = null;

		try {     
				jsonRoot =  (JSONObject) new GetHTTPResponse().getResponse(taskID);			
				
				System.out.println("Baloo Task JSON in Framework format:\n" + jsonRoot.toJSONString());

				if(jsonRoot != null){
					String id[] = jsonRoot.get("friendlyId").toString().split("\\.");
					
					addJsonKeys(jsonRoot,"init:true","source:nlp","version:1.0","name:NOT_FOUND");
					
					if(id.length > 1)
						addJsonKeys(jsonRoot,"scenario:"+id[id.length-1],"appName:"+id[1]);
					
					deleteJsonKeys(jsonRoot,"taskId","_id","type","phase","createdTimestamp","updatedBy","createdBy","__t","difficulty",
							"documentVersion","documents","threads","updatedTimestamp","__v","isActive","levelOfRevision");

					JSONArray jsonSteps =  (JSONArray) jsonRoot.get("steps");	            
					JSONObject jsonStepNum;
					JSONArray jsonMethods,jsonActions;
					JSONObject jsonMethodNum;
					
					Iterator<JSONObject> step = jsonSteps.iterator();
					
					while (step.hasNext()) {
						jsonStepNum = (JSONObject) step.next();
						deleteJsonKeys(jsonStepNum,"skills","_id","threads");
						addJsonKeys(jsonStepNum,"init:true","skip:false");
						jsonMethods =  (JSONArray) jsonStepNum.get("methods");
						Iterator<JSONObject> method = jsonMethods.iterator();
						while (method.hasNext()) {
							jsonMethodNum = (JSONObject) method.next();
							JSONArray balooActions = new JSONArray();
							jsonMethodNum.put("balooActions", balooActions);

							addBalooActions(jsonMethodNum);

							deleteJsonKeys(jsonMethodNum,"status","_id","primary");
							addJsonKeys(jsonMethodNum,"init:true","group:NOT_FOUND");
							jsonActions =  (JSONArray) jsonMethodNum.get("actions");
							jsonActions.clear();
						}

					}
					replaceJsonKeys(jsonRoot,"steps:items","friendlyId:id","title:description");
					
					// Remove scenario name from friendly task id
					String taskfriendlyid = ((String)jsonRoot.get("id"));
					taskfriendlyid = taskfriendlyid.substring(0, taskfriendlyid.length()-3);
					jsonRoot.put("id", taskfriendlyid);
					
					//Get task name from baloo title
					String taskName =  ((String)jsonRoot.get("description"));
					int nameIndex = taskName.indexOf(':');
					if(nameIndex >= 0)
						taskName = taskName.substring(0, nameIndex);
					jsonRoot.put("name", taskName);
				}
				
		}catch (Exception e) {
			//e.printStackTrace();
			//System.exit(0);
		}

//		System.out.println("input json " + jsonRoot.toJSONString());
		return jsonRoot;

	}

	private void deleteJsonKeys(JSONObject jsonObject, String... keys) {		
		for(int i=0;i<keys.length;i++){
			jsonObject.remove(keys[i]);
		}	
	}

	@SuppressWarnings("unchecked")
	private void addJsonKeys(JSONObject jsonObject, String... keys) {		
		for(int i=0;i<keys.length;i++){			
			String key = keys[i].substring(0,keys[i].indexOf(":"));
			String value = keys[i].substring(keys[i].indexOf(":")+1);
			if(value.equals("true") || value.equals("false"))
				jsonObject.put(key,Boolean.valueOf(value));
			else			
				jsonObject.put(key,value);
		}	
	}

	@SuppressWarnings("unchecked")
	private void replaceJsonKeys(JSONObject jsonObject, String... keys) {		
		for(int i=0;i<keys.length;i++){
			String oldkey = keys[i].substring(0,keys[i].indexOf(":"));
			String newkey = keys[i].substring(keys[i].indexOf(":")+1);
			jsonObject.put(newkey, jsonObject.get(oldkey));
			jsonObject.remove(oldkey);
		}	
	}

	@SuppressWarnings("unchecked")
	private void addBalooActions(JSONObject method) {

		JSONArray origActionSet = (JSONArray)method.get("actions");		
		JSONArray balooActions = (JSONArray) method.get("balooActions");

		for(int actNum=0; actNum<origActionSet.size(); actNum++)
		{ 
			String aText = (String) ((JSONObject)origActionSet.get(actNum)).get("text");
			aText = removeHTMLTags(aText);
			
			try {
				JSONObject balooAct = (JSONObject)new JSONParser().parse("{"
						+ "\"text\":\"" + aText + "\""
						+ "}");

				balooActions.add(actNum, balooAct);

			} catch (ParseException e) {
				System.out.println("Error in reading baloo action text while creating json");
				e.printStackTrace();
				System.exit(0);
			}
		}

		method.put("balooActions", balooActions);
	}

	private String removeHTMLTags(String aText) {
		String processedSent = aText;
		
		//step 1: Repalce &nbsp; with space & &amp; with &
		processedSent = processedSent.replaceAll("&nbsp;", " ");
		
		//step 2: Remove <span> tags
		processedSent = processedSent.replaceAll("<span (.*?)\">", "");
		processedSent = processedSent.replaceAll("<span>", "");
		processedSent = processedSent.replaceAll("</span>", "");
		
		//step 3: Repeat step 2 for <font>
		processedSent = processedSent.replaceAll("<font (.*?)\">", "");
		processedSent = processedSent.replaceAll("<font>", "");
		processedSent = processedSent.replaceAll("</font>", "");
		
		//step4: Handle double quotes
		processedSent = processedSent.replaceAll("\"", "\\\\\"");
		
		return processedSent;
	}
}
