package com.worden.db;

import java.sql.*;

/**
 * Oracle访问类（父类：DBUtils）
 * @author Wordon
 * @version 1.0
 */
public class OracleUtils extends DBUtils {

	public OracleUtils() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 根据各参数初始化
	 * @param HostName 主机名
	 * @param SID 实例名
	 * @param UserName 用户名
	 * @param UserPasswd 密码
	 */
	public OracleUtils( String HostName , String SID , String UserName,
			String UserPasswd) {
		ConnectDB(  HostName ,  SID , UserName ,  UserPasswd );
	}
	

	/**
	 * 根据各参数初始化
	 * @param SID 实例名
	 * @param UserName 用户名
	 * @param UserPasswd 密码
	 */
	public OracleUtils( String SID , String UserName,
			String UserPasswd) {
		ConnectDB(   "localhost"  ,  SID , UserName ,  UserPasswd );
	}
	
	/**
	 * 根据各参数初始化
	 * @param UserName 用户名
	 * @param UserPasswd 密码
	 */
	public OracleUtils( String UserName,
			String UserPasswd) {
		ConnectDB(  "localhost" ,  "ora11g" , UserName ,  UserPasswd );
	}
	
	/**
	 * 根据各参数连接Oracle
	 * @param HostName 主机名
	 * @param SID 实例名
	 * @param UserName 用户名
	 * @param UserPasswd 密码
	 */
	public void ConnectDB(  String HostName , String SID , String UserName , String UserPasswd ) {
		String url="jdbc:oracle:thin:@"+HostName+":1521:"+SID;
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			Connection conn = DriverManager.getConnection(url,UserName,UserPasswd);
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			
			this.SetConnection(conn);
			this.SetStatement(stmt);
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断表是否存在
	 * @param table_name 表名
	 * @return boolean 
	 */
	public boolean isTableExist(String table_name) {
		if( QueryCount("user_tables", " table_name = '"+table_name+"'") == 1 ) {
			return true ;
		}
		return false ;
	}
}
