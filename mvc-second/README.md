# HTTP 통신 

- 서버로 요청 데이터 전달 시, 주로 3가지 방법을 사용한다.

### [목차]
1. GET - 쿼리 파라미터
2. POST - HTML Form
3. HTTP message body에 데이터를 직접 담아서 요청 <br>
3-1. HTTP 요청 메시지 - JSON


## 방법1. GET - 쿼리 파라미터

- "/url명?param1=jin&param2=young"
- URL 에 파라미터를 넣어 요청한다.
- 데이터를 요청하는 경우에 사용된다.

## 방법2. POST - HTML Form
- HTML의 form 태그를 사용하여 데이터를 전달하는 방식
- HTTP 헤더 중 content-type 이  application/x-www-form-urlencoded 으로 지정된다.

<br>


### [요청 예시] 방법1, 방법2

#### 1. GET - 쿼리 파라미터

```html
http://localhost:8080/request-param?username=jyp&age=30
```

#### 2. POST - HTML Form
```html
```html
 <form action="/request-param-v1" method="post">
    username: <input type="text" name="username" />
    age: <input type="text" name="age" />
    <button type="submit">전송</button>
 </form>
```

```java
@GetMapping(value = "/mapping-header", headers = "mode=debug")
public String mappingHeader() {
	log.info("mappingHeader");
	return "ok";
}
```

<br>

### [서버에서 처리 방법]

####  1. 서블릿 객체에서 제공하는 파라미터 조회 메서드 사용
```java
@RequestMapping("/request-param-v1")
public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String username = request.getParameter("username");

	int age = Integer.parseInt(request.getParameter("age"));
	log.info("username={}, age={}", username, age);

	response.getWriter().write("ok");
}
```
- URL 쿼리파라미터, FORM 전송 방식 모두 HttpServletRequest 의 getParameter 메서드로 조회할 수 있다.

<br>
<br>

####  2. @RequestParam 사용
```java
@ResponseBody
@RequestMapping("/request-param-v2")
public String requestParamV2(@RequestParam("username") String memberName, @RequestParam("age") int memberAge) {
	log.info("username={}, age={}", memberName, memberAge);
	return "ok";
}
```
- @RequestParam 의 nama="username" 속성을 사용하여 파라미터 이름을 매칭 시켜준다.

<br>
<br>

#### 3. @RequestParam 속성 생략
```java
@ResponseBody
@RequestMapping("/request-param-v3")
public String requestParamV3(@RequestParam String username, @RequestParam int age) {
	log.info("username={}, age={}", username, age);
	return "ok";
}
	
@ResponseBody
@RequestMapping("/request-param-v4")
public String requestParamV4(String username, int age) {
	log.info("username={}, age={}", username, age);
	return "ok";
}
```
- HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
- String , int , Integer 등의 단순 타입이면 @RequestParam 도 생략 가능
- 스프링 부트 3.2부터 자바 컴파일러에 -parameters 옵션을 넣어주어야 애노테이션에 적는 이름을 생략할 수 있다.

<br>

#### 4. 그외 @RequestParam 속성 

```java
@RequestParam(required = true) -> 값이 없으면 400에러 반환 
@RequestParam(defaultValue = "guest") -> 기본값 설정 
```
```java
@RequestParam Map<String, Object> paramMap
```
- 파라미터를 Map, MultiValueMap으로 조회할 수 있다.
- MultiValueMap 의 경우 key에 대응하는 value 들을 List로 반환해서 사용 

### 5. @ModelAttribute
- @RequestParam 은 값을 하나씩 가져오는 방식인 반면, 객체로 매칭시키지 못한다. 
- 스프링은 이것을 해결하기 위해 @ModelAttribute를 구현해두었다.
```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(@ModelAttribute HelloData helloData) {
	log.info("username={}, age={}", helloData.getUsername(),helloData.getAge());
	return "ok";
} 
```

### 6. @ModelAttribute 생략 가능 
```java
@ResponseBody
@RequestMapping("/model-attribute-v2")
public String modelAttributeV2(HelloData helloData) {
	log.info("username={}, age={}", helloData.getUsername(),helloData.getAge());
	return "ok";
}
```
- 값타입(String,int 등) 은 @RequestParam 으로 작동 
- 나머지는 @ModelAttribute 로 작동

<br>

## 방법3. HTTP message body에 데이터를 직접 담아서 요청한 경우 
- HTTP API에서 주로 사용, JSON, XML, TEXT
- 데이터 형식은 주로 JSON 사용
- POST, PUT, PATCH

### [서버에서 처리 방법]

### 1. InputStream 사용해서 직접 읽기

```java
@PostMapping("/request-body-string-v1")
public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
	ServletInputStream inputStream = request.getInputStream();
	
	String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
	
	log.info("messageBody={}", messageBody);
	response.getWriter().write("ok");
}
```
- InputStream 을 통해 HTTP Body 내용을 직접 읽는다.
- 넘어온 데이터는 바이트 코드이기 때문에 인코딩 타입을 전달해주어 올바른 값을 읽어올 수 있도록 한다.

### 2. HttpEntity 사용 
```java
@PostMapping("/request-body-string-v3")
public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {
	String messageBody = httpEntity.getBody();
	log.info("messageBody={}", messageBody);
	return new HttpEntity<>("ok");
}
```
- HTTP header, body 정보를 편리하게 조회 가능
- HttpEntity는 응답에도 사용 가능
- StringHttpMessageConverter 가 이를 처리해준다. 

### 3. @RequestBody 사용 
```java
@ResponseBody
@PostMapping("/request-body-string-v4")
public String requestBodyStringV4(@RequestBody String messageBody) {
	log.info("messageBody={}", messageBody);
	return "ok";
}
```
- HTTP 메시지 바디 정보를 가져온다.
- 헤더 정보는 @RequestHeader 를 사용하면 된다.

---------------------------------------------

[정리]
#### 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
#### HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody


## 3-1. HTTP 요청 메시지 - JSON

### 1. ObjectMapper 사용
```java
private ObjectMapper objectMapper = new ObjectMapper();
@PostMapping("/request-body-json-v1")
public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
	ServletInputStream inputStream = request.getInputStream();
	String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
	log.info("messageBody={}", messageBody);

	HelloData data = objectMapper.readValue(messageBody, HelloData.class);
	log.info("username={}, age={}", data.getUsername(), data.getAge());

	response.getWriter().write("ok");
}
```
- 메시지 바디를 읽어 messageBody 로 변환
- 그리고 Jackson 라이브러리인 objectMapper를 사용해서 자바 객체로 변환

### 2. @RequestBody와 ObjectMapper 사용 
```java
@ResponseBody
@PostMapping("/request-body-json-v2")
public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
	HelloData data = objectMapper.readValue(messageBody, HelloData.class);
	log.info("username={}, age={}", data.getUsername(), data.getAge());
	return "ok";
}
```
- @RequestBody 를 사용해 바로 문자열을 가져오고
- objectMapper.readValue() 를 통해 자바 객체로 변환

### 3. @RequestBody 객체 변환

```java
@ResponseBody
@PostMapping("/request-body-json-v3")
public String requestBodyJsonV3(@RequestBody HelloData data) {
	log.info("username={}, age={}", data.getUsername(), data.getAge());
	return "ok";
}
```
- @RequestBody 를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가 원하는 문
  자나 객체 등으로 변환해준다.

### 4. @RequestBody 객체 변환 + @ResponseBody 로 객체 응답 
```java
@ResponseBody
@PostMapping("/request-body-json-v5")
public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
	log.info("username={}, age={}", data.getUsername(), data.getAge());
	return data;
}
```

- @RequestBody 요청  <br>
JSON 요청 > HTTP 메시지 컨버터 > 객체 
- @ResponseBody 응답 <br>
객체 > HTTP 메시지 컨버터 > JSON 응답 <br>