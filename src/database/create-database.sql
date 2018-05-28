drop table users;
/
drop table books;
/
drop table authors;
/
drop table authors_genres;
/
drop table recommendations;
/
create sequence USERS_SEQ
/
create sequence BOOKS_SEQ
/
create sequence AUTHORS_SEQ
/
create sequence AUTHORS_GENRES_SEQ
/
CREATE sequence RECOMMENDATIONS_SEQ
/
create table USERS
(
	ID NUMBER not null
		constraint USERS_PK
			primary key,
	USERNAME VARCHAR2(50),
	PASSWORD VARCHAR2(50)
);
/
create trigger USERS_TRG
	before insert
	on USERS
	for each row
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID IS NULL THEN
      SELECT USERS_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
create table AUTHORS(
	ID NUMBER not null
		constraint AUTHORS_PK
			primary key,
	NAME VARCHAR2(200)
);
/
create trigger AUTHORS_TRG
	before insert
	on AUTHORS
	for each row
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID IS NULL THEN
      SELECT AUTHORS_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
create table BOOKS(
	id NUMBER not null
		constraint BOOKS_PK
			primary key,
	GENRE VARCHAR2(100),
	AUTHORID NUMBER,
	TITLE VARCHAR2(100),
	YEAR INTEGER,
	ISBN INTEGER,
	RATING INTEGER,
	NROFRATINGS INTEGER
);
/
alter table BOOKS
	add constraint FK_BOOKS
		foreign key (AUTHORID) references AUTHORS
/
create trigger BOOKS_TRG
	before insert
	on BOOKS
	for each row
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID IS NULL THEN
      SELECT BOOKS_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
create table AUTHORS_GENRES(
	ID NUMBER not null
		constraint AUTHORSGENRES_PK
			primary key,
	AUTHORID NUMBER,
	GENRE VARCHAR2(100)
);
/
alter table AUTHORS_GENRES
	add constraint FK_AUTHORS_GENRES
		foreign key (AUTHORID) references AUTHORS
/
create trigger AUTHORS_GENRES_TRG
	before insert
	on AUTHORS_GENRES
	for each row
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID IS NULL THEN
      SELECT AUTHORS_GENRES_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
create table recommendations(
	ID NUMBER not null
		constraint recommendations_pk
			primary key,
	USERID NUMBER,
	FILTERNAME VARCHAR2(100),
	FILTERTYPE VARCHAR2(100),
	CURRENTDATE DATE
);
/
alter table recommendations
	add constraint FK_RECOMMENDATIONS
		foreign key (USERID) references USERS
/
create trigger RECOMMENDATIONS_TG
	before insert
	on recommendations
	for each row
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.ID IS NULL THEN
      SELECT RECOMMENDATIONS_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
