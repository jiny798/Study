### RememberMe 인증

- 로그인 시, 인증 정보를 기억하는 기능
- UsernamePasswordAuthenticationFilter 와 함께 사용되며, AbstractAuthenticationProcessingFilter 슈퍼클래스에서 훅을 통해 구현된다
- 인증 성공 시 RememberMeServices.loginSuccess() 를 통해 RememberMe 토큰을 생성하고 쿠키로 전달한다
- 인증 실패 시 RememberMeServices.loginFail() 를 통해 쿠키를 지운다
- LogoutFilter 와 연계해서 로그아웃 시 쿠키를 지운다

### RememberMe Token
리멤버미 토큰은 아래의 정보로 구성된다
- username
  -  UserDetailsService 로 식별 가능한 사용자 이름
- expirationTime
  -  remember-me 토큰이 만료되는 날짜와 시간, 밀리초로 표현
- algorithmName
  -  remember-me 토큰 서명을 생성하고 검증하는 데 사용되는 알고리즘(기본적으로 SHA-256 알고리즘을 사용)
- algorithmHex(username + ":" + expirationTime + ":" password + ":" + key)
  - expirationTime: remember-me 토큰이 만료되는 날짜와 시간, 밀리초로 표현
  - key: remember-me 토큰의 수정을 방지하기 위한 개인 키
  - username,expirationTime,password,key를 사용하여 복호화가 불가능하도록 암호화한다 

**위의 4개를 다시 base64 로 인코딩하여 토큰으로 사용**

### RememberMeServices

- 기억하기 인증을 위한 토큰을 생성한다
- TokenBasedRememberMeServices : 해싱을 사용하여 쿠키 기반 토큰을 만들기 위해 사용
- PersistentTokenBasedRememberMeServices : 생성한 토큰을 DB와 같은 매체에 저장할 때 사용


rememberMe() API
```java
 http.rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
        .alwaysRemember(true) // 브라우저에서 기억하기 체크를 하지않아도 사용할 수 있게 설정, 기본은 false 이며 체크박스를 통해 리멤버미가 되도록 한다
        .tokenValiditySeconds(3600) // 토큰이 유효한 시간(초 단위)을 지정할 수 있다
        .userDetailsService(userDetailService) // UserDetails 를 조회하기 위해 사용되는 UserDetailsService를 지정한다
        .rememberMeParameter("remember") // 로그인 시 사용자를 기억하기 위해 사용되는 HTTP 매개변수이며 기본값은 'remember-me' 이다
        .rememberMeCookieName("remember") // 기억하기(remember-me) 인증을 위한 토큰을 저장하는 쿠키 이름이며기본값은 'remember-me' 이다
        .key("security") // 기억하기(remember-me) 인증을 위해 생성된 토큰을 식별하는 키를 설정한다
);
```


### RememberMeAuthenticationFilter (Remember-me 인증 처리 과정)
1. Client에서 GET /user 경로로 요청했다고 가정 
2. RememberMeAuthenticationFilter 에서 Authentication 있는지 판단
  - 만약 Authentication이 존재한다면 이미 인증이 된 것이니, chain.doFilter() 를 호출하여 다음 필터로 진행 
3. 없으면 인증을 받을 수 있도록 Remember-me 쿠키를 전달해서 RemeberMeServices.authlogin() 를 실행
4. 해당 쿠키로 사용자 정보 가져와서 RememberMeAuthenticationToken (UserDetails + Authorities) 생성 
   - 폼인증 토큰과 다른점은 실제 존재하는 사용자 정보인 UserDetails 가 저장된다. 폼인증 할때는 username, password 를 넣음
5. 해당 토큰으로 AuthenticationManger 를 통해 인증 처리 시작
6. 성공 시, RememberMeAuthenticationToken(Authentication) 을 SecurityContextHolder 내부 SecurityConext에 저장
7. 이후 SecurityContextRepository를 통해 세션에 SecurityConext 가 저장된다
8. ApplicationEventPublisher 를 통해 인증 성공 이벤트가 발행된다 

### Remember-me 쿠키 발급 과정
- Remeber-me 인증을 하기 위한 쿠키 발급은 UsernamePasswordAuthenticationFilter 의 부모인 AbstractAuthenticationProcessingFilter 에서 이루어진다

```java
// AbstractAuthenticationProcessingFilter 의 doFilter 메서드
...생략
        try {
            Authentication authenticationResult = attemptAuthentication(request, response);
            if (authenticationResult == null) {
            // return immediately as subclass has indicated that it hasn't completed
            return;
        }
        this.sessionStrategy.onAuthentication(authenticationResult, request, response);
        // Authentication success
        if (this.continueChainBeforeSuccessfulAuthentication) {
            chain.doFilter(request, response);
        }
            successfulAuthentication(request, response, chain, authenticationResult);
        }
```
- 폼인증을 성공하면 successfulAuthentication() 이 호출된다

<br>

```java
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
		context.setAuthentication(authResult);
		this.securityContextHolderStrategy.setContext(context);
		this.securityContextRepository.saveContext(context, request, response);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
		}
		// 인증 성공 시 작업 끝
		
		// Remember 관련 작업 
		this.rememberMeServices.loginSuccess(request, response, authResult);
		if (this.eventPublisher != null) {
			this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
		}
		this.successHandler.onAuthenticationSuccess(request, response, authResult);
	}
```
- 인증 성공 시, 작업을 끝내고 this.rememberMeServices.loginSuccess()를 통해 Remember me 관련 작업을 수행
- 리멤버미 쿠키를 만들어 전송하는 것이다

<br>

```java
	@Override
	public void loginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) {
		if (!rememberMeRequested(request, this.parameter)) {
			this.logger.debug("Remember-me login not requested.");
			return;
		}
		onLoginSuccess(request, response, successfulAuthentication);
	}
```
- "rememberme" 파라미터가 맞는지 확인하고, 맞다면 onLoginSuccess() 가 실행된다 

<br>

```java
	@Override
	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) {
		String username = retrieveUserName(successfulAuthentication);
		String password = retrievePassword(successfulAuthentication);
		// If unable to find a username and password, just abort as
		// TokenBasedRememberMeServices is
		// unable to construct a valid token in this case.
		if (!StringUtils.hasLength(username)) {
			this.logger.debug("Unable to retrieve username");
			return;
		}
		if (!StringUtils.hasLength(password)) {
			UserDetails user = getUserDetailsService().loadUserByUsername(username);
			password = user.getPassword();
			if (!StringUtils.hasLength(password)) {
				this.logger.debug("Unable to obtain password for user: " + username);
				return;
			}
		}
		int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
		long expiryTime = System.currentTimeMillis();
		// SEC-949
		expiryTime += 1000L * ((tokenLifetime < 0) ? TWO_WEEKS_S : tokenLifetime);
		String signatureValue = makeTokenSignature(expiryTime, username, password, this.encodingAlgorithm);
		setCookie(new String[] { username, Long.toString(expiryTime), this.encodingAlgorithm.name(), signatureValue },
				tokenLifetime, request, response);
		if (this.logger.isDebugEnabled()) {
			this.logger
				.debug("Added remember-me cookie for user '" + username + "', expiry: '" + new Date(expiryTime) + "'");
		}
	}
```
- Rememberme 쿠키를 생선한다 


<br>

### RememberMe 인증 과정 

```java
// RememberMeAuthenticationFilter
if (this.securityContextHolderStrategy.getContext().getAuthentication() != null) {
	this.logger.debug(LogMessage.of(() -> "SecurityContextHolder not populated with remember-me token, as it already contained: '"
						+ this.securityContextHolderStrategy.getContext().getAuthentication() + "'"));
	chain.doFilter(request, response);
	return;
}
Authentication rememberMeAuth = this.rememberMeServices.autoLogin(request, response);
```
- 브라우저의 JESSION 쿠키를 삭제하면 Authentication 정보가 없기 때문에 rememberMeServices.autoLogin() 가 실행된다 

```java
@Override
	public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
		String rememberMeCookie = extractRememberMeCookie(request);
		if (rememberMeCookie == null) {
			return null;
		}
		this.logger.debug("Remember-me cookie detected");
		if (rememberMeCookie.length() == 0) {
			this.logger.debug("Cookie was empty");
			cancelCookie(request, response);
			return null;
		}
		try {
			String[] cookieTokens = decodeCookie(rememberMeCookie);
			UserDetails user = processAutoLoginCookie(cookieTokens, request, response);
			this.userDetailsChecker.check(user);
			this.logger.debug("Remember-me cookie accepted");
			return createSuccessfulAuthentication(request, user);
		}
		catch...
		cancelCookie(request, response);
		return null;
	}

```
- 브라우저가 보낸 rememberme 쿠키를 추출하여 인증 객체를 만들어 낸다
- 그리고 반환된 토큰을 가지고 인증 처리를 수행한다

```java
	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        ... 생략 
		Authentication rememberMeAuth = this.rememberMeServices.autoLogin(request, response);
		if (rememberMeAuth != null) {
			// Attempt authentication via AuthenticationManager
			try {
				// 인증 처리 수행
				rememberMeAuth = this.authenticationManager.authenticate(rememberMeAuth);
				// Store to SecurityContextHolder
				SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
				context.setAuthentication(rememberMeAuth);
				this.securityContextHolderStrategy.setContext(context);
				onSuccessfulAuthentication(request, response, rememberMeAuth);
				this.logger.debug(LogMessage.of(() -> "SecurityContextHolder populated with remember-me token: '"
						+ this.securityContextHolderStrategy.getContext().getAuthentication() + "'"));
				this.securityContextRepository.saveContext(context, request, response);
				if (this.eventPublisher != null) {
					this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
							this.securityContextHolderStrategy.getContext().getAuthentication(), this.getClass()));
				}
				if (this.successHandler != null) {
					this.successHandler.onAuthenticationSuccess(request, response, rememberMeAuth);
					return;
				}
			}
			catch...
		}
		chain.doFilter(request, response);
	}
```
- 생성된 Rememberme 토큰을 authenticationManager 에 전달하여 인증을 수행한다 
- 그리고 securityContextRepository 을 통해 다시 세션에 SecurityContext를 저장한다




<br>
<br>


### RequestCache - SavedRequest

- 인증되지 않아 로그인 페이지로 리다이렉트되고, 로그인 뒤 이전에 요청 했던 정보를 SavedRequest 객체에 담고,
- 쿠키 or 세션에 저장하고 필요 시 다시 가져와 실행하는 캐시 형태의 구조

![img.png](img.png)

- SavedRequest 는 위와 같은 정보를 담고 있다

### 프로세스

[인증 받지 않은 상태로 /test 에 접근]
1. HttpSessionRequestCache 가 DefaultSavedRequest 생성해서 세션에 저장
2. 로그인 페이지로 이동
3. 로그인 인증 성공,
4. AuthenticationSuccessHandler 에서 세션에 있는 DefaultSavedRequest 를 사용하여, 이전에 요청했던 url("/test") 을 가져온다
5. 해당 url 로 리다이렉트
6. 어떤 요청이든 작동하는 RequestCacheAwareFilter 에서 SavedRequest 가 있는지, SavedRequest 의 url이랑 현재 요청url이랑 같은지 확인
7. 같으면 SavedRequest 사용 , 아니면 그냥 현재 request를 다음 필터로 넘긴다

### RequestCacheAwareFilter

- RequestCacheAwareFilter 는 캐시에 꺼낼 때, 특정 파라미터 값이 일치하는지를 보고 SavedRequest를 가져오는데 ,
- 기본 파라미터 값은 continue 이다.

```java
HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
requestCache.setMatchingRequestParameterName("param1=y");
```

- 다음과 같이 캐시에서 꺼낼 때 사용되는 파라미터를 변경할 수 있다.

```java
SavedRequest savedRequest = requestCache.getRequest(request, response);
String targetUrl = savedRequest.getRedirectUrl(); // continue 또는 설정한 파라미터 (param1=y) 가 붙어서 나옴
```

- 그래서 로그인 성공하고, 캐시에서 getRedirectUrl() 로 url을 가져오면 자동으로 파라미터를 붙여서 다음에 요청할 URL을 준다
- (캐시를 사용하기 위해 )

<br>

> 또한 아래와 같이, /test/test?continue 또는  /test/test?param1=y 파라미터를 안붙이고
>
> 그냥 /test/test 를 보냈을 때 테스트 결과, RequestCacheAwareFilter 에서 SavedRequest를 사용하지는 않지만, 로그인에는 지장이 없는 것으로 보인다.
>

```java

@Override
public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) throws IOException, ServletException {
			SavedRequest savedRequest = requestCache.getRequest(request, response);
			// String targetUrl = savedRequest.getRedirectUrl(); // continue 또는 위에서 설정한 파라미터  param1=y 가 붙어서 나옴
			// System.out.println("캐시에 저장된 URL "+ targetUrl); 
			String url = "/test/test";  // 이렇게 파라미터를 안붙이고 보내도 상관은 없다
			response.sendRedirect(url);
	}


```