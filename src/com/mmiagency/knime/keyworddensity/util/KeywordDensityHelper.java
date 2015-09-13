/*
 * ------------------------------------------------------------------------
 * Copyright by MMI Agency, Houston, Texas, USA
 * Website: http://www.mmiagency.com; Contact: 713-929-6900
 *
 * The MMI KNIME Node is Copyright (C) 2015, MMI Agency The KNIME Nodes 
 * are free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your 
 * option) any later version. 
 * 
 * The KNIME Nodes are distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details. You should have received a 
 * copy of the GNU General Public License along with the KNIME Nodes. If 
 * not, see <http://www.gnu.org/licenses/>.
 * ------------------------------------------------------------------------
 */
package com.mmiagency.knime.keyworddensity.util;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.jsoup.Jsoup;

/**
 * Leverage Jsoup and Lucene to parse HTML content, stop word removal and
 * counting of term frequency.  Term frequency is then used to calculate 
 * percentage of appearance in content.
 * 
 * @author ed
 *
 */
public class KeywordDensityHelper {
	
	private String m_url;
	private String m_content;
	private int m_total = 0;
	private Map<String, Integer> m_keywordMap = new HashMap<String, Integer>();
	private List<String> m_excludeList = new ArrayList<String>();
	private boolean m_includeMetaKeywords = true;
	private boolean m_includeMetaDescription = true;
	private boolean m_includePageTitle = true;

	public KeywordDensityHelper(final String url, final String content, final String exclude, final boolean includeMetaKeywords, final boolean includeMetaDescription, final boolean includePageTitle) {
		m_url = url;
		m_content = content;
		
		if (exclude != null) {
			exclude.replace(",", " ");
			String[] tokens = exclude.split(" ");
			for (String token : tokens) {
				token = token.trim().toLowerCase();
				m_excludeList.add(token);
			}
		}
		
		m_includeMetaKeywords = includeMetaKeywords;
		m_includeMetaDescription = includeMetaDescription;
		m_includePageTitle = includePageTitle;
	}
	
	public void execute() throws IOException {
		
		org.jsoup.nodes.Document jdoc = null;
		
		// pull content using Jsoup 
		if (m_content != null && !m_content.trim().isEmpty()) {
			jdoc = Jsoup.parse(m_content);
		} else {
			jdoc = Jsoup.connect(m_url).get();
		}
		
		StringWriter text = new StringWriter();
        
        if (m_includeMetaKeywords) {
        	text.write(jdoc.select("meta[name=keywords]").attr("content"));
        	text.write(" ");
        }
        if (m_includeMetaDescription) {
        	text.write(jdoc.select("meta[name=description]").attr("content"));
        	text.write(" ");
        }
        if (m_includePageTitle) {
        	text.write(jdoc.select("title").text());
        	text.write(" ");
        }

        text.write(jdoc.select("body").text());

        // analyze content with Lucene
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);    	
        
        FieldType textFieldType = new FieldType();
        textFieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
        textFieldType.setTokenized(true);
        textFieldType.setStored(true);
        textFieldType.setStoreTermVectors(true);
        
        Document doc = new Document();
        Field textField = new Field("content", "", textFieldType);
                
        textField.setStringValue(text.toString());        
        doc.add(textField);
        
        indexWriter.addDocument(doc);        
        indexWriter.commit();
        indexWriter.close();
        
        IndexReader indexReader = DirectoryReader.open(directory);
        Terms termsVector = null;
        TermsEnum termsEnum = null;
        BytesRef term = null;
        String val = null;
        PostingsEnum postingsEnum = null;

        for (int i = 0; i < indexReader.maxDoc(); i++) {
            termsVector = indexReader.getTermVector(i, "content");
            termsEnum = termsVector.iterator();
            while ( (term = termsEnum.next()) != null ) {
                val = term.utf8ToString();
                // skip term matching exclude list
                if (m_excludeList.contains(val)) {
                	continue;
                }
                postingsEnum = termsEnum.postings(postingsEnum);
                if (postingsEnum.nextDoc() >= 0) {
                	add(val, postingsEnum.freq());
                }
            }
        }
        
        indexReader.close();
        directory.close();	
        
        // sort map by value
        sortMap();
	}
	
	private void add(final String keyword, final int frequency) {
		m_keywordMap.put(keyword, frequency);
		m_total += frequency;
	}
	
	public Iterator iterator() {
		return new KeywordDensityIterator(m_url, m_keywordMap, m_total);
	}
	
	private void sortMap() {
		ValueComparator vc =  new ValueComparator(m_keywordMap);
		TreeMap<String,Integer> sortedMap = new TreeMap<String,Integer>(vc);
		sortedMap.putAll(m_keywordMap);
		m_keywordMap = sortedMap;
	}
	
	private static class ValueComparator implements Comparator<String> {
		 
	    Map<String, Integer> map;
	 
	    public ValueComparator(Map<String, Integer> base) {
	        this.map = base;
	    }
	 
	    public int compare(String a, String b) {
	        if (map.get(a) >= map.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys 
	    }
	}
    
	private class KeywordDensityIterator implements Iterator {

		private String url;
		private BigDecimal total;
		private Map<String, Integer> keywordMap;
		private Iterator<Map.Entry<String, Integer>> iterator;
		private NumberFormat percentageFormat = NumberFormat.getPercentInstance();

		public KeywordDensityIterator(String url, Map<String, Integer> map, int total) {
			this.url = url;
			this.keywordMap = map;
			this.total = new BigDecimal(total);
			this.iterator = this.keywordMap.entrySet().iterator();
			this.percentageFormat.setMinimumFractionDigits(2);
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public Object next() {
			Map.Entry<String, Integer> mapEntry = iterator.next();
			KeywordDensityRowFactory factory = new KeywordDensityRowFactory();
			return new KeywordDensityRowEntry(
					url,
					mapEntry.getKey(),
					new BigDecimal(mapEntry.getValue()).divide(total, 8, RoundingMode.HALF_UP).doubleValue(),
					mapEntry.getValue()
					);
		}

		public void remove() {
			iterator.remove();
		}	
	}
}
