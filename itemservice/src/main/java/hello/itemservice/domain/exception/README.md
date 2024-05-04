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

