package com.worden.GetHtml;

/**
 * GetHtml 线程
 * @author Wordon
 * @version 1.0
 */
public class GetHtmlThread  implements Runnable {

	private int nTaskId = -1 ;
	Thread t ;
	
	/**
	 * 以taskId初始化GetHtmlThread
	 * @param taskId 任务ID
	 */
	GetHtmlThread(int taskId ) {
		this.nTaskId = taskId ;
		t = new Thread(this , "GetHtmlTask_"+nTaskId);
		t.start();
	}
	
	/**
	 * 获取TaskID
	 * @return int
	 */
	public int GetTaskId() {
		return this.nTaskId ;
	}
	
	/**
	 * 启动线程
	 */
	public void run() {
		if( nTaskId != -1 ) {
			new GetHtmlTask(nTaskId) ;
			nTaskId=-1;
		} else {
			System.out.println("Error(GetHtmlTask) : nTaskId is -1 !!!") ;
		}
	}

}
