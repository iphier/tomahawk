package com.arunma.th.indexer;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.arunma.th.config.ThField;

public class IndexerHelper {
	static Logger logger = Logger.getRootLogger();

	public static Document convertThFieldsToLuceneDoc(
			List<ThField> fieldsContentAndType) {
		if (fieldsContentAndType == null) {
			return null;
		}
		Document doc = new Document();

		Field field=null;
		for (ThField fieldColl : fieldsContentAndType) {
			ThField thField = (ThField) fieldColl;
			if (thField.getType().equalsIgnoreCase("Text")) {
				field = new Field(thField.getName(), thField.getValue(),
						Field.Store.YES, Field.Index.ANALYZED);
			} else if (thField.getType().equalsIgnoreCase("Keyword")) {
				field = new Field(thField.getName(), thField.getValue(),
						Field.Store.YES, Field.Index.NOT_ANALYZED);
			}
			if (field != null) {
				doc.add(field);
			}
		}

		return doc;
	}
}
