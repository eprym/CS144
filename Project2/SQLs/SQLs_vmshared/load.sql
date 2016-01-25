delete from Locations;
delete from Users;
delete from Items;
delete from Bids;
delete from Categories;

LOAD DATA LOCAL INFILE "~/shared/sqls/Locations.csv" into table Locations
fields terminated by ',' optionally enclosed by '"';