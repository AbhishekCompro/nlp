package com.cmp.taskcreation.base.sentence;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.sentdetect.SentenceDetectorFactory;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class SentenceTrainer {

	public static void trainModel(String trainingSampleDataFile, char[] eosCharacters, String trainedModelFile)
			throws IOException {

		MarkableFileInputStreamFactory factory = new MarkableFileInputStreamFactory(new File(trainingSampleDataFile));

		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream = new PlainTextByLineStream(factory,charset);
		ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);

		SentenceModel model;

		try {
			SentenceDetectorFactory sentenceDetectorFactory = new SentenceDetectorFactory("en", true, new Dictionary(), eosCharacters);
			model = SentenceDetectorME.train("en", sampleStream,sentenceDetectorFactory,TrainingParameters.defaultParams());
		} finally {
			sampleStream.close();
		}

		OutputStream modelOut = null;
		try {
			modelOut = new BufferedOutputStream(new FileOutputStream(trainedModelFile));
			model.serialize(modelOut);
		} finally {
			if (modelOut != null)
				modelOut.close();
		}
	}
}