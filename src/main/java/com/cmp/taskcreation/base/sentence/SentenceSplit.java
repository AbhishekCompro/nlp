package com.cmp.taskcreation.base.sentence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.PatternSyntaxException;

import org.json.simple.parser.ParseException;

import com.cmp.taskcreation.base.keyboard.KeyboardActions;
import com.cmp.taskcreation.base.namefinder.TagExtractor;

public class SentenceSplit extends SentenceProcessing{

	public ArrayList<HashMap<String,String>> addFrameworkAction(String balooInputData) throws ParseException {
		boolean debugging = false;

		ArrayList<HashMap<String,String>> finalActionList = new ArrayList<HashMap<String,String>>();


//		System.out.println("\n******************************************************inputdata ");
		if(debugging)System.out.println(balooInputData);

		//Step0: Preprocess the baloo action
		String inputData = new ActionPreprocessor().getProcessedSentence(balooInputData);

		if(debugging)System.out.println("\n0 - preprocessed action: "); 
		if(debugging)System.out.println(inputData);

		/**
		 *  Check for number of actions
		 *  IF: action count is more than 1 action --> action needs to be splitted further
		 *  ELSE: add action to action set
		 */
		try {
				//Step2: divide sentence based on . (dot)
				SentenceDetector sentenceDetector = new SentenceDetector();
				String[] sentences = sentenceDetector.getSentences(inputData);

				if(debugging)System.out.println("\n1 - sentencedetecter:");
				if(debugging)for(String set : sentences)
					System.out.println(set);

				for(String sentence : sentences)
				{
					//Step3: Update the sentence for special SIMS scenarios
					sentence = updateSentenceForFramework(sentence);

					if(debugging)System.out.println("\n2 - UPDATED: ");
					if(debugging)System.out.println(sentence);

					//Step4: split sentence based on delimiter ', then'
					String[] cmp_sentences = new CMP_SentenceDetection().sentenceDetectionRound2(sentence, ", then ");

					if(debugging)System.out.println("\n3 - , then split --- input: " + sentence); 
					if(debugging)for(String set : cmp_sentences)
						System.out.println(set);

					for(String cmp_sent : cmp_sentences)
					{
						//Step5: split sentence based on delimiter 'and'
						String[] cmp_sentencesRound2 = new CMP_SentenceDetection().sentenceDetectionRound2(cmp_sent, " and ");

						if(debugging)System.out.println("\n4 - and split --- input: " + cmp_sent); 
						if(debugging)for(String set : cmp_sentencesRound2)
							System.out.println(set);

						for(String cmp_sent2 : cmp_sentencesRound2)
						{
							//Step6: split sentence based on delimiter ',' (comma)
							String[] cmp_sentencesRound3 = new CMP_SentenceDetection().sentenceDetectionRound2(cmp_sent2, ",");

							if(debugging)System.out.println("\n5 - COMMA, split --- input: " + cmp_sent2);  
							if(debugging)for(String set : cmp_sentencesRound3)
								System.out.println(set);

							for(String cmp_sentence : cmp_sentencesRound3)
							{
								/**
								 * Check if action is keyboard action (press|type)
								 * IF: Update/split action into framework keyboard action format and then add to action set
								 * ELSE: add action to action set
								 */
								if(cmp_sentence.toLowerCase().startsWith("pressKeyMultipleTimes "))
								{
									finalActionList.add(getFinalAction(cmp_sentence));
								}
								else if(cmp_sentence.toLowerCase().startsWith("press "))
								{
									String[] keyBoardSentences = new KeyboardActions().getSentencesPress(cmp_sentence);

									for(String keyboardSent: keyBoardSentences)
									{
										finalActionList.add(getFinalAction(keyboardSent));
									}

								}
								else if(cmp_sentence.toLowerCase().startsWith("type"))
								{
									cmp_sentence = new KeyboardActions().getSentencesType(cmp_sentence);
									finalActionList.add(getFinalAction(cmp_sentence));

								}
								else
								{
									finalActionList.add(getFinalAction(cmp_sentence));

								}
							}

						}
					}
				}

		} 
		catch(PatternSyntaxException pe) {
			System.err.println("Baloo action text not well formed...adding action text as it is in converted json");
			HashMap<String, String> defaultSplittedAction = new HashMap<String, String>();
			defaultSplittedAction.put("NOT_FOUND", inputData);
			
			finalActionList.add(defaultSplittedAction);
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return finalActionList;
	}

	private HashMap<String, String> getFinalAction(String splitAction) {

		return (new TagExtractor().getActionTypeAndElement(splitAction));
	}
}
