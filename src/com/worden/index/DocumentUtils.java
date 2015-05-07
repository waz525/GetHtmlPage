package com.worden.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

/**
 * 操作Document对象
 * @author Wordon
 * @version 1.0
 */
public class DocumentUtils {
	
	private Document document = new Document() ;
	
	/**
	 * 获取Document对象
	 * @return Document
	 */
	public Document GetDocument() {
		return this.document ;
	}
	
	/**
	 * 向Document添加StringField对象
	 * @param name 域名
	 * @param value 数据
	 * @param storeFlag 是否存储原数据，true/false
	 */
	public void AddStringField(String name , String value , boolean storeFlag ) {
		
		if( storeFlag == true ) {
			document.add(new StringField(name , value , Field.Store.YES) );
		} else {
			document.add(new StringField(name , value , Field.Store.NO) ) ;
		}
		
	}

	/**
	 * 向Document添加StringField对象
	 * @param name 域名
	 * @param value 数据
	 */
	public void AddStringField(String name , String value ) {
		AddStringField(name,value,true) ;		
	}

	/**
	 * 向Document添加TextField对象
	 * @param name 域名
	 * @param value 数据
	 * @param storeFlag 是否存储原数据，true/false
	 */
	public void AddTextField(String name , String value , boolean storeFlag ) {
		
		if( storeFlag == true ) {
			document.add(new TextField(name , value , Field.Store.YES) );
		} else {
			document.add(new TextField(name , value , Field.Store.NO) ) ;
		}
		
	}

	/**
	 * 向Document添加TextField对象
	 * @param name 域名
	 * @param value 数据
	 */
	public void AddTextField(String name , String value ) {
		AddTextField(name,value,true) ;
	}
	
}
