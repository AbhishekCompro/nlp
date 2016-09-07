package com.cmp.taskcreation.base.sentence;

import java.util.ArrayList;
import java.util.Arrays;

import com.cmp.taskcreation.base.NLPBase;

public class CMP_SentenceDetection extends NLPBase {

	public String[] sentenceDetectionRound2(String inputData, String delimiter) {
		
		String[] sentences = null;
		ArrayList<String> finalSent = new ArrayList<String>();

		inputData = inputData.trim();

		if (inputData.contains(delimiter)) {
			String[] splittedSent = inputData.split(delimiter);

			for (int indx = splittedSent.length - 1; indx >= 0; indx--) 
			{
				splittedSent[indx] = splittedSent[indx].trim();

				if (!(getActionCount(splittedSent[indx]) > 0)) 
				{
					if (indx > 0 && (inputData.toLowerCase().startsWith("press") || inputData.toLowerCase().startsWith("type"))) {
						// Add it to the previous array element
						splittedSent[indx - 1] = splittedSent[indx - 1]
								+ delimiter + splittedSent[indx];
						splittedSent[indx] = null;

					} else if (!(getUpperCaseWordList(splittedSent[indx]) == null || getUpperCaseWordList(
							splittedSent[indx]).isEmpty())) {
						if(Arrays.asList(splittedSent[indx].toLowerCase().split(" ")).contains("press") ||
								Arrays.asList(splittedSent[indx].toLowerCase().split(" ")).contains("type"))
						splittedSent[indx] = "press " + splittedSent[indx];
					} else {
						splittedSent[indx] = null;
					}
						

				}
			}

			for (int indx = 0; indx < splittedSent.length; indx++) {
				if (splittedSent[indx] != null)
					finalSent.add(splittedSent[indx].trim());
			}

			sentences = finalSent.toArray(new String[finalSent.size()]);
			int i = 0;
			for (String s : finalSent) {
				sentences[i++] = s;
			}
		} else { 
			sentences = new String[1];
			sentences[0] = inputData;
		}

		if (sentences == null)
			System.exit(0);

		return sentences;

	}
}
