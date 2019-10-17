package fr.triedge.sbi.worker.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

	public static String readFile(File file, Charset encoding) 
			throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		return new String(encoded, encoding);
	}
	
	public static String readFile(File file) 
			throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		return new String(encoded);
	}
	
	public static boolean renameFile(File source, File dest) {
		return source.renameTo(dest);
	}
}
