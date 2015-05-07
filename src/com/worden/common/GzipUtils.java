package com.worden.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 针对byte数组的gzip的压缩与解压
 * @author Wordon
 *
 */
public class GzipUtils {
	
	/**
	 * 对byte数组进行GZIP压缩
	 * @param str byte数组
	 * @return byte[]
	 */
	public static byte[] CompressString(byte[] str ) {
		if (str == null || str.length == 0) { 
			return str ;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		GZIPOutputStream gzip;
		
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str); 
			gzip.close();
			return out.toByteArray() ;
		} catch (IOException e) { 
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对byte数组进行GZIP解压缩
	 * @param str byte数组
	 * @return byte[]
	 */
	public static byte[] UncompressString(byte[] str) {
		 if (str == null || str.length == 0) {
			 return str;   
		 }
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 try {
			ByteArrayInputStream in = new ByteArrayInputStream(str);
			GZIPInputStream gunzip = new GZIPInputStream(in);
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer))>= 0) { 
				out.write(buffer, 0, n);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return out.toByteArray() ; 
	}
}
