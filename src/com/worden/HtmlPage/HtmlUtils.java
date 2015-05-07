package com.worden.HtmlPage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用正则表达式提到Html页面内容
 * @author Wordon
 * @version 1.0
 */
public class HtmlUtils {

    private String content = null ;
    
    public HtmlUtils() {
    }
    
    /**
     * 初始化HtmlUtils，并设置HTML内容
     * @param str HTML内容
     */
    public HtmlUtils(String str) {
    	setContent(str) ;
    }

    /**
     * 设置HTML内容变量
     * @param content HTML内容
     */
    public void setContent(String content) {
    	this.content = content ;
    }

    
    /**
     * 获取content变量
     * @return String
     */
    public String getContent() {
    	return this.content ;
    }
    
	/**
	* 打印content
	*/
    public void printContent() {
    	if( content == null || content.isEmpty() )  {
    		System.out.println("[ERROR] no content !");
    	} else {
    		System.out.println( content ) ;
    	}
    }
    
	/**
	* 获取content所有的网页超链接，使用通用正则表达式，以href开头
	* @return String[] 取得的urs数组
	*/
    public String[] getHtmlUrl() {
        ArrayList<String> list = new ArrayList<String>() ;
        String searchHtmlReg = "(?x)href=('|\")*((http://([\\w-]+\\.)+[\\w-]+(:[0-9]+)*)*(/)*([\\w-]+/)*([\\w-]+\\.(html|htm)))('|\")*";
        if( this.content == null )
        	return null ;
        Pattern pattern = Pattern.compile(searchHtmlReg);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()){
            list.add(matcher.group(2));
        }
        String[] urls = ( String[] ) list.toArray( new String[0] ) ;
        return urls ;
    }

	/**
	* 获取指定url的html页面里所有的正则表达式的值
	* @param searchReg 正则字符串
	* @param sectid 段号
	* @return String[] 取得的url数组
	*/
    public String[] getUrlByReg(String searchReg , int sectid) {
        ArrayList<String> list = new ArrayList<String>() ;
        if( this.content == null )
        	return null ;
        Pattern pattern = Pattern.compile(searchReg);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()){
        	list.add(matcher.group(sectid));
        }
        String[] urls = ( String[] ) list.toArray( new String[0] ) ;
        return urls ;
    }

	/**
	* 获取content里所有的资源url，取通用正则取以src开头 
	* @return String[] 取得的urs数组
	*/
    public String[] getAllRes() {
    	String searchHtmlReg = "(?x)src=('|\")*((http://([\\w-]+\\.)+[\\w-]+(:[0-9]+)*)*(/)*([\\w-]+/)*([\\w-]+)(\\.[\\w-]+))('|\")*(?x)";
    	return getUrlByReg( searchHtmlReg , 2 ) ;
    }
	
	/**
	* 判断content是否包含text
	* @param text 需要检查的内容
	* @return boolean
	*/
    public boolean isContainText( String text ) {
    	try {
        	if( content.indexOf( text ) != -1 ) return true ;
    	}catch( Exception e )
    	{
        	e.printStackTrace();
    	}
    	return false ;
	}
    
	/**
	* 获取content里Title
	* @return String
	*/
    public String getTitle() {
    	if( this.content == null )
    		return "" ;
    	String str = null ;
        int start = -1 ;
        int end = -1 ;
        start = content.indexOf("<title>") ;
        end = content.indexOf("</title>") ;
		if( start == -1 && end == -1 )
		{
			start = content.indexOf("<TITLE>") ;
			end = content.indexOf("</TITLE>") ;
		}

        if( start > -1 && end > -1 && end > start )
        {
               str = content.substring(start+7,end) ;
        }
        return str ;
    }
    
    /**
     * 根据原始Url处理取得的Url集，比如加上hostname等
     * @param firstUrl 原始Url
     * @param oldUrls 超链url集
     * @return String[] 处理后的Url集
     */
    public String[] CompleteUrls(String firstUrl , String[] oldUrls ) {
    	ArrayList<String> list = new ArrayList<String>() ;

		int ind = firstUrl.lastIndexOf('/');
		String urlhead = firstUrl ;
		if( ind > 6) urlhead = firstUrl.substring(0,ind);
		
		for( int i = 0 ; i < oldUrls.length ; i++ )
		{
			/** 去除重复的URL **/
			int flagRep = 0 ;
			for( int j = 0 ; j < i ; j++ )
			{
				if( oldUrls[i].equals(oldUrls[j]) ) 
				{
					flagRep = 1 ;
					break ;
				}
			}
			if( flagRep == 1 ) continue ;
			
			String new_url = oldUrls[i] ;
			if( oldUrls[i].toLowerCase().indexOf("http://") == 0 )
			{
				new_url = oldUrls[i] ;
			}
			else if ( oldUrls[i].indexOf("/") == 0 )
			{
				String ss = urlhead.substring( urlhead.indexOf("//")+2 ) ;
				if( ss.indexOf("/") == -1 )
					ss += "/" ;
				String hostname = ss.substring( 0 , ss.indexOf("/") );
				new_url =  "http://"+hostname+"/"+oldUrls[i] ;
			}
			else
			{
				new_url = urlhead+"/"+oldUrls[i] ;
			}
			list.add(DealWithUrl(new_url) );
		}
		return ( String[] ) list.toArray( new String[0] ) ;
    }
    
    /**
     * 对单个Url处理，比如处理//、/./和/../
     * @param oldUrl 原始url
     * @return String 新的url
     */
    private String DealWithUrl( String oldUrl ) {
		String url = oldUrl ;
		int index1 = url.lastIndexOf("//");
		while( index1 > 6 )
		{
			url = url.substring(0,index1) + url.substring(index1+1) ;
			index1 = url.lastIndexOf("//");
		}

		index1 = url.lastIndexOf("/./");
		while( index1 > 0 )
		{
			url = url.substring(0,index1) + url.substring(index1+2) ;
			index1 = url.lastIndexOf("/./") ;
		}

		index1 = url.indexOf("/../") ;
		while( index1 > 0 )
		{
			int index2 = url.substring(0,index1).lastIndexOf('/') ;
			url = url.substring(0,index2) + url.substring(index1+3) ;
			index1 = url.indexOf("/../") ;
		}
		return url ;
	}

		
}
