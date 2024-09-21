
### AuthenticationManager 

- 다양한 인증 필터로부터 `Authentication` 을 받아 `AuthenticationProvider` 에 인증 처리를 위임하여 결과를 받고, 사용자 정보, 권한 등을 포함한 완전히 채워진 Authentication 객체를 반환한다
- AuthenticationManager 는 여러 AuthenticationProvider 들을 관리하며 AuthenticationProvider 목록을 순차적으로 순회하며 인증 요청을 처리한다


### AuthenticationManagerBuilder

- AuthenticationManager 를 생성하는 역할
- AuthenticationManagerBuilder 를 통해 AuthenticationProvider 와 UserDetailsService 도 추가할 수 있다
- AuthenticationManagerBuilder 를 사용하려면 HttpSecurity.getSharedObject(AuthenticationManagerBuilder.class) 를 통해 사전에 공유된 객체를 참조한다





<br>

-----------------------

### AuthenticationManager 사용 방법

1. HttpSecurity 사용
2. 직접 생성


#### HttpSecurity 사용하여 AuthenticationManager 사용 
 - `기존에 있는 AuthenticationManager 를 사용하는 방식`
```java

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    
    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    
    AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

    AuthenticationManagerauthenticationManager = authenticationManagerBuilder.getObject(); //build()후에는getObject()로 참조해야한다
    
    http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/login").permitAll()
                    .anyRequest().authenticated())
            .authenticationManager(authenticationManager) //HttpSecurity에 생성한 AuthenticationManager 를 저장한다
            .addFilterBefore(customFilter(http, authenticationManager), UsernamePasswordAuthenticationFilter.class);
    returnhttp.build();
}

// AuthenticationManager 는 빈이 아니어서 @Bean 으로 주입 불가 
public CustomAuthenticationFilter customFilter(AuthenticationManager authenticationManager) throws Exception {
    CustomAuthenticationFilter customAuthenticationFilter = newCustomAuthenticationFilter();
    customAuthenticationFilter.setAuthenticationManager(authenticationManager);
    returncustomAuthenticationFilter;
}
```

- AuthenticationManagerBuilder 를 통해 AuthenticationManager 를 얻는다 
- authenticationManagerBuilder.build() 는 한번만 호출해야 하고, 2번 호출하면 오류 발생 
  - 여러번 가져올 때는 authenticationManagerBuilder.getObject() 를 통해 Manager 를 가져올 수 있다
- 


<br>

#### 직접 AuthenticationManager 생성하여 사용

- `관리할 Provider 를 직접 추가하여 새로운 AuthenticationManager 를 사용하는 방식 `

```java

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
    http.formLogin(Customizer.withDefaults());
    http.addFilterBefore(customFilter(), UsernamePasswordAuthenticationFilter.class);
    returnhttp.build();
}

@Bean
public CustomAuthenticationFilter customFilter() {

    // Manager는 Provider를 관리하기 때문에 Provider 를 하나 만들어 Manager 에게 전달하고
    // 새로운 ProviderManager 를 생성할 수 있다
    List<AuthenticationProvider> list1 = List.of(newDaoAuthenticationProvider());
    ProviderManager parent = newProviderManager(list1);
    
    // 부모 역할을 하는 ProviderManager 도 전달하여 생성할 수 있다 
    List<AuthenticationProvider> list2 = List.of(newAnonymousAuthenticationProvider("key"), newCustomAuthenticationProvider());
    ProviderManager authenticationManager = newProviderManager(list2, parent);
    
    CustomAuthenticationFilter customAuthenticationFilter = newCustomAuthenticationFilter();
    customAuthenticationFilter.setAuthenticationManager(authenticationManager);
    
    return customAuthenticationFilter;
}


```












