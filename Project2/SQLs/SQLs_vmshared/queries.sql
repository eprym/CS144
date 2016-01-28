-- 1.	Find the number of users in the database.
select count(*)
from ((select UserID from Sellers) union (select UserID from Bidders)) user;


-- 2.	Find the number of items in "New York", (i.e., items whose location is exactly the string "New York"). Pay special attention to case sensitivity. You should match the items in "New York" but not in "new york".
select count(*)
from Items
where binary ItemLocationName='New York';

-- 3.	Find the number of auctions belonging to exactly four categories.
Select count(distinct ItemID)
from(
	Select ItemID,count(distinct Category)
	from Categories
	group by ItemID
	having count(distinct Category)=4
	)	c4;

-- ?
-- 4.	Find the ID(s) of current (unsold) auction(s) with the highest bid. 
-- Remember that the data was captured at the point in time December 20th, 2001, one second after midnight, 
-- so you can use this time point to decide which auction(s) are current. 
-- Pay special attention to the current auctions without any bid.
Select ItemID max(CurrentPrice)
from Items
where NOofBids>0 and End>'2001-12-20 00:00:01' and (CurrentPrice<BuyPrice or BuyPrice=Null)
Having max(CurrentPrice); 

-- 5.	Find the number of sellers whose rating is higher than 1000.
select count(distinct UserID)
from Sellers
where Rating>1000;

--  
-- 6.	Find the number of users who are both sellers and bidders.
select count(distinct UserID)
from Sellers
where UserID in (select UserID from Bidders);

-- 7.	Find the number of categories that include at least one item with a bid of more than $100
select count(distinct Category)
from Categories C, Items I
where C.ItemID=I.ItemID and I.NOofBids>0 and I.CurrentPrice>100; 

select count(distinct Category)
from Categories
where ItemID in
	(
		select ItemID
	 	from Items
	 	where NOofBids>0 and CurrentPrice>100
	);
