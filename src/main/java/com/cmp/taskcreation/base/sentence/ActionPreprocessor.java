package com.cmp.taskcreation.base.sentence;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionPreprocessor {

	public String getProcessedSentence(String sent) {
		String processedSent = sent;

		// Remove content in brackets () including brackets
		while (processedSent.matches(".+\\([^\\(\\)]*\\).*")) {
			processedSent = processedSent.replaceAll("(\\([^\\(\\)]*\\))+", "");
		}

		// Remove "Alternatively...."
		Pattern pattern = Pattern.compile("Alternatively, ([^\\.]*\\.?)");
		Matcher matcher = pattern.matcher(processedSent);
		if (matcher.find()) {
//			System.out.println("extracted:"+matcher.group(1));
			processedSent = processedSent.replaceAll("(Alternatively, [^\\.]*\\.?)", "");
		}

		// Remove <BR>
		processedSent = processedSent.replaceAll("<br>", "");
		processedSent = processedSent.replaceAll("<BR>", "");

		// Handle 4., update to: 4_. --- workaround for sentence detector
		pattern = Pattern.compile("([0-9]+)\\.[^0-9]");
		matcher = pattern.matcher(processedSent);
		if (matcher.find()) {
			processedSent = processedSent.replaceAll(matcher.group(0),
					(matcher.group(1) + "__####__. "));
		}
		
		// Handle Click any cell in column H., update to: 4_. --- workaround for sentence detector
		pattern = Pattern.compile("([A-Z]+)\\.");
		matcher = pattern.matcher(processedSent);
		if (matcher.find()) {
			processedSent = processedSent.replace(matcher.group(0),
					(matcher.group(1) + "__####__. "));
		}

		return processedSent;
	}

}
