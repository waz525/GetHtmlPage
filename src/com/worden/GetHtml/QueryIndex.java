package com.worden.GetHtml;

import org.apache.lucene.search.ScoreDoc;

import com.worden.index.Searcher;

public class QueryIndex {

	public static void main(String[] args) {

		Searcher searcher = new Searcher();
				
		searcher.InitSearcher("D:\\WebSearch\\index\\fang.com\\");
		//searcher.SetFormatter("<b style='color:red;'>", "</b>");
		ScoreDoc[] sds = searcher.Search( "content:'市政天元城'");
		searcher.ShowScoreDocHighLight(sds,new String[]{"url","content","path"});
	}
}
