-- Sequences
create sequence jpa_seq start with 100;

-- Table: languages
create table languages
(
  id integer not null primary key,
  language character varying(3) not null,
  region character varying(3),
  variant character varying(8),
  constraint u_locale unique (language,region,variant)
);

-- Table: users
create table users
(
  id integer not null primary key,
  username character varying(50) not null,
  password character varying(60) not null,
  enabled boolean not null,
  language_id integer not null,
  constraint fk_users_languages foreign key(language_id) references languages(id),
  constraint u_username unique (username)
);

-- Table: authorities
create table authorities
(
  id integer not null primary key,
  username character varying(50) not null,
  authority character varying(50) not null,
  constraint fk_authorities_users foreign key(username) references users(username),
  constraint u_username_authority unique (username,authority)
);

-- Table: guestbook
create table guestbook
(
  id integer not null primary key,
  name character varying(50) not null,
  text character varying(150) not null,
);