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
 * ������ѯ
 * @author Wordon
 * @version 1.0
 */
public class Searcher {
	
	private Analyzer analyzer = null;
    private IndexSearcher searcher = null ;
    private Query query = null ;
    private int maxResultNum = 20 ;
    /**
     * ��ѯ��ʱ
     */
    private Long TimeUsed = null ;
   	/**
   	 * ������ǩ
   	 */
   	Formatter formatter = new SimpleHTMLFormatter("<span style='color:red;'>", "</span>");
    
    /**
     * ��ʼ������IK�ִ���ΪĬ��
     */
    public Searcher() {
    	InitIKAnalyzer() ;
    }
    
    /**
     * ��ʼ����������searcher
     * @param indexPath ������ַ
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
	 * Lucene��׼�ִ���
	 */
	public void InitAnalyzer() {
		analyzer =  new StandardAnalyzer() ;
	}
	
	/**
	 * IK���ķִ���
	 */
	public void InitIKAnalyzer() {
		analyzer =  new IKAnalyzer();
	}
	
	/**
	 * IK���ķִ���������ָ��ģʽ
	 * @param flag 
	 */
	public void InitIKAnalyzer(boolean flag) {
		analyzer =  new IKAnalyzer(flag);
	}
	

    /**
     * ��ʼ��searcher
     * @param indexPath ������ַ
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
	 * �����ַ���
	 * @param fieldName ����
	 * @param text ����ַ���
	 * @return String
	 */
	public String HightLighterStr(String fieldName,String text){
		try {
	       	String str = null;//�趨�Żؽ��
	       	QueryScorer queryScorer = new QueryScorer(query);//�������Ҫ�����Դ�������
	       	//����������
	       	Highlighter hl = new Highlighter(formatter, queryScorer);
		        
	       	Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
	       	hl.setTextFragmenter(fragmenter);
	       	//��ȡ���ؽ��
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
	 * �ؼ��ʲ�ѯ
	 * @param fieldName ����
	 * @param keyWord �ؼ���
	 * @return ScoreDoc[]
	 */
	public ScoreDoc[] Search(String fieldName, String keyWord) {
		try {
			Long startTime = System.currentTimeMillis();
			
			QueryParser parser = new QueryParser(fieldName,analyzer);
			//parser.setDefaultOperator(QueryParser.AND_OPERATOR);
			parser.setAllowLeadingWildcard(true);//�����һ���ַ�Ϊͨ���
			
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
	 * �ؼ��ʲ�ѯ�������ڹؼ�����ָ����ѯ��
	 * @param keyWord �ؼ���
	 * @return ScoreDoc[]
	 */
	public ScoreDoc[] Search(String keyWord) {
		return Search("", keyWord) ;
	}
	/**
	 * �ؼ��ʲ�ѯ����ѯ���
	 * @param fieldNames �����б�String[]
	 * @param keyWord �ؼ���
	 * @return ScoreDoc[]
	 */
	public ScoreDoc[] Search(String[] fieldNames, String keyWord) {
		try {
			Long startTime = System.currentTimeMillis();
			
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fieldNames, analyzer);
			parser.setAllowLeadingWildcard(true);//�����һ���ַ�Ϊͨ���
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
     * ָ��field���в�ѯ��termquery��ѯ
     * @param fieldName ����
     * @param fieldValue �ؼ�ֵ
     * @param maxResultNum ��󷵻���
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
	 * TermRangeQuery����Χ��ѯ
	 * @param fieldName ����
	 * @param start ��ʼֵ
	 * @param end ����ֵ
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
	 * �����ѯ
	 * @param fieldName ����
	 * @param keyWords �ؼ�ֵ�б�String[]
	 * @param slop ����ַ���
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
	 * ��ʾ�������
	 * @param sds ScoreDoc[]�������
	 * @param fieldNames String[]�����б�
	 */
	public void ShowScoreDoc(ScoreDoc[] sds, String[] fieldNames)
	{
		 try {
			 int i = 0 ;
			 for(ScoreDoc sd:sds) {
		         i++ ;
				 Document doc = searcher.doc(sd.doc);
				 System.out.print("==>��" + i + "���������Ľ����");
				 for( String fieldName:fieldNames) {
					 System.out.println(" ["+fieldName+"] "+ doc.get(fieldName));
				 }
					
			 }
			 System.out.println("==> ������"+TimeUsed+"���룬����ѯ��"+i+"�������");
		 } catch (Exception e) {
			e.printStackTrace();
		 }
	}
	
	/**
	 * ������ӡ�������
	 * @param sds ScoreDoc[]�������
	 * @param fieldNames String[]�����б�
	 */
	public void ShowScoreDocHighLight(ScoreDoc[] sds, String[] fieldNames)
	{
		 try {
			 int i = 0 ;
			 for(ScoreDoc sd:sds) {
		         i++ ;
				 Document doc = searcher.doc(sd.doc);
				 System.out.println("==> ��" + i + "���������Ľ��:");
				 for( String fieldName:fieldNames) {
					 System.out.println(" ["+fieldName+"] "+HightLighterStr( fieldName, doc.get(fieldName)));
				 }
			 }
			 System.out.println("==> ������"+TimeUsed+"���룬����ѯ��"+i+"�������");
		 } catch (Exception e) {
			e.printStackTrace();
		 }
	}
	
	public int GetNumOfIndexDoc() {
		return searcher.getIndexReader().numDocs() ;
	}
	
}
