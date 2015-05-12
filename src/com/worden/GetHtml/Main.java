package com.worden.GetHtml;


import com.worden.common.CommonUtils;
import com.worden.common.PropFileUtils;
import com.worden.db.MysqlUtils;

/**
 * 主进程
 * @author Wordon
 * @version 1.0
 */
public class Main {

	private String propFilePath="./GetHtml.prop" ;

	private GetHtmlThread[] ghThreads  = null  ;
	

	/**
	 * 获取空闲线程变量
	 * @return int
	 */
	private int GetValidThread()
	{
		int res_ind = -1 ;
		for( int i = 0 ; i < ghThreads.length ; i++ ) 
		{
			if( ghThreads[i]== null || ghThreads[i].t.isAlive() == false ) 
			{
				res_ind = i ;
				break ;
			}
		}
		return res_ind ;
	}
	
	/**
	 * 获取正在运行线路数
	 * @return int
	 */	
	private int GetRunningThreadNum()
	{
		int res = 0 ;
		for( int i = 0 ; i < ghThreads.length ; i++ )
		{
			if( ghThreads[i]!= null && ghThreads[i].t.isAlive() == true ) 
			{
				res++ ;
				
			}
			//System.out.println("   "+i+" ==> "+res +"");
		}
		return res ;
	}
	
	/**
	 * 开始启动任务
	 */
	public void StartGetTask() {

		PropFileUtils pfu = new PropFileUtils(propFilePath) ;
		MysqlUtils mysqlUtils = new MysqlUtils(pfu.GetProperty("DBHost"),pfu.GetProperty("DBUser"),
				pfu.GetProperty("DBPassword") , pfu.GetProperty("DBName"));
		
		int threadCount = mysqlUtils.QueryOneValueInt("ConfigTable", "CValue", "CKey = 'TaskNum'") ;
		ghThreads = new GetHtmlThread[threadCount] ;
		
		if( mysqlUtils.QueryCount("tasktable", "1=1") > 0 ) {
			for ( String taskId : mysqlUtils.QueryOneField("TaskTable", "TaskId", "Status=1") ) {
				while( GetValidThread() == -1 ) { }
				CommonUtils.PrintInfo("Start Task ! TaskID:"+taskId, new Exception()) ;
				ghThreads[GetValidThread()] = new GetHtmlThread(Integer.parseInt(taskId)) ;				
			}
		}
		
		//Wait for thread run over
		while( GetRunningThreadNum() >0 ) {}
		
	}
	
	
	public static void main(String[] args) {

		Main m = new Main() ;
		m.StartGetTask();
		CommonUtils.PrintInfo("Run Over in Main !", new Exception());
	}
}
