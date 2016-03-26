<%@ page import="edu.ucla.cs.cs144.ItemServlet" %>
<html>
<head>	
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<meta charset="UTF-8">
<script type="text/javascript" 
	src="http://maps.google.com/maps/api/js?sensor=false"> 
</script> 
<link rel="stylesheet" type="text/css" href="map_style.css">
</head>
<body onload="initialize()">
	
	<div id="header">eBay Search Web Site </div>
	<div id="content">
		<div id="left">
			<a href= "index.html">Back to Welcome Page</a><br>
			

			<h1>Credit Card Input Page</h1>
			<%
			// String XMLdata = (String)request.getAttribute("XMLdata");
			// if(XMLdata.equals("")){out.println("Invalid ItemID");return;}
			ItemServlet.xmlObj xobj = (ItemServlet.xmlObj)request.getAttribute("xobj");
			if(xobj==null){%>
				<p>Please search this item again before you pay.</p>
				<form action = "/eBay/item">
					Item ID : <input type="text" name="id">
				<input type="submit" value="Search">
				</form>
				<a href= "keywordSearch.html">Search with Keywords</a>
			<%}else{%>
				<p>ItemID : <%= xobj.ItemID%> </p>
				<p>ItemName : <%= xobj.Name%> </p>
				<p>Buy Price: <%= xobj.BuyPrice%></p>
				<% String url="https://"+request.getServerName()+":8443"+request.getContextPath()+"/confirm";%>
				<form method="post" action = <%=url%>>
					Credit Card: <input type="text" name="cardNum">
					<input type="hidden" name="itemID" value=<%=xobj.ItemID%>>
					<input type="submit" value="submit">
				</form>
				<!-- <p>Server Name:<%=request.getServerName()%></p>
				<p>Server Port:<%=request.getServerPort()%></p>
				<p>Context Path:<%=request.getContextPath()%></p>
				<p>Is secure:<%=request.isSecure()%></p> -->
			<%}%>
		</div>
	</div>
	<div id="footer">© Copyright by University of California, Los Angeles (UCLA)</div>
</body>
</html>