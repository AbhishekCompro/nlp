package com.cmp.taskcreation.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NLPBase {
	
	// NOTE: Action Type must be one word; i.e. not separated by SPACE
	String[] actionList = {"Double-click", "double-click", "Press", "press", "click", "Click", 
							"Type", "type", "Right-click", "right-click", "Scroll", "scroll",
							"selectSlideFromSlidePane"};
	
	protected String getActionType(String sentence) {
		boolean sentFormat = false;
		String[] wordList = getWordList(sentence);
		for(int i=0; i<wordList.length; i++)
		{
			if(Arrays.asList(actionList).contains(wordList[i].trim()))
				return (wordList[i].trim());
			else if (!sentFormat && isSentUseToSelectFormat(sentence))
				return "Press";
		}
		return null;
	}
	
	protected int getActionCount(String sentence) {
		boolean sentFormat = false;
		String[] wordList = getWordList(sentence);
		int actionCount = 0;
		for(int i=0; i<wordList.length; i++)
		{
			String wrd = wordList[i].trim();
			if(Arrays.asList(actionList).contains(wrd))
				actionCount++;

			if(!sentFormat && isSentUseToSelectFormat(sentence))
			{
				actionCount++;
				sentFormat = true;
			}
				
		}

		return actionCount;
	}
	
	protected boolean isValidAction(String text){
		String acTxt = getActionType(text);
		
		if(acTxt != null && acTxt !="")
			return true;
		else
			return false;
	}
	
  	 // To cover: "use ARROW keys to select Access 2016" action format
		protected boolean isSentUseToSelectFormat(String sentence) {
			Pattern useToRegex = Pattern.compile("use (.*?) to select (.*?)\\.");
			Matcher regexMatcher = useToRegex.matcher(sentence);
			
			if(regexMatcher.find())
				return true;
			return false;
		}
		
		
		protected String getCamelCaseWords(String inputString){
			String[] ipArr = inputString.split(" ");
			String retStr = "";

				for(int i=0; i<ipArr.length; i++)
				{
					
					char[] letters = ipArr[i].trim().toCharArray();
					
					if(letters.length > 0)
					{
						if(Character.isUpperCase(letters[0]) || Character.isDigit(letters[0]))
						{
							if(retStr!="")
								retStr = retStr + " "; 
							retStr = retStr + ipArr[i].trim();
						}
					}
						
				}
				return retStr;
		}
		
		
		protected List<String> getUpperCaseWordList(String inputContent){
			String[] ip = inputContent.trim().split(" ");
			List<String> ret = new ArrayList<String>();
			
			for(int i=0; i<ip.length; i++)
			{
				String[] ipComma = ip[i].split(",");
				
				for(int j=0; j< ipComma.length; j++)
				{
					String txt = ipComma[j].trim();
					if(txt.length() > 0)
					{
						boolean isUpper = true;
						char[] letters = txt.toCharArray();
						for(char c : letters)
						{
							if(Character.isLowerCase(c))
							{
								isUpper = false;
								break;
							}
						}
						
						if(isUpper){
							ret.add(txt);
						}
					}
				}
						

			}
			return ret;
		}
		
		private String[] getWordList(String input){
			
			input = input.replaceAll(",", " , ");
			input = input.replaceAll("\\.", " \\. ");
			String[] wordList = input.split(" ");
				return wordList;
		}
}
