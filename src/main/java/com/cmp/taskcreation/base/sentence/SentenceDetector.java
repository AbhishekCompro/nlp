package com.cmp.taskcreation.base.sentence;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetector {

	String sentenceModelFile = null;
	public static boolean isTrained = false;

	public SentenceDetector() {
		sentenceModelFile = "lib/model/en-sent.bin";
	}

	public SentenceDetector(String sentTrainData, char[] eosCharacters, String sentModel) {
		sentenceModelFile = sentModel;

		try {
			if(!isTrained)
			{
				SentenceTrainer.trainModel(sentTrainData, eosCharacters, sentModel);
				isTrained = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	String[] getSentences(String inputContent) throws IOException {

		String[] sentences = null;

		InputStream modelIn = null;
		try {
			//modelIn = new FileInputStream(sentenceModelFile);
			modelIn = this.getClass().getClassLoader().getResourceAsStream(sentenceModelFile);//anu:change for web app
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			SentenceModel model = new SentenceModel(modelIn);

			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
			sentences = sentenceDetector.sentDetect(inputContent);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
		return sentences;


	}
}
