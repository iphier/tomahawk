package com.arunma.th.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 * @author arun manivannan on Jul 25, 2009
 *
 *	This class populates the TomahawkConfig bean with the values from the config xml
 */
public class ThConfigManager {

	private static ThConfigManager singletonInstance =null;
	Logger logger=Logger.getRootLogger();
	ThConfig tomConfig=null;
	Analyzer analyzer=null;
	
	//This map is supposed to cache all configs (bullshit comment). Okay, Just in case, 
	//the user wants to load in a whole new configuration, then he can pass in the
	//configuration into the getTomConfig instance. This particular map is supposed to 
	//have the filename as the key and a fully populated TomahawkConfig as value
	HashMap<String, ThConfig> configCache=new HashMap<String, ThConfig>();
	
	private ThConfigManager() {
	
	}

	private String configFilePath;
	
	public static ThConfigManager getInstance(){
		
		if (singletonInstance==null){
			singletonInstance=new ThConfigManager();
		}
		return singletonInstance;
	}

	
	public  ThConfig getTomConfig(){
		tomConfig=getTomConfig(configFilePath);	
		return tomConfig;
	}
	

	public ThConfig getTomConfig(String xmlFilePath){
		
		if (configCache.get(xmlFilePath)!=null){
			tomConfig=configCache.get(xmlFilePath);
		}
		else{
			Document document=parseDocumentFromXML(xmlFilePath);
			tomConfig=populateConfigFromDocument(document,xmlFilePath);		
			configCache.put(xmlFilePath, tomConfig);	
		}
		
		
		return tomConfig;
	}
	
	private ThConfig populateConfigFromDocument(Document document,String xmlFile){
		//TODO add code to get information from another config file
		tomConfig=new ThConfig();
		tomConfig.setAnalyzer(analyzer);
		tomConfig.setTxtFields(getTxtFields(document));
		tomConfig.setMimeTypeMap(getMimeTypeMapping(document));
		
		return tomConfig; 
		
	}
	
	
	private String getAnalyzerClassName(Document document){
		String attributeValue=null;
		Attribute attribute=null;
		try {
			attribute = (Attribute)XPath.selectSingleNode(document, "//properties/analyzer/@class");
			if (attribute!=null)
				attributeValue=attribute.getValue();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return attributeValue;
		
	}
	
	public List <ThField> getTxtFields(Document document){
		
		List<ThField> txtFieldList=new ArrayList<ThField>();
		List<Element> elementList=null;
		try {
			elementList = XPath.selectNodes(document, "//txt/fields/luceneField");
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Element element: elementList){
			ThField field=buildThField(element);	
			txtFieldList.add(field);
		}
		return txtFieldList;
		
	}
	
	private Document parseDocumentFromXML(String xmlFilePath){
		
		Document document=new Document();
		SAXBuilder builder=new SAXBuilder();
		try {
			document=builder.build(new File(xmlFilePath));
		} catch (JDOMException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return document;
		
	}
	
	private ThField buildThField(Element element){
		ThField thField=new ThField();
		
		if (element.getAttribute("name")!=null){
			thField.setName(element.getAttributeValue("name"));
		}
		if (element.getAttribute("type")!=null){
			thField.setType(element.getAttributeValue("type"));
		}
		
		return thField;
	}
	
	protected Map<String, String> getMimeTypeMapping(Document document) {
		Map<String, String> mimeTypeMapping = new HashMap<String, String>();
		try {
			List<Element> mimeElements = XPath.selectNodes(document, "//mime");
			String mimeType=null;
			String indexerBeanId=null;
			for (Element mimeElement:mimeElements){
				mimeType=mimeElement.getTextTrim();
				indexerBeanId=(String)mimeElement.getParentElement().getAttributeValue("name");
				mimeTypeMapping.put(mimeType, indexerBeanId);
			}
			

		} catch (JDOMException e) {
			logger.error(e.getMessage());
		}
		return mimeTypeMapping;
	}



	public String getConfigFilePath() {
		return configFilePath;
	}


	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}


	public void setTomConfig(ThConfig tomConfig) {
		this.tomConfig = tomConfig;
	}


	public Analyzer getAnalyzer() {
		return analyzer;
	}


	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
}
