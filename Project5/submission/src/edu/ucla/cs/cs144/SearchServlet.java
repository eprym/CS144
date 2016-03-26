package edu.ucla.cs.cs144;

import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    private class result{

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String itemID=request.getParameter("q");
        int numResultToSkip=0;
        int numResultToReturn=20;
        if(request.getParameter("numResultToSkip")!=null){
        	numResultToSkip=Integer.valueOf(request.getParameter("numResultToSkip"));
        	if(request.getParameter("action").equals("prev")){
        		numResultToSkip-=numResultToReturn;
        		if(numResultToSkip<0)numResultToSkip=0;
        	}
        	else{
        		numResultToSkip+=numResultToReturn;
        	}
        }

        //if(request.getAttribute("numResultToSkip")==null)request.setAttribute("numResultToSkip",66);
        //else{
        
    	//}
        SearchResult[] itemResults=AuctionSearchClient.basicSearch(itemID,numResultToSkip,numResultToReturn);

        if(itemResults.length==0){
        	numResultToSkip-=numResultToReturn;
       		itemResults=AuctionSearchClient.basicSearch(itemID,numResultToSkip,numResultToReturn);
       	}
        
        Map<String,String> resultMap=new LinkedHashMap<>();
        for(SearchResult r:itemResults){
        	resultMap.put(r.getItemId(),r.getName());
       	}
       

        request.setAttribute("itemResults",resultMap);
		request.setAttribute("numResultToSkip",numResultToSkip);
        request.setAttribute("numResultToReturn",numResultToReturn);

        request.getRequestDispatcher("/itemResults.jsp").forward(request, response);
    }
}
