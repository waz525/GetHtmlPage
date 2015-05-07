package com.worden.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * ����Document����
 * @author Wordon
 * @version 1.0
 */
public class DocumentUtils {
	
	private Document document = new Document() ;
	
	/**
	 * ��ȡDocument����
	 * @return Document
	 */
	public Document GetDocument() {
		return this.document ;
	}
	
	/**
	 * ��Document���StringField����
	 * @param name ����
	 * @param value ����
	 * @param storeFlag �Ƿ�洢ԭ���ݣ�true/false
	 */
	public void AddStringField(String name , String value , boolean storeFlag ) {
		
		if( storeFlag == true ) {
			document.add(new StringField(name , value , Field.Store.YES) );
		} else {
			document.add(new StringField(name , value , Field.Store.NO) ) ;
		}
		
	}

	/**
	 * ��Document���StringField����
	 * @param name ����
	 * @param value ����
	 */
	public void AddStringField(String name , String value ) {
		AddStringField(name,value,true) ;		
	}

	/**
	 * ��Document���TextField����
	 * @param name ����
	 * @param value ����
	 * @param storeFlag �Ƿ�洢ԭ���ݣ�true/false
	 */
	public void AddTextField(String name , String value , boolean storeFlag ) {
		
		if( storeFlag == true ) {
			document.add(new TextField(name , value , Field.Store.YES) );
		} else {
			document.add(new TextField(name , value , Field.Store.NO) ) ;
		}
		
	}

	/**
	 * ��Document���TextField����
	 * @param name ����
	 * @param value ����
	 */
	public void AddTextField(String name , String value ) {
		AddTextField(name,value,true) ;
	}
	
}
