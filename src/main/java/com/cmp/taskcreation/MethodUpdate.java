package com.cmp.taskcreation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.cmp.taskcreation.base.FrameworkAction;
import com.cmp.taskcreation.base.NLPBase;

public class MethodUpdate extends NLPBase{
	
	JSONObject method;
	
	public MethodUpdate(JSONObject method){
		this.method = method;
	}
	
	@SuppressWarnings("unchecked")
	protected JSONObject addSplittedActions() throws ParseException {

		JSONArray balooActions = ((JSONArray) method.get("balooActions"));
		JSONArray frameworkActionsSet = ((JSONArray) method.get("actions"));
		
		for(int actionIndex = 0; actionIndex<balooActions.size(); actionIndex++)
		{
			JSONObject balooActionObject = (JSONObject)balooActions.get(actionIndex);
			String balooInputData = (String) balooActionObject.get("text");
			
			frameworkActionsSet = new FrameworkAction().addSplittedActions(actionIndex, balooInputData, frameworkActionsSet);
		}
		
		method.put("actions", frameworkActionsSet);
		
		return method;
	}
}