/*
 * Main.java
 *
 * Created on 6 March 2006, 11:51
 *
 */

package lucene.demo;

import java.util.Iterator;

import lucene.demo.search.*;
import lucene.demo.business.*;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;

/**
 *
 * @author John
 */
public class Main {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

      try {
	// build a lucene index
        System.out.println("rebuildIndexes");
        Indexer  indexer = new Indexer();
        indexer.rebuildIndexes();
        System.out.println("rebuildIndexes done");

        // perform search on "Notre Dame museum"
        // and retrieve the top 100 result
        System.out.println("performSearch");
        SearchEngine se = new SearchEngine();
        TopDocs topDocs = se.performSearch("Notre Dame museum", 100);

        System.out.println("Results found: " + topDocs.totalHits);
        ScoreDoc[] hits = topDocs.scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            Document doc = se.getDocument(hits[i].doc);
            System.out.println(doc.get("name")
                               + " " + doc.get("city")
                               + " (" + hits[i].score + ")");

        }
        System.out.println("performSearch done");
      } catch (Exception e) {
        System.out.println("Exception caught.\n");
      }
    }
    
}
