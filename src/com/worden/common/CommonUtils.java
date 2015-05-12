package com.worden.common;

public class CommonUtils {
	
	public static void PrintInfo(String str , Exception e) {
		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ") ;
		System.out.println( formatter.format(new java.util.Date() )+str+"\t ("+e.getStackTrace()[0].getFileName()+":"+e.getStackTrace()[0].getLineNumber()+")");
	}
	
	public static void PrintInfo(String VariableName , String VariableValue , Exception e) {
		
		PrintInfo(VariableName+" ==> \""+VariableValue+"\" !",e) ;
		
	}
	
	public static void PrintInfo(String VariableName , int VariableValue, Exception e) {
		
		PrintInfo(VariableName,""+VariableValue, e);
		
	}
	
	public static String GetCurrentTimeString() {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		return  formatter.format(new java.util.Date() );
	}
	
	public static void PrintError(String str , Exception e) {

		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ") ;
		System.err.println( formatter.format(new java.util.Date() )+str+"\t ("+e.getStackTrace()[0].getFileName()+":"+e.getStackTrace()[0].getLineNumber()+")"  ) ;
		
	}
}
