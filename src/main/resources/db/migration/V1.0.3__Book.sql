-- 書籍と著者
-- * RestoreFromTempTable
create table book_author (
  book_id integer not null
  , author_id integer not null
  , constraint book_author_PKC primary key (book_id)
) ;

alter table book_author add constraint book_author_IX1
  unique (book_id,author_id) ;

create index book_author_IX2
  on book_author(author_id);

create index book_author_IX3
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
