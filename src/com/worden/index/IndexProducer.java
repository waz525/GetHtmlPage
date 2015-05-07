package com.worden.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 索引生成器
 * @author Wordon
 * @version 1.0
 */
public class IndexProducer {
	private Analyzer analyzer = null;
	private Version luceneVersion = Version.LUCENE_4_10_2 ;	
	private IndexWriter indexWriter = null;
	
	/**
	 * 类初始化，以IK为分词器
	 */
	public IndexProducer() {
		InitIKAnalyzer();
	}
	/**
	 * 类初始化，以IK为分词器，并初始化IndexWriter
	 * @param indexPath 索引目录路径
	 */
	public IndexProducer(String indexPath ) {
		InitIKAnalyzer();
		InitIndexWriter(indexPath) ;
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
	 * 初始化IndexWriter
	 * @param indexPath 索引目录路径
	 */
	public void InitIndexWriter( String indexPath ) {
		try {
			
			Directory directory = FSDirectory.open(new File(indexPath));
			IndexWriterConfig iwConfig = new IndexWriterConfig(luceneVersion, analyzer);
			iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			indexWriter = new IndexWriter(directory, iwConfig);
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * 添加数据
	 * @param doc Document对象
	 */
	public void AddDocument( Document doc) {
		try {
			indexWriter.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭IndexWriter，结束索引创建
	 */
	public void CloseIndexWriter() {
		try {
			indexWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
}

