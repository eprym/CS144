/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    static boolean appendFlag = true;
    
    static final String[] typeName = {
    "none",
    "Element",
    "Attr",
    "Text",
    "CDATA",
    "EntityRef",
    "Entity",
    "ProcInstr",
    "Comment",
    "Document",
    "DocType",
    "DocFragment",
    "Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    // Non-recursive(NR) version of Node.getAttributesByTagName(...)
    static Attr[] getAttributesByTagNameNR(Element e, String tagName){
        org.w3c.dom.NamedNodeMap nattrib = e.getAttributes();
        List<Attr> list = new ArrayList<>();
        if(nattrib != null && nattrib.getLength() > 0){
            for(int i=0; i<nattrib.getLength(); i++){
                if(nattrib.item(i).getNodeName().equals(tagName) && nattrib.item(i).getNodeType() == Node.ATTRIBUTE_NODE){
                    list.add((Attr)nattrib.item(i));
                }
            }
        }
        Attr[] res = new Attr[list.size()];
        res = list.toArray(res);
        return res;
    }
    
    static Attr getAttributeByTagNameNR(Element e, String tagName) {
        org.w3c.dom.NamedNodeMap nattrib = e.getAttributes();
        if(nattrib != null && nattrib.getLength() > 0){
            for(int i=0; i<nattrib.getLength(); i++){
                if(nattrib.item(i).getNodeName().equals(tagName) && nattrib.item(i).getNodeType() == Node.ATTRIBUTE_NODE){
                    return (Attr)nattrib.item(i);
                }
            }
        }
        return null;
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    /* Returns the text associated with the given attribute (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getAttributeText(Attr e) {
        if (e.getChildNodes().getLength() == 1) {
            Text attributeText = (Text) e.getFirstChild();
            return attributeText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    // format the time into MySQL TIMESTAMP format
    static String formatDate(String date){
        SimpleDateFormat toParse = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date parsed = toParse.parse(date);
            return format.format(parsed).toString();
        }
        catch(ParseException ex){
            System.out.println("cannot parse the " + date);
            return null;
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        //System.out.println("Successfully parsed - " + xmlFile);
		//System.out.println("start to write SQL load files");
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        
        /**************************************************************/
        createTable_Items(doc);
        createTable_Category(doc);
        createTable_Bids(doc);
        createTable_Bidders(doc);
        createTable_Sellers(doc);
        //System.out.println("Write SQL load file successfully");
        //recursiveDescent(doc, 0);
    }
    
    static void createTable_Items(Document doc){
        FileWriter fw = null;
        String filename = "./Items.csv";
        try{
            fw = new FileWriter(filename, appendFlag);
            Node root = doc.getDocumentElement();
            //System.out.println(root.getNodeType());
            org.w3c.dom.NodeList nlist = root.getChildNodes();
            String[] tagNames = {"ItemID","Name","Currently","Buy_Price","First_Bid","Number_of_Bids",
                    "Location", "Latitude", "Longitude", "Country", "Started", "Ends","UserID", "Description"};
            for(int i=0; i<nlist.getLength(); i++){
                if(nlist.item(i).getNodeType() == Node.ELEMENT_NODE){
                    for(int j=0; j<tagNames.length; j++){
                        if(!tagNames[j].equals("UserID") && !tagNames[j].equals("Latitude") && !tagNames[j].equals("Longitude")){
                            if(!dfs((Element)nlist.item(i), fw, filename, tagNames[j])){
                                fw.append("\\N");
                            }
                        }
                        else if (tagNames[j].equals("UserID")){
                            if(!dfs_ele_attr((Element)nlist.item(i), fw, filename, "Seller", tagNames[j])){
                                fw.append("\\N");
                            }
                        }
                        else{
                            if(!dfs_ele_attr((Element)nlist.item(i), fw, filename, "Location", tagNames[j])){
                                fw.append("\\N");
                            }
                        }
                        if(j < tagNames.length-1)   fw.append(columnSeparator);
                    }   
                    fw.append("\n");
                }   
            }
            fw.close();
            
        }
        catch(IOException ex){
            System.out.println("cannot open the file" + filename);
            return;
        }
    }
    
    static void createTable_Category(Document doc){
        FileWriter fw = null;
        String filename = "./Category.csv";
        try{
            fw = new FileWriter(filename, appendFlag);
            Node root = doc.getDocumentElement();
            //System.out.println(root.getNodeType());
            org.w3c.dom.NodeList nlist = root.getChildNodes();
            String[] tagNames = {"ItemID","Category"};
            for(int i=0; i<nlist.getLength(); i++){
                StringBuilder id = new StringBuilder();
                List<String> catg = new ArrayList<>();
                if(nlist.item(i).getNodeType() == Node.ELEMENT_NODE){
                    for(int j=0; j<tagNames.length; j++){
                        if(tagNames[j].equals("ItemID")){
                            if(!dfs_string((Element)nlist.item(i), fw, filename, tagNames[j], id)){
                                fw.append("\\N");
                            }
                        }
                        else{
                            if(!dfs_strings((Element)nlist.item(i), fw, filename, tagNames[j], catg)){
                                fw.append("\\N");
                            }
                        }
                    }
                    if(id.length() > 0 && catg.size() > 0){
                        for(int k=0; k<catg.size(); k++){
                            fw.append(id.toString());
                            fw.append(columnSeparator);
                            fw.append(catg.get(k));
                            fw.append("\n");
                        }
                    }
                }   
            }
            fw.close();
            
        }
        catch(IOException ex){
            System.out.println("cannot open the file" + filename);
            return;
        }
    }
    
    static void createTable_Bids(Document doc){
        FileWriter fw = null;
        String filename = "./Bids.csv";
        try{
            fw = new FileWriter(filename, appendFlag);
            Node root = doc.getDocumentElement();
            //System.out.println(root.getNodeType());
            org.w3c.dom.NodeList nlist = root.getChildNodes();
            String[] tagNames = {"ItemID","UserID","Time","Amount"};
            for(int i=0; i<nlist.getLength(); i++){
                if(nlist.item(i).getNodeType() == Node.ELEMENT_NODE){
                    Element bids = getElementByTagNameNR((Element)nlist.item(i), "Bids");
                    if(bids == null)    continue;
                    Element[] bid = getElementsByTagNameNR(bids, "Bid");
                    if(bid.length == 0) continue;
                    StringBuilder id = new StringBuilder();
                    List<String> usrid = new ArrayList<>();
                    List<String> time = new ArrayList<>();
                    List<String> amount = new ArrayList<>();
                    for(int j=0; j<tagNames.length; j++){
                        if(tagNames[j].equals("ItemID")){
                            if(!dfs_string((Element)nlist.item(i), fw, filename, tagNames[j], id)){
                                System.out.println("cannot not found the ItemID");
                                throw new FileNotFoundException();
                            }
                        }
                        else{
                            if(tagNames[j].equals("UserID")){
                                for(Element b : bid){
                                    StringBuilder toReturn = new StringBuilder();
                                    if(dfs_ele_attr_string(b, fw, filename, "Bidder", "UserID", toReturn)){
                                        usrid.add(toReturn.toString());
                                    }
                                    else usrid.add("\\N");
                                }
                            }
                            else if(tagNames[j].equals("Time")){
                                for(Element b : bid){
                                    StringBuilder toReturn = new StringBuilder();
                                    if(dfs_string(b, fw, filename, "Time", toReturn)){
                                        time.add(formatDate(toReturn.toString()));
                                    }
                                    else time.add("\\N");
                                }
                            }
                            else if(tagNames[j].equals("Amount")){
                                for(Element b : bid){
                                    StringBuilder toReturn = new StringBuilder();
                                    if(dfs_string(b, fw, filename, "Amount", toReturn)){
                                        amount.add(strip(toReturn.toString()));
                                    }
                                    else amount.add("\\N");
                                }
                            }
                            
                        }
                        
                    }
                    for(int k=0; k<bid.length; k++){
                        fw.append(id.toString());
                        fw.append(columnSeparator);
                        fw.append(usrid.get(k));
                        fw.append(columnSeparator);
                        fw.append(time.get(k));
                        fw.append(columnSeparator);
                        fw.append(amount.get(k));
                        fw.append("\n");
                    }
                }   
            }
            fw.close();
            
        }
        catch(IOException ex){
            System.out.println("cannot open the file" + filename);
            return;
        }
    }
    
    static void createTable_Bidders(Document doc){
        FileWriter fw = null;
        String filename = "./Bidders.csv";
        try{
            fw = new FileWriter(filename, appendFlag);
            Node root = doc.getDocumentElement();
            //System.out.println(root.getNodeType());
            org.w3c.dom.NodeList nlist = root.getChildNodes();
            String[] tagNames = {"UserID","Rating","Location", "Country"};
            for(int i=0; i<nlist.getLength(); i++){
                if(nlist.item(i).getNodeType() == Node.ELEMENT_NODE){
                    Element bids = getElementByTagNameNR((Element)nlist.item(i), "Bids");
                    if(bids == null)    continue;
                    Element[] bid = getElementsByTagNameNR(bids, "Bid");
                    if(bid.length == 0) continue;
                    StringBuilder id = new StringBuilder();
                    List<String> usrid = new ArrayList<>();
                    List<String> time = new ArrayList<>();
                    List<String> amount = new ArrayList<>();
                    for(Element b : bid){
                        for(int j=0; j<tagNames.length; j++){
                            if(tagNames[j].equals("UserID") || tagNames[j].equals("Rating")){
                                if(!dfs_ele_attr(b, fw, filename, "Bidder", tagNames[j])){
                                    fw.append("\\N");
                                }
                            }
                            else{
                                if(!dfs(b, fw, filename, tagNames[j]))  fw.append("\\N");
                            }
                            if(j < tagNames.length-1)   fw.append(columnSeparator);
                        }
                        fw.append("\n");
                    }
                }   
            }
            fw.close();
            
        }
        catch(IOException ex){
            System.out.println("cannot open the file" + filename);
            return;
        }
        
    }
    
    static void createTable_Sellers(Document doc){
        FileWriter fw = null;
        String filename = "./Sellers.csv";
        try{
            fw = new FileWriter(filename, appendFlag);
            Node root = doc.getDocumentElement();
            //System.out.println(root.getNodeType());
            org.w3c.dom.NodeList nlist = root.getChildNodes();
            String[] tagNames = {"UserID", "Rating"};
            for(int i=0; i<nlist.getLength(); i++){
                if(nlist.item(i).getNodeType() == Node.ELEMENT_NODE){
                    for(int j=0; j<tagNames.length; j++){
                        if(!dfs_ele_attr((Element)nlist.item(i), fw, filename, "Seller", tagNames[j])){
                            fw.append("\\N");
                        }
                        if(j < tagNames.length-1)   fw.append(columnSeparator);
                    }   
                    fw.append("\n");
                }   
            }
            fw.close();
        }
        catch(IOException ex){
            System.out.println("cannot open the file" + filename);
            return;
        }
    }
    
    static  boolean dfs(Element root, FileWriter fw, String fileName, String tagName){
        Attr att = getAttributeByTagNameNR(root, tagName);
        if(att != null){
            try{
                if(tagName.equals("Currently") || tagName.equals("Buy_Price") || tagName.equals("First_Bid")){
                    fw.append(strip(getAttributeText(att)));
                }
                else if(tagName.equals("Started") || tagName.equals("Ends")){
                    fw.append(formatDate(getAttributeText(att)));
                }
                else fw.append(getAttributeText(att));
                //fw.append(",");
                return true;
            }
            catch(IOException ex){
                System.out.println("cannot write into the file " + fileName);
                return true;
            }
        }
        
        Element ele = getElementByTagNameNR(root, tagName);
        if(ele != null){
            try{
                if(tagName.equals("Currently") || tagName.equals("Buy_Price") || tagName.equals("First_Bid")){
                    fw.append(strip(getElementText(ele)));
                }
                else if(tagName.equals("Started") || tagName.equals("Ends")){
                    fw.append(formatDate(getElementText(ele)));
                }
                else fw.append(getElementText(ele));
                //fw.append(",");
                return true;
            }
            catch(IOException ex){
                System.out.println("cannot write into the file " + fileName);
                return true;
            }
        }
        org.w3c.dom.NodeList nlist = root.getChildNodes();
        for(int i=0; i<nlist.getLength(); i++){
            if(nlist.item(i).getNodeType() == Node.ELEMENT_NODE){
                if(dfs((Element)nlist.item(i), fw, fileName, tagName))  return true;
            }
        }
        return false;   
    }
    
    static boolean dfs_string(Element root, FileWriter fw, String fileName, String tagName, StringBuilder toReturn){
        Attr att = getAttributeByTagNameNR(root, tagName);
        if(att != null){
            toReturn.append(getAttributeText(att));
            return true;
        }
        
        Element ele = getElementByTagNameNR(root, tagName);
        if(ele != null){
            toReturn.append(getElementText(ele));
            return true;
        }
        org.w3c.dom.NodeList nlist = root.getChildNodes();
        for(int i=0; i<nlist.getLength(); i++){
            if(nlist.item(i).getNodeType() == Node.ELEMENT_NODE){
                if(dfs_string((Element)nlist.item(i), fw, fileName, tagName, toReturn)) return true;
            }
        }
        return false;   
    }
    
    static boolean dfs_strings(Element root, FileWriter fw, String fileName, String tagName, List<String> toReturn){
        Attr[] att = getAttributesByTagNameNR(root, tagName);
        if(att != null && att.length > 0){
            for(int i=0; i<att.length; i++){
                toReturn.add(getAttributeText(att[i]));
            }
            return true;
        }
        
        Element[] ele = getElementsByTagNameNR(root, tagName);
        if(ele != null && ele.length > 0){
            for(int i=0; i<ele.length; i++){
                toReturn.add(getElementText(ele[i]));
            }
            return true;
        }
        org.w3c.dom.NodeList nlist = root.getChildNodes();
        for(int i=0; i<nlist.getLength(); i++){
            if(nlist.item(i).getNodeType() == Node.ELEMENT_NODE){
                if(dfs_strings((Element)nlist.item(i), fw, fileName, tagName, toReturn))    return true;
            }
        }
        return false;   
    }
    
    static boolean dfs_ele_attr(Element root, FileWriter fw, String fileName, String eleName, String attrName){
        Element ele = getElementByTagNameNR(root, eleName);
        if(ele != null){
            Attr att = getAttributeByTagNameNR(ele, attrName);
            if(att != null){
                try{
                    fw.append(getAttributeText(att));
                }
                catch(IOException ex){
                    System.out.println("cannot write into the file " + fileName);   
                }
                return true;
            }
            else return false;
        }
        else{
            org.w3c.dom.NodeList nlist = root.getChildNodes();
            for(int i=0; i<nlist.getLength(); i++){
                if(nlist.item(i).getNodeType() == Node.ELEMENT_NODE){
                    if(dfs_ele_attr((Element)nlist.item(i), fw, fileName, eleName, attrName))   return true;
                }
            }
            return false;
        }
    }
    
    static boolean dfs_ele_attr_string(Element root, FileWriter fw, String fileName, String eleName, String attrName, StringBuilder toReturn){
        Element ele = getElementByTagNameNR(root, eleName);
        if(ele != null){
            Attr att = getAttributeByTagNameNR(ele, attrName);
            if(att != null){
                toReturn.append(getAttributeText(att));
                return true;
            }
            else return false;
        }
        else{
            org.w3c.dom.NodeList nlist = root.getChildNodes();
            for(int i=0; i<nlist.getLength(); i++){
                if(nlist.item(i).getNodeType() == Node.ELEMENT_NODE){
                    if(dfs_ele_attr_string((Element)nlist.item(i), fw, fileName, eleName, attrName, toReturn))  return true;
                }
            }
            return false;
        }
    }
    
    
    public static void recursiveDescent(Node n, int level) {
        // adjust indentation according to level
        for(int i=0; i<4*level; i++)
            System.out.print(" ");
        
        // dump out node name, type, and value  
        String ntype = typeName[n.getNodeType()];
        String nname = n.getNodeName();
        String nvalue = n.getNodeValue();
        
        System.out.println("Type = " + ntype + ", Name = " + nname + ", Value = " + nvalue);
        
        // dump out attributes if any
        org.w3c.dom.NamedNodeMap nattrib = n.getAttributes();
        if(nattrib != null && nattrib.getLength() > 0)
            for(int i=0; i<nattrib.getLength(); i++)
                recursiveDescent(nattrib.item(i),  level+1);
        
        // now walk through its children list
        org.w3c.dom.NodeList nlist = n.getChildNodes();
        
        for(int i=0; i<nlist.getLength(); i++)
            recursiveDescent(nlist.item(i), level+1);
    }  
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
