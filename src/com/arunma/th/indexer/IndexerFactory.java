package com.arunma.th.indexer;

import java.io.File;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.springframework.beans.factory.BeanFactory;

import com.arunma.th.config.ThConfig;

public class IndexerFactory {

	
	public static Indexer getIndexer(File file, ThConfig config, BeanFactory factory){
		Indexer indexer=null;
		String mimeType=getMimeTypeFromFile(file);
		Map<String, String> supportedMimeTypeMap=config.getMimeTypeMap();
		String beanName=null;
		if (supportedMimeTypeMap.containsKey(mimeType)){
			beanName=supportedMimeTypeMap.get(mimeType);
			indexer=(Indexer)factory.getBean(beanName);
			System.out.println("File name : indexerfact" +file.getAbsolutePath());
			indexer.setFilePath(file.getAbsolutePath());
			indexer.setConfig(config);
		}
		return indexer;
	}

	private static String getMimeTypeFromFile (File file){
		return (MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(file));
		
	}
}
