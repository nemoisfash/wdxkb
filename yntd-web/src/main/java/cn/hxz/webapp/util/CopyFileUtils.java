package cn.hxz.webapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.Properties;

public class CopyFileUtils{
	
	private static final String CONFIG_FILE = "copyfile/copyfile.properties";
	
	private static final String RESOURCE = "resource";
	
	private static final String TARGET = "target";
 	
	public static String getValue(String key) {
		Properties prop = new Properties();
		InputStream in = CopyFileUtils.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
		try {
			prop.load(new InputStreamReader(in, "utf-8"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "";

		}
		return prop.getProperty(key);
	}
	
	
	public static Boolean getResource(){
		boolean flag = true;
		File file = new File(getValue(RESOURCE));
		if(!file.exists()){
			System.out.println("源文件不存在");
			flag=false;
		} 
		return flag;
	}
	
	public static void getTarget() throws IOException{
		boolean flag = true;
		File file = new File(TARGET);
		if(file.exists()){
			file.delete();
			file.createNewFile();
		}
	}
	
	@SuppressWarnings("resource")
	public static Boolean copyFileByNio() throws IOException {
		Boolean success = true;
		if(getResource()){
			getTarget();
			FileChannel inputChannel = null;    
		    FileChannel outputChannel = null;
		 try{
			  inputChannel = new FileInputStream(getValue(RESOURCE)).getChannel();
			  outputChannel = new FileOutputStream(TARGET).getChannel();
			  outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		  }catch (IOException e) {
			  success=false;
		   } finally {
		   		try {
					inputChannel.close();
					outputChannel.close();
				} catch (IOException e) {
						e.printStackTrace();
				}
		}
		 			
	}
		return success;
	}
	
}
