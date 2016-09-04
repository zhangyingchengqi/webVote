create database vote;


create table voteuser(
	uid int primary key auto_increment,
	uname varchar(50),
	pwd varchar(50)
);


select *from voteuser;


create table votesubject(
	vsid int primary key auto_increment,
 	title varchar(2000),
 	stype int 
)

create table voteoption(
	voteid int primary key,
	voteoption varchar(2000) not null,
	vsid int not null, 
	voteorder int not null
)

alter table voteoption modify voteid int auto_increment ;

alter table voteoption
	add constraint fk_voteoption_vsid
		foreign key(vsid) references votesubject(vsid);

create table voteitem(
	viid int primary key auto_increment,
	voteid int not null,
	vsid int not null,
	uid int not null
)

select *from voteoption;

alter table voteitem
	add constraint fk_voteitem_voteid
		foreign key(voteid) references voteoption(voteid);
		
		
alter table voteitem
	add constraint fk_voteitem_vsid
		foreign key(vsid) references votesubject(vsid);
		
alter table voteitem
	add constraint fk_voteitem_uid
		foreign key(uid) references voteuser(uid);

		
--添加用户

		insert into voteuser(uname,pwd)values('a','a');
		insert into voteuser(uname,pwd)values('b','a');
		insert into voteuser(uname,pwd)values('c','a');
		
--添加投票主题		
		insert into votesubject(title,stype)values('选出你心目中最好的下载工具',2);
		insert into votesubject(title,stype)values('选出你心目中最好的输入法',1);
		insert into votesubject(title,stype)values('选出你心目中最好的网络聊天工具',1);
		insert into votesubject(title,stype)values('选出你心目中最想去的地方',1);
		insert into votesubject(title,stype)values('选出你心目中最好的浏览器',1);
		insert into votesubject(title,stype)values('选出你心目中最好的杀毒软件',1);
		insert into votesubject(title,stype)values('中国的首都是？',1);
		insert into votesubject(title,stype)values('where are you from？',1);
		
	select *from votesubject;
	delete from votesubject where vsid=4;
--添加主题中的选项
		insert into voteoption(voteoption,vsid,voteorder)values('腾讯QQ',3,1);
		insert into voteoption(voteoption,vsid,voteorder)values('MSN',3,2);
		insert into voteoption(voteoption,vsid,voteorder)values('迅雷',1,1);
		insert into voteoption(voteoption,vsid,voteorder)values('新浪UC',3,3);
		insert into voteoption(voteoption,vsid,voteorder)values('飞信',3,4);
		insert into voteoption(voteoption,vsid,voteorder)values('skype',3,5);
		insert into voteoption(voteoption,vsid,voteorder)values('阿里旺旺',3,6);
		insert into voteoption(voteoption,vsid,voteorder)values('百度HI',3,7);
		insert into voteoption(voteoption,vsid,voteorder)values('IE浏览器',5,1);
		insert into voteoption(voteoption,vsid,voteorder)values('谷歌浏览器',5,2);
		insert into voteoption(voteoption,vsid,voteorder)values('网际快车',1,2);
		insert into voteoption(voteoption,vsid,voteorder)values('电驴',1,3);
		insert into voteoption(voteoption,vsid,voteorder)values('搜狗拼音',2,1);
		insert into voteoption(voteoption,vsid,voteorder)values('qq拼音',2,2);
		insert into voteoption(voteoption,vsid,voteorder)values('百度拼音',2,3);
         
		
		insert into voteitem(voteid,vsid,uid)values(1,3,1);
		insert into voteitem(voteid,vsid,uid)values(2,3,1);
		insert into voteitem(voteid,vsid,uid)values(4,3,1);
		insert into voteitem(voteid,vsid,uid)values(4,3,2);
		insert into voteitem(voteid,vsid,uid)values(3,1,1);
		insert into voteitem(voteid,vsid,uid)values(3,2,2);
		insert into voteitem(voteid,vsid,uid)values(2,4,2);
		
