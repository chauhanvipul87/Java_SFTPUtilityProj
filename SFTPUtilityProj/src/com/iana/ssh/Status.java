package com.iana.ssh;

/**
 * @author Vipul Chauhan
 * 
 */
public interface Status {

	public static final String ERROR				 = "ERROR";
	public static final String SUCCESS 				 = "SUCCESS";
	public static final String LOCAL_FILE_NOT_FOUND  = "Error. Local file not found.";
	public static final String SOURCE_FILE_NOT_FOUND = "Source file doesn't exist";
	public static final String FILE_UPLOADED 		 = "UPLOADED";
	public static final String FILE_MOVED			 = "MOVED";
	public static final String FILE_DOWNLOADED 		 = "DOWNLOADED";
	public static final String FILE_DELETED 		 = "DELETED";
	public static final String FAILED_TO_DELETE		 = "Fail to delete file.";	
}

