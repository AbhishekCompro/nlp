package com.cmp.taskcreation.base;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.cmp.taskcreation.ActionMapping;
import com.cmp.taskcreation.base.sentence.SentenceSplit;

public class ActionSplitter {
	
@SuppressWarnings("unchecked")
public  JSONArray splitNCreateActionSet(String balooInputData) throws ParseException {
		JSONArray actionObjArray = new JSONArray();
		
		ArrayList<HashMap<String,String>> splittedActionList = new SentenceSplit().addFrameworkAction(balooInputData);
		
		for(HashMap<String,String> splitAct: splittedActionList)
		{
			JSONObject actObj = (JSONObject) new ActionMapping().getMappedAction(splitAct);
			actionObjArray.add(actObj);
		}
		
		return actionObjArray;
	}

}
