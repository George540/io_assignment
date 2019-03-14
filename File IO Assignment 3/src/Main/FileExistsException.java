package Main;

public class FileExistsException extends Exception {
	
	public FileExistsException(String message) {
		super(message);
	}
	
	@Override
	public String getMessage() {
		return "Exception: There is already an existing file for that author. File will be renamed as BU, and older BU files will be deleted!";	
	}
}
