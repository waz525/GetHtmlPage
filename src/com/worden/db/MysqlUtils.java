package com.worden.db;

import java.sql.*;

/**
 * Mysql�����ࣨ���ࣺDBUtils��
 * @author Wordon
 * @version 1.0
 */
public class MysqlUtils extends DBUtils {

	public MysqlUtils() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * ���ݸ�������ʼ��
	 * @param HostName ������/IP
	 * @param UserName �û���
	 * @param UserPasswd ����
	 * @param DBName ��ռ���
	 */
	public MysqlUtils( String HostName, String UserName,
			String UserPasswd, String DBName) {
		ConnectDB(  HostName ,  UserName ,  UserPasswd ,  DBName);
	}

	/**
	 * ���ݸ�������ʼ��
	 * @param UserName �û���
	 * @param UserPasswd ����
	 * @param DBName ��ռ���
	 */
	public MysqlUtils( String UserName,
			String UserPasswd, String DBName) {
		ConnectDB(  "localhost" ,  UserName ,  UserPasswd ,  DBName);
	}
	
	/**
	 * ���ݸ���������Mysql
	 * @param HostName ������/IP
	 * @param UserName �û���
	 * @param UserPasswd ����
	 * @param DBName ��ռ���
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
	 * �жϱ��Ƿ����
	 * @param tablespace_name ��ռ��
	 * @param table_name ����
	 * @return boolean
	 */
	public boolean isTableExist(String tablespace_name , String table_name) {
		if( QueryCount("information_schema.TABLES", "table_schema ='"+tablespace_name+"' and table_name = '"+table_name+"'") == 1 ) {
			return true ;
		}
		return false ;
	}
}
