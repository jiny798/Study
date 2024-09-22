### AuthenticationManager

- 다양한 인증 필터로부터 `Authentication` 을 받아 `AuthenticationProvider` 에 인증 처리를 위임하여 결과를 받고, 사용자 정보, 권한 등을 포함한 완전히 채워진
  Authentication 객체를 반환한다
- AuthenticationManager 는 여러 AuthenticationProvider 들을 관리하며 AuthenticationProvider 목록을 순차적으로 순회하며 인증 요청을 처리한다

### AuthenticationManagerBuilder

- AuthenticationManager 를 생성하는 역할
- AuthenticationManagerBuilder 를 통해 AuthenticationProvider 와 UserDetailsService 도 추가할 수 있다
- AuthenticationManagerBuilder 를 사용하려면 HttpSecurity.getSharedObject(AuthenticationManagerBuilder.class) 를 통해 사전에 공유된 객체를
  참조한다

<br>

-----------------------

### AuthenticationManager 사용 방법

1. HttpSecurity 사용
2. 직접 생성

#### HttpSecurity 사용하여 AuthenticationManager 사용

- `기존에 있는 AuthenticationManager 를 사용하는 방식`

```java

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{

	AuthenticationManagerBuilder authenticationManagerBuilder=http.getSharedObject(AuthenticationManagerBuilder.class);

	AuthenticationManager authenticationManager=authenticationManagerBuilder.build();

	AuthenticationManagerauthenticationManager=authenticationManagerBuilder.getObject(); //build()후에는getObject()로 참조해야한다

	http
	.authorizeHttpRequests(auth->auth
	.requestMatchers("/api/login").permitAll()
	.anyRequest().authenticated())
	.authenticationManager(authenticationManager) //HttpSecurity에 생성한 AuthenticationManager 를 저장한다
	.addFilterBefore(customFilter(http,authenticationManager),UsernamePasswordAuthenticationFilter.class);
	returnhttp.build();
	}

// AuthenticationManager 는 빈이 아니어서 @Bean 으로 주입 불가 
public CustomAuthenticationFilter customFilter(AuthenticationManager authenticationManager)throws Exception{
	CustomAuthenticationFilter customAuthenticationFilter=newCustomAuthenticationFilter();
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
public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
	http.authorizeHttpRequests(auth->auth.anyRequest().authenticated());
	http.formLogin(Customizer.withDefaults());
	http.addFilterBefore(customFilter(),UsernamePasswordAuthenticationFilter.class);
	returnhttp.build();
	}

@Bean
public CustomAuthenticationFilter customFilter(){

	// Manager는 Provider를 관리하기 때문에 Provider 를 하나 만들어 Manager 에게 전달하고
	// 새로운 ProviderManager 를 생성할 수 있다
	List<AuthenticationProvider> list1=List.of(newDaoAuthenticationProvider());
	ProviderManager parent=newProviderManager(list1);

	// 부모 역할을 하는 ProviderManager 도 전달하여 생성할 수 있다 
	List<AuthenticationProvider> list2=List.of(newAnonymousAuthenticationProvider("key"),newCustomAuthenticationProvider());
	ProviderManager authenticationManager=newProviderManager(list2,parent);

	CustomAuthenticationFilter customAuthenticationFilter=newCustomAuthenticationFilter();
	customAuthenticationFilter.setAuthenticationManager(authenticationManager);

	return customAuthenticationFilter;
	}


```

<br>

----------------------------

<br>

### AuthenticationManager 초기화 과정

HttpSecurityConfiguration

```java
// HttpSecurityConfiguration
@Bean(HTTPSECURITY_BEAN_NAME)
@Scope("prototype")
HttpSecurity httpSecurity() throws Exception{
	LazyPasswordEncoder passwordEncoder=new LazyPasswordEncoder(this.context);
	// AuthenticationManagerBuilder 생성
	AuthenticationManagerBuilder authenticationBuilder=new DefaultPasswordEncoderAuthenticationManagerBuilder(
	this.objectPostProcessor,passwordEncoder);

	// AuthenticationManagerBuilder 에 authenticationManager 설정 
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
- 초기화 처음에는 DefaultPasswordEncoderAuthenticationManagerBuilder 를 새로 생성하여 사용하고,
- authenticationManager() 내부에서는 컨테이너에 등록된 AuthenticationManagerBuilder 를 사용하고 build 를 호출해서 manager 를 반환 받는다
- 새로 생성한 DefaultPasswordEncoderAuthenticationManagerBuilder 는 build 를 호출하지 않고, 공유 객체로 만들어지게 되며
- 그렇기 때문에 직접 만든 Config 파일에서 AuthenticationManagerBuilder 의 build() 를 호출할 수 있는 것으로 보임 

```java
private AuthenticationManager authenticationManager()throws Exception{
	return this.authenticationConfiguration.getAuthenticationManager();
	}
```

```java
public AuthenticationManager getAuthenticationManager()throws Exception{
	if(this.authenticationManagerInitialized){
	    return this.authenticationManager;
	}
	AuthenticationManagerBuilder authBuilder=this.applicationContext.getBean(AuthenticationManagerBuilder.class);
	if(this.buildingAuthenticationManager.getAndSet(true)){
	    return new AuthenticationManagerDelegator(authBuilder);
	}
	for(GlobalAuthenticationConfigurerAdapter config:this.globalAuthConfigurers){
	    authBuilder.apply(config);
	}
	this.authenticationManager=authBuilder.build();
	if(this.authenticationManager==null){
	    this.authenticationManager=getAuthenticationManagerBean();
	}
	this.authenticationManagerInitialized=true;
	return this.authenticationManager;
}
```

- Bean 으로 등록된 AuthenticationManagerBuilder 를 불러온다
- authBuilder.build() 를 호출하여 AuthentiationManager 를 생성하는데, 다음 과정을 거친다

<br>

AuthenticationManagerBuilder

```java
	@Override
	public AuthenticationManagerBuilder authenticationProvider(AuthenticationProvider authenticationProvider) {
		this.authenticationProviders.add(authenticationProvider);
		return this;
	}
```
- AuthenticationManager 는 Provider 가 필요하기 때문에
- 여기에서 AuthenticationProvider 를 추가하고 있다.
- 여기에서는 DaoAuthenticationProvider 가 추가됨 (Form 인증), DaoAuthenticationProvider 는 이전에 초기화 과정속에서 기본으로 생성됨 

```java
@Override
protected ProviderManager performBuild()throws Exception{
	if(!isConfigured()){
	    this.logger.debug("No authenticationProviders and no parentAuthenticationManager defined. Returning null.");
	    return null;
	}
	// 추가한 Provider 를 넘겨 Manager 생성
	ProviderManager providerManager=new ProviderManager(this.authenticationProviders,
	this.parentAuthenticationManager);
	if(this.eraseCredentials!=null){
	    providerManager.setEraseCredentialsAfterAuthentication(this.eraseCredentials);
	}
	if(this.eventPublisher!=null){
	    providerManager.setAuthenticationEventPublisher(this.eventPublisher);
	}
	providerManager=postProcess(providerManager);
	
	return providerManager;
}
```
- Provider 를 넘겨 AuthenticationManager 를 생성하고 있다. (ProviderManager 구현체 )


<br>

```java
	public HttpSecurity(ObjectPostProcessor<Object> objectPostProcessor,
			AuthenticationManagerBuilder authenticationBuilder, Map<Class<?>, Object> sharedObjects) {
		super(objectPostProcessor);
		Assert.notNull(authenticationBuilder, "authenticationBuilder cannot be null");
		setSharedObject(AuthenticationManagerBuilder.class, authenticationBuilder);
		for (Map.Entry<Class<?>, Object> entry : sharedObjects.entrySet()) {
			setSharedObject((Class<Object>) entry.getKey(), entry.getValue());
		}
		ApplicationContext context = (ApplicationContext) sharedObjects.get(ApplicationContext.class);
		this.requestMatcherConfigurer = new RequestMatcherConfigurer(context);
	}
```
- 이후 setSharedObject() 를 통해 AuthenticationManagerBuilder 를 공유하기 위한 객체로 변경하고 있다. 


----------------------------------------------------

### AnonymousProvider 

- 익명 사용자를 담당하는 AnonymousProvider 도 초기화 작업 중 추가된다 

AnonymousConfigurer
```java
	@Override
	public void init(H http) {
		if (this.authenticationProvider == null) {
			this.authenticationProvider = new AnonymousAuthenticationProvider(getKey());
		}
		this.authenticationProvider = postProcess(this.authenticationProvider);
		http.authenticationProvider(this.authenticationProvider);
	}
```
- init 과정에서 AnonymousProvider 를 추가한다
- authenticationProvider 를 사용하여 Provider 를 추가한다


```java
	@Override
	public HttpSecurity authenticationProvider(AuthenticationProvider authenticationProvider) {
		getAuthenticationRegistry().authenticationProvider(authenticationProvider);
		return this;
	}
```
- 추가하는 위치는 getAuthenticationRegistry() 인데, 해당 객체가 바로 AuthenticationManagerBuilder 이다
- DaoAuthenticationProvider 는 부모 AuthenticationManager 에 추가하였고,
- 현재 AuthenticationManager 에 AnonymousProvider 를 추가하여 Manager 를 리턴하는 것이다 
- 사용자 Config 파일에도 AuthenticationManagerBuilder 를 통해 Manager 를 생성하고, Provider 를 추가할 수 있다 


-----------------------------

