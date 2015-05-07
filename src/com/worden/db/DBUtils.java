package com.worden.db;

import java.sql.*;
import java.util.ArrayList;

/**
* ���ݿ��������
* @author Wordon
* @version 1.0
*/
public class DBUtils {

	private Connection conn = null;
	private Statement stmt = null ;

	/**
	* ��ȡConnection
	* @return Connection
	*/
	public Connection GetConnection() {
		return this.conn ;
	}
	
	/**
	* ��ȡStatement
	* @return Statement
	*/
	public Statement GetStatement() {
		return this.stmt ;
	}

	/**
	* ����Connection
	* @param Connection
	*/
	public void SetConnection(Connection connection) {
		this.conn = connection ;
	}

	/**
	* ����Statement
	*/
	public void SetStatement( Statement statement ) {
		this.stmt = statement ;
	}

	/**
	* �رմ򿪵ľ��
	*/
	public void Close()
	{
		try{
			if( this.stmt != null )
			{
				stmt.close();
				stmt = null ;				
			}
			if( this.conn != null )
			{
				conn.close() ;
				conn = null ;
			}
		}catch( SQLException e )
		{
			e.printStackTrace() ;
		}
	}

	/**
	* ����sql���в�ѯ
	* @return ResultSet
	*/
	public ResultSet Query( String sql )
	{
		try {
			return stmt.executeQuery(sql) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���ݱ������ֶν��в�ѯ
	 * @param table_name ����
	 * @param fileds �ֶ��б�
	 * @return ResultSet ����
	 */
	public ResultSet Query(String table_name ,  String fileds ) {
		String sql = "select "+fileds+" from "+table_name ;
		try {
			return stmt.executeQuery(sql) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	* ���ݱ������ֶκ��������в�ѯ
	 * @param table_name ����
	 * @param fileds �ֶ��б�
	 * @param qualification ��ѯ����
	* @return ResultSet ����
	*/
	public ResultSet Query( String table_name , String fileds , String qualification )	{
		String sql = "select "+fileds+" from "+table_name+" where "+qualification ;
		try {
			return stmt.executeQuery(sql) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	* ����sql����Update
	* @return int
	*/
	public int Update( String sql ) {
		int rst = 0 ;
		try {
			rst = stmt.executeUpdate( sql ) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rst ;
	}

	/**
	* ���ݱ������ֶκ���������Update
	 * @param table_name ����
	 * @param content �ֶ���ֵ
	 * @param qualification ����
	* @return int �޸ļ�¼����
	*/
	public int Update( String table_name , String content , String qualification ) {
		String sql = "update "+table_name+" set "+content+" where "+qualification ;
		int rst = 0 ;
		try {
			rst = stmt.executeUpdate( sql ) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rst ;
	}

	/**
	* ���ݱ������ֶκ�ֵ����Insert
	 * @param table_name ����
	 * @param fields �ֶ��б�
	 * @param values ֵ�б�
	* @return int �ɹ�����
	*/
	public int Insert( String table_name , String fields , String values ) {
		String sql = "insert into "+table_name+" ( "+fields+" ) values ( "+values+" )" ;
		int rst = 0 ;
		try {
			rst = stmt.executeUpdate( sql ) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rst ;
	}

	/**
	* ���ݱ�������������Delete
	* @return int
	*/
	public int Delete( String table_name , String qualification ) 
	{
		String sql = "delete from "+table_name+" where "+qualification+" ";
		int rst = 0 ;
		try {
			rst = stmt.executeUpdate( sql ) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rst ;
	}

	/**
	* ��ѯ����¼����
	* @param table_name
	* @return int
	*/
	public int QueryTableCount( String table_name ) {
		String sql = "select count(*) from "+table_name ;
		ResultSet rs;
		int count = 0 ;
		try {
			rs = stmt.executeQuery(sql);
			rs.next() ;
			count = rs.getInt(1) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count ;
	}

	/**
	* ��ѯ���������ļ�¼����
	* @return int
	*/
	public int QueryCount( String table_name , String qualification ) {
		String sql = "select count(*) from "+table_name+" where "+qualification ;
		ResultSet rs;
		int count = 0 ;
		try {
			rs = stmt.executeQuery(sql);
			rs.next() ;
			count = rs.getInt(1) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count ;
	}
	
	/**
	* ��ѯ���������ĵ��ֶμ�¼
	 * @param table_name ����
	 * @param filedName �ֶ���
	 * @param qualification ����
	* @return String[] �����
	*/
	public String[] QueryOneField(String table_name , String filedName , String qualification) {

		ArrayList<String> list = new ArrayList<String>() ;
		try {
			ResultSet rs = Query(table_name , filedName, qualification) ;
			while( rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list.toArray(new String[0]) ;
		
	}

	/**
	* ��ѯ���������ĵ����ֶεĵ���ֵ
	 * @param table_name ����
	 * @param filedName �ֶ���
	 * @param qualification ����
	* @return String ��һ�����ֵ
	*/
	public String QueryOneValue(String table_name , String filedName , String qualification) {
		String rst = null ;
		try {
			ResultSet rs = Query(table_name, filedName, qualification) ;
			rs.next();
			rst = rs.getString(1) ;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rst ;
	}

	/**
	* ͨ���������ֶ��б���н���
	* @return int
	*/
	public int CreateTable(String table_name , String fileds) {
		String sql = "create table "+table_name+" ( "+fileds+" ) " ;
		return Update(sql);
	}

	/**
	* ͨ����������ɾ��
	* @return int
	*/
	public int DropTable(String table_name) {
		String sql = "drop table "+table_name ;
		return Update(sql);
	}
}
