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
			<a href= "keywordSearch.html">Search with Keywords</a><br>
			<br>
			<form action = "/eBay/item">
				Item ID : <input type="text" name="id">
				<input type="submit" value="Search">
			</form>


			<%
			String XMLdata = (String)request.getAttribute("XMLdata");
			if(XMLdata.equals("")){out.println("Invalid ItemID");return;}
			ItemServlet.xmlObj xobj = (ItemServlet.xmlObj)request.getAttribute("xobj");
			%>
			<p>ItemID : <%= xobj.ItemID%> </p>
			<p>ItemName : <%= xobj.Name%> </p>
			<p>Current Price : <%= xobj.Currently%></p>
			<% if(xobj.BuyPrice!=null){%>
				<p>Buy Price: <%= xobj.BuyPrice%></p>
				<form method="get" action ="/eBay/check?itemID=<%=xobj.ItemID%>">
					<input type='hidden' name="itemID" value=<%=xobj.ItemID%>>
					<button type="submit">Pay Now</button>
				</form>
			<%}else{%>
				<p>Buy Price: </p>
			<%}%>
			<p>First Bid Price : <%= xobj.First_Bid%></p>
			<p>Number of Bids : <%= xobj.Number_of_Bids%></p>
			<p>Location : <%= xobj.Location%></p>
			<p>Latitude : <%= xobj.Latitude%></p>
			<p>Longitude : <%= xobj.Longitude%></p>
			<p>Start Time : <%= xobj.Started%></p>
			<p>End Time : <%= xobj.Ends%></p>
			<p>Seller Rating : <%= xobj.SellerRating%></p>
			<p>Seller ID : <%= xobj.SellerID%></p>
			<p>Categories : 
				<%
				for(int i=0; i<xobj.Categories.length; i++){
				%><%= xobj.Categories[i] + ", "%><%
				}
				%>
			</p>
			<p>Description : <%= xobj.Description%></p>
			<table border="1">
				<tr>
					<th>BidderID</th>
					<th>BidderRating</th>
					<th>Location</th>
					<th>Country</th>
					<th>Time</th>
					<th>Amount</th>
				</tr>
				<%
				if(xobj.bids != null){
				for(int j=0; j<xobj.bids.length; j++){
					ItemServlet.xmlObj.Bid bid = xobj.bids[j];%>
					<tr>
						<td><%= bid.BidderID%></td>
						<td><%= bid.BidderRating%></td>
						<td><%= bid.BidLocation%></td>
						<td><%= bid.BidCountry%></td>
						<td><%= bid.Time%></td>
						<td><%= bid.Amount%></td>
					</tr>
				<%}
				}
				%>
			</table>
		</div>
		<div id="right">
			<div id="map_canvas" style="width:100%; height:80%"></div>
			<script type="text/javascript"> 
			  // function initialize() { 
			  //   var latlng = new google.maps.LatLng(34.063509,-118.44541); 
			  //   var myOptions = { 
			  //     zoom: 14, // default is 8  
			  //     center: latlng, 
			  //     mapTypeId: google.maps.MapTypeId.ROADMAP 
			  //   }; 
			  //   var map = new google.maps.Map(document.getElementById("map_canvas"), 
			  //       myOptions); 
			  // } 
			function initialize() {
			  	//var latlng = new google.maps.LatLng(40.730885,-73.997383);
			  	var myOptions;
			  	var map;
			  	var marker;
			  	geocoder = new google.maps.Geocoder();
			  	codeLatLng(function(latlng0){
			  		if(latlng0){
			  			myOptions = { 
			      			zoom: 10, // default is 8  
			      			center: latlng0, 
			     			mapTypeId: google.maps.MapTypeId.ROADMAP 
			  			}; 
			  		}
			  		else{
			  			latlng0=new google.maps.LatLng(0.01,0.01);
			  			myOptions = { 
			      			zoom: 2, // default is 8 
			      			center: latlng0, 
			     			mapTypeId: google.maps.MapTypeId.ROADMAP 
			  			}; 
			  		}
			  		map = new google.maps.Map(document.getElementById("map_canvas"),myOptions)
			  		//alert(latlng0);
			  		//map.setCenter(latlng0);
			  		marker = new google.maps.Marker({
			  			map: map,
			  			position: latlng0,
			  			//label:"<%= xobj.Location%>",
			  			title:"<%= xobj.Location%>",
			  			animation: google.maps.Animation.DROP
			  		});
			 	});
			}

			function codeLatLng(callback) {
				// var latlng = new google.maps.LatLng(40.730885,-73.997383);
				var address="<%= xobj.Location%>";
				var lat="<%= xobj.Latitude%>";
				var lng="<%= xobj.Longitude%>";
				
				if (geocoder) {
					if(lat && lng ){
						var latlng = new google.maps.LatLng(lat,lng);
						callback(latlng);
					}
					else if(address){
						geocoder.geocode({'address': address}, function(results, status) {
							if (status == google.maps.GeocoderStatus.OK) {
								if (results[0]) {
			            			//map.setCenter(results[0].geometry.location);
			            			callback(results[0].geometry.location);
				            	} else {
				            		callback("");
			    	        		//alert("No results found");
			        			}
			    			} else {
			    					callback("");
			     	     		//alert("Geocoder failed due to: " + status);
			      			}
			 			});
					}
					else{
						callback("");
					}
				}
			}
			</script>
		</div>
	</div>
	<div id="action">

	</div>
	<div id="footer">© Copyright by University of California, Los Angeles (UCLA)</div>
</body>
</html>