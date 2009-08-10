package com.arunma.th.indexer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;

import com.arunma.th.config.ThConfig;
import com.arunma.th.config.ThField;


public interface Indexer {

	public String getContent() throws IOException;
	public String getFilePath();
	public void setFilePath(String filePath);
	public List<ThField> getPopulatedFieldsToIndex();
	public String getClassName();
	public void setConfig(ThConfig config);
	

	/**
	 * Just read that opening and closing the indexwriter for every document
	 * addition is expensive. Moving the optimize and the writer closing to a
	 * different method.(commit)
	 * 
	 */
	
	public abstract void addDocument(Document document);
	
	public abstract void addDocument() throws IOException;

	public abstract void openIndexForWriting(File indexDir,
			boolean createNewIndex) throws IOException;
	

	public abstract void openIndexForWriting(String indexDir,
			boolean createNewIndex) throws IOException;

	public abstract void openIndexForWriting(Directory indexDir,
			boolean createNewIndex) throws IOException;
	
	public abstract void save();
	
	public int getDocumentCount() throws IOException;
}
