<%@ page import="edu.ucla.cs.cs144.SearchResult,java.util.*" %>
<%--
  Created by IntelliJ IDEA.
  User: Kami
  Date: 2016/2/24
  Time: 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>There is <%=((Map<String,String>)request.getAttribute("itemResults")).keySet().size() %> items</title>
  <script type="text/javascript" src="autosuggest.js"></script>
  <script type="text/javascript" src="suggestions.js"></script>
  <link rel="stylesheet" type="text/css" href="autosuggest.css" />
  <script type="text/javascript">
        window.onload = function () {
            var oTextbox = new AutoSuggestControl(document.getElementById("input_txt"), new SuggestionProvider()); 
        }
  </script>
</head>
<body>
<h1>eBay Search Web Site </h1>

<a href= "getItem.html">Search with an ItemID</a><br>
<br>


<form action="search">
  Keyword Search : <input type="text" id="input_txt" name="q">
  <input type="submit" value="Search">
</form>

<h2>There is <%=((Map<String,String>)request.getAttribute("itemResults")).keySet().size() %> items:</h2>
<%
	//int len=request.getAttribute("itemResults").length;
	Map<String,String> itemResults=(Map<String,String>)request.getAttribute("itemResults");
%>
<%
  for(String id:itemResults.keySet()){
%>
  <a href="http://localhost:1448/eBay/item?id=<%=id%>"><%=id+","+itemResults.get(id)%></a><br>
  <%
    //out.println(id+","+itemResults.get(id));
  %>
<%
  }
%>
<h2>
  <a href="http://localhost:1448/eBay/search?q=<%=request.getParameter("q")%>&numResultToSkip=<%=request.getAttribute("numResultToSkip")%>&action=prev">PREV</a>
  <a href="http://localhost:1448/eBay/search?q=<%=request.getParameter("q")%>&numResultToSkip=<%=request.getAttribute("numResultToSkip")%>&action=next">NEXT</a>
</h2>
</body>
</html>
