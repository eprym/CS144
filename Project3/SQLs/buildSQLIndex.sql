drop table if exists ItemSpatial;

create table ItemSpatial (
		ItemID VARCHAR(40) not null,
		ItemName VARCHAR(4000) not null,
		Latitude decimal(9,6) not null,
		Longitude decimal(9,6) not null,
		Coordinate GEOMETRY not null,
		primary key (ItemID),
		foreign key (ItemID) references Items(ItemID)
		)engine=myisam;

insert into ItemSpatial(ItemID,ItemName,Latitude,Longitude)
	select ItemID,ItemName,Latitude,Longitude from Items
	where Latitude is not null and Longitude is not null;

update ItemSpatial set Coordinate=Point(Latitude,Longitude);

create spatial index sp_index on ItemSpatial(Coordinate);



