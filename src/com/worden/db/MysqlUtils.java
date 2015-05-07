package com.worden.db;

import java.sql.*;

/**
 * Mysql访问类（父类：DBUtils）
 * @author Wordon
 * @version 1.0
 */
public class MysqlUtils extends DBUtils {

	public MysqlUtils() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 根据各参数初始化
	 * @param HostName 主机名/IP
	 * @param UserName 用户名
	 * @param UserPasswd 密码
	 * @param DBName 表空间名
	 */
	public MysqlUtils( String HostName, String UserName,
			String UserPasswd, String DBName) {
		ConnectDB(  HostName ,  UserName ,  UserPasswd ,  DBName);
	}

	/**
	 * 根据各参数初始化
	 * @param UserName 用户名
	 * @param UserPasswd 密码
	 * @param DBName 表空间名
	 */
	public MysqlUtils( String UserName,
			String UserPasswd, String DBName) {
		ConnectDB(  "localhost" ,  UserName ,  UserPasswd ,  DBName);
	}
	
	/**
	 * 根据各参数连接Mysql
	 * @param HostName 主机名/IP
	 * @param UserName 用户名
	 * @param UserPasswd 密码
	 * @param DBName 表空间名
	 */
	public void ConnectDB(  String HostName , String UserName , String UserPasswd , String DBName) {
		String mysql_url="jdbc:mysql://"+HostName+"/"+DBName+"?user="+UserName+"&password="+UserPasswd;
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(mysql_url);
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
	 * @param tablespace_name 表空间表
	 * @param table_name 表名
	 * @return boolean
	 */
	public boolean isTableExist(String tablespace_name , String table_name) {
		if( QueryCount("information_schema.TABLES", "table_schema ='"+tablespace_name+"' and table_name = '"+table_name+"'") == 1 ) {
			return true ;
		}
		return false ;
	}
}
