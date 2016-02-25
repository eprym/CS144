<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
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
  <title>There is <%=request.getAttribute("itemResults").length%> items</title>
</head>
<body>
<form>
  What else are you looking for?<input type="text",name="keyword">
  <input type="submit" value="Search">
</form>
<%
	int len=request.getAttribute("itemResults").length;
	SearchResult[] itemResults=request.getAttribute("itemResults");
%>
<%
  for(int i=0;i<len;++i){
%>
  <p><%=itemResults[i].getItemId()+","+itemResults[i].getName()%></p>
  <%
    out.println(""+itemResults[i].getItemId()+","+itemResults[i].getName());
  %>
<%
  }
%>
</body>
</html>
