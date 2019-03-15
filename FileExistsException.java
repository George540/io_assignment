import java.io.File;

public class FileExistsException extends Exception {

	
	
	public FileExistsException(String err_msg) {
		super(err_msg);
	}
	
	public FileExistsException() {
		super("Exception. There is already an existing file for that author. File will be renamed as BU, and older BU files wil be deleted!");
	}
	
	public static void renameFileBU(String author, File IEEE, File ACM, File NJ) {
		File IEEEBU = new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src\\"+author+"-IEEE-BU.json");
		File ACMBU = new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src\\"+author+"-ACM-BU.json");
		File NJBU = new File("C:\\Users\\felix\\eclipse-workspace\\COMP249A3\\src\\"+author+"-NJ-BU.json");
		
		IEEE.renameTo(IEEEBU);
		ACM.renameTo(ACMBU);
		NJ.renameTo(NJBU);
	}
	

}
