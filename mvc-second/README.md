## HTTP 통신 

- 주로 3가지 방법을 사용한다.

<br>

### 1. GET - 쿼리 파라미터

- "/url명?param1=jin&param2=young"
- URL 에 파라미터를 넣어 요청한다.
- 데이터를 요청하는 경우에 사용된다.

<br>

```java
@RequestMapping("/hello-basic")
public String helloBasic() {
	log.info("helloBasic");
	return "ok";
}
```
- @RequestMapping 은 모든 HTTP 메서드를 처리한다.
- 특정 메서드만 처리할 경우, @GetMapping, @PostMapping 등을 사용하면 된다.

```java
@GetMapping(value = "/mapping-header", headers = "mode=debug")
public String mappingHeader() {
	log.info("mappingHeader");
	return "ok";
}
```
- 특정 header에 맞는 요청만 처리하도록 설정하는 것도 가능하다.
- 또한 아래처럼 consumes, produces 를 사용하여 특정 타입만 처리하도록 할 수 있다.

```java
@PostMapping(value = "/mapping-produce", produces = "text/html")
-> HTTP 요청 메시지의 Accept 정보를 보고 매칭
```


### 1.1 HTTP 요청 헤더 조회
```JAVA
@RequestMapping("/headers")
	public String headers(HttpServletRequest request, HttpServletResponse response, HttpMethod httpMethod,
		Locale locale,
		@RequestHeader MultiValueMap<String, String> headerMap,
		@RequestHeader("host") String host,
		@CookieValue(value = "myCookie", required = false) String cookie
	) {
		log.info("request={}", request);
		log.info("response={}", response);
		log.info("httpMethod={}", httpMethod);
		log.info("locale={}", locale);
		log.info("headerMap={}", headerMap);
		log.info("header host={}", host);
		log.info("myCookie={}", cookie);
		return "ok";
	}
```
- 서블릿 객체 request,response 파라미터는 물론
- @RequestHeader, @CookieValue 를 통해 헤더 정보를 조회할 수 있다.

<br>

### 2. POST - HTML Form 
- html의 form 태그를 사용하여 데이터를 전달하는 방식
- HTTP 헤더 중 content-type 이  application/x-www-form-urlencoded 으로 지정된다.
- 
