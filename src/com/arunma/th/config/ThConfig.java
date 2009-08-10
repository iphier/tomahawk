package com.arunma.th.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;

public class ThConfig implements Serializable {
	//holds the mime type to Indexer class name
	private Map<String, String> mimeTypeMap=new HashMap<String, String>();
	
	//Fields to index and search in a text file
	private List<ThField> txtFields;
	
	private Analyzer analyzer;
	

	public Map<String, String> getMimeTypeMap() {
		return mimeTypeMap;
	}
	public void setMimeTypeMap(Map<String, String> mimeTypeMap) {
		this.mimeTypeMap = mimeTypeMap;
	}
	public List<ThField> getTxtFields() {
		return txtFields;
	}
	public void setTxtFields(List<ThField> txtFields) {
		this.txtFields = txtFields;
	}
	public Analyzer getAnalyzer() {
		return analyzer;
	}
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
}
