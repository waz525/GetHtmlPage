package com.worden.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropFileUtils {
	
	Properties prop = null ;
	
	public PropFileUtils(String filePath) {
		ReadPropFile(filePath) ;
	}
	
	public void ReadPropFile(String filePath) {
		prop = null ;
		if( ! FileUtils.isFile(filePath)) {
			System.out.println( "[ERROR] "+filePath+" is not a file !!!" ) ;
			return ;
		}
		prop = new Properties() ;

		try {
			InputStream is = new BufferedInputStream(new FileInputStream(filePath)) ;
			prop.load(is) ;
			is.close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String GetProperty(String key) {
		if( prop == null ) return null ;
		String temp ; 
		temp = prop.getProperty(key);
		if(  temp == null || temp.length() == 0 ) temp ="" ;
		return temp ;
	}
	
	public int GetPropertyInt(String key) {
		if( prop == null ) return  -999999999;
		String value = GetProperty(key) ;
		if( value.equalsIgnoreCase("")) return -999999999 ;
		return Integer.parseInt(value) ;
	}
	
	public boolean isEnable() {
		if( this.prop != null && ! this.prop.isEmpty()) {
			return true;
		} else {
			return false ;
		}
	}
	
	public boolean isHaveKey(String key) {
		return prop.containsKey(key) ;
	}
}
