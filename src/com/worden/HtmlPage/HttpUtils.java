package com.worden.HtmlPage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import com.worden.common.*;

/**
 * ��ȡ��ҳ����
 * @author Wordon
 * @version 1.0
 */
public class HttpUtils {

	/**
	 * ��Ŵӷ���˻�ȡ�����ݣ�����ҳѹ����Ϊ��ѹ���������
	 */
	private byte[] fileContent = null ;
	
	/**
	 * �ӷ������������ݳ��ȣ�����ҳѹ���򳤶���fileContent��һ��
	 */
	private int fileLength = -1 ;
	
	/**
	 * ��ҳ�����ʽ
	 */
	private String contentType = null ;
	
	/**
	 * �ͻ������ͣ���UserAgent��
	 */
	private String userAgent = "Wget/1.10.2 (Red Hat modified)" ;

    
    /**
     * ȡ���ļ�����
     * @return int
     */
    public int getFileLength() {
    	return this.fileLength ;
    }
    
    /**
     * ȡ�ñ�������
     * @return String
     */
    public String getContentType() {
    	int ind = this.contentType.lastIndexOf('=') ;
    	return this.contentType.substring(ind+1) ;
    }
    
    /**
     * ȡ����ҳԭʼ����
     * @return byte[]
     */
    public byte[] getFileContent() {
    	return this.fileContent ;
    }
    
    /**
     * ȡ����ҳ���ݣ���byte����ת����String
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
     * ����UserAgent
     * @param ua UserAgent�ַ���
     */
    public void setUserAgent(String ua ) {
    	this.userAgent = ua ;    	
    }
    /**
     * ȡ��UserAgent
     * @return String
     */
    public String getUserAgent() {
    	return this.userAgent ;
    }
    
    /**
     * ��HTTP��������ȡ���ݣ���ҳ��ѹ�����ѹ��
     * @param httpUrl ����URL
     * @param cookies Ϊ��ʱΪ��Ч����
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
			 //����ѹ��ҳ��
			 if( contentEncoding.equalsIgnoreCase("gzip" ) ) {
				 fileContent = GzipUtils.UncompressString(fileContent) ;
			 }
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 
		 return fileContent;
	}
	 
	 /**
	  * ��byte����copy����һ��byte����
	  * @param dst Ŀ������
	  * @param src Դ����
	  * @param ind_begin Ŀ���������ʼλ��
	  * @param len_src Դ����ĳ���
	  */
	private void BytesCopy( byte[] dst , byte[] src , int ind_begin , int len_src ) {
		for( int i = 0 ; i< len_src ; i++) {
			dst[ind_begin+i] = src[i] ;
		}
	}
	
	/**
	 * ��byte���ݴ�ӡ��ʮ�����ƴ�
	 * @param bs ����ӡ������
	 * @param len ���鳤��
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
	* ��ȡhtml����
    * @param httpUrl ��ҳURL
    * @param encodeType ��������
	* @return String HTML����
	*/
    public byte[] getHttpCode(String httpUrl) {
        return getHttpCode(httpUrl,"") ;
    }

    /**
     * ��ȡ��¼cookie
     * @param url_login ��¼URL
     * @param login_param ��¼Post����
     * @return String Cookie�ַ���
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
