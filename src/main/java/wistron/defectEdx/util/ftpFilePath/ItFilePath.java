package wistron.defectEdx.util.ftpFilePath;

public class ItFilePath {
	public static String getFileName(String filePath) {
		return filePath.substring(filePath.lastIndexOf("/")+1,filePath.length());
	}
	
	public static String getFileDirectory(String filePath) {
		return filePath.substring(0,filePath.lastIndexOf("/")+1);
	}
}
