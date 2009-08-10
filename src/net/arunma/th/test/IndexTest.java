package net.arunma.th.test;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.arunma.th.config.ThConfig;
import com.arunma.th.config.ThConfigManager;
import com.arunma.th.indexer.Indexer;
import com.arunma.th.indexer.IndexerFactory;
import com.arunma.th.indexer.text.TextIndexer;


/**
 * @author arun manivannan on Jul 25, 2009
 * First written class for this effort. This is a pet project totally written for the 
 * purposes of learning. Somebody told me that the more you look at good code, the more
 * your programming improves...therefore i am looking into the Lius code available in 
 * sourceforge
 *
 */
public class IndexTest extends TestCase{

	String textFileName="testfiles/BUILD.txt";
	BeanFactory beanfactory=null;
	@Override
	protected void setUp() throws Exception {
		Resource res = new FileSystemResource(new File("resources/spring-tomahawk.xml"));
		 beanfactory = new XmlBeanFactory(res);

	}
	public void testTextContentRetrieval(){
		
		try {
			ThConfigManager manager=(ThConfigManager)beanfactory.getBean("configManager");
			ThConfig config=manager.getTomConfig();
			File file=new File(textFileName);
			Indexer indexer=IndexerFactory.getIndexer(file, config, beanfactory);
			assertNotNull(indexer);
			//indexer.setFileName(fileName);
			assertNotNull(indexer.getContent());
		} catch (Exception e) {
			fail (e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void testTextIndexingFields(){
		TextIndexer textIndexer=(TextIndexer)beanfactory.getBean("textIndexer");
		
		System.out.println(textIndexer.getFieldsToIndex().toString());
		assertNotNull(textIndexer.getFieldsToIndex());
		
	}
	
//Sorry, I lost total touch of spring.. i am really not convinced with the magic it does.
	public void testSpringInitializationOfAnalyzer(){
		
		Analyzer analyzer=(Analyzer)beanfactory.getBean("analyzer");
		assertNotNull(analyzer);
		System.out.println(analyzer);
		
	}
	
	public void testIndexerForMimeType(){
		ThConfigManager manager=(ThConfigManager)beanfactory.getBean("configManager");
		ThConfig config=manager.getTomConfig();
		File file=new File(textFileName);
		Indexer indexer=IndexerFactory.getIndexer(file, config, beanfactory);
		assertNotNull(indexer);
		assertEquals(indexer.getClassName(), "com.arunma.th.indexer.text.TextIndexer");
	}
	
	public void testTextIndexing(){
		ThConfigManager manager=(ThConfigManager)beanfactory.getBean("configManager");
		ThConfig config=manager.getTomConfig();
		File file=new File(textFileName);
		Indexer indexer=IndexerFactory.getIndexer(file, config, beanfactory);
		assertNotNull(indexer);
		try {
			indexer.openIndexForWriting(new RAMDirectory(), true);
			indexer.addDocument();
			assertEquals (1, indexer.getDocumentCount());
			indexer.save();
		} catch (IOException e) {
			fail (e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
}
