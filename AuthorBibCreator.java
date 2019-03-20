/*Name(s) and ID(s): Johanson Felix 40071581
COMP249
Assignment # 3
Question: AuthorBibCreator
Due Date : Mar 21/19
 ----------------------------------------------------- 
 Program description:  This program asks a user to enter an author search name. It then travseres a directory of 10 files to find 
 						articles written by the searched author name. If the search is successful then 3 .JSON files are created to 
 						store the bibliographies for the articles by the searched author name in 3 formats IEEE, ACM, NJ.
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


/** This class implements an Author Bibliography tool that  will require the user to enter an author name to search for, 
 * 	then the tool will search 10 input files (in one execution), detect all articles that belong to the author name being searched, 
 * 	and creates the bibliographies in three formats (IEEE, ACM and NJ), each stored in a separate file. 
 * @author Johanson Felix
 * @version 1.0
 */

public class AuthorBibCreator {

	/*
	 * List of static vairiables used: 
	 * author - author name parsed from input files
	 * search - user entered name to search through input files
	 * IEEE - file to store bibliographies in IEEE format
	 * ACM - file to store bibliographies in ACM format
	 * NJ - file to store bibliographies in NJ format
	 * count - article counter
	 * counter - article counter
	 * BUExcits - stores truth value if back files exist
	 * dir - directory specified to be traversed
	 * fileKeyword - inputfiles keyword in file name
	 * 
	 * 
	 */
	static String author = null, search= null;
	static File IEEE, ACM, NJ;	
	static PrintWriter[] outputStream = new PrintWriter[3];
	static int count=0, counter =0;
	static Scanner inputStream = null;	
	static boolean BUExists =false;
	
	/*
	 *  ------------------------------| Specify directory and file keyword | -------------------------------
	 */
	static File dir= new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src");
	static String fileKeyword ="Latex";
	


	/**processBibFiles takes 11 parameters to be used in generating the IEEE, ACM and NJ formats to be stored.
	 * @param author author name
	 * @param journal journal title
	 * @param title article title
	 * @param year year
	 * @param vol volume
	 * @param number number
	 * @param pages page numbers
	 * @param keywords keywords 
	 * @param doi Digital Object Identifier
	 * @param ISSN International Standard Serial Number
	 * @param month month
	 */
	public static void processBibFiles(String author, String journal, String title, int year, String vol, String number,String pages, String keywords, String doi, String ISSN, String month) {
		counter++;
		for(int i = 0; i<3;i++) {
			if(i==0) {
				outputStream[i].println(author.replace("and", ",")+". \""+title+"\", "+journal+", vol. "+vol+", no."+number+", p. "+pages+", "+month+" "+year+".");

			}

			if(i==1) {
				if(author.contains("and")){
					outputStream[i].println("["+counter+"]    "+author.substring(0,author.indexOf("and"))+" et al. "+year+". "+title+". "+journal+". "+vol+","+number+"("+year+"), "+pages+". DOI:https://"+doi+".");

				}
				else 
					outputStream[i].println("["+counter+"]    "+author+". "+year+". "+title+". "+journal+". "+vol+","+number+"("+year+"), "+pages+". DOI:https://"+doi+".");

			}

			if(i==2) 
				outputStream[i].println(author.replace("and", "&")+". "+title+". "+journal+". "+vol+", "+pages+"("+year+").");

			outputStream[i].println();
		}
	}


	/**
	 * Main Method
	 * @param args String array arguments
	 */
	public static void main(String[] args) {

		/*
		 * Ask user to enter author name to search. 
		 * Store name
		 */
		Scanner in = new Scanner(System.in);

		System.out.println("Welcome to BibCreator!\n");
		System.out.print("Please enter the author name you are targeting: ");
		search = in.nextLine();
		System.out.println();
		in.close();
		/*
		 * Search for author name through files
		 */
		search();

		/*
		 * If the number of articles found is more than zero then print the number and the name of files
		 */
		if(countArticles()>0) {
			System.out.println("A total of "+countArticles()+" records were found for author(s) with name: "+search);
			System.out.println("Files "+IEEE.getName()+", "+ACM.getName()+", and "+NJ.getName()+" have been created!\n");

		}
		/*
		 * If no files were found, then print no files were found with the same author name
		 */

		else {
			System.out.println("No records were found for author(s) with the name: "+search);
			System.out.println("No files have been created!");
		}
		System.out.println("\nGoodbye! Hope you have enjoyed creating the needed files using AuthorBibCreator.");


	} 



	/**
	 * Search traverses 10 input files to find authors that match the user search word. If there is a match then the processBibFiles method is called. 
	 * 
	 */
	public static void search()  {

		/*
		 * currentLine - currentLine be streamed
		 * ALL other variables represent tags from input files
		 */
		String currentLine = null,journal = null, title = null, keywords= null, pages=null, volume= null, doi=null, ISSN=null, month=null, number = null;
		int year = 0;		

		try {
			
			/*
			 * file array storing all files in specified directory
			 */
			File [] fileArray = dir.listFiles();

			/*
			 * If article count is greater than zero then files are opened to search 
			 */
			if(countArticles()>0) {

				/*
				 * files are created with path and name
				 */
				IEEE = new File (dir+"\\"+search+"-IEEE.json");
				ACM= new File(dir+"\\"+search+"-ACM.json");
				NJ= new File(dir+"\\"+search+"-NJ.json");

				/*
				 * Output Streams are created
				 */
				outputStream[0]= new PrintWriter(new FileOutputStream(IEEE,true));
				outputStream[1]= new PrintWriter(new FileOutputStream(ACM,true));
				outputStream[2]= new PrintWriter(new FileOutputStream(NJ,true));


				/*
				 * Traversing directory specified to search for author
				 */
				for(File folderItem:fileArray) {


					if(folderItem.isFile()&& folderItem.getName().contains(fileKeyword)) {

						if(folderItem.length() ==0)
							continue;

						inputStream = new Scanner(new FileInputStream(folderItem));

						while(inputStream.hasNextLine()) {

							if(inputStream.nextLine().contains("@ARTICLE")) {
								currentLine= inputStream.nextLine();

								while(currentLine.length()==0) {

									inputStream.nextLine();
									currentLine=inputStream.nextLine();
								}

								if(currentLine.contains("author")) {					
									author=currentLine.substring(8,currentLine.indexOf("}"));			
									currentLine= inputStream.nextLine();					
								}				

								if(currentLine.contains("journal")) {
									journal=currentLine.substring(9,currentLine.indexOf("}"));
									currentLine=inputStream.nextLine();					 
								}

								if(currentLine.contains("title")) {
									title=currentLine.substring(7,currentLine.indexOf("}"));
									currentLine=inputStream.nextLine();
								}

								if(currentLine.contains("year")) {
									year=Integer.parseInt(currentLine.substring(6,currentLine.indexOf("}")));
									currentLine=inputStream.nextLine();
								}
								if(currentLine.contains("volume")) {
									volume=currentLine.substring(8,currentLine.indexOf("}"));
									currentLine=inputStream.nextLine();

								}
								if(currentLine.contains("number")) {
									number=currentLine.substring(8,currentLine.indexOf("}"));
									currentLine=inputStream.nextLine();
								}
								if(currentLine.contains("pages")) {
									pages=currentLine.substring(7,currentLine.indexOf("}"));
									currentLine=inputStream.nextLine();
								}

								if(currentLine.contains("keywords")) {
									keywords = currentLine.substring(10,currentLine.indexOf("}"));
									currentLine=inputStream.nextLine();					 					 
								}

								if(currentLine.contains("doi")) {
									doi=currentLine.substring(5,currentLine.indexOf("}"));
									currentLine=inputStream.nextLine();
								}

								if(currentLine.contains("ISSN")) {
									ISSN = currentLine.substring(6,currentLine.indexOf("}"));
									currentLine=inputStream.nextLine();
								}

								if(currentLine.contains("month")) {
									month= currentLine.substring(7,currentLine.indexOf("}"));
									currentLine=inputStream.nextLine();
								}

								if(author!=null) {

									if(author.contains(search) && !BUExists) {

										if((IEEE.exists())&&(IEEE.length()>0)&&(ACM.exists())&&(ACM.length()>0)&&(NJ.exists())&&(NJ.length()>0))											
											throw new FileExistsException();
									}

									if(author.contains(search)) {
										processBibFiles(author, journal, title, year, volume, number, pages, keywords, doi, ISSN, month);										
									}
								}

							}

						}
					}

					else if(!folderItem.isFile()&&!folderItem.exists()) {

						throw new FileExistsException("Could not open Input File "+folderItem+" for reading."+"\nPlease check if file exists! Program will terminate after closing any opened files.");
					}
				}
			}
		}

		catch (FileNotFoundException e) {
			try {
				if(!IEEE.canRead()|| !IEEE.createNewFile())
					System.out.println("Could not create file: "+IEEE.getName());
				if(!ACM.canRead()|| !ACM.createNewFile()) {
					System.out.println("Could not create file: "+ACM.getName());
					IEEE.delete();
					NJ.delete();
				}
				if(!NJ.canRead()|| !NJ.createNewFile()) {
					System.out.println("Could not create file: "+NJ.getName());
					IEEE.delete();
					ACM.delete();
				}
			} 

			catch (IOException e1) {
				System.out.println("Could not create file: "+IEEE.getName());
				System.out.println("Could not create file: "+ACM.getName());
				System.out.println("Could not create file: "+NJ.getName());				
			}
		}
		catch (FileExistsException e) {
			File IEEEBU = new File(dir+"\\"+search+"-IEEE-BU.json");
			File ACMBU = new File(dir+"\\"+search+"-ACM-BU.json");
			File NJBU = new File(dir+"\\"+search+"-NJ-BU.json");


			if((IEEEBU.exists())&&(IEEEBU.length()>0)&&(ACMBU.exists())&&(ACMBU.length()>0)&&(NJBU.exists())&&(NJBU.length()>0)) {

				IEEEBU.delete();	
				ACMBU.delete();
				NJBU.delete();

				outputStream[0].close();
				outputStream[1].close();
				outputStream[2].close();

				IEEE.renameTo(IEEEBU);
				ACM.renameTo(ACMBU);
				NJ.renameTo(NJBU);

				search();

				System.out.println("There is already an existing file for that author. File will be renamed as BU, and older BU files will be deleted!");
				System.out.println("\nA total of "+countArticles()+" records were found for author(s) with name: "+search);
				System.out.println("Files "+IEEE.getName()+", "+ACM.getName()+", and "+NJ.getName()+" have been created!\n");
				System.out.println("\nGoodbye! Hope you have enjoyed creating the needed files using AuthorBibCreator.");
			}

			else {

				if((IEEE.exists())&&(IEEE.length()>0)&&(ACM.exists())&&(ACM.length()>0)&&(NJ.exists())&&(NJ.length()>0)) {

					System.out.println("A file already exists with the name: "+IEEE.getName());
					System.out.println("File will be renamed as: "+search+"-IEEE-BU.json\n");					
					outputStream[0].close();
					IEEE.renameTo(IEEEBU);

					System.out.println("A file already exists with the name: "+ACM.getName());
					System.out.println("File will be renamed as: "+search+"-ACM-BU.json\n");					
					outputStream[1].close();
					ACM.renameTo(ACMBU);

					System.out.println("A file already exists with the name: "+NJ.getName());
					System.out.println("File will be renamed as: "+search+"-NJ-BU.json\n");
					outputStream[2].close();
					NJ.renameTo(NJBU);

					System.out.println("A total of "+countArticles()+" records were found for author(s) with name: "+search);

					BUExists = true;
					search();

					System.out.println("Files "+IEEE.getName()+", "+ACM.getName()+", and "+NJ.getName()+" have been created!\n");
					System.out.println("\nGoodbye! Hope you have enjoyed creating the needed files using AuthorBibCreator.");
				}
			}
			System.exit(0);

		}
		/*
		 * close streams
		 */
		finally{

			if(inputStream !=null && outputStream[0]!=null && outputStream[1]!=null && outputStream[2]!=null) { 

				outputStream[0].close();
				outputStream[1].close();
				outputStream[2].close();
				inputStream.close();
			}

		}
	}
	/*
	 * Method to count number of articles that exist based on author search
	 */

	/**
	 * countArticles counts the number of matches of authors whose name contains the word being searched for.
	 * @return returns count of Articles with a match
	 */
	public static int countArticles()  {

		File [] fileArray = dir.listFiles();
		String currentLine = null;
		int count =0;

		for(File folderItem:fileArray) {
			if(folderItem.isFile()&& folderItem.getName().contains(fileKeyword)) {
				if(folderItem.length() ==0)
					continue;

				try {					
					inputStream = new Scanner(new FileInputStream(folderItem));					
				} 
				catch (FileNotFoundException e) {
					System.out.println("Please check if file exists! Program will terminate after closing any opened files.");
					System.exit(0);
				}

				while(inputStream.hasNextLine()) {

					if(inputStream.nextLine().contains("@ARTICLE")) {
						currentLine = inputStream.nextLine();

						while(currentLine.length()==0) {

							inputStream.nextLine();
							currentLine=inputStream.nextLine();
						}

						if(currentLine.contains("author")) {					
							author=currentLine.substring(8,currentLine.indexOf("}"));			
							currentLine= inputStream.nextLine();					
						}
						if(author!=null) {
							if(author.contains(search)) {
								count++;
							}
						}

					}

				}

			}
		}
		return count;
	}

}
