package com.worden.HtmlPage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import com.worden.common.*;

/**
 * 获取网页内容
 * @author Wordon
 * @version 1.0
 */
public class HttpUtils {

	/**
	 * 存放从服务端获取的数据，若网页压缩则为解压缩后的数据
	 */
	private byte[] fileContent = null ;
	
	/**
	 * 从服务器接收数据长度，若网页压缩则长度与fileContent不一致
	 */
	private int fileLength = -1 ;
	
	/**
	 * 网页编码格式
	 */
	private String contentType = null ;
	
	/**
	 * 客户端类型，即UserAgent段
	 */
	private String userAgent = "Wget/1.10.2 (Red Hat modified)" ;

    
    /**
     * 取得文件长度
     * @return int
     */
    public int getFileLength() {
    	return this.fileLength ;
    }
    
    /**
     * 取得编码类型
     * @return String
     */
    public String getContentType() {
    	int ind = this.contentType.lastIndexOf('=') ;
    	return this.contentType.substring(ind+1) ;
    }
    
    /**
     * 取得网页原始数据
     * @return byte[]
     */
    public byte[] getFileContent() {
    	return this.fileContent ;
    }
    
    /**
     * 取得网页数据，将byte数组转换成String
     * @return String
     */
    public String getContent() {
    	try {
			return new String( this.fileContent ,getContentType()) ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return "" ;
    }
	
    /**
     * 设置UserAgent
     * @param ua UserAgent字符串
     */
    public void setUserAgent(String ua ) {
    	this.userAgent = ua ;    	
    }
    /**
     * 取得UserAgent
     * @return String
     */
    public String getUserAgent() {
    	return this.userAgent ;
    }
    
    /**
     * 从HTTP服务器获取数据，若页面压缩则解压缩
     * @param httpUrl 数据URL
     * @param cookies 为空时为无效参数
     * @return
     */
	 public byte[] getHttpCode(String httpUrl ,String cookies) {
		 try {
			 URL url = new URL(httpUrl);

			 URLConnection con = url.openConnection();			 
			 con.setRequestProperty("User-Agent" , userAgent);
			 if( cookies != null && cookies.length() > 0  ) con.setRequestProperty("Cookie",cookies) ;
			 con.setConnectTimeout(60000);
			 con.setReadTimeout(180000);

			 InputStream is = con.getInputStream();
			 fileLength = con.getContentLength();
			 contentType = con.getContentType() ;
			 
			 String contentEncoding = con.getContentEncoding() ;
			 if( contentEncoding == null) contentEncoding = "" ;
			 
			 byte[] bs = new byte[1024];
			 
			 if(fileLength > 0 ) {
				 fileContent = new byte[fileLength];
	
				 int len , ind = 0;
				 while ((len = is.read(bs)) != -1) {
					 BytesCopy(fileContent , bs , ind , len ) ;
					 ind += len ;
	
				 }
			 } else {
				 fileLength = 0;
				 int len = 0 ;
				 byte[] bs1=new byte[fileLength+len];
				 byte[] bs2=new byte[fileLength+len];
				 while ((len = is.read(bs)) != -1) {
					 bs1=new byte[fileLength+len];
					 BytesCopy(bs1 , bs2 , 0 , fileLength ) ;
					 BytesCopy(bs1 , bs , fileLength , len ) ;
					 bs2=new byte[fileLength+len];
					 fileLength += len ;
					 BytesCopy(bs2 , bs1 , 0 , fileLength ) ;
				 }
				 fileContent = new byte[fileLength];
				 BytesCopy(fileContent , bs2 , 0 , fileLength ) ;
				 
			 }
			 //处理压缩页面
			 if( contentEncoding.equalsIgnoreCase("gzip" ) ) {
				 fileContent = GzipUtils.UncompressString(fileContent) ;
			 }
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 
		 return fileContent;
	}
	 
	 /**
	  * 将byte数组copy到另一个byte数据
	  * @param dst 目标数组
	  * @param src 源数组
	  * @param ind_begin 目标数组的起始位置
	  * @param len_src 源数组的长度
	  */
	private void BytesCopy( byte[] dst , byte[] src , int ind_begin , int len_src ) {
		for( int i = 0 ; i< len_src ; i++) {
			dst[ind_begin+i] = src[i] ;
		}
	}
	
	/**
	 * 将byte数据打印成十六进制串
	 * @param bs 待打印的数据
	 * @param len 数组长度
	 */
	public void printBytes(byte[] bs  , int len ) {
		System.out.print("000000: ");

    	for( int i = 0 , j = 0 ; i< len ; i++ ) {
    		System.out.printf("%02x ", bs[i]) ;
    		if( (i+1)%16 == 0 ) {
    			System.out.printf("\n%06x: ",++j);
    		}
    	}
	}
	    

	/**
	* 获取html内容
    * @param httpUrl 网页URL
    * @param encodeType 编码类型
	* @return String HTML内容
	*/
    public byte[] getHttpCode(String httpUrl) {
        return getHttpCode(httpUrl,"") ;
    }

    /**
     * 获取登录cookie
     * @param url_login 登录URL
     * @param login_param 登录Post内容
     * @return String Cookie字符串
     */
    @SuppressWarnings("unused")
	public String getCookie(String url_login , String login_param ) {
        String cookies = "" ;
        try {
            PrintWriter out = null;
            BufferedReader in = null;
            String key=null;
            URL loginUrl = new URL(url_login);
            URLConnection conn = loginUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(login_param);
            out.flush();
            String cookieVal = null;
            for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++ ) {
                    if (key.equalsIgnoreCase("set-cookie")) {
                        cookieVal = conn.getHeaderField(i);
                        cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                        cookies = cookies+cookieVal+";";
                    }
            }
            conn.connect();
            InputStream ins = conn.getInputStream();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
		return cookies ;
    }

}
