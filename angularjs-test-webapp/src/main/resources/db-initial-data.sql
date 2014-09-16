-- Table: languages
insert into languages values (1,'es','ES',null);
insert into languages values (2,'en','UK',null);
insert into languages values (3,'en','US',null);

-- Table: users
insert into users values (1,'test','$2a$10$qNjVTCkcE9ggkBytuVHWguEC3v2vpeGbsbFu9nhHXP0OZxLh/Z36m', true, 1);
insert into users values (2,'blabla','$2a$10$VsMPfxFKgKZNgoKuLP1lhOSdw8oRIm7caIfkY3bhZnsVmDx8Kg3QW', false, 2);

-- Table: authorities
insert into authorities values (1,'test','ROLE_ADMIN');
insert into authorities values (2,'blabla','ROLE_USER');

-- Table: guestbook
insert into guestbook values (1,'user 1','Hello');
insert into guestbook values (2,'user 2','World!');