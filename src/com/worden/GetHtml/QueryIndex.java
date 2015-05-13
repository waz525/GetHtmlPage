package com.worden.GetHtml;

import org.apache.lucene.search.ScoreDoc;

import com.worden.common.CommonUtils;
import com.worden.index.IndexProducer;
import com.worden.index.Searcher;

@SuppressWarnings("unused")
public class QueryIndex {

	public static void main(String[] args) {

		Searcher searcher = new Searcher();
				
		searcher.InitSearcher("D:\\WebSearch\\index\\fang.com\\");
		//searcher.SetFormatter("<b style='color:red;'>", "</b>");
		ScoreDoc[] sds = searcher.Search( "content:'¼Û'");
		searcher.ShowScoreDocHighLight(sds,new String[]{"PageId","url","content"});
		
		CommonUtils.PrintInfo("Num of doc : "+searcher.GetNumOfIndexDoc(),new Exception() ) ;
		
		/*
		IndexProducer producer = new IndexProducer("D:\\WebSearch\\index\\fang.com\\") ;
		producer.DeleteRecord("PageId", "1_1431335668_235");
		producer.CloseIndexWriter(); 
		*/

	}
}
