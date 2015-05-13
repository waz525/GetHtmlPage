package com.worden.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 索引查询
 * @author Wordon
 * @version 1.0
 */
public class Searcher {
	
	private Analyzer analyzer = null;
    private IndexSearcher searcher = null ;
    private Query query = null ;
    private int maxResultNum = 20 ;
    /**
     * 查询耗时
     */
    private Long TimeUsed = null ;
   	/**
   	 * 高亮标签
   	 */
   	Formatter formatter = new SimpleHTMLFormatter("<span style='color:red;'>", "</span>");
    
    /**
     * 初始化，以IK分词器为默认
     */
    public Searcher() {
    	InitIKAnalyzer() ;
    }
    
    /**
     * 初始化，并设置searcher
     * @param indexPath 索引地址
     */
    public Searcher(String indexPath) {
    	InitIKAnalyzer() ;
    	InitSearcher(indexPath) ;
    }
    
    public void SetMaxResultNum(int max) {
    	this.maxResultNum = max ;
    }
    
    public int GetMaxResultNum() {
    	return this.maxResultNum ;
    }
    
    public Analyzer GetAnalyzer() {
    	return this.analyzer ;
    }
    
    public IndexSearcher GetIndexSearcher() {
    	return this.searcher ;
    }
    
    public Query GetQuery() {
    	return this.query ;
    }
    
    public Long GetTimeUsed() {
    	return this.TimeUsed ;
    }
    
    public void SetFormatter(String perTag , String postTag) {
    	formatter = new SimpleHTMLFormatter(perTag,postTag) ;
    }
	/**
	 * Lucene标准分词器
	 */
	public void InitAnalyzer() {
		analyzer =  new StandardAnalyzer() ;
	}
	
	/**
	 * IK中文分词器
	 */
	public void InitIKAnalyzer() {
		analyzer =  new IKAnalyzer();
	}
	
	/**
	 * IK中文分词器，并可指定模式
	 * @param flag 
	 */
	public void InitIKAnalyzer(boolean flag) {
		analyzer =  new IKAnalyzer(flag);
	}
	

    /**
     * 初始化searcher
     * @param indexPath 索引地址
     */
	public void InitSearcher(String indexPath) {

		try {
			Directory directory = FSDirectory.open(new File(indexPath));
			DirectoryReader IndexReader = DirectoryReader.open(directory);
			searcher = new IndexSearcher(IndexReader);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 高亮字符串
	 * @param fieldName 域名
	 * @param text 结果字符串
	 * @return String
	 */
	public String HightLighterStr(String fieldName,String text){
		try {
	       	String str = null;//设定放回结果
	       	QueryScorer queryScorer = new QueryScorer(query);//如果有需要，可以传入评分
	       	//高亮分析器
	       	Highlighter hl = new Highlighter(formatter, queryScorer);
		        
	       	Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
	       	hl.setTextFragmenter(fragmenter);
	       	//获取返回结果
	       	str = hl.getBestFragment(analyzer, fieldName,text);
	       	if(str == null){
		        return text;
	       	}
	       	return str;
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
		

	/**
	 * 关键词查询
	 * @param fieldName 域名
	 * @param keyWord 关键词
	 * @return ScoreDoc[]
	 */
	public ScoreDoc[] Search(String fieldName, String keyWord) {
		try {
			Long startTime = System.currentTimeMillis();
			
			QueryParser parser = new QueryParser(fieldName,analyzer);
			//parser.setDefaultOperator(QueryParser.AND_OPERATOR);
			parser.setAllowLeadingWildcard(true);//允许第一个字符为通配符
			
			query = parser.parse(keyWord);
			TopDocs tds = searcher.search(query,maxResultNum);
			
			TimeUsed = System.currentTimeMillis() - startTime ;
			return tds.scoreDocs;
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	/**
	 * 关键词查询，必须在关键词中指定查询域
	 * @param keyWord 关键词
	 * @return ScoreDoc[]
	 */
	public ScoreDoc[] Search(String keyWord) {
		return Search("", keyWord) ;
	}
	/**
	 * 关键词查询，查询多段
	 * @param fieldNames 段名列表，String[]
	 * @param keyWord 关键词
	 * @return ScoreDoc[]
	 */
	public ScoreDoc[] Search(String[] fieldNames, String keyWord) {
		try {
			Long startTime = System.currentTimeMillis();
			
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fieldNames, analyzer);
			parser.setAllowLeadingWildcard(true);//允许第一个字符为通配符
			query = parser.parse(keyWord);
			TopDocs tds = searcher.search(query,maxResultNum);
			
			TimeUsed = System.currentTimeMillis() - startTime ;

			return tds.scoreDocs;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
		
	/**
     * 指定field进行查询，termquery查询
     * @param fieldName 域名
     * @param fieldValue 关键值
     * @param maxResultNum 最大返回数
	 * @return ScoreDoc[]
     */
	public ScoreDoc[] SearchByTerm( String fieldName , String fieldValue) {
		try {

			Long startTime = System.currentTimeMillis();

			 query = new TermQuery(new Term(fieldName,fieldValue));
			 TopDocs tds = searcher.search(query, maxResultNum);

			 TimeUsed = System.currentTimeMillis() - startTime ;

			 return tds.scoreDocs;
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}

	/**
	 * TermRangeQuery，范围查询
	 * @param fieldName 域名
	 * @param start 开始值
	 * @param end 结束值
	 * @return ScoreDoc[]
	 */
	public ScoreDoc[] SearchByTermRange(String fieldName,String start,String end) {
		try {

			Long startTime = System.currentTimeMillis();

			query = new TermRangeQuery(fieldName,new BytesRef(start.getBytes()),new BytesRef(end.getBytes()) , true, true);
            TopDocs tds = searcher.search(query, maxResultNum);
            
            TimeUsed = System.currentTimeMillis() - startTime ;

            return tds.scoreDocs;
		} catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null ;
	}

	/**
	 * 距离查询
	 * @param fieldName 域名
	 * @param keyWords 关键值列表，String[]
	 * @param slop 间隔字符数
	 * @return ScoreDoc[]
	 */
	public ScoreDoc[] SearchAsSlop( String fieldName , String[] keyWords , int slop ){
		try {
			Long startTime = System.currentTimeMillis();
      	
			PhraseQuery query=new PhraseQuery();
			query.setSlop(slop);
			for (int i=0;i<keyWords.length;i++){
				query.add(new Term(fieldName,keyWords[i]));
			}
			
			TopDocs tds = searcher.search(query,maxResultNum);
			
			TimeUsed = System.currentTimeMillis() - startTime ;
			
			return tds.scoreDocs;
			
		} catch(Exception e) {
            e.printStackTrace();
        }
		return null ;
	}


	/**
	 * 显示检索结果
	 * @param sds ScoreDoc[]，结果集
	 * @param fieldNames String[]，域列表
	 */
	public void ShowScoreDoc(ScoreDoc[] sds, String[] fieldNames)
	{
		 try {
			 int i = 0 ;
			 for(ScoreDoc sd:sds) {
		         i++ ;
				 Document doc = searcher.doc(sd.doc);
				 System.out.print("==>第" + i + "个检索到的结果：");
				 for( String fieldName:fieldNames) {
					 System.out.println(" ["+fieldName+"] "+ doc.get(fieldName));
				 }
					
			 }
			 System.out.println("==> 花费了"+TimeUsed+"毫秒，共查询出"+i+"个结果！");
		 } catch (Exception e) {
			e.printStackTrace();
		 }
	}
	
	/**
	 * 高亮打印检索结果
	 * @param sds ScoreDoc[]，结果集
	 * @param fieldNames String[]，域列表
	 */
	public void ShowScoreDocHighLight(ScoreDoc[] sds, String[] fieldNames)
	{
		 try {
			 int i = 0 ;
			 for(ScoreDoc sd:sds) {
		         i++ ;
				 Document doc = searcher.doc(sd.doc);
				 System.out.println("==> 第" + i + "个检索到的结果:");
				 for( String fieldName:fieldNames) {
					 System.out.println(" ["+fieldName+"] "+HightLighterStr( fieldName, doc.get(fieldName)));
				 }
			 }
			 System.out.println("==> 花费了"+TimeUsed+"毫秒，共查询出"+i+"个结果！");
		 } catch (Exception e) {
			e.printStackTrace();
		 }
	}
	
	public int GetNumOfIndexDoc() {
		return searcher.getIndexReader().numDocs() ;
	}
	
}
