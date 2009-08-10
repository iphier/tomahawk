package com.arunma.th.indexer.impl;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.arunma.th.config.ThConfig;
import com.arunma.th.config.ThConfigManager;
import com.arunma.th.indexer.Indexer;
import com.arunma.th.indexer.IndexerHelper;
import com.arunma.th.utils.ThUtils;

public abstract class IndexerImpl implements Indexer {

	private Analyzer analyzer;
	static Logger logger = Logger.getRootLogger();
	protected ThConfig config = null;
	private ThConfigManager manager = null;
	private String filePath = null;
	private IndexWriter indexWriter;

	public synchronized void addDocument() throws IOException {

		Document document = IndexerHelper
				.convertThFieldsToLuceneDoc(getPopulatedFieldsToIndex());
		addDocument(document);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arunma.th.indexer.impl.Indexer#addDocument(org.apache.lucene.document
	 * .Document)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arunma.th.indexer.Indexer#index(org.apache.lucene.document.Document,
	 * org.apache.lucene.analysis.Analyzer, org.apache.lucene.store.Directory)
	 */
	public synchronized void addDocument(Document document) {
		try {
			indexWriter.addDocument(document);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arunma.th.indexer.impl.Indexer#openIndexForWriting(java.lang.String,
	 * boolean)
	 */
	public synchronized void openIndexForWriting(String indexDir,
			boolean createNewIndex) throws IOException {

		File file = new File(indexDir);
		openIndexForWriting(file, createNewIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.arunma.th.indexer.impl.Indexer#openIndexForWriting(java.lang.String,
	 * boolean)
	 */
	public synchronized void openIndexForWriting(File indexDir,
			boolean createNewIndex) throws IOException {
		Directory directory = FSDirectory.getDirectory(indexDir);
		openIndexForWriting(directory, createNewIndex);
	}

	public synchronized void openIndexForWriting(Directory directory,
			boolean createNewIndex) throws IOException {

		if (indexWriter == null) {
			indexWriter = new IndexWriter(directory, getAnalyzer(),
					createNewIndex, IndexWriter.MaxFieldLength.LIMITED);
		}

	}

	public synchronized void openIndexForWriting(String indexDir,
			Analyzer analyzer, boolean createNewIndex) throws IOException {
		File indexDirFile = new File(indexDir);
		openIndexForWriting(indexDirFile, createNewIndex);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.arunma.th.indexer.impl.Indexer#save()
	 */
	public synchronized void save() {
		try {
			indexWriter.optimize();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (indexWriter != null) {
				ThUtils.closeQuietly(indexWriter);
			}
			indexWriter = null;
		}
	}

	public String getClassName() {

		return getClass().getName();
	}

	public Analyzer getAnalyzer() {
		return (config.getAnalyzer());
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public ThConfig getConfig() {
		return config;
	}

	public void setConfig(ThConfig config) {
		this.config = config;
	}

	public ThConfigManager getManager() {
		return manager;
	}

	public void setManager(ThConfigManager manager) {
		this.manager = manager;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getDocumentCount() throws IOException {
		int numDocs = 0;
		if (indexWriter != null) {
			numDocs = indexWriter.maxDoc();
		}
		return numDocs;
	}
}
