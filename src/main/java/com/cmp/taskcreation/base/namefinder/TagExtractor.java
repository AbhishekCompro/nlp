package com.cmp.taskcreation.base.namefinder;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cmp.taskcreation.base.NLPBase;

public class TagExtractor extends NLPBase{
	
	HashMap<String, String> finalAct = new HashMap<String, String>();
	
	public HashMap<String, String> getActionTypeAndElement(String splittedAction){
		
//		System.out.println("splittedaction: " + splittedAction);
		
		String elementName = "";
		
		String actionFinderModelFile = "lib/model/actionapril.bin";
		//String actionFinderModelFile = "lib/model/cmp-action-model.bin";
		String elementFinderModelFile = "lib/model/en-ner-element-v1.bin";

		if(isSentUseToSelectFormat(splittedAction))
		{
			finalAct = getUseToFormatAction(splittedAction);
		}
		else if(splittedAction.toLowerCase().startsWith("pressKeyMultipleTimes"))
		{
			if(splittedAction.split(" ").length > 1)
				elementName = splittedAction.split(" ")[1].trim();
			else
				elementName = splittedAction;
			finalAct.put("pressKeyMultipleTimes", elementName);
		}
		else if(splittedAction.toLowerCase().startsWith("press") || splittedAction.toLowerCase().startsWith("enter_text")
											|| splittedAction.toLowerCase().startsWith("type"))
		{
			finalAct = getPressTypeAction(splittedAction);
		}
		else if(splittedAction.startsWith("selectSlideFromSlidePane"))
		{
			if(splittedAction.split(" ").length > 1)
				elementName = splittedAction.split(" ")[1].trim();
			else
				elementName = splittedAction;
			finalAct.put("selectSlideFromSlidePane", elementName);
		}
		else if(splittedAction.startsWith("selectCellRange"))
		{
			if(splittedAction.split(" ").length > 1)
				elementName = splittedAction.split(" ")[1].trim();
			else
				elementName = splittedAction;
			finalAct.put(splittedAction.split(" ")[0].trim(), elementName);
		}
		else if(splittedAction.startsWith("selectCell"))
		{
			if(splittedAction.split(" ").length > 1)
				elementName = splittedAction.split(" ")[1].trim();
			else
				elementName = splittedAction;
			finalAct.put("selectCell", elementName);
		}
		else if(splittedAction.startsWith("rightClickOnCell"))
		{
			if(splittedAction.split(" ").length > 1)
				elementName = splittedAction.split(" ")[1].trim();
			else
				elementName = splittedAction;
			finalAct.put("rightClickOnCell", elementName);
		}
		else if(splittedAction.contains("Ribbon__"))
		{
			elementName = splittedAction.substring(splittedAction.indexOf("Ribbon__"));
			finalAct.put("Click", elementName);
		}
		else 
		{
			String[] actionTypeList, elementNameList;
			try {
				actionTypeList = new NameFinder().nameFinder(splittedAction, actionFinderModelFile);
				
				if(actionTypeList.length > 0 && actionTypeList.length!=1)
				{
					finalAct.put("NOT_FOUND", splittedAction);
				}

				else if(actionTypeList.length == 1)
				{
					String actText = actionTypeList[0].trim();
					if(!isValidAction(actText))
					{
						finalAct.put("NOT_FOUND", splittedAction);
					}
					else
					{
						
						elementNameList = new NameFinder().nameFinder(splittedAction, elementFinderModelFile);
						
						if(elementNameList.length == 1)
						{
							finalAct.put(actText, elementNameList[0]);
						}
						else if(elementNameList.length > 0)
						{
							finalAct.put(actText, replaceSpaceWithUnderscore(elementNameList));
						}
						else
						{
							String elename = splittedAction.replace((actionTypeList[0] + " "), "");
							//UNDERSCORE
							finalAct.put(actionTypeList[0], elename);
						}
					}
				}
				
				else
				{
					String actText = getActionType(splittedAction);
					if(actText != null && actText !="")
						finalAct.put(actText, splittedAction);
					else
						finalAct.put("NOT_FOUND", splittedAction);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}
		
//		Iterator<Entry<String, String>> itt = finalAct.entrySet().iterator();
//		while(itt.hasNext())
//			System.out.println(itt.next());
		
		return finalAct;
	}
	
	private HashMap<String, String> getUseToFormatAction(String splittedAction1) {
		
		Pattern useToRegex = Pattern.compile("use (.*?) to select (.*?)");
		Matcher regexMatcher = useToRegex.matcher(splittedAction1);
		
		if(regexMatcher.find())
		{
			finalAct.put("Press", regexMatcher.group(1));
			return finalAct;
		}
		else
			return null;
	}
	
    private HashMap<String, String> getPressTypeAction(String sAct) {
    	
		String act = "NOT_FOUND";
		String param = sAct;
		
		if(sAct.split(" ").length > 0)
			act = sAct.split(" ")[0].trim();
		
		if(sAct.indexOf(' ') > 0)
			param = sAct.substring((sAct.indexOf(' ')+1), sAct.length());
		
		if(act.equalsIgnoreCase("press"))
			finalAct.put("Press", param);
		else
			finalAct.put("enterText", param);
		
		return finalAct;
	}
    
	private String replaceSpaceWithUnderscore(String[] param) {
		
		String ret = "";
		
		for(String paramVal : param) {
		if(!(paramVal.startsWith("Ribbon__") || paramVal.startsWith("Dialog__")))
			paramVal = paramVal.replaceAll(" ", "_");	
		ret = ret + paramVal;
		}
		return ret;
	}
}
