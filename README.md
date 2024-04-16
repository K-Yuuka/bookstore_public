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

API|説明|
[POST]/bookstore 書籍と著者を登録|書籍名と著者名を指定して登録します。|
[GET]/bookstore 著者&書籍リストの検索|指定された著者名で書籍を検索します。著者名が指定されていない場合は登録されているすべての情報を取得します。|
[PUT]/bookstore/{bookId} 書籍の著者を変更|指定された書籍IDの著者名を変更します。|
[GET]/book 書籍の検索|書籍名を指定して書籍を検索します。書籍名が指定されていない場合は登録されているすべての情報を取得します。| 
[PUT]/book/{bookId} 書籍名の変更|書籍IDを指定して書籍名を変更します。|
[DELETE]/book/{bookId} 書籍を削除|書籍IDを指定して書籍を削除します。|
[GET]/author 著者の検索|著者名を指定して著者を検索します。著者名が指定されていない場合は登録されているすべての情報を取得します。|
[PUT]/author/{authorId} 著者名の変更|著者IDを指定して著者名を変更します。|
[DELETE]/author/{authorId} 著者の削除|著者IDを指定して著者を削除します。紐づいている書籍が存在する場合は削除に失敗します。|

