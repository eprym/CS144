package edu.ucla.cs.cs144;

import java.util.Calendar;
import java.util.Date;

import edu.ucla.cs.cs144.AuctionSearchClient;
import edu.ucla.cs.cs144.SearchResult;
import org.apache.axis2.AxisFault;

public class AuctionSearchTest {
	public static void main(String[] args1)
	throws AxisFault {

		String message = "Test message";
		String reply = AuctionSearchClient.echo(message);
		System.out.println("Reply: " + reply);
		
		String query = "superman";
		SearchResult[] basicResults = AuctionSearchClient.basicSearch(query, 0, 20);
		System.out.println("Basic Seacrh Query: " + query);
		System.out.println("Received " + basicResults.length + " results");
		for(SearchResult result : basicResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
		
                SearchRegion region =
                    new SearchRegion(33.774, -118.63, 34.201, -117.38);
                SearchResult[] spatialResults = AuctionSearchClient.spatialSearch("camera", region, 0, 20);
                System.out.println("Spatial Seacrh");
                System.out.println("Received " + spatialResults.length + " results");
                for(SearchResult result : spatialResults) {
                        System.out.println(result.getItemId() + ": " + result.getName());
                }

		String itemId = "1497595357";
		String item = AuctionSearchClient.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// Add your own test here
	}
}
