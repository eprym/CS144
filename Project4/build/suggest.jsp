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
  <title>There</title>
  <script type="text/javascript" src="autosuggest.js"></script>
  <script type="text/javascript" src="suggestions.js"></script>
  <link rel="stylesheet" type="text/css" src="autosuggest.css" />
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


<form action="suggest">
  Keyword Search : <input type="text" id="input_txt" name="q">
  <input type="submit" value="Search">
</form>
<p>target URL:<%=request.getAttribute("targetUrl")%></p>
<%
	//int len=request.getAttribute("itemResults").length;
	out.println(request.getAttribute("suggests"));
%>
</body>
</html>
