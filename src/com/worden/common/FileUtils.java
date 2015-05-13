package com.worden.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;

/**
 * 文件处理类
 * @author Wordon
 * @version 1.0
 */
public class FileUtils {

    /**
     * 判断文件是否存在
     * @param filePath 文件全路径
     * @return boolean
     */
	public static boolean isFile( String filePath )
	{
		File file = new File(filePath);
		if( file.isFile() )
		{
			return true ;
		}
		else
		{
			return false ;
		}
	}
	

	/**
	 * 若文件存在，则删除文件
	 * @param filePath 文件全路径
	 * @return boolean
	 */
	public static void RemoveFile( String filePath ) {
		File file = new File(filePath);
		
		if( file.exists() ) {
			if(file.isFile() ) {
				file.delete();
			} else if( file.isDirectory()) {
				File[] files = file.listFiles() ;
				for( File nFile:files) {
					RemoveFile(nFile.getAbsolutePath());
				}
				file.delete();
			}
		}
		
	}
	
	public static boolean Mkdirs(String filePath) {
		File file = new File(filePath);
		if( !file.exists()) {
			return file.mkdirs();
		}
		return true ;
	}
	

	/**
	 * 读取文件内容到字符串
	 * @param filePath 文件全路径
	 * @return String
	 */
	public static String ReadFileToString( String filePath) {
		return  ReadFileToString(filePath , "") ;
	}
	
	/**
	 * 读取文件内容到字符串
	 * @param filePath 文件全路径
	 * @param contentType 编码格式
	 * @return String
	 */
	public static String ReadFileToString( String filePath , String contentType )
	{
		String rst = "" ;
		Reader reader = null;
		try
		{
			int charread = 0;
			if( contentType != null && contentType.length() > 0 ) {
				reader = new InputStreamReader(new FileInputStream(filePath) , contentType );
			} else {
				reader = new InputStreamReader(new FileInputStream(filePath) );
			}
			
			while ((charread = reader.read()) != -1)
			{
				rst = rst + (char)charread ;
			}
			reader.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			System.out.println(e);
		}
		finally 
		{
			if (reader != null) 
			{
				try 
				{ 
					reader.close();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
		}
		return rst ;
	}
	
	/**
	 * 将字符串写入文件,存在则清空
	 * @param filePath  文件全路径
	 * @param content 内容
	 */
	public static void WriteStringToFile( String filePath , String content ) {
		WriteStringToFile(filePath , content , 0) ;
	}
	
	/**
	 * 将字符串写入文件，支持追加
	 * @param filePath 文件全路径
	 * @param content 内容
	 * @param writeFlag 写入标识，0：新建；1：追加
	 */
	public static void WriteStringToFile( String filePath , String content , int writeFlag)
	{
		try
		{
			File outFile = new File( filePath ) ;
			FileWriter fwstr ;
			if( writeFlag == 1 ) {
				fwstr = new FileWriter( outFile  , true ) ;
			} else {
				fwstr = new FileWriter( outFile ) ;
			}
			
			fwstr.write( content ) ;
			fwstr.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**
	 * 将字符串追加写入文件
	 * @param filePath 文件全路径
	 * @param content 内容
	 */
	public static void AddStringToFile(String filePath , String content) {
		WriteStringToFile(  filePath ,  content , 1) ;
	}

	/**
	 * 将byte数组写入文件
	 * @param filePath 文件全路径
	 * @param content 内容
	 */
    public static void WriteBytesToFile(String filePath , byte[] content ) {
    	try {
			OutputStream os = new FileOutputStream(filePath);
			os.write(content);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 递归得到文件列表
     * @param f	文件类，起始文件
     * @param fileList 结果文件列表
     */
    public static void listFile(File f,List<String> fileList){
		if(f.isDirectory()){
			File[] files = f.listFiles();
			for(int i=0;i < files.length ; i++ )
			{
				listFile(files[i],fileList) ;
			}
		}else{
				fileList.add(f.getAbsolutePath());
		}
	}
}