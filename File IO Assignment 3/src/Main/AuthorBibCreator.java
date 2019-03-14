package Main;

import java.util.*;
import java.io.*;

public class AuthorBibCreator {
	
	static String author = null, journal = null, title = null, year = null, volume = null, number = null, pages = null, keywords = null, doi = null, ISSN = null, month = null;
	static String search;
	static String name = null;
	static File saveIEEE;

	public static void main(String[] args) {
		
		Scanner sc = null;
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("Welcome to BibCreator!");
		System.out.println();
		System.out.print("Please enter the author name you are targeting: ");
		search = keyboard.nextLine();
		
		String line = null;
		int filecount = 0;
		
		File folder = new File("C:\\Users\\gdmav\\Dropbox\\COMP249_A3\\Latex");
		File[] fileArray = folder.listFiles();
		System.out.println();
		
		try {
			for (File file : fileArray) {
			    if (file.isFile()) {

			    	if (file.length() == 0)
			    		continue;
			    	
			    	sc = new Scanner(new FileInputStream(file));
			    	
			    	while (sc.hasNextLine()) {
			    		if (sc.nextLine().contains("@ARTICLE")) {
			    			
			    			line = sc.nextLine();
			    			while (line.length() == 0) {
			    				sc.nextLine();
				    			line = sc.nextLine();
			    			}

			    			if (line.equals("") || line.equals(" "))
				    			continue;
				    		else if (line.charAt(0) == '@')
				    			continue;
				    		else if (line.equals("}"))
				    			continue;
				    		else if (line.charAt(0) == '0' || line.charAt(0) == '1' || line.charAt(0) == '2'
									|| line.charAt(0) == '3' || line.charAt(0) == '4' || line.charAt(0) == '5'
									|| line.charAt(0) == '6' || line.charAt(0) == '7' || line.charAt(0) == '8'
									|| line.charAt(0) == '9') {
				    			continue;
				    		}
			    			
			    			if (line.contains("author")) {
								author = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		if (line.contains("journal")) {
								journal = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		if (line.contains("title")) {
								title = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		if (line.contains("year")) {
								year = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		if (line.contains("volume")) {
								volume = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		if (line.contains("number")) {
								number = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		if (line.contains("pages")) {
								pages = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		if (line.contains("keywords")) {
								keywords = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		if (line.contains("doi")) {
								doi = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		if (line.contains("ISSN")) {
								ISSN = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		if (line.contains("month")) {
								month = line.substring(line.indexOf("{")+1, line.indexOf("}"));
								line = sc.nextLine();
							}
				    		hasAuthor(author);
			    		}
			    	}
			    	filecount++;
			    }
			    else {
			    	saveIEEE = file;
			    	throw new FileNotFoundException();
			    }
			}			
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not open input file " + name + " for reading. Please check if file exists! Program will terminate after closing any opened files.");
			sc.close();
	    	System.exit(0);
		}
		
		PrintWriter[] IEEE = new PrintWriter[10];
		PrintWriter[] ACM = new PrintWriter[10];
		PrintWriter[] NJ = new PrintWriter[10];
		
		// Initialize arrays indexes to null
		for (int i = 0; i < IEEE.length; i++) {
			IEEE[i] = null;
			ACM[i] = null;
			NJ[i] = null;
		}
		
		int k = 0;
		String directory = "C:\\Users\\gdmav\\Dropbox\\COMP249_A3\\JSON Files\\";
		try {
			for (k = 0; k < IEEE.length; k++) {
				IEEE[k] = new PrintWriter(new FileOutputStream(directory + "IEEE" + (k + 1) + ".json"));
				ACM[k] = new PrintWriter(new FileOutputStream(directory + "ACM" + (k + 1) + ".json"));
				NJ[k] = new PrintWriter(new FileOutputStream(directory + "NJ" + (k + 1) + ".json"));
			}
		}
		catch (FileNotFoundException e) { //catch block for when it cannot create the files. Takes a FileNotFoundException
			String name = e.getMessage();
			name = name.substring(0, 11);
			System.out.println("Could not create any of these files due to an error. Specifically, file " + name + ". Please try again");
			for (int i = 0; i < k; i++) {
				IEEE[i].close();
				ACM[i].close();
				NJ[i].close();
			}
			sc.close();
		}

		
	}
	
	public static void hasAuthor(String str) {
		if (author != null) {
			if (str.contains(search)) {
				System.out.println("@ARTICLE");
				System.out.println(author);
				System.out.println(journal);
				System.out.println(title);
				System.out.println(year);
				System.out.println(volume);
				System.out.println(number);
				System.out.println(pages);
				System.out.println(keywords);
				System.out.println(doi);
				System.out.println(ISSN);
				System.out.println(month);
				System.out.println();
		    }
		}
	}

}
