use TEST;
drop table if exists Actors;
create table Actors(
	Name varchar(40),
	Movie varchar(80),
	Year int,
	Role varchar(40)
	);

load data local infile '~/data/actors.csv' into table Actors
fields terminated by ',' optionally enclosed by '"';

select Name from Actors where Movie = 'Die Another Day';
drop table Actors;