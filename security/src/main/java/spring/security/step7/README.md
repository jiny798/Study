
### 1. CORS (Cross Origin Resource Sharing 교차 출처 리소스 공유)

![img.png](img.png)


- 웹은 한 페이지에서 다른 웹 페이지의 데이터를 직접 불러오는 것을 제한하며, 이것은 "동일 출처 정책(Same-Origin Policy)"
라고 한다
- 다른 출처의 리소스를 사용하고자 할 때, CORS 를 사용해야 하며, HTTP 헤더를 통해 다른 출처의 리소스에 접근할 수 있도록 하는 정책이다.


- aaa.com 페이지에 접속 했을 때, JS 코드인 XMLHttpRequest로 bbb.com의 데이터를 가져오는 경우,
브라우저는 스크립트에서 실행하는 교차 출처 HTTP 요청을 제한하게 된다.
- 다른 출처의 리소스를 불러오기 위해서, 해당 출처에서 CORS 헤더를 포함한 응답을 반환해야 한다 

<br>

### 2. CORS 종류

출처가 다른 리소스를 허가할 것인지 검사하는 방식은 2가지가 있다

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

--------------------------

<br>
<br>


### 3. CSRF 
- CSRF는 쿠키같은 경우 요청 시 자동으로 전달되기 때문에, 사용자가 의도하지 않은 요청을 서버로 전송하게 하는 공격이다
- 쿠키는 요청 시 브라우저가 자동으로 쿠키를 전송하는 것을 이용한 공격 

[흐름도]
1. 사용자 ----> 웹사이트 : 사용자가 웹사이트(a.com)에 로그인 완료
2. 공격자 ----> 사용자 : 공격자가 사용자에게(attack.com) 링크 전달
3. attack.com 에는 a.com/upload 와 같은 이미지 태그가 있다
4. 사용자가 해당 태그를 누르면 attack.com 에서 a.com/upload 와 같은 링크 실행
5. 쿠기가 자동으로 전달되어, 로그인되었다고 판단해 원치않는 a.com/upload 가 실행됨

----------------------------

### CSRF 활성화

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


---

### CSRF 토큰 저장 읽기 - CsrfTokenRepository

CsrfToken은 CsrfTokenRepository 를 사용하여 관리하며, HttpSessionCsrfTokenRepository 와 CookieCsrfTokenRepository 를 지원한다

```java
@Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
    http.csrf(csrf -> csrf.csrfTokenRepository(repository));
    return http.build();
}

```

- HttpSessionCsrfTokenRepository : 세션에 토큰을 저장한다 
- HttpSessionCsrfTokenRepository는 기본적으로 HTTP 요청 헤더인 **X-CSRF-TOKEN** 또는 요청 매개변수인 _csrf에서 토큰을 읽는다

```java
@Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    CookieCsrfTokenRepository repository = new CookieCsrfTokenRepository();
    // 아래 둘 중 하나만 가능 
    http.csrf(csrf -> csrf.csrfTokenRepository(repository));
    http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
    return http.build();
}
```

- JavaScript 기반 애플리케이션을 지원하기 위해 CsrfToken 을 쿠키에 유지할 수 있으며 구현체로 CookieCsrfTokenRepository를 사용할 수 있다
- CookieCsrfTokenRepository 는 기본적으로 **XSRF-TOKEN** 명을 가진 쿠키에 작성하고 HTTP 요청 헤더인 X-XSRF-TOKEN 또는 요청 매개변수인 _csrf에서 읽는다
- 기본적으로 Csrf 토큰 쿠키는 HTTPOnly 이기 때문에 JavaScript 에서 쿠키를 읽을 수 있도록 HttpOnly를 명시적으로 false로 설정할 수 있다
- 직접 쿠키를 읽을 필요가 없는 경우 보안을 개선하기 위해 기본 설정을 사용하는 것이 좋다 


----

### CsrfTokenRequestHandler

해당 클래스의 역할은 다음과 같다

- Csrf 토큰을 생성하고 응답한다.
- HTTP 헤더 또는 요청 매개변수로부터 토큰의 유효성을 검증하도록 한다
- 구현체로 XorCsrfTokenRequestAttributeHandler 와 CsrfTokenRequestAttributeHandler 를 제공하며, 사용자가 직접 구현할 수도 있다
  - XorCsrfTokenRequestAttributeHandler 는 토큰을 암호화를 하며, CsrfTokenRequestAttributeHandler 를 상속받고 있다

```java
@Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    XorCsrfTokenRequestAttributeHandler csrfTokenHandler = new XorCsrfTokenRequestAttributeHandler();
    http.csrf(csrf -> csrf.csrfTokenRequestHandler(csrfTokenHandler));
    return http.build();
}
```

- “_csrf ” 및 CsrfToken.class.getName() 명으로 HttpServletRequest 속성에 CsrfToken 을 저장하기 때문에 HttpServletRequest 에서 꺼내 사용할 수 있다
- 클라이언트의 매 요청마다 CSRF 토큰 값(UUID) 에 난수를 인코딩하여 변경한 CsrfToken 이 반환 되도록 보장 (세션의 원본 토큰 값은 유지)


-----

#### CSRF 디버깅

[CsrfFilter.class]
```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
	
    // 1단계
    DeferredCsrfToken deferredCsrfToken = this.tokenRepository.loadDeferredToken(request, response);
    request.setAttribute(DeferredCsrfToken.class.getName(), deferredCsrfToken);
    this.requestHandler.handle(request, response, deferredCsrfToken::get);

    // 2단계
    if (!this.requireCsrfProtectionMatcher.matches(request)) {
        if (this.logger.isTraceEnabled()) {
        this.logger.trace("Did not protect against CSRF since request did not match "
        + this.requireCsrfProtectionMatcher);
        }
     filterChain.doFilter(request, response);
     return;
     }

     // 3단계
    CsrfToken csrfToken = deferredCsrfToken.get();
    String actualToken = this.requestHandler.resolveCsrfTokenValue(request, csrfToken);
    if (!equalsConstantTime(csrfToken.getToken(), actualToken)) {
        boolean missingToken = deferredCsrfToken.isGenerated();
        this.logger.debug(LogMessage.of(() -> "Invalid CSRF token found for " + UrlUtils.buildFullRequestUrl(request)));
		
        AccessDeniedException exception = (!missingToken) ? new InvalidCsrfTokenException(csrfToken, actualToken): new MissingCsrfTokenException(actualToken);
        this.accessDeniedHandler.handle(request, response, exception);
        return;
     }
	
    filterChain.doFilter(request, response);
}
```
[주요 체크 사항]
- DeferredCsrfToken : Supplier를 통해 토큰을 가져오는 것을 미룬다. 
- request.getMethod() 에 따라 다음 필터로 진행할 것인지, CSRF 토큰을 생성할 것인지 진행 
- csrfToken은 세션에 저장되어 있는 토큰
- actualToken 은 클라이언트로 보낸 토큰을 읽어 온 것
- 그리고 유효성 체크 시작. 다르면 권한 에러를 발생시킨다

<details>
<summary>DeferredCsrfToken 구현체 보기 </summary>

- DeferredCsrfToken 의 구현체는 RepositoryDeferredCsrfToken 이다
- 실제 토큰을 가지고 있으며, get() 하는 시점에 실제 토큰을 가져온다 
  - 토큰을 가져올 때 없으면 생성하여 저장소(쿠키 or 세션)에 저장한다 
- csrfTokenRepository 는 기본적으로 세션을 사용한다 

```java
final class RepositoryDeferredCsrfToken implements DeferredCsrfToken {
    private final CsrfTokenRepository csrfTokenRepository;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private CsrfToken csrfToken;
    private boolean missingToken;

    RepositoryDeferredCsrfToken(CsrfTokenRepository csrfTokenRepository, HttpServletRequest request, HttpServletResponse response) {
        this.csrfTokenRepository = csrfTokenRepository;
        this.request = request;
        this.response = response;
    }

    public CsrfToken get() {
        // get() 을 호출하면 실제 토큰을 가져온다 
        this.init();
        return this.csrfToken;
    }

    public boolean isGenerated() {
        this.init();
        return this.missingToken;
    }

    private void init() {
        if (this.csrfToken == null) {
            this.csrfToken = this.csrfTokenRepository.loadToken(this.request);
            this.missingToken = this.csrfToken == null;
            if (this.missingToken) {
                this.csrfToken = this.csrfTokenRepository.generateToken(this.request);
                this.csrfTokenRepository.saveToken(this.csrfToken, this.request, this.response);
            }

        }
    }
}
```

</details>

[흐름도]
 
>주요 로직은 코드에 주석한 것처럼 크게 1,2,3 단계로 나눌 수 있다

<strong>1단계</strong>

1단계는 모든 요청마다 실행하는 공통 단계이다
DeferredCsrfToken 안에 실제 CsrfToken 변수를 가지고 있으며, get 하기 전까지는 실제 토큰을 가져오지 않는다. 실제로 필요한 시점에 호출하도록 하여 미루는 것이다.


this.requestHandler.handle 에서 Supplier로 한번 더 감싸고,
```java
@Override
public void handle(HttpServletRequest request, HttpServletResponse response,
			Supplier<CsrfToken> deferredCsrfToken) {
	Assert.notNull(request, "request cannot be null");
	Assert.notNull(response, "response cannot be null");
	Assert.notNull(deferredCsrfToken, "deferredCsrfToken cannot be null");

	request.setAttribute(HttpServletResponse.class.getName(), response);
	CsrfToken csrfToken = new SupplierCsrfToken(deferredCsrfToken);
	request.setAttribute(CsrfToken.class.getName(), csrfToken);
	String csrfAttrName = (this.csrfRequestAttributeName != null) ? this.csrfRequestAttributeName
				: csrfToken.getParameterName();
	request.setAttribute(csrfAttrName, csrfToken);
}
```
이 지연된 객체를 request에 저장한다. 그리고 로그인 페이지를 보여줘야 할 때 hiden input 에 토큰을 저장할 때 사용하기도 함

csrfRequestAttributeName 이 null 이라면, 실제 토큰을 호출하여 파라미터 이름을 가져오는데, 이때 실제 토큰을 불러오게 되면서, Deferred 효과는 사라진다

---

<strong>2단계</strong>

Matcher 객체를 보면 다음과 같은 코드가 있다.
```java
private final HashSet<String> allowedMethods = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));
```
"GET", "HEAD", "TRACE", "OPTIONS" 요청은 CSRF 처리를 하지 않고, 다음 필터로 진행한다 

-----

<strong>3단계</strong>

POST,DELETE 등 요청인 경우 실행된다고 볼 수 있다.

실제 토큰을 얻는 부분이다

쿠키 또는 세션에서 토큰을 가져온다.
만약 인증을 받지 않아 첫 요청이면 세션에 없다. 
그럼 새로운 토큰을 만들어 발급하고, 세션에 새로 저장한다 

이후 다시 요청이 오면, 클라에서 온 토큰과 실제 토큰을 비교하는 로직을 수행 

-> 클라 토큰은 _csrf 파라미터 네임 또는 X-CSRF-TOKEN 헤더 네임으로 읽어 본다 . (변경 가능)

-> 실패시 권한 거부하여 반환한다 

이러한 작업을 CsrfFilter 가 담당하여 Csrf 공격을 방어한다 


---

<br>

<br>

#### HTML 페이지에 토큰을 자동으로 삽입되는 이유

그럼 클라이언트는 해당 토큰을 언제 받을까, 만약 인증이 필요한 경우, 로그인 페이지로 갈때, 다음 로직을 거친다

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

<br>

---

#### CSRF 토큰을 쿠키에 저장 
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
- CookieCsrfTokenRepository.withHttpOnlyFalse() 로 설정하면 HTTPOnly 옵션을 off 할 수 있다.
- csrfTokenRequestHandler() 로 핸들러도 교체할 수 있다

<br>

---

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