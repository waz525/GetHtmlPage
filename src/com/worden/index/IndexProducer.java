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
 * ����������
 * @author Wordon
 * @version 1.0
 */
public class IndexProducer {
	private Analyzer analyzer = null;
	private Version luceneVersion = Version.LUCENE_4_10_2 ;	
	private IndexWriter indexWriter = null;
	
	/**
	 * ���ʼ������IKΪ�ִ���
	 */
	public IndexProducer() {
		InitIKAnalyzer();
	}
	/**
	 * ���ʼ������IKΪ�ִ���������ʼ��IndexWriter
	 * @param indexPath ����Ŀ¼·��
	 */
	public IndexProducer(String indexPath ) {
		InitIKAnalyzer();
		InitIndexWriter(indexPath) ;
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
	 * ��ʼ��IndexWriter
	 * @param indexPath ����Ŀ¼·��
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
	 * �������
	 * @param doc Document����
	 */
	public void AddDocument( Document doc) {
		try {
			indexWriter.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �ر�IndexWriter��������������
	 */
	public void CloseIndexWriter() {
		try {
			indexWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
}

