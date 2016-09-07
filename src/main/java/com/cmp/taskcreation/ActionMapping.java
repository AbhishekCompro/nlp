package com.cmp.taskcreation;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cmp.taskcreation.base.NLPBase;

public class ActionMapping extends NLPBase{
	
	JSONObject finalActionInJsonFormat = null;
	
	String jsonString = "{ \"init\": true,"
			+ " \"balooActionIndex\": -1 "
			+ " \"name\": \"NOT_FOUND\", "
			+ " \"values\": ["
			+ " {"
			+ " \"actKey\": \"NOT_FOUND\","
			+ " \"actVal\": \"NOT_FOUND\""
			+ "}"
			+ "],"
			+ " \"syntax\": \"NOT_FOUND\""
			+ "}";
	
	@SuppressWarnings("unused")
	private JSONObject updatedMethod = null;
	
	public JSONObject getMappedAction(HashMap<String, String> splitAct) throws ParseException  {
		
		try {
			finalActionInJsonFormat = (JSONObject) new JSONParser().parse(jsonString);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		String actionType = (String) splitAct.keySet().toArray()[0];
		String eleN = splitAct.get(actionType);
		
		mapToFrameworkAction(actionType, eleN);
		
		return finalActionInJsonFormat;
	}
	
    
	@SuppressWarnings({"unchecked" })
	private void mapToFrameworkAction(String actionType, String eleN) throws ParseException{
		
		String name = "NOT_FOUND";
		String param = "NOT_FOUND";
		String syntax = "NOT_FOUND";
		String param1= "NOT_FOUND";
		String eleN1 = "NOT_FOUND";

		eleN = eleN.replace("__####__", "");
		

	 if(actionType == null)
		 actionType = "";
	     
	     switch(actionType.toLowerCase())
	     {
	     	case "double-click": {

     			name = "doubleClick()";
     			param = "elementName";
     			syntax = "doubleClick(String elementName)";
     			eleN = replaceSpaceWithUnderscore(eleN);
	     		break;
	     	}
	     	
	     	case "press": {

	     		// check for modifier key
	     		boolean isModifierKey = checkModifierKey(eleN);
	     		
	     		if(isModifierKey)
	     		{
	     			name=	"pressNreleaseKey()";
	     			param = "keyName";
	     			syntax = "pressNreleaseKey(MyKeys keyName)";
	     		}
	     		else
	     		{
	     			switch(eleN){
	     			case "TAB":
	     			case "ARROW_DOWN":
	     			case "ARROW_UP":
	     			case "ARROW_RIGHT":
	     			case "ARROW_LEFT":
		     			name=	"pressKeyMultipleTimes()";
		     			syntax = "pressKeyMultipleTimes(MyKeys keyName, String numOfTimes)";
		     			param = "keyName";
		     			param1 = "numOfTimes";
		     			eleN1 = "1";
		     			break;

	     			default:
		     			name=	"pressKey()";
		     			syntax = "pressKey(MyKeys keyName)";
		     			param = "keyName";
			     		break;
	     			}
	     		}

	     		break;
	     	}
	     	
	     	case "release": {
     			name=	"releaseKey()";
     			param = "keyName";
     			syntax = "releaseKey(MyKeys keyName)";
	     		
	     		break;
	     	}
	     	
	     	case "entertext":{
     			name=	"enterText()";
     			param = "text";
     			syntax = "enterText(String text)";
	     		break;
	     	}
	     	case "click": {
     			name=	"clickAndWait()";
     			param = "elementName";
     			syntax = "clickAndWait(String elementName)";
     			eleN = replaceSpaceWithUnderscore(eleN);

	     		break;
	     	}
	     	case "right-click": {
	     		name = "rightClick()";
	     		syntax = "rightClick(String elementName)";
	     		param = "elementName";
	     		eleN = replaceSpaceWithUnderscore(eleN);
	     		
	     		break;
	     	}
	     	case "scroll": {
	     		name = "scroll()";
	     		syntax = "scroll(String elementName)";
	     		param = "elementName";
	     		eleN = replaceSpaceWithUnderscore(eleN);
	     		
	     		break;
	     	}
	     	
	     	case "selectslidefromslidepane": {
	     		name = "selectSlideFromSlidePane()";
	     		syntax = "selectSlideFromSlidePane(String slideNumber)";
	     		param = "slideNumber";
	     		
	     		// Extract slide number from eleN
	     		Pattern pattern = Pattern.compile("(\\s)Slide (\\d)+");
	     		Matcher matcher = pattern.matcher(eleN);
	     		if(matcher.find())
	     		{
	     			eleN = matcher.group(1);
	     		}
	     		else
	     			eleN = replaceSpaceWithUnderscore(eleN);
	     		
	     		break;
	     	}
	     	
	     	case "selectcell": {
	     		name = "selectCell()";
	     		syntax = "selectCell(String cellName)";
	     		param = "cellName";
	     		break;
	     	}
	     	
	     	case "rightClickOnCell": {
	     		name = "rightClickOnCell()";
	     		syntax = "rightClickOnCell(String cellName)";
	     		param = "cellName";
	     		break;
	     	}
	     	
	     	case "selectcellrange": {
	     		name = "selectCellRange()";
	     		syntax = "selectCellRange(String cellRange, Method methodType)";
	     		param = "cellRange";
	     		param1 = "methodType";
	     		eleN1 = "MOUSE";
	     		break;
	     	}
	     	
	     	case "selectcellrangekeyboard": {
	     		name = "selectCellRange()";
	     		syntax = "selectCellRange(String cellRange, Method methodType)";
	     		param = "cellRange";
	     		param1 = "methodType";
	     		eleN1 = "KEYBOARD";
	     		break;
	     	}
	     	
//	     	case "pressKeyMultipleTimes": {
//	     		name = "pressKeyMultipleTimes()";
//	     		syntax = "pressKeyMultipleTimes(MyKeys keyName , String numOfTimes)";
//	     		param = "keyName";
//	     		param1 = "numOfTimes";
//	     		eleN1 = "1";
//	     		break;
//	     	}

	     	
	     	default: {
	     		name = eleN;
	     		param = "NOT_FOUND";
     			syntax = "NOT_FOUND";
	     	}
	     }
	     
  		     finalActionInJsonFormat.put("name",	name);
			((JSONObject)((JSONArray)finalActionInJsonFormat.get("values")).get(0)).put("actKey", param);
		    ((JSONObject)((JSONArray)finalActionInJsonFormat.get("values")).get(0)).put("actVal", eleN);
		    
		    if(!param1.equals("NOT_FOUND"))
		    {
		    	JSONObject secondParam = (JSONObject) new JSONParser().parse("{ "
			+ " \"actKey\": \"" + param1 + "\","
			+ " \"actVal\": \"" + eleN1 + "\""
			+ "}");
		    	((JSONArray)finalActionInJsonFormat.get("values")).add(secondParam);
		    }
		    	
			finalActionInJsonFormat.put("syntax", syntax);

		
	}
	
	private String replaceSpaceWithUnderscore(String paramVal) {
		
		if(!(paramVal.startsWith("Ribbon__") || paramVal.startsWith("Dialog__")))
			paramVal = paramVal.replaceAll(" ", "_");		
		return paramVal;
	}


	private boolean checkModifierKey(String keyName) {
		String kName = keyName.toUpperCase() + "__";
		
		if(("ALT__SHIFT__CTRL__CONTROL__").contains(kName))
			return true;
		return false;
	}


}
