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
 * 使用HtmlParser提取网页中的内容
 * @author Wordon
 * @version 1.0
 */
public class ParseHtml {
	
	private Parser parser = null ;
	
	public ParseHtml() {
		
	}
	
	/**
	 * 初始化ParseHtml类，并由Html路径初化Parser变量
	 * @param HtmlResource Html路径
	 */
	public ParseHtml(String HtmlResource ) {
		createParserByResource(HtmlResource) ;
	}
	
	/**
	 * 初始化ParseHtml类，并由Html路径和编码类型初化Parser变量
	 * @param HtmlResource Html路径
	 */
	public ParseHtml(String HtmlResource  , String contentType) {
		createParserByResource(HtmlResource ,contentType ) ;
	}

	/**
	 * 由Html路径初化Parser变量
	 * @param HtmlResource Html路径
	 */
	public void createParserByResource(String HtmlResource ) {
		try {
			parser = new Parser(HtmlResource);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 由Html路径和编码格式初化Parser变量
	 * @param HtmlResource Html路径
	 * @param contentType 编码格式
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
	 * 由页面内容和编码格式初化Parser变量
	 * @param HtmlContent
	 * @param contentType
	 */
	public void createParserByContent(String HtmlContent ,String contentType ) {
		parser = Parser.createParser(HtmlContent , contentType) ;
	}

	/**
	 * 根据nodeIndex提到网页内容
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
	 * 获取Title字符串
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
	 * 获取页面纯文本
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
	 * 打印页面纯文本和超链接URL
	 */
	public void showSimpleTextAndLink() {
		
		try {
			parser.reset();
			
			NodeList nodeList = null;
			
			NodeFilter textFilter = new NodeClassFilter(TextNode.class);
			NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
			
			//暂时不处理 meta
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
					//@todo 过滤jsp标签:可以自己实现这个函数
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
	 * 获取页面纯文本
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
	 * 获取页面中超链接URL
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
