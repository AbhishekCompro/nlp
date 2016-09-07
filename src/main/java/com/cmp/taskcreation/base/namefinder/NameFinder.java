package com.cmp.taskcreation.base.namefinder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;

public class NameFinder {

	public String[] nameFinder(String inputData, String nameFinderModelFile) throws FileNotFoundException{

		//InputStream modelIn = new FileInputStream(nameFinderModelFile);
		InputStream modelIn =  this.getClass().getClassLoader().getResourceAsStream(nameFinderModelFile);//anu:change for web app
		TokenNameFinderModel model = null;
		String[] output = null;

		try {
			model = new TokenNameFinderModel(modelIn);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
		
		try {
			NameFinderME nameFinder = new NameFinderME(model);
			Tokenizer tokenizer =  WhitespaceTokenizer.INSTANCE;
			
			String tokens[] = tokenizer.tokenize(inputData);			
			Span nameSpans[] = nameFinder.find(tokens);

			output = Span.spansToStrings(nameSpans, tokens);
			
//			for(String s : output)
//				System.out.println("output: " + s);

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return output;

	}
}
