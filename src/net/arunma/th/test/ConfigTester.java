package net.arunma.th.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import junit.framework.TestCase;

import com.arunma.th.config.ThConfig;
import com.arunma.th.config.ThConfigManager;
import com.arunma.th.config.ThField;

public class ConfigTester extends TestCase {
	
	ThConfig thConfig=null;

	BeanFactory beanfactory=null;
	ThConfigManager manager=null;
	@Override
	protected void setUp() throws Exception {
		Resource res = new FileSystemResource(new File("resources/spring-tomahawk.xml"));
		 beanfactory = new XmlBeanFactory(res);
		 manager=(ThConfigManager)beanfactory.getBean("configManager");
		 thConfig=manager.getTomConfig();
	}
	
	
	/**
	 * 
	 */
	/*public void testAnalyzerClassName(){
		
		String analyzerClassName=thConfig.getAnalyzer();
		
		assertNotNull(analyzerClassName);
//		assertEquals("org.apache.lucene.analysis.standard.StandardAnalyzer",analyzerClassName);
	}*/

	public void testThField(){
		
		List<ThField> txtFields=thConfig.getTxtFields();
		
		assertNotNull(txtFields);
		
		assertEquals("fullText",txtFields.get(0).getName());
		assertEquals("content",txtFields.get(0).getValue());
		assertEquals("Text",txtFields.get(0).getType());
//		assertEquals("org.apache.lucene.analysis.standard.StandardAnalyzer",analyzerClassName);
	}
	
	public void testMimeTypeMappings(){
		Map<String, String> mimeTypeMappings=thConfig.getMimeTypeMap();
		assertNotNull(mimeTypeMappings);
		assertEquals("textIndexer", mimeTypeMappings.get("text/plain"));
	}
	
	
}
