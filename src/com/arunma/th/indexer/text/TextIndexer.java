package com.arunma.th.indexer.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.arunma.th.config.ThField;
import com.arunma.th.indexer.impl.IndexerImpl;

public class TextIndexer extends IndexerImpl{
	
	

	public List<ThField> getFieldsToIndex() {
		return (getManager().getTomConfig().getTxtFields());
	}


	public List<ThField> getPopulatedFieldsToIndex() {
		List<ThField> populatedThFieldList=new ArrayList<ThField>();
		//the skeletons taken from th-config.xml
		List <ThField> fieldNames=getManager().getTomConfig().getTxtFields();
		
		ThField tempPopulatedThField=null;
		String fieldName=null;
		String fieldType=null;
		for (ThField fieldNameField:fieldNames){
			tempPopulatedThField=new ThField();
			fieldName=fieldNameField.getName();
			fieldType=fieldNameField.getType();
			if (fieldName.equals("fileName")){
				tempPopulatedThField.setValue(getFilePath());
			}
			else if (fieldName.equals("content")){
				tempPopulatedThField.setValue(getContent());
			} 
			
			tempPopulatedThField.setName(fieldName);
			tempPopulatedThField.setType(fieldType);
			populatedThFieldList.add(tempPopulatedThField);
		}
		return populatedThFieldList;

	}
	public String getContent() {
		
		StringBuffer buffer=null;
		FileReader fileReader=null;
		BufferedReader reader=null;
		try {
			buffer = new StringBuffer();
			fileReader=new FileReader(getFilePath());
			reader=new BufferedReader(fileReader);
			String line=null;
				while ((line=reader.readLine())!=null){
					buffer.append(line);
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(fileReader);
		}

		return buffer.toString();
	}

}