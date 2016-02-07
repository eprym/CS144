package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

	/** Creates a new instance of Indexer */
	public Indexer() {
	}

	private IndexWriter indexWriter=null;
	public void rebuildIndexes() {

		Connection conn = null;

		// create a connection to the database to retrieve Items from MySQL
		try {
			conn = DbManager.getConnection(true);
		} catch (SQLException ex) {
			System.out.println(ex);
		}


	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
		//get data from RDBS
		try {
			Statement stmt=conn.createStatement();
			String selectItems="select ItemID,ItemName,Description from Items order by ItemID";
			String selectCategories="select * from Categories order by ItemID";
//			PreparedStatement select=conn.prepareStatement("SELECT ? FROM ?");
			ResultSet nameDescriptions = stmt.executeQuery(selectItems);
			ResultSet categories = stmt.executeQuery(selectCategories);

			//build the indexWriter
			Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1/"));
			IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
			indexWriter = new IndexWriter(indexDir, iwConfig);
			while(true) {
				String curID=nameDescriptions.getString("ItemID");
				Document doc = new Document();
				doc.add(new StringField("ItemID", curID, Field.Store.YES));
				doc.add(new TextField("ItemName",nameDescriptions.getString("ItemName"),Field.Store.YES));
				doc.add(new TextField("Description",nameDescriptions.getString("Description"), Field.Store.NO));
				StringBuilder fullContent=new StringBuilder();
				fullContent.append(nameDescriptions.getString("ItemName"));
				fullContent.append(" "+nameDescriptions.getString("Description"));
				StringBuilder allCategories=new StringBuilder();
				while(categories.getString("ItemID").equals(curID)){
					allCategories.append(categories.getString("Category"+" "));
					if(!categories.next())break;
				}
				if(!categories.getString("ItemID").equals(curID))categories.previous();
				doc.add(new TextField("Categories",allCategories.toString(),Field.Store.NO));
				fullContent.append(" "+allCategories.toString());
				doc.add(new TextField("Content",fullContent.toString(), Field.Store.NO));
				indexWriter.addDocument(doc);
				if(!nameDescriptions.next())break;
			}
		}catch(SQLException ex){
			System.out.println("SQLException caught");
			System.out.println("---");
			while ( ex != null ){
				System.out.println("Message   : " + ex.getMessage());
				System.out.println("SQLState  : " + ex.getSQLState());
				System.out.println("ErrorCode : " + ex.getErrorCode());
				System.out.println("---");
				ex = ex.getNextException();
			}
		}catch (IOException ex){
			ex.printStackTrace();
		}
		//build the indexWriter

		//close the indexWriter
		try{
			if(indexWriter!=null)indexWriter.close();
		}catch (IOException ex){
			ex.printStackTrace();
		}


		// close the database connection
		try {
			conn.close();
		} catch (SQLException ex) {
			System.out.println(ex);
		}
	}

	public static void main(String args[]) {
		Indexer idx = new Indexer();
		idx.rebuildIndexes();
	}
}
