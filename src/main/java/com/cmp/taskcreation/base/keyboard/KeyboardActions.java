package com.cmp.taskcreation.base.keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cmp.taskcreation.base.NLPBase;

public class KeyboardActions extends NLPBase{

	public String[] getSentencesPress(String sentence) {
		
		String[] returnSet = null;
		ArrayList<String> retSet = new ArrayList<String>();

		// Press ALT,V,H case
		if(sentence.length() >6 && sentence.substring(6, sentence.length()).startsWith("ALT ,") || sentence.substring(6, sentence.length()).startsWith("ALT,"))
		{
			sentence = sentence.replaceAll(" and ", ", ");
			retSet.add("Press " + removeLowerCaseWords(sentence));
		}
		
		// Press ALT+N
		else if(sentence.contains("+"))
		{
			if(sentence.matches(".* or .*"))
			{
				sentence = sentence.replaceAll("( or .*)", "");
			}
			retSet.add("Press " + removeLowerCaseWords(sentence));		
		}
		else if(sentence.length() > 6)
		{
			String newSentence = sentence.substring(6, sentence.length());
			
			//Remove content after 'or'
			Pattern p = Pattern.compile("(,)? or (.*)?$");
			Matcher m = p.matcher(newSentence);
			
			 //Press TAB or SPACEBAR
			if(m.find())
			{
				newSentence = newSentence.replaceAll(m.group(0), "");
			}
			
			List<String> wordSet = getUpperCaseWordList(newSentence);
			String retSent = "";
			for(int i=0; i<wordSet.size(); i++)
				{
				if( ((i+1)<wordSet.size()) && !((wordSet.get(i)+" "+wordSet.get(i+1)).equals(getArrowKey(wordSet.get(i)+" "+wordSet.get(i+1)))) )
				{
					// assuming that key with two words will be arrow key
					// handling arrow key & converting to framework format, e.g. DOWN ARROW -> ARROW_DOWN
					String arrwoKey = getArrowKey(wordSet.get(i) + " " + wordSet.get(++i).trim());
					retSent = retSent + arrwoKey;
				}
				else if(wordSet.get(i) == "ARROW")
				{
					retSent = retSent + "ARROW_DOWN";
				}
				else
				{
					retSent = retSent + wordSet.get(i);
				}
				
				if((i+1) < wordSet.size())
				{
					retSent = retSent + ",";
				}

			}
			
			retSent = "Press " + retSent;
			retSet.add(retSent);
			
		}
		else {
			retSet.add(sentence);
		}

		returnSet = retSet.toArray(new String[retSet.size()]);
		
		int i=0;
		for(String s: retSet){
			returnSet[i++] = s;
		}
		
		return returnSet;
	}

	private String getArrowKey(String arrKey) {
		
		switch(arrKey){
		case "DOWN ARROW": {
			return "ARROW_DOWN";
		}
		case "UP ARROW": {
			return "ARROW_UP";
		}

		case "RIGHT ARROW": {
			return "ARROW_RIGHT";
		}

		case "LEFT ARROW": {
			return "ARROW_LEFT";
		}
		case "PAGE DOWN": {
			return "PAGE_DOWN";
		}
		case "PAGE UP": {
			return "PAGE_UP";
		}
		case "BACK SPACE": {
			return "BACK_SPACE";
		}

		default:{
			return arrKey;
		}
			
		}
	}

	public String getSentencesType(String sentence){
	String returnSet = null;
	if(sentence.replaceFirst("type", "") !=null)
		returnSet = "Enter_Text " + sentence.replaceFirst("type ", "");
	else
		returnSet = "Enter_Text ";

	return returnSet;

	}
	
	private String removeLowerCaseWords(String text){
		
		List<String> keysList = getUpperCaseWordList(text);
		String retSent = "";
		int addComma = 1;
		for(String key : keysList)
		{
			retSent = retSent + key;
			if(addComma < keysList.size())
			{
				retSent = retSent + ",";
				addComma++;
			}
				
		}
		
		return retSent;
	}
	

}
