### Security 적용 시

SpringBootWebSecurityConfiguration

- 자동 설정에 의한 기본 보안 설정 클래스를 생성하며, 아래와 같다

```java
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;

@ConditionalOnDefaultWebSecurity
static class SecurityFilterChainConfiguration {
	@Bean
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
		http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
		return http.build();
	}
}

```

- HttpSecurity를 통해서 보안 설정 진행

### 초기화 작업

앱 실행 시, 인증,인가 처리 관련 설정을 하는 인터페이스는 SecurityBuilder 와 SecurityConfigurer 가 있다.

- SecurityBuilder 는 웹 보안을 구성하는 클래스를 생성하며, 구현체로 WebSecurity, HttpSecurity 가 존재
- SecurityConfigurer 는 HTTP 요청에 관한 보안처리를 담당하는 필터를 생성하고, 초기화 설정을 돕는다
- SecurityBuilder 가 SecurityConfigurer 를 참조하여 사용하며, SecurityConfigurer 를 통해 인증/인가 초기화 작업을 진행한다

<br>

SecurityBuilder 의 구현체는 WebSecurity, HttpSecurity, AuthenticationManagerBuilder

SecurityConfigurer 의 구현체는 SecurityContextConfigurer, FormLoginConfigurer, CsrfConfigurer 등이 있다.

SecurityBuilder -> SecurityConfigurer의 구현체를 통해 인증/인가에 관련된 초기화 작업 진행

### 초기화 구체적인 순서

1. AutoConfiguration 의 build() 를 통해 빌더 클래스(SecurityBuilder) 생성
    - HttpSecurity(SecurityBuilder 를 상속받은) 객체 생성
2. HttpSecurity 가 SecurityConfigurer 타입의 설정 클래스를 생성
    - FormLoginConfigurer, HttpBasicConfigurer, PasswordManagementConfigurer ... 등 생성
    - SecurityConfigurer 는 init(SecurityBuilder b), configurer(SecurityBuilder b) 메서드를 가지고 있다
3. 설정 클래스는 init(SecurityBuilder b), configurer(SecurityBuilder b) 를 사용해서 필터 생성과 초기화 작업을 진행
    - 인자로 HttpSecurity(SecurityBuilder 타입)가 전달된다
    - init, configurer 안에서 각종 보안 필터들을 생성한다.

### 디버깅 해보기

### HttpSecurityConfiguration class

```java
@Bean(HTTPSECURITY_BEAN_NAME)
@Scope("prototype")
HttpSecurity httpSecurity() throws Exception{
        LazyPasswordEncoder passwordEncoder=new LazyPasswordEncoder(this.context);
        AuthenticationManagerBuilder authenticationBuilder=new DefaultPasswordEncoderAuthenticationManagerBuilder(
        this.objectPostProcessor,passwordEncoder);
        authenticationBuilder.parentAuthenticationManager(authenticationManager());
        authenticationBuilder.authenticationEventPublisher(getAuthenticationEventPublisher());
        HttpSecurity http=new HttpSecurity(this.objectPostProcessor,authenticationBuilder,createSharedObjects());
        WebAsyncManagerIntegrationFilter webAsyncManagerIntegrationFilter=new WebAsyncManagerIntegrationFilter();
        webAsyncManagerIntegrationFilter.setSecurityContextHolderStrategy(this.securityContextHolderStrategy);
        // @formatter:off
        http
            .csrf(withDefaults())
            .addFilter(webAsyncManagerIntegrationFilter)
            .exceptionHandling(withDefaults())
            .headers(withDefaults())
            .sessionManagement(withDefaults())
            .securityContext(withDefaults())
            .requestCache(withDefaults())
            .anonymous(withDefaults())
            .servletApi(withDefaults())
            .apply(new DefaultLoginPageConfigurer<>());
        http.logout(withDefaults());
        // @formatter:on
        applyCorsIfAvailable(http);
        applyDefaultConfigurers(http);
        return http;
        }

```


```java
public HttpSecurity csrf(Customizer<CsrfConfigurer<HttpSecurity>>csrfCustomizer)throws Exception{
        ApplicationContext context=getContext();
        csrfCustomizer.customize(getOrApply(new CsrfConfigurer<>(context)));
        // new CsrfConfigurer 클래스를 생성해서 어딘가에 적용(Apply)하고 있다
        return HttpSecurity.this;
}

```
- CsrfConfigurer는 SecurityConfigurer 를 상속받는다 
- Configurer 설정 클래스를 이용해서 초기화 준비 작업을 진행한다는 것이 중요 

```java
public HttpSecurity exceptionHandling(
            Customizer<ExceptionHandlingConfigurer<HttpSecurity>> exceptionHandlingCustomizer) throws Exception {
        exceptionHandlingCustomizer.customize(getOrApply(new ExceptionHandlingConfigurer<>()));
        return HttpSecurity.this;
}
```

- exceptionHandling 도 동일하게 Configurer 를 사용해서 처리
- HttpSecurityConfiguration 의 httpSecurity() 에서 이러한 작업을 10개 정도하면서, 인증 및 인가 작업을 진행한다
- 그리고 HttpSecurity 를 반환해서 빈을 생성한다 

### SpringBootWebSecurityConfiguration class
```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnDefaultWebSecurity
static class SecurityFilterChainConfiguration {

   @Bean
   @Order(SecurityProperties.BASIC_AUTH_ORDER)
   SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
      http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
      http.formLogin(withDefaults()); // 여기서도 설정 클래스 생성
      http.httpBasic(withDefaults()); // 여기에서도 설정 클래스 생성 
      return http.build(); // build 에서 설정 클래스의 init, configure 메서드를 통해 초기화 진행 
   }

}
```
- 그리고 SpringBootWebSecurityConfiguration 의 defaultSecurityFilterChain 인자에 위에서 만든 HttpSecurity 를 주입해서 사용한다 
- 즉 인증 및 인가 작업을 마친 HttpSecurity 가 들어온다 


![img.png](img.png)

- 또한 http 내부에 configurers 클래스가 생성되어 있는 것을 볼 수 있다.
- 그리고 http.build()에서 각 configurer 마다 init, configure 메서드를 호출하여 초기화를 본격적으로 진행한다























