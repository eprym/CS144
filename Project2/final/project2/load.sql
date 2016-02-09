LOAD DATA LOCAL INFILE "Items.csv" into table Items
fields terminated by '|*|';
LOAD DATA LOCAL INFILE "Bids.csv" into table Bids
fields terminated by '|*|';
LOAD DATA LOCAL INFILE "Bidders.csv" into table Bidders
fields terminated by '|*|';
LOAD DATA LOCAL INFILE "Sellers.csv" into table Sellers
fields terminated by '|*|';
LOAD DATA LOCAL INFILE "Category.csv" into table Categories
fields terminated by '|*|';
