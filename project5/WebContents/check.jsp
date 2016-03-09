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
				<form method="post" action = "https://localhost:8443/eBay/confirm">
					Credit Card: <input type="text" name="cardNum">
					<input type="hidden" name="itemID" value=<%=xobj.ItemID%>>
					<input type="submit" value="submit">
				</form>
			<%}%>
		</div>
	</div>
	<div id="footer">© Copyright by University of California, Los Angeles (UCLA)</div>
</body>
</html>