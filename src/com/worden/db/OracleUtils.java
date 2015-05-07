package com.worden.db;

import java.sql.*;

/**
 * Oracle�����ࣨ���ࣺDBUtils��
 * @author Wordon
 * @version 1.0
 */
public class OracleUtils extends DBUtils {

	public OracleUtils() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * ���ݸ�������ʼ��
	 * @param HostName ������
	 * @param SID ʵ����
	 * @param UserName �û���
	 * @param UserPasswd ����
	 */
	public OracleUtils( String HostName , String SID , String UserName,
			String UserPasswd) {
		ConnectDB(  HostName ,  SID , UserName ,  UserPasswd );
	}
	

	/**
	 * ���ݸ�������ʼ��
	 * @param SID ʵ����
	 * @param UserName �û���
	 * @param UserPasswd ����
	 */
	public OracleUtils( String SID , String UserName,
			String UserPasswd) {
		ConnectDB(   "localhost"  ,  SID , UserName ,  UserPasswd );
	}
	
	/**
	 * ���ݸ�������ʼ��
	 * @param UserName �û���
	 * @param UserPasswd ����
	 */
	public OracleUtils( String UserName,
			String UserPasswd) {
		ConnectDB(  "localhost" ,  "ora11g" , UserName ,  UserPasswd );
	}
	
	/**
	 * ���ݸ���������Oracle
	 * @param HostName ������
	 * @param SID ʵ����
	 * @param UserName �û���
	 * @param UserPasswd ����
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
	 * �жϱ��Ƿ����
	 * @param table_name ����
	 * @return boolean 
	 */
	public boolean isTableExist(String table_name) {
		if( QueryCount("user_tables", " table_name = '"+table_name+"'") == 1 ) {
			return true ;
		}
		return false ;
	}
}
