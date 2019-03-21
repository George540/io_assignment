package Main;

import java.util.*;
import java.io.*;
import java.nio.file.Files;

/** The class implements programming concepts such as OOP, exceptions and I/O to create a useful tool for creating
 * bibliographies based on an author's name search. The program imports different files, each containing a set of article(s).
 * The program reads the files, counts the amount of articles found and saves the information of the article based whether it is
 * under the author's name provided by the user. The author's data are printed in new JSON files in 3 different kinds of bibliographies:
 * IEEE, ACM and NJ. If the files already exist, a backup file is created. If a backup already exists, the old backup is deleted and the
 * original file is renamed to backup.
 * @author George Mavroeidis
 * @version 2.0
 */
/**
 * @author gdmav
 *
 */
public class AuthorBibCreator {
	
	/**
	 * Variables:
	 * sc: Scanner for inputstream
	 * wr: Array of output streams for different files
	 * search: user's attempt to search author in a string
	 * author
	 * author: author's name
	 * count, counter: article counter
	 * folder: string of reading files directory
	 * folder2: string of writing files directory
	 */
	static Scanner sc = null;
	static PrintWriter[] wr = new PrintWriter[3];
	static String search, author = null;
	static File IEEE, ACM, NJ;
	static int count, counter = 0;
	static boolean isBU = false;
	static File folder = new File("C:\\Users\\gdmav\\Dropbox\\COMP249_A3\\Latex");
	static File folder2 = new File("C:\\Users\\gdmav\\Dropbox\\COMP249_A3\\JSON Files");
	static final String KEYWORD = "Latex";
	
	/**
	 * @param author author name
	 * @param journal journal title
	 * @param title article title
	 * @param year publication year
	 * @param vol volume of article
	 * @param number number of article
	 * @param pages number of pages
	 * @param keywords keywords
	 * @param doi Digital Object Identifier
	 * @param ISSN International Standard Serial Number
	 * @param month month of publication
	 */
	public static void processBibFiles(String author, String journal, String title, String year, String vol, String number,String pages, String keywords, String doi, String ISSN, String month) {
		// Iterating to the amount of files found
		counter++;
		for (int i = 0; i < 3; i++) {
			// Print articles according to their type (IEEE, ACM, NJ)
			if (i == 0)
				wr[i].println(author.replace("and", ",") + ". \"" + title + "\", " + journal + ", vol. " + vol + ", no." + number + ", p. " + pages + ", " + month + " " + year + ".");
			
			if(i == 1) {
				if(author.contains("and"))
					wr[i].println("[" + counter + "]    " + author.substring(0,author.indexOf("and")) + " et al. " + year +". " + title + ". " + journal + ". " + vol + "," + number + "(" + year + "), " + pages + ". DOI:https://" + doi + ".");
				else 
					wr[i].println("[" + counter + "]    " + author + ". " + year + ". " + title + ". " + journal + ". " + vol + "," + number + "(" + year + "), " + pages + ". DOI:https://" + doi + ".");
			}
			if (i == 2) 
				wr[i].println(author.replace("and", "&") + ". " + title + ". " + journal + ". " + vol + ", " + pages + "(" + year + ").");

			wr[i].println();
		}
	}
	
	/**
	 * countArticles counts thenumber of matches of authors whose name contains the word being searched for
	 * @return integer with the number of articles with a match
	 */
	public static int countArticles() {
		// Create array of files to store files from Latex Folder
		File[] fileArray = folder.listFiles();
		String line = null;
		
		// Iterate through each file
		for (File file : fileArray) {
			if (file.isFile() && file.getName().contains("Latex")) {
				
				if (file.length() == 0)
		    		continue;
				
				try {
					sc = new Scanner(new FileInputStream(file));
				}
				catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				while (sc.hasNextLine()) {
					if (sc.nextLine().contains("@ARTICLE")) {
						line = sc.nextLine();
						
						// If line is empty, move to next line
						 while (line.length() == 0) {
							 sc.nextLine();
							 line = sc.nextLine();
						 }
						 
						 // Save author's name 
						 if (line.contains("author")) {
							 author = line.substring(line.indexOf("{")+1, line.indexOf("}"));
							 line = sc.nextLine();
						 }
						 if (author != null) {
							 // If author is a match, increase counter
							 if (author.contains(search))
								 count++;
						 }
					}
				}
			}
		}
		return count;
	}

	/**
	 * Main Method
	 * @param args String array arguments
	 */
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		
		System.out.println("Welcome to BibCreator!");
		System.out.println();
		System.out.print("Please enter the author name you are targeting: ");
		search = keyboard.nextLine();
		
		System.out.println();
		keyboard.close();
		search();
		
		if (countArticles() > 0) {
			System.out.println("TEST");
			System.out.println("A total of " + countArticles() + " records were found for author(s) with name: " + search);
			System.out.println("Files " + IEEE.getName() + ", " + ACM.getName() + ", and " + NJ.getName() + " have been created!");
			System.out.println();
		}
		else {
			System.out.println("No records were found for author(s) with the name: " + search);
			System.out.println("No files have been created!");
		}
		System.out.println("Goodbye! Hope you have enjoyed creating the needed files using AuthorBibCreator.");
		
		
	}

	public static void search() {
		String line = null, journal = null, title = null, year = null, volume = null, number = null, pages = null, keywords = null, doi = null, ISSN = null, month = null;
		System.out.println();
		
		try {
			File[] fileArray = folder.listFiles();
			
			if (countArticles() > 0) {
				// Create files for exporting
				IEEE = new File(folder2 + "\\" + search + "-IEEE.json");
				ACM= new File(folder2 + "\\" + search + "-ACM.json");
				NJ= new File(folder2 + "\\" + search + "-NJ.json");
				
				// Initialize printwriters for different file types
				wr[0] = new PrintWriter(new FileOutputStream(IEEE, true));
				wr[1] = new PrintWriter(new FileOutputStream(ACM, true));
				wr[2] = new PrintWriter(new FileOutputStream(NJ, true));
				
				for (File file : fileArray) {
				    if (file.isFile() && file.getName().contains(KEYWORD)) {

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
				    			
				    			// Save attributes from file that are a match
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
					    		if (author != null) {
					    			// If match is a backup, through exception
					    			if (author.contains(search) && !isBU) {
					    				if ((IEEE.exists()) && (IEEE.length() > 0) && (ACM.exists()) && (ACM.length() > 0) && (NJ.exists()) && (NJ.length() > 0)) {
					    					throw new FileExistsException();
					    				}
					    				// If it's not a backup, create exported files
					    				if (author.contains(search)) 
					    					processBibFiles(author, journal, title, year, volume, number, pages, keywords, doi, ISSN, month);
					    			}
					    		}
				    		}
				    	}
				    }
				    // If file is not found or it is nonexistant, through exception
				    else if (!file.isFile() && !file.exists())
				    	throw new FileNotFoundException("Could not open Input File "+ file +" for reading." + "Please check if file exists! Program will terminate after closing any opened files.");
				}
			}
		}
		catch (FileNotFoundException e) {
			try {
				if (!IEEE.canRead() || !IEEE.createNewFile())
					System.out.println("Could not create file: " + IEEE.getName());
				if (!ACM.canRead() || !ACM.createNewFile()) {
					System.out.println("Could not create file: " + ACM.getName());
					IEEE.delete();
					NJ.delete();
				}
				if (!NJ.canRead() || !NJ.createNewFile()) {
					System.out.println("Could not create file: " + NJ.getName());
					IEEE.delete();
					ACM.delete();
				}
			} 

			catch (IOException e1) {
				System.out.println("Could not create file: " + IEEE.getName());
				System.out.println("Could not create file: " + ACM.getName());
				System.out.println("Could not create file: " + NJ.getName());				
			}
		}
		catch (FileExistsException e) {
			// Create backups
			File IEEEBU = new File(folder2 + "\\" + search + "-IEEE-BU.json");
			File ACMBU = new File(folder2 + "\\" + search + "-ACM-BU.json");
			File NJBU = new File(folder2 + "\\" + search + "-NJ-BU.json");

			// If backups exist, rename originals and delete existing backups
			if ((IEEEBU.exists()) && (IEEEBU.length() > 0) && (ACMBU.exists()) && (ACMBU.length() > 0) && (NJBU.exists()) && (NJBU.length() > 0)) {
				
				IEEEBU.delete();	
				ACMBU.delete();
				NJBU.delete();

				wr[0].close();
				wr[1].close();
				wr[2].close();

				IEEE.renameTo(IEEEBU);
				ACM.renameTo(ACMBU);
				NJ.renameTo(NJBU);

				search();
				
				System.out.println("There is already an existing file for that author. File will be renamed as BU, and older BU files will be deleted!");
				System.out.println();
				System.out.println("A total of " + countArticles()+" records were found for author(s) with name: " + search);
				System.out.println("Files " + IEEE.getName() + ", " + ACM.getName() + ", and " + NJ.getName() + " have been created!");
				System.out.println();
				System.out.println("Goodbye! Hope you have enjoyed creating the needed files using AuthorBibCreator.");
			}

			else {
				if ((IEEE.exists()) && (IEEE.length() > 0) && (ACM.exists()) && (ACM.length() > 0) && (NJ.exists()) && (NJ.length() > 0)) {
					System.out.println("A file already exists with the name: " + IEEE.getName());
					System.out.println("File will be renamed as: " + search + "-IEEE-BU.json\n");					
					wr[0].close();
					IEEE.renameTo(IEEEBU);

					System.out.println("A file already exists with the name: " + ACM.getName());
					System.out.println("File will be renamed as: " + search + "-ACM-BU.json\n");					
					wr[1].close();
					ACM.renameTo(ACMBU);

					System.out.println("A file already exists with the name: " + NJ.getName());
					System.out.println("File will be renamed as: " + search + "-NJ-BU.json\n");
					wr[2].close();
					NJ.renameTo(NJBU);

					System.out.println("A total of " + countArticles() + " records were found for author(s) with name: " + search);

					isBU = true;
					search();

					System.out.println("Files " + IEEE.getName() + ", " + ACM.getName() + ", and " + NJ.getName() + " have been created!");
					System.out.println();
					System.out.println("Goodbye! Hope you have enjoyed creating the needed files using AuthorBibCreator.");
				}
			}
			System.exit(0);
		}
		finally {
			if (sc != null && wr[0] != null && wr[1] != null && wr[2] != null) { 
				wr[0].close();
				wr[1].close();
				wr[2].close();
				sc.close();
			}

		}
	}
}

