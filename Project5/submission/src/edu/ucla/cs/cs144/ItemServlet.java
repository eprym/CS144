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


public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}
    
    public class xmlObj{
    	public String ItemID, Name, Currently,BuyPrice,First_Bid, Number_of_Bids, Location;
    	public String Latitude, Longitude, Started, Ends, SellerRating, SellerID, Description;
    	public String[] Categories;
    	public Bid[] bids;
    	
    	public class Bid implements Comparable<Bid>{
    		public String BidderRating, BidderID, BidLocation, BidCountry, Time, Amount;
    		public Map<String, Integer> map = new HashMap<>();
    		public Bid(){
    			map.put("Jan", 1);
    			map.put("Feb", 2);
    			map.put("Mar", 3);
    			map.put("Apr", 4);
    			map.put("May", 5);
    			map.put("Jun", 6);
    			map.put("Jul", 7);
    			map.put("Aug", 8);
    			map.put("Sep", 9);
    			map.put("Oct", 10);
    			map.put("Nov", 11);
    			map.put("Dec", 12);
    		}

            @Override
    		public int compareTo(Bid that){
    			String[] thisTime = this.Time.split("-");
    			String[] thatTime = that.Time.split("-");
                String thisTimeYear = thisTime[2].split(" ")[0];
                String thatTimeYear = thatTime[2].split(" ")[0];
                String thisTimeMonth = thisTime[0];
                String thatTimeMonth = thatTime[0];
                String thisTimeDay = thisTime[1];
                String thatTimeDay = thatTime[1];
                String thisTimeHour = thisTime[2].split(" ")[1];
                String thatTimeHour = thatTime[2].split(" ")[1];
    			if(!thisTimeYear.equals(thatTimeYear))   return thatTimeYear.compareTo(thisTimeYear);
                else if(!thisTimeMonth.equals(thatTimeMonth))   return map.get(thatTimeMonth) - map.get(thisTimeMonth);
                else if(!thisTimeDay.equals(thatTimeDay))   return thatTimeDay.compareTo(thisTimeDay);
                else return thatTimeHour.compareTo(thisTimeHour);
    		}
    	}
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session=request.getSession(true);

        String XMLdata = AuctionSearchClient.getXMLDataForItemId(request.getParameter("id"));
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource source = new InputSource(new StringReader(XMLdata));
            Document doc = null;
        
        	doc = builder.parse(source);
        	xmlObj xobj = new xmlObj();
        	xobj.ItemID = ((Element)doc.getElementsByTagName("Item").item(0)).getAttribute("ItemID");
        	xobj.Name = doc.getElementsByTagName("Name").item(0).getTextContent();
        	xobj.Currently = doc.getElementsByTagName("Currently").item(0).getTextContent();
            if(doc.getElementsByTagName("Buy_Price").getLength()!=0){
                xobj.BuyPrice = doc.getElementsByTagName("Buy_Price").item(0).getTextContent();
            }
        	xobj.First_Bid = doc.getElementsByTagName("First_Bid").item(0).getTextContent();
            xobj.Number_of_Bids = doc.getElementsByTagName("Number_of_Bids").item(0).getTextContent();
        	Node location = doc.getElementsByTagName("Location").item(0);
        	xobj.Location = location.getTextContent();
        	xobj.Latitude = ((Element)location).getAttribute("Latitude");
        	xobj.Longitude = ((Element)location).getAttribute("Longitude");
        	xobj.Started = doc.getElementsByTagName("Started").item(0).getTextContent();
        	xobj.Ends = doc.getElementsByTagName("Ends").item(0).getTextContent();
        	Node seller = doc.getElementsByTagName("Seller").item(0);
        	xobj.SellerID = ((Element)seller).getAttribute("UserID");
        	xobj.SellerRating = ((Element)seller).getAttribute("Rating");
        	xobj.Description = doc.getElementsByTagName("Description").item(0).getTextContent();
        	NodeList catlist = doc.getElementsByTagName("Category");
        	xobj.Categories = new String[catlist.getLength()];
        	for(int i=0; i<xobj.Categories.length; i++){
        		xobj.Categories[i] = catlist.item(i).getTextContent();
        	}
        	NodeList bidderlist = doc.getElementsByTagName("Bidder");
        	NodeList timelist = doc.getElementsByTagName("Time");
        	NodeList amountlist = doc.getElementsByTagName("Amount");
        	if(bidderlist.getLength() != 0)	{
                xobj.bids = new xmlObj.Bid[bidderlist.getLength()];
        	   for(int i=0; i<xobj.bids.length; i++){
        		  xobj.bids[i] = xobj.new Bid();
        		  xobj.bids[i].BidderID = ((Element)bidderlist.item(i)).getAttribute("UserID");
        		  xobj.bids[i].BidderRating = ((Element)bidderlist.item(i)).getAttribute("Rating");
        		  xobj.bids[i].Time = timelist.item(i).getTextContent();
        		  xobj.bids[i].Amount = amountlist.item(i).getTextContent();
        		  xobj.bids[i].BidCountry = ((Element)bidderlist.item(i)).getElementsByTagName("Country").item(0).getTextContent();
        		  xobj.bids[i].BidLocation = ((Element)bidderlist.item(i)).getElementsByTagName("Location").item(0).getTextContent();
        	   }
        	   Arrays.sort(xobj.bids);
            }
            session.setAttribute(xobj.ItemID,xobj);
        	request.setAttribute("xobj", xobj);
            request.setAttribute("XMLdata", XMLdata);
            request.getRequestDispatcher("getItem.jsp").forward(request, response);
            return;
        }
        catch(IOException ex){
        	ex.printStackTrace();
        }
        catch(SAXException ex){
        	ex.printStackTrace();
        }
        catch(ParserConfigurationException ex){
            ex.printStackTrace();
        }
        request.setAttribute("XMLdata", "");
        request.getRequestDispatcher("getItem.jsp").forward(request, response);

    }
}
