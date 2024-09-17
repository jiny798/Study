### LogoutFilter

1. Client Post /logout 요청 
2. LogoutFilter - RequestMatcher 를 통해 요청 정보가 매칭 되는지 확인 
   - 로그아웃 요청이 아니면 chain.doFilter 를 통해 다음 필터로 진입
3. 로그아웃이면 LogoutHandler 실행
4. 이후 LogoutSuccessHandler 실행


### 로그아웃 
- DefaultLogoutPageGeneratingFilter 를 통해 로그아웃 페이지를 제공하며 “ GET / logout ” URL 로 접근이 가능하다
- 로그아웃 실행은 기본적으로 “ POST / logout “ 으로만 가능하나 CSRF 기능을 비활성화 할 경우 혹은 RequestMatcher 를 사용할 경우 GET, PUT, DELETE 모두 가능하다
- 사용자가 직접 스프링 MVC 에서 커스텀 하게 로그아웃 기능 구현 가능 (로그아웃 필터를 사용하지 않음)
- 또한 로그인 페이지를 커스텀하게 생성할 경우, 어쩔수 없이 로그아웃 기능도 커스텀하게 구현해줘야 한다


### 로그아웃 API

```java
http.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
    .logoutUrl("/logoutProc") // 로그아웃 URL 을 지정 (기본값은 “/logout” 이다)
    .logoutRequestMatcher(new AntPathRequestMatcher("/logoutProc","POST")) // 로그아웃이 발생하는 RequestMatcher 을 지정한다. logoutUrl 보다 우선적이다
    .logoutSuccessUrl("/logoutSuccess") // 로그아웃이 발생한 후 리다이렉션 될 URL이다. 기본값은 ＂/login?logout＂이다
    .logoutSuccessHandler((request, response, authentication) -> { // 사용할 LogoutSuccessHandler 를 설정합니다.
        response.sendRedirect("/logoutSuccess"); // 이것이 지정되면 logoutSuccessUrl(String)은 무시된다
    })
    .deleteCookies("JSESSIONID“, “CUSTOM_COOKIE”) // 로그아웃 성공 시 제거될 쿠키의 이름을 지정할 수 있다
    .invalidateHttpSession(true) // HttpSession을 무효화해야 하는 경우 true (기본값), 그렇지 않으면 false 이다
    .clearAuthentication(true) // 로그아웃 시 SecurityContextLogoutHandler가 인증(Authentication)을 삭제 해야 하는지 여부를 명시한다
    .addLogoutHandler((request, response, authentication) -> {}) // 기존의 로그아웃 핸들러 뒤에 새로운 LogoutHandler를 추가 한다
    .permitAll() // logoutUrl(), RequestMatcher() 의 URL 에 대한 모든 사용자의 접근을 허용 함
```


### 디버깅

```java
	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (requiresLogout(request, response)) {
			Authentication auth = this.securityContextHolderStrategy.getContext().getAuthentication();
			if (this.logger.isDebugEnabled()) {
				this.logger.debug(LogMessage.format("Logging out [%s]", auth));
			}
			this.handler.logout(request, response, auth);
			this.logoutSuccessHandler.onLogoutSuccess(request, response, auth);
			return;
		}
		chain.doFilter(request, response);
	}
```

<br>

- requiresLogout(request, response) : 로그아웃 요청 URL 이 맞는지 체크한다 
- handler.logout() : 핸들러를 통해 로그아웃 진행 (핸들러 종류는 여러가지가 포함됨)
- SecurityContextLogoutHandler 에서 세션 무효화
- 쿠키, csrf LogoutHandler 에서도 로그아웃 관련 작업 진행 
- 그리고 사용자가 직접 등록한 로그아웃 핸들러도 실행된다 


<br>

SecurityContextLogoutHandler

```java
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		Assert.notNull(request, "HttpServletRequest required");
		if (this.invalidateHttpSession) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
				if (this.logger.isDebugEnabled()) {
					this.logger.debug(LogMessage.format("Invalidated session %s", session.getId()));
				}
			}
		}
		SecurityContext context = this.securityContextHolderStrategy.getContext();
		this.securityContextHolderStrategy.clearContext();
		if (this.clearAuthentication) {
			context.setAuthentication(null);
		}
		SecurityContext emptyContext = this.securityContextHolderStrategy.createEmptyContext();
		this.securityContextRepository.saveContext(emptyContext, request, response);
	}
```
- 세션 제거
- SecurityContext 초기화
- 비어있는 Context 를 다시 SecurityContextHolder 에 저장하여, 다시 인증받도록 유도 

