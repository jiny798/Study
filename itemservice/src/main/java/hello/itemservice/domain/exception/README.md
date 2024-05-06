### 1. Exception 

개발자가 만든 로직에서 예외가 발생 시, 흐름 과정

WAS << 필터 << 서블릿 << 인터셉터 << 컨트롤러(예외 발생 지점)

```java
@GetMapping("/error-ex")
public void errorEx() {
	throw new RuntimeException("예외 발생!");
}
```
```properties
# 스프링 부트가 제공하는 기본 예외 페이지 제거
server.error.whitelabel.enabled=false
```

RuntimeException 발생 시, 
HTTP Status 500 – Internal Server Error 화면이 보인다.

즉 Exception 발생 시, WAS에서 500 화면을 보내고 있는 것이다

<br>

### 2. response.sendError(HTTP 상태 코드, 오류 메시지)

sendError 를 사용하면 상태 코드와 메시지를 전달할 수 있다.

```java
@GetMapping("/error-500")
public void error500(HttpServletResponse response) throws IOException {
    response.sendError(500);
}
```
sendError 흐름

WAS(sendError 기록 확인) << 필터 << 서블릿 << 인터셉터 << 컨트롤러
(response.sendError() 실행)

- sendError 기록이 있으면, 오류 코드와 메시지를 고객에게 보여준다.


### 3. 오류 화면 제어
과거에는 web.xml 을 통해 설정했지만, 현재는 부트에서 제공하는 방식으로 사용된다

```java
@Component
public class WebServerCustomizer implements
WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
	
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/errorpage/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
	
}
```

factory 에 ErrorPage(코드, 경로) 를 설정해두면 <br>

response.sendError(500) 를 호출하면 errorPage500을 호출하게 되는 것이다.


### [정리] 예외 처리 방법

 1. Exception 발생
    - WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
2.  sendError 사용
    - WAS(sendError 기록 확인) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(sendError())


### 오류 결과 처리까지 흐름
1. WAS는 RuntimeRexception 발생 시, 아래 오류페이지 정보를 확인한다

    " **new ErrorPage(RuntimeException.class, "/error-page/500")** "

2. . WAS는 오류 페이지를 출력하기 위해 /error-page/500 를 다시 요청한다.

   WAS(`/error-page/500` 다시 요청) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-page/500)
   -> View
- 그리고 부트는 BasicErrorController 컨트롤러를 미리 등록하는데, ErrorPage 에서 등록한 /error 를 매핑해서 처리하는 컨트롤러다.
    즉 `/error-page/500` 를 처리하는 로직이 미리 만들어진다.

<br>

### 4. API 예외 
Json 방식으로 요청하여 예외가 발생한 경우, 응답에 사용할 예외 응답을 추가하면 된다.

```java
@RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request, HttpServletResponse response) {
    Map<String, Object> result = new HashMap<>();
	
    Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
    result.put("status", request.getAttribute(ERROR_STATUS_CODE));
    result.put("message", ex.getMessage());
	
    Integer statusCode = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    return new ResponseEntity(result, HttpStatus.valueOf(statusCode));
}
```

- produces = MediaType.APPLICATION_JSON_VALUE 설정이기 때문에 HTTP Header의
  Accept 의 값이 application/json 일 때 해당 메서드가 호출된다.

```json
{
 "message": "잘못된 사용자",
 "status": 500
}
```

<br>

### 5. API 예외처리 - 스프링 부트 기본 오류 처리 

```java
// 스프링 부트에 포함된 BasicErrorController
// 모두 "/error" 경로를 처리
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {}

@RequestMapping
public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {}
```

- 클라이언트 요청의 Accept 해더 값이 text/html 인 경우에는 errorHtml() 을 호출해서 view를 제공한다
- 그 외 요청에는 ResponseEntity 로 HTTP Body에 JSON 데이터를 반환

<br>

직접 구현한 WebServerCustomizer 가 없다면 아래처럼 스프링 부트는 BasicErrorController 가 제공하는 기본 정보들을 활용해서 오류 API를 생성해준다

```json
{
 "timestamp": "2021-04-28T00:00:00.000+00:00",
 "status": 500,
 "error": "Internal Server Error",
 "exception": "java.lang.RuntimeException",
 "trace": "java.lang.RuntimeException: 잘못된 사용자\n\t athello.exception.web.api.ApiExceptionController.getMember(ApiExceptionController.
java:19...,
 "message": "잘못된 사용자",
 "path": "/api/members/ex"
}
```

다음 옵션으로 자세한 정보를 추가할 수 있다.
- server.error.include-binding-errors=always
- server.error.include-exception=true
- server.error.include-message=always
- server.error.include-stacktrace=always


<br>
<br>

### 6. API 예외 처리 - HandlerExceptionResolver 

스프링부트는 기본적으로 RuntimeException, IllegalArgumentException 예외가 발생하면 모두 500으로 처리한다.
IllegalArgumentException 을 400으로 처리하기 위해서는 별도로 설정이 필요하다.

이떄 발생한 에러를 해결하고, 새로 정의할 수 있는 메커니즘을 제공하는 것이 HandlerExceptionResolver 이다.
해당 Resolver를 적용하면 흐름은 다음과 같다.

![img.png](img.png)

<br>

WebMvcConfiguration에 커스텀 HandlerExceptionResolver 를 등록하였다.
```java
// HandlerExceptionResolver
@Override
public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
	try {
		if (ex instanceof IllegalArgumentException) {
			log.info("IllegalArgumentException resolver to 400");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());

			return new ModelAndView();
		}
	} catch (IOException e) {
		log.error("resolver ex", e);
	}
	return null;
}
```

또한 UserException일 경우 응답을 커스텀해서 보내려면
```java
if (ex instanceof UserException) {
    log.info("UserException resolver to 400");
    String acceptHeader = request.getHeader("accept");
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    if ("application/json".equals(acceptHeader)) {
        Map<String, Object> errorResult = new HashMap<>();
        errorResult.put("ex", ex.getClass());
        errorResult.put("message", ex.getMessage());

        String result = objectMapper.writeValueAsString(errorResult);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);

    return new ModelAndView();
    }else {
        return new ModelAndView("error/400");
    }
}
```

이런식으로 직접 값을 설정해야하고, Resolver도 다시 등록해야한다. <br>
과정이 복잡하기 때문에 스프링에서 제공하는 ExceptionResolver 를 사용해보자

<br>

### 7. API 예외 처리 - 스프링이 제공하는 ExceptionResolver1

스프링부트는 HandlerExceptionResolverComposite 를 통해 Resolver를 관리하는데, 다음 순서로 등록한다.

1. ExceptionHandlerExceptionResolver
2. ResponseStatusExceptionResolver
3. DefaultHandlerExceptionResolver

### 7-1. ExceptionHandlerExceptionResolver

### 7-2. ResponseStatusExceptionResolver
- ResponseStatusExceptionResolver 는 예외에 따라서 HTTP 상태 코드를 지정해주는 역할
- @ResponseStatus 가 달려있는 예외, ResponseStatusException 예외를 처리한다.

[사용 예시]
```java
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
public class BadRequestException extends RuntimeException { ... }
```
- 해당 예외가 발생하면 ResponseStatusExceptionResolver 가 ResponseStatus 내용을 참고하여 응답에 적용한다.
- ResponseStatusExceptionResolver 내부적으로 response.sendError(statusCode, resolvedReason) 를 호출하여 다시 컨트롤러로 요청하는 것이다.
- reason 은 messages.properties 메시지 코드를 사용할 수 있다.

<br>

[참고]

@ResponseStatus 는 내가 만든 예외에만 적용할 수있고, 라이브러리의 예외에는 적용할 수가 없는 단점이 있는데,
ResponseStatusException 를 사용하여 해결하면 된다.
```java
throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
```

### 7-3. DefaultHandlerExceptionResolver