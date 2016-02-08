package edu.ucla.cs.cs144;

import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public interface IAuctionSearch {
	
	/**
	 * Performs a basic keyword search for the given query string.
	 * 
	 * @param query The keyword phrase for the search.
	 * @param numResultsToSkip The desired number of results to skip from 
	 * the beginning of the full results.
	 * @param numResultsToReturn The desired number of results to return.
	 * @return An array of at most numResultsToReturn SearchResult objects 
	 * representing the results of the query after skipping numResultsToSkip
	 * SearchResult objects.
	 */
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn);
	
	/**
	 * Searhc for all items with the given keywords, which are located 
         * within the specified rectangular region
	 * 
	 * @param query The keyword phrase for the search.
	 * @param region SearchRegion object that represents the rectangular
                  spatial region (in latitude and langitude)
	 * @param numResultsToSkip The desired number of results to skip from 
	 * the beginning of the full results.
	 * @param numResultsToReturn The desired number of results to return.
	 * @return An array of at most numResultsToReturn SearchResult objects 
	 * representing the results of the query after skipping numResultsToSkip
	 * SearchResult objects.
	 */
	public SearchResult[] spatialSearch(String query, SearchRegion region, 
			int numResultsToSkip, int numResultsToReturn);
	
	/**
	 * Rebuilds an Item XML Element (and all of its sub-Elements), for the given
	 * ItemId.
	 * 
	 * @param itemId The ItemId of an item.
	 * @return A String of valid XML data (conforming to the original items.dtd)
	 * containing data about the requested itemId. null is returned when itemId 
	 * is not valid.
	 */
	public String getXMLDataForItemId(String itemId);
	
	/**
	 * Simple Web Service to verify your deployment is in working order.
	 * 
	 * @param message The message to echo.
	 * @return The same message that is passed in.
	 */
	public String echo(String message);

}
