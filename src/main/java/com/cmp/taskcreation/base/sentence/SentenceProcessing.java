package com.cmp.taskcreation.base.sentence;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cmp.taskcreation.base.NLPBase;

public class SentenceProcessing extends NLPBase {

	protected String updateSentenceForFramework(String tmpSentence) {
		
		try{

		// TRIM
		tmpSentence = tmpSentence.trim();

//		// Remove ", if necessary." with "."
//		tmpSentence = tmpSentence.replaceAll(", if necessary.", ".");
		
		// Remove ", if necessary." with "."
		tmpSentence = tmpSentence.replaceAll("Optional: ", "");

		// Remove full stop '.'
		if (tmpSentence.endsWith("."))
			tmpSentence = tmpSentence.substring(0, (tmpSentence.length() - 1));

		// Update different sentence conjunction into one, i.e. 'and'
		tmpSentence = tmpSentence.replaceAll(", and then,", " and");
		tmpSentence = tmpSentence.replaceAll(", and then", " and");
		tmpSentence = tmpSentence.replaceAll(" and then", " and");
		tmpSentence = tmpSentence.replaceAll(", then", ",");

		// Remove "With <some text>, "
		tmpSentence = tmpSentence.replaceFirst("^Wit(.*?),", "");

		// Remove "On the ... Toolbar, "
		tmpSentence = tmpSentence.replaceFirst("^On the (.*?) Toolbar, ", "");

		// Remove "On the ... Page, "
		tmpSentence = tmpSentence.replaceFirst("^On the (.*?) page, ", "");

		// Remove "In the Page Setup dialog box, on the Margins tab, "
		tmpSentence = tmpSentence.replaceFirst(
				"^In the (.*?) dialog box, on the (.*?) tab, ", "");

		// Handling for:
		// "On the Home tab, in the Find group, click Go To and click Next."
		Pattern pattern = Pattern
				.compile("[IO]n( the)? (.*?) tab, in( the)? (.*?) group, (.*?) and (.*\\.?)");
		Matcher matcher = pattern.matcher(tmpSentence);
		if (matcher.find()) {
			// check for action type
			String[] grp5 = matcher.group(5).split(" ");
			if(grp5.length > 0 && matcher.group(6).split(" ").length > 0)
			{
				String actionType1 = getActionType(grp5[0]);
				String element1 = getCamelCaseWords(matcher.group(5).substring(
						(matcher.group(5).indexOf(" ") + 1),
						matcher.group(5).length()));
				

				String actionType2 = getActionType(matcher.group(6).split(" ")[0]);
				String element2 = getCamelCaseWords(matcher.group(6).substring(
						(matcher.group(6).indexOf(" ") + 1),
						matcher.group(6).length()));

				String upSent = "Click Ribbon__" + matcher.group(2) + ", "
						+ actionType1 + " Ribbon__" + matcher.group(4) + "__"
						+ element1 + ", " + actionType2 + " Ribbon__"
						+ matcher.group(4) + "__" + element1 + "__" + element2;

				tmpSentence = tmpSentence.replaceAll(matcher.group(0), upSent);

			}
		}

		// System.out.println("6: " + tmpSentence);

		// Handling for: "On the Home tab, in the Views group, click View."
		pattern = Pattern
				.compile("[IO]n( the)? (.*?) tab, in( the)? (.*?) group, (.*\\.?)");
		matcher = pattern.matcher(tmpSentence);
		if (matcher.find()) {
			// check for action type
			if(matcher.group(5).split(" ").length > 0)
			{
				String actionType1 = getActionType(matcher.group(5).split(" ")[0]);
				String element1 = matcher.group(5).substring(
						(matcher.group(5).indexOf(" ") + 1),
						matcher.group(5).length());
				element1 = getCamelCaseWords(element1);

				String upSent = "Click Ribbon__" + matcher.group(2) + ", "
						+ actionType1 + " Ribbon__" + matcher.group(4) + "__"
						+ element1;

				tmpSentence = tmpSentence.replaceAll(matcher.group(0), upSent);
			}
		}


		// Handling for: "Click the Insert tab."
		pattern = Pattern.compile("^[Cc]lick( the)? (.*?) tab");
		matcher = pattern.matcher(tmpSentence);
		if (matcher.find()) {
			tmpSentence = tmpSentence.replace(matcher.group(0),
					"Click Ribbon__" + matcher.group(2));
		}


		// Handling for:
		// "In the Header & Footer group, click Header and click Edit Header."
		pattern = Pattern
				.compile("^[IOio]n( the)? (.*?) group, (.*?) and (.*\\.?)");
		matcher = pattern.matcher(tmpSentence);
		if (matcher.find()) {

			// check for action type
			String[] grp3 = matcher.group(3).split(" ");
			if(grp3.length > 0 && matcher.group(4).split(" ").length > 0)
			{
				String actionType1 = getActionType(grp3[0]);
				String element1 = getCamelCaseWords(matcher.group(3).substring(
						(matcher.group(3).indexOf(" ") + 1),
						matcher.group(3).length()));

				String actionType2 = getActionType(matcher.group(4).split(" ")[0]);
				String element2 = getCamelCaseWords(matcher.group(4).substring(
						(matcher.group(4).indexOf(" ") + 1),
						matcher.group(4).length()));

				tmpSentence = actionType1 + " Ribbon__" + matcher.group(2) + "__"
						+ element1 + ", " + actionType2 + " Ribbon__"
						+ matcher.group(2) + "__" + element1 + "__" + element2;

			}
		}

		// Handling for: "In the Relationships group, click Relationships."
		pattern = Pattern.compile("^[IO]n( the)? (.*?) group, (.*\\.?)");
		matcher = pattern.matcher(tmpSentence);
		if (matcher.find()) {
			// check for action type
			String[] grp3 = matcher.group(3).split(" ");
			if(grp3.length > 0)
			{
				String actionType1 = getActionType(grp3[0]);
				String element1 = getCamelCaseWords(matcher.group(3).substring(
						(matcher.group(3).indexOf(" ") + 1),
						matcher.group(3).length()));

				tmpSentence = actionType1 + " Ribbon__" + matcher.group(2) + "__"
						+ element1;

			}
		}
		
		// Handling for: "Click the Layout tab, and in the Paragraph group, click the Paragraph Settings Dialog Box Launcher"
		pattern = Pattern.compile("[, ]in the (.*?) group, (.*\\.?)");
		matcher = pattern.matcher(tmpSentence);
		if (matcher.find()) {
			// check for action type
			String[] grp2 = matcher.group(2).split(" ");
			if(grp2.length > 0)
			{
				String actionType1 = getActionType(grp2[0]);
				String element1 = getCamelCaseWords(matcher.group(2).substring(
						(matcher.group(2).indexOf(" ") + 1),
						matcher.group(2).length()));

				tmpSentence = tmpSentence.replace(matcher.group(0), ("," + actionType1 + " Ribbon__" + matcher.group(1) + "__"
						+ element1));

			}
		}


		// Handling for Dialog: "Click OK." - Add dialog
		tmpSentence = tmpSentence.replaceAll("(?i)click ok( button)?",
				"Click Dialog__OK");

		// On the Slide pane, click Slide 2.
		pattern = Pattern
				.compile("[I|O]n the Slide(s?) pane, click Slide (.*\\.?)");
		matcher = pattern.matcher(tmpSentence);
		if (matcher.find()) {
			if (matcher.group(2) != null)
				tmpSentence = "selectSlideFromSlidePane " + matcher.group(2);
			else
				tmpSentence = "selectSlideFromSlidePane " + matcher.group(1);
		}

		// remove: "On Slide 2, "
		pattern = Pattern.compile("(?i)On( the)? Slide \\d+, ");
		matcher = pattern.matcher(tmpSentence);
		if (matcher.find()) {
			tmpSentence = tmpSentence.replaceAll(matcher.group(0), "");
		}

		tmpSentence = tmpSentence.replaceAll("press and hold", "press");
		
		// handling for excel application: "If necessary, use ARROW keys to select cell A6."
		pattern = Pattern.compile("[Uu]se ARROW keys to select cell ([A-Z]+[0-9]+)");
		matcher = pattern.matcher(tmpSentence);
		if(matcher.find())
		{
			String excelAction = "selectCell " + matcher.group(1);
			tmpSentence = tmpSentence.replace(matcher.group(0), excelAction);
		}
		
		// handling for excel application: "Click cell H8"
		boolean checkPattern = true;
		while(checkPattern)
		{
			pattern = Pattern.compile("([Cc]lick to )?([Cc]lick|[Ss]elect)( in)? cell ([A-Z]+[0-9]+)");
			matcher = pattern.matcher(tmpSentence);
			if(matcher.find())
			{
				String excelAction = "selectCell " + matcher.group(4);
				tmpSentence = tmpSentence.replace(matcher.group(0), excelAction);
			}
			else
				checkPattern = false;

		}
		
		// handling for excel application: "Click cell H8"
		checkPattern = true;
		while(checkPattern)
		{
			pattern = Pattern.compile("[Rr]ight-click cell ([A-Z]+[0-9]+)");
			matcher = pattern.matcher(tmpSentence);
			if(matcher.find())
			{
				String excelAction = "rightClickOnCell " + matcher.group(1);
				tmpSentence = tmpSentence.replace(matcher.group(0), excelAction);
			}
			else
				checkPattern = false;

		}
		
		// handling for excel application: "Use SHIFT+ARROW keys to select the range C4:K4"
		checkPattern = true;
		while(checkPattern)
		{
			pattern = Pattern.compile("Use SHIFT\\+ARROW keys to select the range ([A-Z]+[0-9]+:[A-Z]+[0-9]+)");
			matcher = pattern.matcher(tmpSentence);
			if(matcher.find())
			{
				System.out.println(matcher.group(0));
				String excelAction = ",selectCellRangeKeyboard " + matcher.group(1);
				tmpSentence = tmpSentence.replace(matcher.group(0), excelAction);
			}
			else
				checkPattern = false;

		}
		
		// handling for excel application: "Select cells A4:B9" & "Select cells A4 through B9"
		checkPattern = true;
		while(checkPattern)
		{
			pattern =          Pattern.compile("(, )?[Ss]elect( the)? (?:range|cells) ([A-Z]+[0-9]+)(?: through |\\:)([A-Z]+[0-9]+)");
			matcher = pattern.matcher(tmpSentence);
			if(matcher.find())
			{
				String excelAction = ",selectCellRange " + matcher.group(3) + ":" + matcher.group(4);
				tmpSentence = tmpSentence.replace(matcher.group(0), excelAction);
			}
			else
				checkPattern = false;

		}
		
		//handling for key press multiple times
		checkPattern = true;
		while(checkPattern)
		{
			pattern = Pattern.compile("[Pp]ress( the)? ([A-Z]+[0-9]+) until (.*?) selected[ ,\\.]");
			matcher = pattern.matcher(tmpSentence);
			if(matcher.find())
			{
				String updatedAction = "pressKeyMultipleTimes " + matcher.group(2) + ",";
				tmpSentence = tmpSentence.replace(matcher.group(0), updatedAction);
			}
			else
				checkPattern = false;
		}

		if (tmpSentence != null && tmpSentence.replaceAll(" ", "") != "")
			return tmpSentence;
		else
			return null;
		
		}
		catch(Exception e) {
			System.out.println("Error while parsing sentence:\n=============\n"
					+ tmpSentence 
					+ "\n=============");
			return tmpSentence;
		}

	}
		


}
