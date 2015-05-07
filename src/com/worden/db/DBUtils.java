package com.worden.db;

import java.sql.*;
import java.util.ArrayList;

/**
* 数据库操作基类
* @author Wordon
* @version 1.0
*/
public class DBUtils {

	private Connection conn = null;
	private Statement stmt = null ;

	/**
	* 获取Connection
	* @return Connection
	*/
	public Connection GetConnection() {
		return this.conn ;
	}
	
	/**
	* 获取Statement
	* @return Statement
	*/
	public Statement GetStatement() {
		return this.stmt ;
	}

	/**
	* 设置Connection
	* @param Connection
	*/
	public void SetConnection(Connection connection) {
		this.conn = connection ;
	}

	/**
	* 设置Statement
	*/
	public void SetStatement( Statement statement ) {
		this.stmt = statement ;
	}

	/**
	* 关闭打开的句柄
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
	* 根据sql进行查询
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
	 * 根据表名和字段进行查询
	 * @param table_name 表名
	 * @param fileds 字段列表
	 * @return ResultSet 变量
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
	* 根据表名、字段和条件进行查询
	 * @param table_name 表名
	 * @param fileds 字段列表
	 * @param qualification 查询条件
	* @return ResultSet 变量
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
	* 根据sql进行Update
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
	* 根据表名、字段和条件进行Update
	 * @param table_name 表名
	 * @param content 字段与值
	 * @param qualification 条件
	* @return int 修改记录条数
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
	* 根据表名、字段和值进行Insert
	 * @param table_name 表名
	 * @param fields 字段列表
	 * @param values 值列表
	* @return int 成功条数
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
	* 根据表名、条件进行Delete
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
	* 查询意表记录数量
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
	* 查询符合条件的记录数量
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
	* 查询符合条件的单字段记录
	 * @param table_name 表名
	 * @param filedName 字段名
	 * @param qualification 条件
	* @return String[] 结果集
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
	* 查询符合条件的单个字段的单个值
	 * @param table_name 表名
	 * @param filedName 字段名
	 * @param qualification 条件
	* @return String 第一个结果值
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
	* 通过表名和字段列表进行建表
	* @return int
	*/
	public int CreateTable(String table_name , String fileds) {
		String sql = "create table "+table_name+" ( "+fileds+" ) " ;
		return Update(sql);
	}

	/**
	* 通过表名进行删表
	* @return int
	*/
	public int DropTable(String table_name) {
		String sql = "drop table "+table_name ;
		return Update(sql);
	}
}
