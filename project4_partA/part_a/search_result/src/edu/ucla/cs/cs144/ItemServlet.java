package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String itemID =request.getParameter("q");
        int numResultToSkip=5;
        int numResultToReturn=20;
        SearchResult[] itemResults=AuctionSearchClient.basicSearch(itemID,numResultToSkip,numResultToReturn);

        //request.setAttribute("numResultToSkip",numResultToSkip);
        //request.setAttribute("numResultToReturn",numResultToReturn);
        request.setAttribute("itemResults",itemResults);

        request.getRequestDispatcher("/itemResults.jsp").forward(request,response);

    }
}
