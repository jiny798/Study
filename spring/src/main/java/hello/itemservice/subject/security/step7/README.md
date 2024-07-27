
### 1. CORS (Cross Origin Resource Sharing 교차 출처 리소스 공유)

- 웹은 한 페이지에서 다른 웹 페이지의 데이터를 직접 불러오는 것을 제한하며, 이것은 "동일 출처 정책(Same-Origin Policy)"
라고 한다
- 다른 출처의 리소스를 사용하고자 할 때, CORS 를 사용해야 하며, HTTP 헤더를 통해 다른 출처의 리소스에 접근할 수 있도록 하는 정책이다.


- aaa.com 페이지에 접속 했을 때, JS 코드인 XMLHttpRequest로 bbb.com의 데이터를 가져오는 경우,
브라우저는 스크립트에서 실행하는 교차 출처 HTTP 요청을 제한하게 된다.
- 다른 출처의 리소스를 불러오기 위해서, 해당 출처에서 CORS 헤더를 포함한 응답을 반환해야 한다 

<br>

### 2. CORS 종류
2-1. Simple Request 
- 해당 요청은 서버에 본요청을 바로 보내서, 응답의 헤더를 보고 브라우저에서
교차 출처 리소스를 사용해도 되는지 판단하는 방식 

- 요청을 보낼 때, ORIGIN을 명시해야 하며, 서버에서 받은 Access-Control-Allow-Origin 헤더 값을 보고,
브라우저에서 판단하여 CORS 가 작동하는 방식 

[조건]
- GET,POST,HEAD 중 한가지 메서드 사용
- 헤더는 Accept, Accept-Language, Content-Language, Content-Type, DPR, Downlink, Save-Data, Viewport-Width Width 만 가능
- 그 외 커스텀 헤더를 사용하면 Preflight request 로 넘어간다
- Context-type 도 application/x-www-form-urlencoded, multipart/form-data, text/plain 만 가능 


<br>

<br>

**2-2. Preflight request (예비요청)**
- Simple Request 조건에 맞지 않으면 모두 Preflight request 사용

[흐름도]
1. JS ----> 브라우저 : JS에서 fetch 함수로 요청
2. 브라우저 -----> Server : 브라우저에서 OPTIONS 메서드와 ORIGIN 헤더 포함하여 서버로 전송
3. 브라우저 <---- Server : 서버에서 200 응답, Access-Control-Allow-Origin:*
4. 서버에서 받은 CORS 관련 헤더 값과 기존 요청의 헤더(ORIGIN)을 보고, 출처가 다르지만 리소스 공유가 가능하다고 판단
5. 브라우저 -----> Server : 실제 요청 전송


<br>
<br>


### 3. CSRF 
- CSRF는 쿠키같은 경우 요청 시 자동으로 전달되기 때문에, 사용자가 의도하지 않은 요청을 서버로 전송하게 하는 공격이다

[흐름도]
1. 사용자 ----> 웹사이트 : 사용자가 웹사이트(a.com)에 로그인 완료
2. 공격자 ----> 사용자 : 공격자가 사용자에게(attack.com) 링크 전달
3. attack.com 에는 a.com/upload 와 같은 이미지 태그가 있다
4. 사용자가 해당 태그를 누르면 attack.com 에서 a.com/upload 와 같은 링크 실행
5. 쿠기가 자동으로 전달되어, 로그인되었다고 판단해 원치않는 a.com/upload 가 실행됨

```java
@Bean
SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
	http.csrf(Customizer.withDefaults());
	//http.csrf(csrf -> csrf.ignoringRequestMatchers("/test/*")); // 특정 url csrf 보호 제외 
	return http.build();
}
```
- csrf 기능은 기본적으로 활성화 
- CSRF용 토큰이 클라이언트마다 발급되고, 클라이언트의 세션에 저장된다.
- 클라이언트는 Form 그리고 모든 변경 요청에 해당 토큰을 같이 전달해야 한다.
  - 변경 요청 : POST, PUT, DELETE 4
- CSRF 공격은 브라우저가 자동으로 전달되는 특성을 방어하기 위함이니, 쿠기가 아닌 HTTP 매개변수나 헤더로 받는 것이 안전하다


<br>

#### 3-1. CSRF 디버깅 포인트

[CsrfFilter.class]
```java
DeferredCsrfToken deferredCsrfToken = this.tokenRepository.loadDeferredToken(request, response);
request.setAttribute(DeferredCsrfToken.class.getName(), deferredCsrfToken);
this.requestHandler.handle(request, response, deferredCsrfToken::get);
if (!this.requireCsrfProtectionMatcher.matches(request)) {
	if (this.logger.isTraceEnabled()) {
		this.logger.trace("Did not protect against CSRF since request did not match "
		+ this.requireCsrfProtectionMatcher);
	}
filterChain.doFilter(request, response);
return;
}
```
- DeferredCsrfToken : Supplier를 통해 토큰을 가져오는 것을 미룬다. 
- request.getMethod() 에 따라 다음 필터로 진행할 것인지, CSRF 토큰을 생성할 것인지 진행 
<br>

만약 인증이 필요한 경우, 로그인 페이지로 갈때, 다음 로직을 거친다

[DefaultLoginPageConfigurer.class]
- 로그인 페이지 자동 생성 객체 
- 토큰은 기본적으로 세션에 저장되며, 임시로 request에 저장하고
- request에 CSRF 토큰을 가져와서 hidden input 안에 넣어 주는 코드도 수행
- 즉 MVC, 사용자 코드에서도 해당 토큰을 얻어낼 수 있음 
```java
// DefaultLoginPageConfigurer.class
private Map<String, String> hiddenInputs(HttpServletRequest request) {
    CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    return (token != null) ? Collections.singletonMap(token.getParameterName(), token.getToken()) : Collections.emptyMap();
}
```

```java
CsrfToken csrfToken = deferredCsrfToken.get(); // 토큰을 얻음 
String actualToken = this.requestHandler.resolveCsrfTokenValue(request, csrfToken); 
```
- csrfToken은 세션에 저장되어 있는 토큰 
- actualToken 은 클라이언트로 보낸 토큰을 읽어 온 것 
- 그리고 유효성 체크 시작. 다르면 권한 에러를 발생시킨다


<br>

#### 3-2. CSRF 토큰 쿠키에 저장 
- 기본적으로 세션에 저장되지만, 쿠키에 저장하도록 변경 가능 

```java
@Bean
SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
	CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
	http
		.authorizeHttpRequests(auth -> auth
			.requestMatchers("/csrf").permitAll()
			.anyRequest().authenticated())
		.formLogin(Customizer.withDefaults())
		.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository));
	return http.build();
}
```


<br>

### Samesite 속성 

- SameSite 는 CSRF 공격을 방어하는 방법 중 하나로, SameSite 속성을 통해 크로스 사이트 간 쿠키 전송을 제어할 수 있다
- Security는 세션 쿠키 생성을 관리하지 않고, Spring Session 에서 SameSite 속성을 변경하도록 지원한다

<br>

### Samesite 속성

#### "Strict" : 동일한 사이트만 허용하고, 크로스 사이트간 쿠키가 전송되지 않는다.
1. 나의 사이트 "a.com" 에 로그인
2. "a.com" 에 대해서는 쿠키 전송됨
3. 내가 b.com 사이트 입장 (공격자가 제공한 사이트라고 가정)
4. b.com 에서 a.com 링크를 클릭
5. b에서 출발했기 때문에 a.com 에 쿠키가 전송되지 않음


#### "Lax" : 동일 사이트 및 Top Level Navigation 에서 오는 읽기 요청인 경우에만 쿠키 전송을 허용하고, 나머지는 Strict 처럼 처리

- 위 Strict 과정 중 b.com 에서  a.com 링크를 클릭할 때, 읽기 요청만 쿠키 전송
- 즉 \<a> 를 클릭하거나 window, location, replace,302 다이렉트 등 이동은 쿠키 전송
- 나머지 img 태그 , iframe, AJAX 인 경우는 쿠키 전송하지 않음 


#### "None" : 크로스 사이트간 쿠키 전송 허용
<br>

### Same Site 속성 설정 
```groovy
implementation group: 'org.springframework.session', name: 'spring-session-core', version: '3.2.1'
```
```java
@Configuration
@EnableSpringHttpSession // JSESSIONID(WAS에서 관리) -> SESSION(스프링) 으로 변경,
public class HttpSessionConfig {

	@Bean
	public CookieSerializer cookieSerializerCustomizer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setUseSecureCookie(true); // 보안 쿠키 사용 - 스크립트 언어로 접근 불가
		serializer.setUseHttpOnlyCookie(true); // HTTP 통신에만 사용
		serializer.setSameSite("Strict"); // Same Site 강도 설정

		return serializer; // SESSION 쿠키만을 의미
	}

	@Bean
	public SessionRepository<MapSession> sessionRepository(){
		return new MapSessionRepository(new ConcurrentHashMap<>());
	}
}
```