package com.worden.HtmlPage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ʹ��������ʽ�ᵽHtmlҳ������
 * @author Wordon
 * @version 1.0
 */
public class HtmlUtils {

    private String content = null ;
    
    public HtmlUtils() {
    }
    
    /**
     * ��ʼ��HtmlUtils��������HTML����
     * @param str HTML����
     */
    public HtmlUtils(String str) {
    	setContent(str) ;
    }

    /**
     * ����HTML���ݱ���
     * @param content HTML����
     */
    public void setContent(String content) {
    	this.content = content ;
    }

    
    /**
     * ��ȡcontent����
     * @return String
     */
    public String getContent() {
    	return this.content ;
    }
    
	/**
	* ��ӡcontent
	*/
    public void printContent() {
    	if( content == null || content.isEmpty() )  {
    		System.out.println("[ERROR] no content !");
    	} else {
    		System.out.println( content ) ;
    	}
    }
    
	/**
	* ��ȡcontent���е���ҳ�����ӣ�ʹ��ͨ��������ʽ����href��ͷ
	* @return String[] ȡ�õ�urs����
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
	* ��ȡָ��url��htmlҳ�������е�������ʽ��ֵ
	* @param searchReg �����ַ���
	* @param sectid �κ�
	* @return String[] ȡ�õ�url����
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
	* ��ȡcontent�����е���Դurl��ȡͨ������ȡ��src��ͷ 
	* @return String[] ȡ�õ�urs����
	*/
    public String[] getAllRes() {
    	String searchHtmlReg = "(?x)src=('|\")*((http://([\\w-]+\\.)+[\\w-]+(:[0-9]+)*)*(/)*([\\w-]+/)*([\\w-]+)(\\.[\\w-]+))('|\")*(?x)";
    	return getUrlByReg( searchHtmlReg , 2 ) ;
    }
	
	/**
	* �ж�content�Ƿ����text
	* @param text ��Ҫ��������
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
	* ��ȡcontent��Title
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
     * ����ԭʼUrl����ȡ�õ�Url�����������hostname��
     * @param firstUrl ԭʼUrl
     * @param oldUrls ����url��
     * @return String[] ������Url��
     */
    public String[] CompleteUrls(String firstUrl , String[] oldUrls ) {
    	ArrayList<String> list = new ArrayList<String>() ;

		int ind = firstUrl.lastIndexOf('/');
		String urlhead = firstUrl ;
		if( ind > 6) urlhead = firstUrl.substring(0,ind);
		
		for( int i = 0 ; i < oldUrls.length ; i++ )
		{
			/** ȥ���ظ���URL **/
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
     * �Ե���Url�������紦��//��/./��/../
     * @param oldUrl ԭʼurl
     * @return String �µ�url
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
