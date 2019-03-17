import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
public class AuthorBibCreator {

	static String author = null, search= null;
	static File IEEE, ACM, NJ;	
	static PrintWriter[] outputStream = new PrintWriter[3];
	static int count=0;
	static Scanner inputStream = null;	
	static boolean BUExists =false;
	static File dir= new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src");
	static String fileKeyword ="Latex";
	static int counter =0;


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

	public static void main(String[] args) {


		Scanner in = new Scanner(System.in);

		System.out.println("Welcome to BibCreator!\n");
		System.out.print("Please enter the author name you are targeting: ");
		search = in.nextLine();
		System.out.println();
		in.close();
		search();


		if(countArticles()>0) {
			System.out.println("A total of "+countArticles()+" records were found for author(s) with name: "+search);
			System.out.println("Files "+IEEE.getName()+", "+ACM.getName()+", and "+NJ.getName()+" have been created!\n");
		}
		else {
			System.out.println("No records were found for author(s) with the name: "+search);
			System.out.println("No files have been created!");
		}
		System.out.println("\nGoodbye! Hope you have enjoyed creating the needed files using AuthorBibCreator.");


	} 


	public static void search()  {

		String currentLine = null,journal = null, title = null, keywords= null, pages=null, volume= null, doi=null, ISSN=null, month=null, number = null;

		int year = 0;		

		try {

			File [] fileArray = dir.listFiles();

			if(countArticles()>0) {
				IEEE = new File (dir+"\\"+search+"-IEEE.json");
				ACM= new File(dir+"\\"+search+"-ACM.json");
				NJ= new File(dir+"\\"+search+"-NJ.json");

				outputStream[0]= new PrintWriter(new FileOutputStream(IEEE,true));
				outputStream[1]= new PrintWriter(new FileOutputStream(ACM,true));
				outputStream[2]= new PrintWriter(new FileOutputStream(NJ,true));


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

					BUExists = true;
					search();

					System.out.println("Files "+IEEE.getName()+", "+ACM.getName()+", and "+NJ.getName()+" have been created!\n");
					System.out.println("\nGoodbye! Hope you have enjoyed creating the needed files using AuthorBibCreator.");
				}
			System.exit(0);
		}

		finally{

			if(inputStream !=null && outputStream[0]!=null && outputStream[1]!=null && outputStream[2]!=null) { 

				outputStream[0].close();
				outputStream[1].close();
				outputStream[2].close();
				inputStream.close();
			}

		}
	}
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
