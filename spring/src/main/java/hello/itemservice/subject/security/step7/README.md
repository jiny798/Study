
### CORS (Cross Origin Resource Sharing 교차 출처 리소스 공유)

- 웹은 한 페이지에서 다른 웹 페이지의 데이터를 직접 불러오는 것을 제한하며, 이것은 "동일 출처 정책(Same-Origin Policy)"
라고 한다
- 다른 출처의 리소스를 사용하고자 할 때, CORS 를 사용해야 하며, HTTP 헤더를 통해 다른 출처의 리소스에 접근할 수 있도록 하는 정책이다.


- aaa.com 페이지에 접속 했을 때, JS 코드인 XMLHttpRequest로 bbb.com의 데이터를 가져오는 경우,
브라우저는 스크립트에서 실행하는 교차 출처 HTTP 요청을 제한하게 된다.
- 다른 출처의 리소스를 불러오기 위해서, 해당 출처에서 CORS 헤더를 포함한 응답을 반환해야 한다 

<br>

### CORS 종류
1. Simple Request 
- 해당 요청은 서버에 본요청을 바로 보내서, 응답의 헤더를 보고 브라우저에서
교차 출처 리소스를 사용해도 되는지 판단하는 방식 

[조건]
- GET,POST,HEAD 중 한가지 메서드 사용
- 헤더는 Accept, Accept-Language, Content-Language, Content-Type, DPR, Downlink, Save-Data, Viewport-Width Width 만 가능
- 그 외 커스텀 헤더를 사용하면 Preflight request 로 넘어간다
- Context-type 도 application/x-www-form-urlencoded, multipart/form-data, text/plain 만 가능 


<br>

<br>

**2. Preflight request (예비요청)**
- Simple Request 조건에 맞지 않으면 모두 Preflight request 사용

[흐름도]
1. JS ----> 브라우저 : JS에서 fetch 함수로 요청
2. 브라우저 -----> Server : 브라우저에서 OPTION 메서드로 요청
3. 브라우저 <---- Server : 서버에서 200 응답, Access-Control-Allow-Origin:*
4. CORS 관련 헤더 값을 보고, 출처가 다르지만 리소스 공유가 가능하다고 판단하여 실제 요청 전송
5. 브라우저 -----> Server : 실제 요청 전송
