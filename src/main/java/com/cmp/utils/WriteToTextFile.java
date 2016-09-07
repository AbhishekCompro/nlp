package com.cmp.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class WriteToTextFile {

	public void writeToFile (String contentToWrite, String completeFilePath) {
        BufferedWriter writer = null;
        try {

            File logFile = new File(completeFilePath);

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(contentToWrite);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }

}
