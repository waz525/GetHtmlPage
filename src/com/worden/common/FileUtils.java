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
 * �ļ�������
 * @author Wordon
 * @version 1.0
 */
public class FileUtils {

    /**
     * �ж��ļ��Ƿ����
     * @param filePath �ļ�ȫ·��
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
	 * ���ļ����ڣ���ɾ���ļ�
	 * @param filePath �ļ�ȫ·��
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
	 * ��ȡ�ļ����ݵ��ַ���
	 * @param filePath �ļ�ȫ·��
	 * @return String
	 */
	public static String ReadFileToString( String filePath) {
		return  ReadFileToString(filePath , "") ;
	}
	
	/**
	 * ��ȡ�ļ����ݵ��ַ���
	 * @param filePath �ļ�ȫ·��
	 * @param contentType �����ʽ
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
	 * ���ַ���д���ļ�,���������
	 * @param filePath  �ļ�ȫ·��
	 * @param content ����
	 */
	public static void WriteStringToFile( String filePath , String content ) {
		WriteStringToFile(filePath , content , 0) ;
	}
	
	/**
	 * ���ַ���д���ļ���֧��׷��
	 * @param filePath �ļ�ȫ·��
	 * @param content ����
	 * @param writeFlag д���ʶ��0���½���1��׷��
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
	 * ���ַ���׷��д���ļ�
	 * @param filePath �ļ�ȫ·��
	 * @param content ����
	 */
	public static void AddStringToFile(String filePath , String content) {
		WriteStringToFile(  filePath ,  content , 1) ;
	}

	/**
	 * ��byte����д���ļ�
	 * @param filePath �ļ�ȫ·��
	 * @param content ����
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
     * �ݹ�õ��ļ��б�
     * @param f	�ļ��࣬��ʼ�ļ�
     * @param fileList ����ļ��б�
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