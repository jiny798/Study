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


### RememberMeAuthenticationFilter

1. RememberMeAuthenticationFilter 에서 Authentication 있는지 판단
2. 없으면 리멤버미 쿠기 전달해서 RemeberMeServices.authlogin() 실행
3. 해당 쿠키로 사용자 정보 가져와서 RememberMeAuthenticationToken 생성
4. 해당 토큰으로 AuthenticationManger 를 통해 인증 처리 시작
5. 성공 시, Authentication 을 SecurityConext에 저장하고, Context를 세션에 저장



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