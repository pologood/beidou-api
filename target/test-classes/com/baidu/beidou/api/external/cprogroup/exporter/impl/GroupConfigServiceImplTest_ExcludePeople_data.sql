delete from cprogroupvt;
delete from cprogroup;
delete from vtpeople;


INSERT INTO vtpeople values(118,'kitemao',115,1,30,0,8,'2011-11-28 20:54:31','2011-11-28 20:54:31','2011-11-28 20:54:31',8,8,0,0,0);
INSERT INTO vtpeople values(119,'11',115,1,30,0,8,'2011-11-28 20:54:31','2011-11-28 20:54:31','2011-11-28 20:54:31',8,8,0,0,0);
INSERT INTO vtpeople values(120,'11',115,1,30,0,8,'2011-11-28 20:54:31','2011-11-28 20:54:31','2011-11-28 20:54:31',8,8,0,0,0);
INSERT INTO cprogroupvt VALUES (1,1649492,32,8,16,118,1,'2011-11-29 16:33:19','2011-11-29 16:33:19',8,8);

INSERT INTO cprogroup VALUES (1649492,32,8,'我的推广组-1',3,0,'2014-05-15 20:36:05','2014-05-26 16:48:39',8,8,2,7,0);
INSERT INTO cproplan VALUES (32,8,'u88联盟2830224a',0,0,500,0,0,20141119,0,NULL,16775168,16775168,16775168,16775168,16775168,16775168,16775168,'2008-11-19 14:34:06','2008-11-19 21:10:16',8,8,0,100,0,0);


delete from cprogroupit;
delete from group_interest_price;
INSERT INTO cprogroup VALUES (1649493,32,8,'我的推广组-1',3,0,'2014-05-15 20:36:05','2014-05-26 16:48:39',8,8,64,7,0);
insert into cprogroupit values (263,32,1649492,8,2,0,'2012-07-08 14:25:13',8);
insert into cprogroupit values (264,32,1649492,8,8,0,'2012-07-08 14:25:13',8);
insert into cprogroupit values (265,32,1649492,8,10,0,'2012-07-08 14:25:13',8);

insert into group_interest_price values (170,1649492,32,8,2,0,120,'2012-10-23 21:39:25',8,'2012-10-23 21:39:25',8);
insert into group_interest_price values (171,1649492,32,8,8,0,120,'2012-10-23 21:39:25',8,'2012-10-23 21:39:25',8);
insert into group_interest_price values (172,1649492,32,8,10,0,120,'2012-10-23 21:39:25',8,'2012-10-23 21:39:25',8);

insert into group_pack values(339,1649492,32,8,20,3,0,'2012-10-23 16:17:38',8,'2012-10-23 16:17:38',8);
insert into group_pack values(340,1649492,32,8,80,3,0,'2012-10-23 16:17:38',8,'2012-10-23 16:17:38',8);
insert into group_pack values(341,1649492,32,8,100,3,0,'2012-10-23 16:17:38',8,'2012-10-23 16:17:38',8);

insert into group_pack values(342,1649493,32,8,20,3,0,'2012-10-23 16:17:38',8,'2012-10-23 16:17:38',8);
insert into group_pack values(343,1649493,32,8,80,3,0,'2012-10-23 16:17:38',8,'2012-10-23 16:17:38',8);
insert into group_pack values(344,1649493,32,8,100,3,0,'2012-10-23 16:17:38',8,'2012-10-23 16:17:38',8);

insert into word_pack values(20,20,'keyword-pack-1',8,7,7,'2012-10-22 21:33:14',8,'2012-11-12 16:18:18',8);
insert into word_pack values(80,80,'keyword-pack-2',8,7,7,'2012-10-22 21:33:14',8,'2012-11-12 16:18:18',8);
insert into word_pack values(100,100,'keyword-pack-3',8,7,7,'2012-10-22 21:33:14',8,'2012-11-12 16:18:18',8);
