### 목차

- Custom DSL
- 이중화 설정 - Redis


### Custom Dsl

- 시큐리티는 Configurer 마다 init, configure 를 호출 하여 필터들이 생성되도록 초기화 작업을 진행하는데,
- 여기서 사용자가 직접 정의한 필터가 들어갈 수있도록, 시큐리티는 사용자 정의 DSL을 구현할 수 있도록 지원함
- 즉 DSL을 구성하면 그곳에 초기화 작업을 모두 넣을 수 있어서 필터, 핸들러, 메서드, 속성 등을 한 곳에 정의하여 처리할 수 있는 편리함이 있다

<br>

> 그럼 Custom 필터를 만들어 필터 목록에 넣는 커스텀 DSL을 정의하는 방법을 살펴보자

<br>

#### 1. 커스텀 필터 작성 
- 간단히 username, password 를 받아 로그인을 처리하는 커스텀 필터를 만듬
- 외부에 주입하는 flag에 따라 작동 on/off 가 되도록 설정 
```java
public class MyCustomFilter extends OncePerRequestFilter {
    private boolean flag;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (flag) {
            try{
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                request.login(username, password);
            }catch (Exception e){
                System.out.println(e);
            }

        }
        filterChain.doFilter(request, response);

    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}

```


<br>

#### 2. AbstractHttpConfigurer<AbstractHttpConfigurer, HttpSecurityBuilder> 

- 사용자 DSL 을 구현 하기 위해 초기화 작업을 위한 init(), configure() 메서드를 오버라이딩 한다
- 여기에서 외부에서 받은 flag를 필터에 설정하도록 함 

```java
public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {

    private boolean flag;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        super.init(builder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        MyCustomFilter myCustomFilter = new MyCustomFilter();
        myCustomFilter.setFlag(flag);
        //http.addFilterBefore(myCustomFilter , UsernamePasswordAuthenticationFilter.class);
        // -> myCustomFilter 인자로 넘어온 request 가 AwareRequestFilter 에서 래핑된 객체라서 윗처럼 하면 처리가 안됨
        http.addFilterBefore(myCustomFilter , SecurityContextHolderAwareRequestFilter.class);
        super.configure(http);
    }

    public boolean setFlag(boolean value) {
        return flag = value;
    }

    public static MyCustomDsl customDsl() {
        return new MyCustomDsl();
    }

}


```


<br>

#### API
- 이제 우리가 만든 DSL을 등록하기 위해서는 API 가 필요한데, 바로 HttpSecurity 의 with() 를 통해 등록

> HttpSecurity.with(C configurer, Customizer<C> customizer)

    - configurer 는 우리가 만든 DSL을 구현한 클래스
    - customizer 는 DSL 구현 클래스에서 정의한 여러 API를 커스트 마이징하도록 한다 : EX. 해당 필터가 적용될 것인지 true, false 로 설정하도록 할 수 있다
    - 여러 DSL 클래스를 반복 적용해도 한번만 적용된다     

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/user").hasAuthority("ROLE_USER")
        .requestMatchers("/db").hasAuthority("ROLE_DB")
        .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
        .anyRequest().permitAll())
            .formLogin(Customizer.withDefaults())
                .with(MyCustomDsl.customDsl(), myCustomDsl -> myCustomDsl.setFlag(true));

        return http.build();
}

```