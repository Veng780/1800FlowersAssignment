create table flower_comment
(
   user_id integer not null,
   id integer not null,
   title varchar(150) not null,
   body varchar(255) not null,
   primary key(id)
);



create table user
(
   id integer not null,   
   primary key(id)
);
