package com.worden.HtmlPage;

import java.util.ArrayList;

import org.htmlparser.*;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;
import org.htmlparser.visitors.TextExtractingVisitor;

/**
 * ʹ��HtmlParser��ȡ��ҳ�е�����
 * @author Wordon
 * @version 1.0
 */
public class ParseHtml {
	
	private Parser parser = null ;
	
	public ParseHtml() {
		
	}
	
	/**
	 * ��ʼ��ParseHtml�࣬����Html·������Parser����
	 * @param HtmlResource Html·��
	 */
	public ParseHtml(String HtmlResource ) {
		createParserByResource(HtmlResource) ;
	}
	
	/**
	 * ��ʼ��ParseHtml�࣬����Html·���ͱ������ͳ���Parser����
	 * @param HtmlResource Html·��
	 */
	public ParseHtml(String HtmlResource  , String contentType) {
		createParserByResource(HtmlResource ,contentType ) ;
	}

	/**
	 * ��Html·������Parser����
	 * @param HtmlResource Html·��
	 */
	public void createParserByResource(String HtmlResource ) {
		try {
			parser = new Parser(HtmlResource);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��Html·���ͱ����ʽ����Parser����
	 * @param HtmlResource Html·��
	 * @param contentType �����ʽ
	 */
	public void createParserByResource(String HtmlResource , String contentType ) {
		try {
			parser = new Parser(HtmlResource);
			parser.setEncoding(contentType);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ҳ�����ݺͱ����ʽ����Parser����
	 * @param HtmlContent
	 * @param contentType
	 */
	public void createParserByContent(String HtmlContent ,String contentType ) {
		parser = Parser.createParser(HtmlContent , contentType) ;
	}

	/**
	 * ����nodeIndex�ᵽ��ҳ����
	 * @param nodeIndex node id
	 * @return String
	 */
	public String extactText(int nodeIndex ) {
		
		StringBuffer text = new StringBuffer() ;
		parser.reset();	
		
		try {
			
			@SuppressWarnings("serial")
			NodeList nodes = parser.extractAllNodesThatMatch(new NodeFilter() {
				public boolean accept(Node arg0) {
					return true;
				}
			}) ;
			/*
			System.out.println("1==> "+nodes.size() );
			for( int i = 0 ; i<10; i++) 
			{
				System.out.println("==================================================================================") ;
				Node node = nodes.elementAt(i);
				System.out.println("("+i+")==> "+node.getText() ) ;
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$") ;
				System.out.println(node.toPlainTextString().length() ) ;
				System.out.println("**********************************************************************************") ;
			}
			*/
			Node node = nodes.elementAt(nodeIndex);
			text.append(new String(node.toPlainTextString())) ;
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return text.toString() ;
	}
	
	/**
	 * show Table Tag
	 */
	public void showTableTag() {
		try {
			parser.reset();	
			String filterStr = "table" ;
			NodeFilter filter = new TagNameFilter(filterStr) ;
			NodeList nodeList = parser.extractAllNodesThatMatch(filter) ;
			//TableTag tableTag = (TableTag)nodeList.elementAt(nodeIndex) ;
			//System.out.println(tableTag.toHtml()) ;
			
			
			System.out.println("1==> "+nodeList.size() );
			for( int i = 0 ; i<nodeList.size(); i++) 
			{
				System.out.println("==================================================================================") ;
				TableTag tableTag1 = (TableTag) nodeList.elementAt(i);
				System.out.println("("+i+")==> "+tableTag1.getStringText() ) ;
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$") ;
				System.out.println(tableTag1.toHtml().length() ) ;
				System.out.println("**********************************************************************************") ;
			}
			
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ��ȡTitle�ַ���
	 * @return String
	 */
	public String getTitle() {
		try {
			parser.reset();
			
			HtmlPage visitor = new HtmlPage(parser);
			parser.visitAllNodesWith(visitor);
			return visitor.getTitle();
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	/**
	 * ��ȡҳ�洿�ı�
	 * @return String
	 */
	public String getExtractedText() {
		try {
			parser.reset();
			
			TextExtractingVisitor visitor = new TextExtractingVisitor();
			parser.visitAllNodesWith(visitor);
			return visitor.getExtractedText();
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return null ;
	}

	/**
	 * ��ӡҳ�洿�ı��ͳ�����URL
	 */
	public void showSimpleTextAndLink() {
		
		try {
			parser.reset();
			
			NodeList nodeList = null;
			
			NodeFilter textFilter = new NodeClassFilter(TextNode.class);
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			
			//��ʱ������ meta
			//NodeFilter metaFilter = new NodeClassFilter(MetaTag.class);
			
			OrFilter lastFilter = new OrFilter();
			lastFilter.setPredicates(new NodeFilter[] { textFilter, linkFilter });
			
			nodeList = parser.parse(lastFilter);
			
			Node[] nodes = nodeList.toNodeArray();
			
			for (int i = 0; i < nodes.length; i++) {
				Node anode = (Node) nodes[i];
				String line = "";
				if (anode instanceof TextNode) {
					TextNode textnode = (TextNode) anode;
					//line = textnode.toPlainTextString().trim();
					line = textnode.getText();
					
				} else if (anode instanceof LinkTag) {
					LinkTag linknode = (LinkTag) anode;
					line = linknode.getLink();
					//@todo ����jsp��ǩ:�����Լ�ʵ���������
					//line = StringFunc.replace(line, "<%.*%>", "");
				}
				
				if (line.isEmpty()) continue;
				System.out.println(line);
			}
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * ��ȡҳ�洿�ı�
	 * @return String[]
	 */
	public String[] getSimpleText() {
		
		ArrayList<String> list = new ArrayList<String>() ;
		String line ;
		
		try {
			parser.reset();
			
			NodeList nodeList = null;
			nodeList = parser.parse(new NodeClassFilter(TextNode.class));
			Node[] nodes = nodeList.toNodeArray();
			
			for (int i = 0; i < nodes.length; i++) {
				TextNode textnode = (TextNode) nodes[i];
				//line = textnode.toPlainTextString().trim();
				line = textnode.getText();
				if (line.isEmpty()) continue;
				
				list.add(line);
			}
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return list.toArray(new String[0]) ;
	}
	
	/**
	 * ��ȡҳ���г�����URL
	 * @return String[]
	 */
	public String[] getLink() {
		ArrayList<String> list = new ArrayList<String>() ;
		String line ;
		
		try {
			parser.reset();
			
			NodeList nodeList = null;						
			nodeList = parser.parse(new NodeClassFilter(LinkTag.class));			
			Node[] nodes = nodeList.toNodeArray();
			for (int i = 0; i < nodes.length; i++) {
				
				LinkTag linknode = (LinkTag) nodes[i];				
				line = linknode.getLink();
				if (line.isEmpty()) continue;
				
				list.add(line);
			}
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return list.toArray(new String[0]) ;
	}
	
	
}
