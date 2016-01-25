Drop table if exists Items;
Drop table if exists Bids;
Drop table if exists Users;
Drop table if exists Locations;
Drop table if exists Categories;

-- the format of time and prices
-- the length of those varchar

create table Locations(LocationName VARCHAR(100) not null,
					   Country VARCHAR(100) not null,
					   Latitude decimal(9,6),
					   Longitude decimal(9,6),
					   primary key (LocationName, Country)
					  );

create table Users(UserID VARCHAR(40) not null,
				   UserLocationName VARCHAR(100),
				   Country VARCHAR(100),
				   BRating Integer not null,
				   SRating Integer not null,
				   isBidder Boolean not null default false,
				   isSeller Boolean not null default false,
				   foreign key (UserLocationName, Country) references
				   				Locations(LocationName, Country),
				   primary key (UserID)
				   );


create table Items(ItemID VARCHAR(40) not null,
				   ItemName VARCHAR(4000) not null,
				   CurrentPrice decimal(18,2) not null,
				   BuyPrice decimal(18,2),
				   FirstBid decimal(18,2) not null,
				   NOofBids Integer not null,
				   ItemLocationName VARCHAR(100) not null,
				   Country VARCHAR(100) not null,
				   Started timestamp not null,
				   End timestamp not null,
				   SellerID VARCHAR(40) not null references Users(UserID),
				   Description VARCHAR(4000) not null,
				   primary key (ItemID),
				   foreign key (ItemLocationName,Country) references
				   				Locations(LocationName,Country)
				   );

create table Bids(ItemID VARCHAR(40) not null references Items(ItemID),
	 			  BidderID VARCHAR(40) not null references Users(UserID),
	 			  BidTime timestamp not null,
	 			  Amount Integer not null,
	 			  primary key (ItemID,BidderID,BidTime)
	 			  );

create table Categories(ItemID VARCHAR(40) not null references Items(ItemID),
					    Category VARCHAR(100) not null,
					    primary key (ItemID,Category)
					    );