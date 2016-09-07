package com.cmp.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFromFile {
	
		public String readFile(String fileName) throws IOException {
		    BufferedReader br = new BufferedReader(new FileReader(fileName));
		    try {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null && line != "") {
		            sb.append(line.trim());
		            sb.append("\n");
		            line = br.readLine();
		        }
		        return sb.toString();
		    } finally {
		        br.close();
		    }
		}
    }

