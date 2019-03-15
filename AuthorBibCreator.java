import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
public class AuthorBibCreator {

	static String author = null;
	static File IEEE;
	static File ACM ;
	static File NJ ;
	static PrintWriter[] outputStream = new PrintWriter[3];
	static int count=0;
	static Scanner inputStream = null;
	static String search;

	public static void processBibFiles(String author, String journal, String title, int year, String volume, String number,String pages, String keywords, String doi, String ISSN, String month) {

		for(int i = 0; i<3f;i++) {

			outputStream[i].println("Author: "+author);
			outputStream[i].println("Journal: "+journal);
			outputStream[i].println("Title: "+title);
			outputStream[i].println("Year: "+year);
			outputStream[i].println("Volume: "+volume);
			outputStream[i].println("Number: "+number);
			outputStream[i].println("Pages: "+pages);
			outputStream[i].println("keywords: "+keywords);
			outputStream[i].println("doi: "+doi);
			outputStream[i].println("ISSN: "+ISSN);
			outputStream[i].println("Month: "+month);
			outputStream[i].println("");

		}


	}

	public static int countArticles()  {

		File dir= new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src");
		File [] fileArray = dir.listFiles();
		String currentLine = null;
		int count =0;
		for(File folderItem:fileArray) {


			if(folderItem.isFile()&& folderItem.getName().contains("Latex")) {

				if(folderItem.length() ==0)
					continue;

				try {
					inputStream = new Scanner(new FileInputStream(folderItem));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

	public static void main(String[] args) {


		Scanner in = new Scanner(System.in);

		String currentLine = null,journal = null, title = null, keywords= null, pages=null, volume= null, doi=null, ISSN=null, month=null;

		int year = 0;
		String number = null;


		System.out.println("Welcome to BibCreator!\n");


		try {


			System.out.print("Please enter the author name you are targeting: ");
			search = in.nextLine();
			System.out.println();


			File dir= new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src");
			File [] fileArray = dir.listFiles();

			if(countArticles()>0) {
				IEEE = new File ("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src\\"+search+"-IEEE.json");
				ACM= new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src\\"+search+"-ACM.json");
				NJ= new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src\\"+search+"-NJ.json");

				outputStream[0]= new PrintWriter(new FileOutputStream(IEEE,true));
				outputStream[1]= new PrintWriter(new FileOutputStream(ACM,true));
				outputStream[2]= new PrintWriter(new FileOutputStream(NJ,true));


				for(File folderItem:fileArray) {


					if(folderItem.isFile()&& folderItem.getName().contains("Latex")) {

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


									if(author.contains(search)) {



										if((IEEE.exists())&&(IEEE.length()>0)&&(ACM.exists())&&(ACM.length()>0)&&(NJ.exists())&&(NJ.length()>0))
											throw new FileExistsException();






										processBibFiles(author, journal, title, year, volume, number, pages, keywords, doi, ISSN, month);



										System.out.println("Author: "+author);
										System.out.println("Journal: "+journal);
										System.out.println("Title: "+title);
										System.out.println("Year: "+year);
										System.out.println("Volume: "+volume);
										System.out.println("Number: "+number);
										System.out.println("Pages: "+pages);
										System.out.println("keywords: "+keywords);
										System.out.println("doi: "+doi);
										System.out.println("ISSN: "+ISSN);
										System.out.println("Month: "+month);
										System.out.println();
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

			if(countArticles()>0) {
				System.out.println("A total of "+countArticles()+" records were found for author(s) with name: "+search);
				System.out.println("Files "+IEEE.getName()+", "+ACM.getName()+", and "+NJ.getName()+" have been created!\n");
			}
			else {
				System.out.println("No records were found for author(s) with the name: "+search);
				System.out.println("No files have been created!");
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
				System.out.println("IO exception");
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}



		}




		catch (FileExistsException e) {


			File IEEEBU = new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src\\"+search+"-IEEE-BU.json");
			File ACMBU = new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src\\"+search+"-ACM-BU.json");
			File NJBU = new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src\\"+search+"-NJ-BU.json");


			if((IEEEBU.exists())&&(IEEEBU.length()>0)&&(ACMBU.exists())&&(ACMBU.length()>0)&&(NJBU.exists())&&(NJBU.length()>0)) {

				IEEEBU.delete();	
				ACMBU.delete();
				NJBU.delete();


				IEEE.renameTo(IEEEBU);
				ACM.renameTo(ACMBU);
				NJ.renameTo(NJBU);

			}
			else
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

					System.out.println("Files "+IEEE.getName()+", "+ACM.getName()+", and "+NJ.getName()+" have been created!\n");
				}
			System.exit(0);

		}

		finally{
			System.out.println("\nGoodbye! Hope you have enjoyed creating the needed files using AuthorBibCreator.");


			if(inputStream !=null && in!=null && outputStream[0]!=null && outputStream[1]!=null && outputStream[2]!=null) { 

				in.close();
				outputStream[0].close();
				outputStream[1].close();
				outputStream[2].close();
			}
		}



	}

}
