package com.cmp.utils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class TEMP {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		String cV = "";
		
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpGet httpget = new HttpGet("https://baloo.stg-prsn.com/");

    	HttpResponse response = httpclient.execute(httpget);
    	
    	Header[] responseHeaders = response.getAllHeaders();
    	Header[] h = response.getHeaders("set-cookie");
    	String cookieVal = h[0].getValue();

    	Pattern p = Pattern.compile("connect.sid=(.*); Path=?");
    	Matcher m = p.matcher(cookieVal);
    	
    	if(m.find())
    	{
    		System.out.println("0: " + m.group(0));
    		System.out.println("1: " + m.group(1));
    		cV = m.group(1);
    	}
    	
    	System.out.println(h[0].getValue());
    	
    	for (Header header : responseHeaders) {
    		System.out.println("Key : " + header.getName() 
    		      + " ,Value : " + header.getValue());
    	}
    	
    	//release connection
    	HttpEntity entity = response.getEntity();
    	if( entity != null ) {
            EntityUtils.consume(entity);
         }//if
    	
    	
    	httpget = new HttpGet("https://baloo.stg-prsn.com/scenarios/SKL16.PPT.01.01.04.T1");
    	
    	//set cookie
    	httpget.setHeader("Cookie", "connect.sid=" + cV + ";SERVERID=" + "s1");
    	
    	response = httpclient.execute(httpget);    	
    	entity = response.getEntity();
    			
    			
    	if (entity != null) {
		    String fileContent = EntityUtils.toString(entity);
		    System.out.println(fileContent);
		}


//		System.out.println(tmpSentence);
//		System.out.println("output:" + new TEMP().checkCamelCase(tmpSentence));
//		
//		Pattern pattern = Pattern.compile("(?i)On( the)? Slide \\d+, ");
//		Matcher matcher = pattern.matcher(tmpSentence);
//		if(matcher.find())
//		{
//			tmpSentence = tmpSentence.replaceAll(matcher.group(0), "");
//		}
////		}
//		System.out.println("****");
//		System.out.println(tmpSentence);

	}
	
	public String[] tabNameFinder(){
		return null;
		
	}
	
	String checkCamelCase(String ss){
		String ip = ss;
		String retStr = "";
		
		String[] ipArr = ip.split(" ");
		
		if(ipArr.length > 0)
		{
			for(int i=0; i<ipArr.length; i++)
			{
				System.out.println("word: " + ipArr[i]);
				char[] letters = ipArr[i].toCharArray();
				
				if(Character.isUpperCase(letters[0]))
				{
					if(retStr!="")
						retStr = retStr + " "; 
					retStr = retStr + ipArr[i];
				}
					
			}
		}
			return retStr;
	}

}
