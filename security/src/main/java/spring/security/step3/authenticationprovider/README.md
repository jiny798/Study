### AuthenticationProvider 

![img.png](img.png)

- AuthenticationManager 로 부터 위임받아 실질적인 인증을 수행하는 객체 
- 사용자 이름과 비밀번호를 기반으로 한 인증, 토큰 기반 인증, 지문 인식 등을 처리할 수 있다
- 인증 성공 후에는 Authentication 객체를 반환하고, 이 객체에는 사용자의 신원 정보와 인증된 자격 증명을 포함한다
- 인증 과정 중에 문제가 발생한 경우 AuthenticationException 과 같은 예외를 발생시켜 문제를 알리는 역할을 한다

### AuthenticationProvider 사용 방법 


#### 1. 커스텀 객체로 생성하여 사용 

- 다음은 빈이 아닌 Provider 객체를 생성하여 추가하는 방법 (POJO)

```java


public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    
    managerBuilder.authenticationProvider(new CustomAuthenticationProvider());
    http.authenticationProvider(new CustomAuthenticationProvider2());
    
    http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
    http.formLogin(Customizer.withDefaults());
    return http.build();
}
```
- AuthenticationManagerBuilder 를 사용하든, http 의 API(authenticationProvider() 메서드) 를 사용하든 결과는 같다

- 다음과 같이 Manager 에 Provider 가 추가된다
![img_1.png](img_1.png)



#### 2. 빈으로 생성

```JAVA
@Bean
public AuthenticationProvider customAuthenticationProvider(){
    return new CustomAuthenticationProvider();
}
```

```java
@Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManagerBuilder builder, AuthenticationConfiguration configuration) 
throws Exception {
 AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
 managerBuilder.authenticationProvider(customAuthenticationProvider());
 ProviderManager providerManager = (ProviderManager)configuration.getAuthenticationManager();
 providerManager.getProviders().remove(0);
 builder.authenticationProvider(new DaoAuthenticationProvider());
 http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
 return http.build();
 }

```