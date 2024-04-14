INSERT INTO author(author_name) VALUES ('夏目漱石');
INSERT INTO author(author_name) VALUES ('東野圭吾');
INSERT INTO author(author_name) VALUES ('黒柳徹子');
INSERT INTO author(author_name) VALUES ('西加奈子');
INSERT INTO author(author_name) VALUES ('スティーヴン・キング');
INSERT INTO author(author_name) VALUES ('オーウェン・キング');

INSERT INTO book(book_name) VALUES ('こころ');
INSERT INTO book(book_name) VALUES ('坊っちゃん');
INSERT INTO book(book_name) VALUES ('吾輩は猫である');
INSERT INTO book(book_name) VALUES ('悪意');
INSERT INTO book(book_name) VALUES ('秘密');
INSERT INTO book(book_name) VALUES ('手紙');
INSERT INTO book(book_name) VALUES ('窓際のトットちゃん');
INSERT INTO book(book_name) VALUES ('サラバ!');
INSERT INTO book(book_name) VALUES ('漁港の肉子ちゃん');
INSERT INTO book(book_name) VALUES ('さくら');
INSERT INTO book(book_name) VALUES ('きいろいゾウ');
INSERT INTO book(book_name) VALUES ('眠れる(上)');
INSERT INTO book(book_name) VALUES ('眠れる(下)');

INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='夏目漱石'), (SELECT book_id FROM book WHERE book_name='こころ'));
INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='夏目漱石'), (SELECT book_id FROM book WHERE book_name='坊っちゃん'));
INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='夏目漱石'), (SELECT book_id FROM book WHERE book_name='吾輩は猫である'));

INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='東野圭吾'), (SELECT book_id FROM book WHERE book_name='悪意'));
INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='東野圭吾'), (SELECT book_id FROM book WHERE book_name='秘密'));
INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='東野圭吾'), (SELECT book_id FROM book WHERE book_name='手紙'));

INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='黒柳徹子'), (SELECT book_id FROM book WHERE book_name='窓際のトットちゃん'));

INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='西加奈子'), (SELECT book_id FROM book WHERE book_name='サラバ!'));
INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='西加奈子'), (SELECT book_id FROM book WHERE book_name='漁港の肉子ちゃん'));
INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='西加奈子'), (SELECT book_id FROM book WHERE book_name='さくら'));
INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='西加奈子'), (SELECT book_id FROM book WHERE book_name='きいろいゾウ'));

INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='スティーヴン・キング'), (SELECT book_id FROM book WHERE book_name='眠れる(上)'));
INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='スティーヴン・キング'), (SELECT book_id FROM book WHERE book_name='眠れる(下)'));
INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='オーウェン・キング'), (SELECT book_id FROM book WHERE book_name='眠れる(上)'));
INSERT INTO book_author(author_id,book_id) VALUES ((SELECT author_id FROM author WHERE author_name='オーウェン・キング'), (SELECT book_id FROM book WHERE book_name='眠れる(下)'));
