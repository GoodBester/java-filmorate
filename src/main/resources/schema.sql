drop table if exists genre, rating, film, user, film_genre, film_likes, friends;

create table if not exists genre (
	id integer generated by default as identity not null primary key,
	name varchar(255)
);
create table if not exists rating (
	id integer primary key,
	name varchar(255)
);

create table if not exists film (
	id integer generated by default as identity not null primary key,
	name varchar(100),
	description varchar(1000),
	release_date date,
	duration integer,
	rating_id integer references film(rating_id)
);

create table if not exists user (
	id integer generated by default as identity not null primary key,
	name varchar(100),
	email varchar(100),
	login varchar(100),
	birthday date
);

create table if not exists film_genre (
	film_id integer not null references film(id) on delete cascade,
	genre_id integer not null references genre(id) on delete cascade,
	primary key (film_id, genre_id)
);
create table if not exists film_likes (
	film_id integer not null references film(id) on delete cascade,
	user_id integer not null references user(id) on delete cascade,
	primary key (film_id, user_id)
);
create table if not exists friends (
	user integer not null references user(id) on delete cascade,
	friend integer not null references user(id) on delete cascade,
	status varchar(100),
	primary key (user, friend)
);