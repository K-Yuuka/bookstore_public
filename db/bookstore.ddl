-- Project Name : bookstore
-- Date/Time    : 2024/04/14 8:34:40
-- Author       : kumam
-- RDBMS Type   : PostgreSQL
-- Application  : A5:SQL Mk-2

/*
  << 注意！！ >>
  BackupToTempTable, RestoreFromTempTable疑似命令が付加されています。
  これにより、drop table, create table 後もデータが残ります。
  この機能は一時的に $$TableName のような一時テーブルを作成します。
  この機能は A5:SQL Mk-2でのみ有効であることに注意してください。
*/

-- 書籍と著者
-- * RestoreFromTempTable
create table book_author (
  book_id integer not null
  , author_id integer not null
  , constraint book_author_PKC primary key (book_id,author_id)
) ;

create index book_author_IX1
  on book_author(author_id);

create index book_author_IX2
  on book_author(book_id);

-- 著者
-- * RestoreFromTempTable
create table author (
  author_id serial not null
  , author_name varchar(255) not null
  , constraint author_PKC primary key (author_id)
) ;

alter table author add constraint author_IX1
  unique (author_name) ;

create index author_IX3
  on author(author_id);

-- 書籍
-- * RestoreFromTempTable
create table book (
  book_id serial not null
  , book_name varchar(255) not null
  , constraint book_PKC primary key (book_id)
) ;

create index book_IX1
  on book(book_name);

create index book_IX2
  on book(book_id);

alter table book_author
  add constraint book_author_FK1 foreign key (author_id) references author(author_id);

alter table book_author
  add constraint book_author_FK2 foreign key (book_id) references book(book_id)
  on delete cascade;

comment on table book_author is '書籍と著者';
comment on column book_author.book_id is '書籍ID';
comment on column book_author.author_id is '著者ID';

comment on table author is '著者';
comment on column author.author_id is 'ID';
comment on column author.author_name is '著者名';

comment on table book is '書籍';
comment on column book.book_id is 'ID';
comment on column book.book_name is '書籍名';

