create database exam;

create table people(
	name	varchar(10)	primary key,
	age		int(3)				not null,
	addr		varchar(100)	not null
);


insert into people values('bomz', 21, 'seoul gangnam');
insert into people values('hong', 45, 'jeju');