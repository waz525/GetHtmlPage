package com.worden.GetHtml;

/**
 * GetHtml �߳�
 * @author Wordon
 * @version 1.0
 */
public class GetHtmlThread  implements Runnable {

	private int nTaskId = -1 ;
	Thread t ;
	
	/**
	 * ��taskId��ʼ��GetHtmlThread
	 * @param taskId ����ID
	 */
	GetHtmlThread(int taskId ) {
		this.nTaskId = taskId ;
		t = new Thread(this , "GetHtmlTask_"+nTaskId);
		t.start();
	}
	
	/**
	 * ��ȡTaskID
	 * @return int
	 */
	public int GetTaskId() {
		return this.nTaskId ;
	}
	
	/**
	 * �����߳�
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
