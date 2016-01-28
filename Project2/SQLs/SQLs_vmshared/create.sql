Drop table if exists Items;
Drop table if exists Bids;
Drop table if exists Bidders;
Drop table if exists Sellers;
Drop table if exists Categories;

-- the format of time and prices
-- the length of those varchar

-- create table Locations(LocationName VARCHAR(100) not null,
-- 					   Country VARCHAR(100) not null,
-- 					   Latitude decimal(9,6),
-- 					   Longitude decimal(9,6),
-- 					   primary key (LocationName, Country)
-- 					  );

create table Bidders(UserID VARCHAR(40) not null,
					 Rating Integer not Null,
					 UserLocationName VARCHAR(100),
					 Country VARCHAR(100),
					 /*foreign key (UserLocationName, Country) references
				   					Locations(LocationName, Country),*/
					 primary key (UserID)
					);

create table Sellers(UserID VARCHAR(40) not null,
					Rating Integer not null,
					primary key(UserID)
					);

create table Items(ItemID VARCHAR(40) not null,
				   ItemName VARCHAR(4000) not null,
				   CurrentPrice decimal(18,2) not null,
				   BuyPrice decimal(18,2),
				   FirstBid decimal(18,2) not null,
				   NOofBids Integer not null,
				   ItemLocationName VARCHAR(100) not null,
				   Latitude decimal(9,6),
				   Longitude decimal(9,6),
				   Country VARCHAR(100) not null,
				   Started timestamp not null,
				   End timestamp not null,
				   SellerID VARCHAR(40) not null references Sellers(UserID),
				   Description VARCHAR(4000) not null,
				   primary key (ItemID)
				   -- foreign key (ItemLocationName,Country) references
				   -- 				Locations(LocationName,Country)
				   );

create table Bids(ItemID VARCHAR(40) not null references Items(ItemID),
	 			  BidderID VARCHAR(40) not null references Bidders(UserID),
	 			  BidTime timestamp not null,
	 			  Amount Integer not null,
	 			  primary key (ItemID,BidderID,BidTime)
	 			  );

create table Categories(ItemID VARCHAR(40) not null references Items(ItemID),
					    Category VARCHAR(100) not null,
					    primary key (ItemID,Category)
					    );
