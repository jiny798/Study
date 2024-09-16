### 익명사용자

- 익명 인증 사용자의 권한을 별도로 운용할 수 있다. 즉 인증 된 사용자가 접근할 수 없도록 구성이 가능하다
- 익명 인증 객체는 세션에 저장하지 않는다

### 익명 사용자 API
- Form 인증에서 사용하던 Filter 에서 UsernamePasswordAuth~~Token 을 만든 것 처럼
- 인증받지 않은 경우(세션에 인증 객체가 없는 경우) AnonymousAuthenticationFilter 에서 AnonymousAuthenticationToken 을 생성한다
- AnonymousAuthenticationToken 에는 기본적으로 principal 에 문자열로 "anonymousUser", 권한은 ROLE_ANONYMOUS 가 저장되어 있다

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests( auth -> auth.anyRequest().authenticated())
        .formLogin(Customizer.withDefaults())
        .anonymous(anonymous -> anonymous
        .principal("guest")
        .authorities("ROLE_GUEST")
        );
    return http.build();
}
```


<br>

### MVC에서 익명 사용자 객체 얻기

```java
public String method(Authentication authentication) {
	if (authentication instanceof AnonymousAuthenticationToken) {
        return "anonymous";
    } else {
        return "not anonymous";
    }
}
```
- 인증된 경우에는 authentication 이 존재하지만
- 인증되지 않은 경우, authentication 에는 null 값이 들어온다 

```java
public String method(@CurrentSecurityContext SecurityContext context){
    return context.getAuthentication().getName();
}
```
- @CurrentSecurityContext 을 사용하면 익명 사용자 객체를 얻을 수 있다
- 이때 해당 인자를 처리해주는 Resolver는 CurrentSecurityContextArgumentResolver 이다.






