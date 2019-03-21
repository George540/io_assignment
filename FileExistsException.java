package Main;

import java.io.*;

public class FileExistsException extends Exception {
	
	public FileExistsException() {
		super("Exception. There is already an existing file for that author. File will be renamed as BU, and older BU files wil be deleted!");
	}
	
	public FileExistsException(String message) {
		super(message);
	}
	
	@Override
	public String getMessage() {
		return "Exception: There is already an existing file for that author. File will be renamed as BU, and older BU files will be deleted!";	
	}
	
}
