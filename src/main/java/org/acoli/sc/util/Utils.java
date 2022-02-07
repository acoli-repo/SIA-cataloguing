package org.acoli.sc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class Utils {
	
	
	public static File getResouceFile(String fileName) {
		
		return stream2File(ClassLoader.getSystemClassLoader().getResourceAsStream(fileName), fileName);
	}
	
	
	public static File stream2File (InputStream in, String fileName) {
		  
		   String ext = getFileExtensions(fileName);
		  
		   try {
			   
			   File tempFile = File.createTempFile(fileName, ext);
			   tempFile.deleteOnExit();
			   
			   FileOutputStream out = new FileOutputStream(tempFile);
		   
		       IOUtils.copy(in, out);
		       
		       return tempFile;
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
		  
		return null;
		
	}
	
	
	public static String getFileExtensions(String fileName) {
		
		String ext = "";
		
		while (!fileName.equals(FilenameUtils.getBaseName(fileName))) {
			ext ="."+FilenameUtils.getExtension(fileName)+ext;
			fileName = FilenameUtils.getBaseName(fileName);
		}
		
		return ext;
	}
	

}
