//package edu.ucla.cs.cs144;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;




import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//import edu.ucla.cs.cs144.DbManager;
//import edu.ucla.cs.cs144.SearchRegion;
//import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
		TopDocs topDocs=null;
		try {
			SearchEngine searchEngine = new SearchEngine();
			topDocs=searchEngine.performSearch(query,numResultsToReturn+numResultsToSkip);
			ScoreDoc[] scoreDocs=topDocs.scoreDocs;
			SearchResult[] r=new SearchResult[numResultsToReturn];
			for(int i=numResultsToSkip;i<scoreDocs.length;++i){
				Document doc=searchEngine.getDocument(scoreDocs[i].doc);
				String itemID=doc.get("ItemID");
				String itemName=doc.get("ItemName");
				r[i-numResultsToSkip]=new SearchResult(itemID,itemName);
				return r;
			}
		}catch (IOException ex){
			ex.printStackTrace();
		}catch (ParseException ex){
			ex.printStackTrace();
		}
		return new SearchResult[0];
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		SearchResult[] r1=null;
		try {
			TopDocs topDocs=null;
			SearchEngine searchEngine = new SearchEngine();
			topDocs=searchEngine.performSearch(query,numResultsToReturn+numResultsToSkip);
			ScoreDoc[] scoreDocs=topDocs.scoreDocs;
			r1=new SearchResult[numResultsToReturn];
			for(int i=numResultsToSkip;i<scoreDocs.length;++i){
				Document doc=searchEngine.getDocument(scoreDocs[i].doc);
				String itemID=doc.get("ItemID");
				String itemName=doc.get("ItemName");
				r1[i-numResultsToSkip]=new SearchResult(itemID,itemName);
//				return r1;
			}
		}catch (IOException ex){
			ex.printStackTrace();
		}catch (ParseException ex){
			ex.printStackTrace();
		}

		Connection conn=null;
		double lx=region.getLx(),ly=region.getLy(),rx=region.getRx(),ry=region.getRy();
		String spatialQuery="select ItemID,ItemName,AsText(Coordinate)" +
				"from ItemSpatial where within(Coordinate,GeomFromText(" +
				"'Polygon(("+lx+" "+ly+","+lx+" "+ry+","+rx+" "+ry+","
				+rx+" "+ly+","+lx+" "+ly+"))')) order by ItemID;";
		try{
			conn=DbManager.getConnection(true);
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(spatialQuery);
		}catch (SQLException ex){
			ex.printStackTrace();
		}

		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		try{
			Connection conn = DbManager.getConnection(true);
			Statement stmt = conn.createStatement();
			String query = "select * from Items where ItemID = " + itemId;
			ResultSet rs = stmt.executeQuery(query);
			if(!rs.next())	return "";
			String[] addName = {"ItemID", "ItemName", "CurrentPrice", "BuyPrice", "FirstBid", "NOofBids",
					"ItemLocationName", "Latitude", "Longitude", "Country", "Description"};
			List<String> list = getFirstLayer(addName, rs);
			String Started = rs.getTimestamp("Started").toString();
			Started = formatDate(Started);
			String End = rs.getTimestamp("End").toString();
			End = formatDate(Started);
			String SellerID = rs.getString("SellerID");
			
			String query_Bids = "select * from Bids where ItemID = " + itemId;
			ResultSet rs_Bids = stmt.executeQuery(query_Bids);
			
			String query_Categories = "select * from Categories where ItemID = " + itemId;
			ResultSet rs_Categories = stmt.executeQuery(query_Categories);
			
			String query_Sellers = "select * from Bidders where UserID = " + SellerID;
			ResultSet rs_Sellers = stmt.executeQuery(query_Sellers);
			
			DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder icBuilder;
	       
	        icBuilder = icFactory.newDocumentBuilder();
	        org.w3c.dom.Document doc = icBuilder.newDocument();
	        Element mainRootElement = doc.createElementNS(null, "Item");
	        mainRootElement.setAttribute("ItemID", list.get(0));
	        doc.appendChild(mainRootElement);
	        mainRootElement.appendChild(getItemElements(doc, "Name", list.get(1)));
	        while(rs_Categories.next()){
	        	mainRootElement.appendChild(getItemElements(doc, "Category", rs_Categories.getString("Category")));
	        }
	        mainRootElement.appendChild(getItemElements(doc, "Currently", list.get(2)));
	        mainRootElement.appendChild(getItemElements(doc, "First_Bid", list.get(4)));
	        if(list.get(3) != null)	mainRootElement.appendChild(getItemElements(doc, "Buy_Price", list.get(3)));
	        mainRootElement.appendChild(getItemElements(doc, "Number_of_Bids", list.get(5)));
	        
	        Element bids = doc.createElement("Bids");
	        while(rs_Bids.next()){
	        	String bidderID = rs_Bids.getString("BidderID");
	        	String query_bidder = "select * from Bidders where UserID = " + bidderID;
	        	ResultSet rs_Bidders = stmt.executeQuery(query_bidder);
	        	Element bidder = doc.createElement("Bidder");
	        	bidder.setAttribute("UserID", rs_Bidders.getString("UserID"));
	        	bidder.setAttribute("Rating", rs_Bidders.getString("Rating"));
	        	if(rs_Bidders.getString("Location") != null){
	        		bidder.appendChild(getItemElements(doc, "Location", rs_Bidders.getString("Location")));
	        	}
	        	if(rs_Bidders.getString("Country") != null){
	        		bidder.appendChild(getItemElements(doc, "Country", rs_Bidders.getString("Country")));
	        	}
	        	bids.appendChild(bidder);
	        	bids.appendChild(getItemElements(doc, "Time", rs_Bids.getString("Time")));
	        	bids.appendChild(getItemElements(doc, "Amount", rs_Bids.getString("Amount")));
	        }
	        
	        Element location = (Element)getItemElements(doc, "Location", list.get(6));
	        if(list.get(7) != null)	location.setAttribute("Latitude", list.get(7));
	        if(list.get(8) != null)	location.setAttribute("Longitude", list.get(8));
	        mainRootElement.appendChild(location);
	        
	        mainRootElement.appendChild(getItemElements(doc, "Country", list.get(9)));
	        mainRootElement.appendChild(getItemElements(doc, "Started", Started));
	        mainRootElement.appendChild(getItemElements(doc, "Ends", End));
	        
	        Element seller = doc.createElement("Seller");
	        seller.setAttribute("UserID", rs_Sellers.getString("UserID"));
	        seller.setAttribute("Rating", rs_Sellers.getString("Rating"));
	        mainRootElement.appendChild(seller);
	        
	        mainRootElement.appendChild(getItemElements(doc, "Description", list.get(10)));
	        
	        Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));
            return writer.getBuffer().toString();
	        
		}
		catch(SQLException ex){
			ex.printStackTrace();
			return null;
		}
		catch(ParserConfigurationException ex){
			ex.printStackTrace();
			return null;
		}
		catch(TransformerConfigurationException ex){
			ex.printStackTrace();
			return null;
		}
		catch(TransformerException ex){
			ex.printStackTrace();
			return null;
		}
		
	}
	
	public String echo(String message) {
		return message;
	}
	
	private List<String> getFirstLayer(String[] addName, ResultSet rs){
		List<String> list = new ArrayList<>();
		try{
			for(String add : addName){
				list.add(rs.getString(add));
			}
			return list;
		}
		catch(SQLException ex){
			ex.printStackTrace();
			return null;
		}
		
		
	}
	private String formatDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat toParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date parsed = toParse.parse(date);
            return format.format(parsed).toString();
        }
        catch(java.text.ParseException ex){
            ex.printStackTrace();
            return null;
        }
    }
	
	private Node getItemElements(org.w3c.dom.Document doc, String name, String value){
		Element node = doc.createElement(name);
		node.appendChild(doc.createTextNode(value));
		return node;
	}
	

}
