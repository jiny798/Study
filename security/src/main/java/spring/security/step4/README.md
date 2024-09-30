
### 1. securityContextRepository 설정 과정 

- SecurityContext 를 "Session 객체" 또는 "request 객체"에 저장하거나,
- 해당 객체로 부터 SecurityContext 불러오기 위한 securityContextRepository 가 설정되는 과정을 설명한다 

<br>

**Filter 를 만드는 초기화 과정 속  SecurityContextConfigurer 의 init(), configure() 가 호출된다**

```java
public void configure(H http) {
    SecurityContextRepository securityContextRepository = this.getSecurityContextRepository();
    
    if (this.requireExplicitSave) {
        SecurityContextHolderFilter securityContextHolderFilter = (SecurityContextHolderFilter) this.postProcess(new SecurityContextHolderFilter(securityContextRepository));
        securityContextHolderFilter.setSecurityContextHolderStrategy(this.getSecurityContextHolderStrategy());
        http.addFilter(securityContextHolderFilter);
    } else {
        SecurityContextPersistenceFilter securityContextFilter = new SecurityContextPersistenceFilter(securityContextRepository);
        securityContextFilter.setSecurityContextHolderStrategy(this.getSecurityContextHolderStrategy());
        SessionManagementConfigurer<?> sessionManagement = (SessionManagementConfigurer) http.getConfigurer(SessionManagementConfigurer.class);
        SessionCreationPolicy sessionCreationPolicy = sessionManagement != null ? sessionManagement.getSessionCreationPolicy() : null;
        if (SessionCreationPolicy.ALWAYS == sessionCreationPolicy) {
            securityContextFilter.setForceEagerSessionCreation(true);
            http.addFilter((Filter) this.postProcess(new ForceEagerSessionCreationFilter()));
        }

        securityContextFilter = (SecurityContextPersistenceFilter) this.postProcess(securityContextFilter);
        http.addFilter(securityContextFilter);
    }

}
```

- 공유된 SecurityContextRepository 를 불러온다 
  - SecurityContextRepository 내부에는 DelegatingSecurityContextRepository 를 가지며
  - DelegatingSecurityContextRepository 는 HttpSession 와 RequestAttribute 에 Context 를 저장하기 위한 Repository 를 2개 가진다 ()

- requireExplicitSave 가 (default)true 이면 SecurityContextHolderFilter 를 만들 때 DelegatingSecurityContextRepository 를 전달한다
  - 즉 SecurityContextHolderFilter 는  명시적으로 Context 를 저장하기 위한 Repository 를 들고 있다 
  - // @TODO : SecurityContextHolderFilter 는 DelegatingSecurityContextRepository 를 사용하나 ..? 봐야함


**그다음 FormLoginConfigurer 의 부모인 AbstractAuthenticationFilterConfigurer 설정 과정을 본다**

```java
// AbstractAuthenticationFilterConfigurer.configure() 내부 
SecurityContextConfigurer securityContextConfigurer = (SecurityContextConfigurer) http.getConfigurer(SecurityContextConfigurer.class);
if( securityContextConfigurer !=null && securityContextConfigurer.isRequireExplicitSave() ){
    SecurityContextRepository securityContextRepository = securityContextConfigurer.getSecurityContextRepository();
    this.authFilter.setSecurityContextRepository(securityContextRepository);
}
```
- 폼인증을 담당하는 UsernamePasswordAuthenticationFilter 에 securityContextRepository 를 넣고 있다
- UsernamePasswordAuthenticationFilter 도 인증을 끝내고 Context를 세션에 저장해야하기 때문에 securityContextRepository 가 필요하다 

<br>
<br>

--------------------------

<br>
<br>
<br>

### 2. 익명 사용자 요청 시, AnonymousAuthenticationToken 생성 과정 

- 익명 요청인 경우, AnonymousAuthenticationToken 을 만들어 SecurityContext 에 저장되는 과정을 설명한다 

SecurityContextHolderFilter
```java
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request.getAttribute(FILTER_APPLIED) != null) {
            chain.doFilter(request, response);
        } else {
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            
            Supplier<SecurityContext> deferredContext = this.securityContextRepository.loadDeferredContext(request);

            try {
                this.securityContextHolderStrategy.setDeferredContext(deferredContext);
                chain.doFilter(request, response);
            } finally {
                this.securityContextHolderStrategy.clearContext();
                request.removeAttribute(FILTER_APPLIED);
            }

        }
    }

```
- 세션과 request 객체로 부터 Supplier 타입의 deferredContext 를 불러온다 
- 구현체는 SupplierDeferredSecurityContext 가 되는데, 여러 Supplier 를 가진다
- **securityContextHolderStrategy 에 deferredContext 를 저장하고, 그다음 필터로 넘어간다**
- deferredContext 는 필요한 시점에 실제 SecurityContext 를 필요로 하는 곳에서 사용할 것이다


<br>
<br>

AnonymousAuthenticationFilter
```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
  Supplier<SecurityContext> deferredContext = this.securityContextHolderStrategy.getDeferredContext();
  this.securityContextHolderStrategy.setDeferredContext(this.defaultWithAnonymous((HttpServletRequest)req, deferredContext));
  chain.doFilter(req, res);
}
```
- AnonymousAuthenticationFilter 에서도 설정만 하고 넘어간다 (당장 여기서 필요하지 않기 때문에)

<br>
<br>

AuthorizationFilter
```java
    private Authentication getAuthentication() {
        Authentication authentication = this.securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("An Authentication object was not found in the SecurityContext");
        } else {
            return authentication;
        }
    }
```
- 인가 처리시점에 Authentication 을 가져오기 위해 실제 SecurityContext 를 가져온다 
- 세션, request 객체에서 Authentication 을 찾는다
- 여기에서는 익명 사용자 이기 때문에 null을 반환한다  
- Authentication 이 null이면 AnonymousAuthenticationFilter 에서 AnonymousAuthenticationToken 을 만들어 Context 에 저장한다 