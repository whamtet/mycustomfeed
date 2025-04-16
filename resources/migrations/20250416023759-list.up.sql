--;;
create table user
(
    user_id    integer primary key asc,
    email      text unique,
    first_name text,
    last_name  text,
    q          text
);
--;;
insert into user (email, first_name, last_name, q)
values ('whamtet@gmail.com', 'Matthew', 'Molloy', 'matthew molloy');
--;;
create table list
(
    user_id integer primary key asc,
    title text,
    detail text,
    foreign key(user_id) references user(user_id)
);
