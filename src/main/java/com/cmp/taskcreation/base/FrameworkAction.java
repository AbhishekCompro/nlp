package com.cmp.taskcreation.base;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class FrameworkAction {

	@SuppressWarnings("unchecked")
	public JSONArray addSplittedActions(int actionIndex, String balooInputData,JSONArray frameworkActionsSet) throws ParseException {
		
		JSONArray actionObjArray = new ActionSplitter().splitNCreateActionSet(balooInputData);
		
		for(int idx = 0; idx < actionObjArray.size(); idx++)
		{
			JSONObject actObj = (JSONObject)actionObjArray.get(idx);
			actObj.put("balooActionIndex", actionIndex);
			actObj.put("balooActionText", balooInputData);
			frameworkActionsSet.add(actObj);
		}
		return frameworkActionsSet;
	}
	
}
