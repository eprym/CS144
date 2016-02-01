Rational Database Schema:
Bidders{UserID,Rating,UseLocationName,Country} with primary key(UserID);
Sellers{UserID,Rating} with primary key(UserID);
Items{ItemID,ItemName,CurrentPrice,BuyPrice,FirstBid,NOofBids,ItemLocationName,Latitude,Longitude,Country,Started,End,SellerID,Decription}
	with primary key(ItemID) and SellerID references Sellers(UserID);
Bids{ItemID,BidderID,BidTime,Amount} with primary key(ItemID,BidderID,BidTime) and ItemID references Items(ItemID) and BidderID references Bidders(UserID);
Categories{ItemID,Category} with primary key(ItemID,Category) and ItemID references Items(ItemID);

Functional dependencies:
Bidders{UserID->Rating,UseLocationName,Country};
Sellers{UserID->Rating};
Items{ItemID->ItemName,CurrentPrice,BuyPrice,FirstBid,NOofBids,ItemLocationName,Latitude,Longitude,Country,Started,End,SellerID,Decription};
Bids{(ItemID,BidderID,BidTime)->Amount};
Categories{};

For BCNF:
Since all the left side of¡¡FDs are exactly the keys, all of the relations are in BCNF.

For 4NF:
Since all relations are in BCNF and there is not any nontrivial multivalue dependencies, all the relations are in 4NF.