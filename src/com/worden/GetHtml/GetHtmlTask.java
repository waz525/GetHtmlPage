package com.worden.GetHtml;

import java.util.Random;

import com.worden.HtmlPage.HtmlUtils;
import com.worden.HtmlPage.HttpUtils;
import com.worden.HtmlPage.ParseHtml;
import com.worden.common.CommonUtils;
import com.worden.common.FileUtils;
import com.worden.common.PropFileUtils;
import com.worden.db.MysqlUtils;
import com.worden.index.DocumentUtils;
import com.worden.index.IndexProducer;

/**
 * ����Mysql������ã���ȡ��������ID�µ���ҳ������������
 * @author Wordon
 * @version 1.0
 */
public class GetHtmlTask {

	private String nPropFilePath="./GetHtml.prop" ;
	
	private int nTaskID = -1 ;
	
	MysqlUtils nMysqlUtils = null ;
	IndexProducer nIndexProducer = new IndexProducer();
	
	private String nFirstUrl = "" ;
	private int nDepth = 20 ;
	private String nRegUrlExpression = ""  ;
	private int nRegUrlSectID = 0;
	
	private String nFilterList = "+-" ;
	private String nPageTableName = "" ;
	private String nUserAgent = "" ; 
	private boolean isSaveHtml = false ;
	private String nHtmlPath = "" ;
	private String nIndexPath = "" ;
	
	
	GetHtmlTask( int taskId) {
		nTaskID = taskId ;
		nPageTableName="WebSite_"+nTaskID+"_PageTable" ;
		StartTask();
	}
	
	/**
	 * ��ʼ��������
	 */
	public void StartTask() {
		
		GetPropFromDB();
		
		
		nIndexProducer.InitIndexWriter(nIndexPath );
		
		GetHtml(this.nFirstUrl,this.nDepth) ;
		
		nIndexProducer.CloseIndexWriter();
		
		
	}
	
	/**
	 * �Եݹ�ķ�ʽ��ȡ��ҳ
	 * @param url ��ҳUrl
	 * @param depth ���>-1
	 */
	public void GetHtml(String url , int depth) {
		CommonUtils.PrintInfo( "Depth "+depth+" : "+url , new Exception());
		if( isUrlRepeate(url) ) {
			CommonUtils.PrintError("Url repeate ! "+url, new Exception()) ;
			return  ;
		}
		String PageId = GeneratePageId(this.nTaskID) ;
		
		HttpUtils httpUtils = new HttpUtils() ;
		ParseHtml parseHtml = new ParseHtml() ;	
		HtmlUtils htmlUtils = new HtmlUtils() ;
		
		httpUtils.setUserAgent(nUserAgent);
		httpUtils.getHttpCode(url);
		parseHtml.createParserByContent(httpUtils.getContent(), httpUtils.getContentType());
		htmlUtils.setContent(httpUtils.getContent());
		
		DocumentUtils doc = new DocumentUtils() ;
		doc.AddStringField("PageId", PageId);
		doc.AddStringField("url", url);
		doc.AddTextField("content", parseHtml.getExtractedText());
		nIndexProducer.AddDocument(doc.GetDocument());
		
		if( isSaveHtml ) {
			FileUtils.WriteBytesToFile(nHtmlPath+"/"+PageId+".html", httpUtils.getFileContent());
		}
		
		nMysqlUtils.Insert(nPageTableName, "PageId,Url,GetTime" , " '"+PageId+"' , '"+url+"' , '"+CommonUtils.GetCurrentTimeString()+"' ");
		
		
		if ( depth > 0  ) {
			String[] getHtmlUrls = htmlUtils.getUrlByReg(nRegUrlExpression, nRegUrlSectID) ;
			String[] htmlUrls = htmlUtils.CompleteUrls(url, getHtmlUrls);
			for( int i = 0 ; i < htmlUrls.length ; i++ ) {
				GetHtml( htmlUrls[i] , depth -1 ) ;
			}
		}
		
		
		
	}
	
	/**
	 * ����PageId
	 * @param nTaskID ����ID
	 * @return String
	 */
	private String GeneratePageId(int nTaskID) {
		String rst  ;
		Random rdm = new Random(System.currentTimeMillis());
		rst=nTaskID+"_"+System.currentTimeMillis()/1000+"_"+Math.abs(rdm.nextInt())%1000 ;
		return rst ;
	}
	
	/**
	 * �ж�Url�Ƿ��ظ�
	 * @param url Url
	 * @return
	 */
	private boolean isUrlRepeate(String url) {
		if( this.nFilterList.indexOf("+"+url+"-") != -1 ) {
			return true ;
		}
		
		if( this.nMysqlUtils.QueryCount(nPageTableName , "Url = '"+url+"'") > 0 ) return true ;
		
		return false ;
		
	}
	
	/**
	 * ����Mysql��������ã���ʼ����˽�б���
	 */
	public void GetPropFromDB() {
		
		PropFileUtils pfu = new PropFileUtils(nPropFilePath) ;
		
		nMysqlUtils = new MysqlUtils(pfu.GetProperty("DBHost"),pfu.GetProperty("DBUser"),
				pfu.GetProperty("DBPassword") , pfu.GetProperty("DBName"));
		
		isSaveHtml = nMysqlUtils.QueryOneValueInt("ConfigTable", "CValue", "CKey = 'SaveHtmlFile' ")==1;
		
		nFirstUrl = nMysqlUtils.QueryOneValue("TaskConfigTable", "FirstUrl", "TaskId = "+nTaskID) ;
		nDepth = nMysqlUtils.QueryOneValueInt("TaskConfigTable", "Depth", "TaskId = "+nTaskID) ;
		nRegUrlExpression = nMysqlUtils.QueryOneValue("TaskConfigTable", "RegUrlExpression", "TaskId = "+nTaskID) ;
		nRegUrlSectID =  nMysqlUtils.QueryOneValueInt("TaskConfigTable", "RegUrlSectID", "TaskId = "+nTaskID) ;
		
		if( nMysqlUtils.QueryCount("ConfigTable", "CKey = 'UserAgent' ") > 0 ) {
			nUserAgent=nMysqlUtils.QueryOneValue("ConfigTable", "CValue",  "CKey = 'UserAgent' ");
		}
		
		nIndexPath = ""+nMysqlUtils.QueryOneValue("ConfigTable", "CValue", "CKey = 'WorkDir'")+"/index/" 
				+nMysqlUtils.QueryOneValue("TaskTable","WebSiteName","TaskId = "+nTaskID)+"/" ;
		FileUtils.Mkdirs(nIndexPath);
		
		if( isSaveHtml ) {
			nHtmlPath = ""+nMysqlUtils.QueryOneValue("ConfigTable", "CValue", "CKey = 'WorkDir'")+"/html/" 
					+nMysqlUtils.QueryOneValue("TaskTable","WebSiteName","TaskId = "+nTaskID)+"/" ;
			FileUtils.Mkdirs(nHtmlPath);
		}
		
		if( ! nMysqlUtils.isTableExist(pfu.GetProperty("DBName"), nPageTableName))  {
			nMysqlUtils.CreateTable(nPageTableName, " PageId varchar(100) not null , Url varchar(1000) not null , GetTime varchar(30) ,OrgFile varchar(1000),isHaveImage int " ) ;
		}
		
		/*
		Exception e = new Exception() ;
		CommonUtils.PrintInfo("nFirstUrl", nFirstUrl , e);
		CommonUtils.PrintInfo("nDepth", nDepth, e);
		CommonUtils.PrintInfo("nRegUrlExpression", nRegUrlExpression, e);
		CommonUtils.PrintInfo("nRegUrlSectID", nRegUrlSectID, e);
		CommonUtils.PrintInfo("nUserAgent", nUserAgent, e);
		CommonUtils.PrintInfo("nIndexPath", nIndexPath, e);
		CommonUtils.PrintInfo("nHtmlPath", nHtmlPath, e);
		*/
		
	}
}
