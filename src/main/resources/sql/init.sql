create table account(
  id int auto_increment primary key,
  money number(20,2) not NULL
);

insert into account (id,money) values (1,100.0);
insert into account (id,money) values (2,4000.0);
insert into account (id,money) values (3,10.0);
insert into account (id,money) values (4,30.0);
insert into account (id,money) values (5,0.0);
commit;