package com.worden.GetHtml;

import java.io.UnsupportedEncodingException;
import org.apache.lucene.search.ScoreDoc;
import com.worden.HtmlPage.HtmlUtils;
import com.worden.HtmlPage.HttpUtils;
import com.worden.HtmlPage.ParseHtml;
import com.worden.common.*;
import com.worden.index.*;

@SuppressWarnings("unused")
public class HtmlTest {

	public static void main(String[] args) {
		
		HtmlUtils htmlU = new HtmlUtils() ;
		HttpUtils httpU = new HttpUtils() ;
		ParseHtml parseHtml = new ParseHtml() ;	
		
		
		
		httpU.getHttpCode("http://esf.nanjing.fang.com/chushou/3_154751008.htm");
		System.out.println(httpU.getContentType() +" ==> "+httpU.getFileLength() + " ==> " +httpU.getFileContent().length );

		FileUtils.WriteBytesToFile("D:\\WebSearch\\3_154751008.htm", httpU.getFileContent());
		
		
		
		//parseHtml.createParserByResource("D:\\WebSearch\\3_154751008.htm" ,"gb2312");
		//parseHtml.createParserByContent(httpU.getContent(), httpU.getContentType());
		/*
		String text = parseHtml.extactText( 0 ) ;
		System.out.println("text Len ==> "+text.length()) ;
		System.out.println(text);
		*/
		//System.out.println("Title: "+ parseHtml.getTitle() );
		//System.out.println("Text: "+ parseHtml.getExtractedText() );
		//parseHtml.getSimpleTextAndLink() ;
		
		/*
		String con = "";
		String[] text = parseHtml.getSimpleText() ;
		for( int i = 0 ; i<text.length ; i++ ) {

			System.out.println("================================================================================== "+i) ;
			con+=text[i];
			System.out.println(text[i]);
		}
		
		FileUtils.WriteStringToFile("D:\\WebSearch\\3_154751008.txt", con);
		*/
		
		/*
		FileUtils.RemoveFile("D:\\WebSearch\\index");
		parseHtml.createParserByResource("D:\\WebSearch\\3_154751008.htm" ,"gb2312");
		
		IndexProducer indexProducer = new IndexProducer();
		indexProducer.InitIndexWriter("D:\\WebSearch\\index");
		
		DocumentUtils doc = new DocumentUtils() ;
		doc.AddStringField("url", "http://esf.nanjing.fang.com/chushou/3_154751008.htm");
		doc.AddStringField("id", "3_154751008");
		doc.AddStringField("path", "D:\\WebSearch\\3_154751008.htm");
		String content = parseHtml.getExtractedText() ;
		
		doc.AddTextField("content", parseHtml.getExtractedText() ) ;
		//System.out.println(parseHtml.getExtractedText());
		indexProducer.AddDocument(doc.getDocument());
		
		indexProducer.CloseIndexWriter(); 
		*/
		
		/*
		Searcher searcher = new Searcher();
				
		searcher.InitSearcher("D:\\WebSearch\\index");
		//searcher.SetFormatter("<b style='color:red;'>", "</b>");
		ScoreDoc[] sds = searcher.Search( "content:'市政天元城'");
		searcher.ShowScoreDocHighLight(sds,new String[]{"url","id","content","path"});
		*/
		
		
		/*
		parseHtml.createParserByResource("D:\\WebSearch\\3_154751008.htm" ,"gb2312");
		String[] text = parseHtml.getLink() ;
		for( int i = 0 ; i<text.length ; i++ ) {

			System.out.println("================================================================================== "+i) ;
			System.out.println(text[i]);
		}
		*/
		System.out.println("Run Over !!!") ;
		 
		 
		 
	}
		

}