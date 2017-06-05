create table t_vehicle(
id int not null,
licenseplate varchar2(6) not null,
owner varchar2(100) not null,
email varchar2(300) not null,
registrydate date not null,
jobtype varchar2(50) not null,
active number(1) default 0 not null,
requiredetails number(1) default 0 not null,
status number(1) default 0 not null,
constraint t_vehicle_pk primary key(id)
);

create sequence s_vehicle;