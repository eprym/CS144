package edu.ucla.cs.cs144;

import java.util.*;
import java.io.*;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class CheckServlet extends HttpServlet implements Servlet {
       
    public CheckServlet() {}
    
    // public class xmlObj{
    // 	public String ItemID, Name, Currently,BuyPrice,First_Bid, Number_of_Bids, Location;
    // 	public String Latitude, Longitude, Started, Ends, SellerRating, SellerID, Description;
    // 	public String[] Categories;
    // 	public Bid[] bids;
    	
    // 	public class Bid implements Comparable<Bid>{
    // 		public String BidderRating, BidderID, BidLocation, BidCountry, Time, Amount;
    // 		public Map<String, Integer> map = new HashMap<>();
    // 		public Bid(){
    // 			map.put("Jan", 1);
    // 			map.put("Feb", 2);
    // 			map.put("Mar", 3);
    // 			map.put("Apr", 4);
    // 			map.put("May", 5);
    // 			map.put("Jun", 6);
    // 			map.put("Jul", 7);
    // 			map.put("Aug", 8);
    // 			map.put("Sep", 9);
    // 			map.put("Oct", 10);
    // 			map.put("Nov", 11);
    // 			map.put("Dec", 12);
    // 		}

    //         @Override
    // 		public int compareTo(Bid that){
    // 			String[] thisTime = this.Time.split("-");
    // 			String[] thatTime = that.Time.split("-");
    //             String thisTimeYear = thisTime[2].split(" ")[0];
    //             String thatTimeYear = thatTime[2].split(" ")[0];
    //             String thisTimeMonth = thisTime[0];
    //             String thatTimeMonth = thatTime[0];
    //             String thisTimeDay = thisTime[1];
    //             String thatTimeDay = thatTime[1];
    //             String thisTimeHour = thisTime[2].split(" ")[1];
    //             String thatTimeHour = thatTime[2].split(" ")[1];
    // 			if(!thisTimeYear.equals(thatTimeYear))   return thatTimeYear.compareTo(thisTimeYear);
    //             else if(!thisTimeMonth.equals(thatTimeMonth))   return map.get(thatTimeMonth) - map.get(thisTimeMonth);
    //             else if(!thisTimeDay.equals(thatTimeDay))   return thatTimeDay.compareTo(thisTimeDay);
    //             else return thatTimeHour.compareTo(thisTimeHour);
    // 		}
    // 	}
    // }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session=request.getSession(true);

        String itemID=(String)request.getParameter("itemID");
        ItemServlet.xmlObj xobj=(ItemServlet.xmlObj)session.getAttribute(itemID);
        request.setAttribute("xobj", xobj);
        //request.setAttribute("XMLdata", XMLdata);
        request.getRequestDispatcher("check.jsp").forward(request, response);
        return;



        // try{
            

        //     session.setAttribute("xobj",xobj);
        // 	request.setAttribute("xobj", xobj);
        //     request.setAttribute("XMLdata", XMLdata);
        //     request.getRequestDispatcher("getItem.jsp").forward(request, response);
        //     return;
        // }
        // catch(IOException ex){
        // 	ex.printStackTrace();
        // }
        // catch(SAXException ex){
        // 	ex.printStackTrace();
        // }
        // catch(ParserConfigurationException ex){
        //     ex.printStackTrace();
        // }
        // request.setAttribute("XMLdata", "");
        // request.getRequestDispatcher("getItem.jsp").forward(request, response);

    }
}
