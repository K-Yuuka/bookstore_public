# bookstore

## Java Version
Java 21  
https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html  

## Execute
DemoApplication.ktを実行すると以下のコンテナが起動します。  
・DB  
  bookstore > postgres:latest  
・ swagger-ui  
  bookstore > swaggerapi/swagger-ui  

## API
http://localhost:8002/

・[POST]/bookstore 書籍と著者を登録
・[GET]/bookstore 著者&書籍リストの取得
・[PUT]/bookstore/{bookId} 書籍の著者を変更
・[GET]/book 書籍の検索
・[PUT]/book/{bookId} 書籍名の変更
・[DELETE]/book/{bookId} 書籍を削除
・[GET]/author 著者の検索
・[PUT]/author/{authorId} 著者名の変更
・[DELETE]/author/{authorId} 著者の削除

