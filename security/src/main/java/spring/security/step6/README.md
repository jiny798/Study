
### ExceptionTranslationFilter

예외를 처리하는 필터로 AuthenticationException(인증 예외), AccessDeniedException(인가 예외) 를 처리한다 

- AuthorizationFilter 로 부터 인가 예외를 받아 처리를 하는 필터

<br>
------

### AuthenticationException

AuthenticationException 발생 시, 처리하는 과정 

1. SecurityContext 에서 인증 정보 삭제 - 기존의 Authentication 이 더 이상 유효하지 않다고 판단하고 Authentication 을 초기화
2. AuthenticationEntryPoint 호출 - ExceptionTranslationFilter 는 인증 예외가 발생하면 인증 실패를 공통적으로 처리하기 위해 AuthenticationEntryPoint 를 호출
3. 인증 프로세스의 요청 정보를 저장 - 인증 프로세스 동안 전달되는 요청을 세션 혹은 쿠키 (RequestCache & SavedRequest)
   - 구현은 HttpSessionRequestCache 이며, SavedRequest 속성을 가진다 

----

###  AuthenticationEntryPoint

AuthenticationEntryPoint 는 인증 프로세스마다 기본적으로 제공되는 클래스들이 있다

- UsernamePasswordAuthenticationFIlter 은 LoginUrlAuthenticationEntryPoint 을 가지며, 기본적으로 인증 실패 시, "/login" 으로 이동 시킨다 
- BasicAuthenticationFilter - BasicAuthenticationEntryPoint
- 인증 프로세스가 설정 되지 않으면 기본적으로 Http403ForbiddenEntryPoint가 사용된다 

-----

### AccessDeniedHandler

- AccessDeniedException 인가 예외 발생 시, 처리하는 클래스 
- AccessDeniedHandler 는 기본적으로 AccessDeniedHandlerImple 클래스가 사용된다
- 인가 예외더라도, 익명 사용자인 경우 인증예외처리로 넘어가서 실행됨 

-----

### ExceptionTranslationFilter 프로세스 

인증을 받지 않은 사용자가 인증이 필요한 "/user" 로 요청을 보냈다고 가정

(Filter 중 마지막에 위치한 AuthorizationFilter 에서 권한을 확인)

1. AuthorizationFilter ---> AuthorizationManager :  AuthorizationManager 통해 접근이 가능한지 판단 
2. AuthorizationFilter <--- AuthorizationManager :  AccessDeniedException 를 전달
3. AuthorizationFilter 보다 앞에 위치한 ExceptionTranslationFilter 가 AccessDeniedException 를 확인한다
4. ExceptionTranslationFilter 는 인증 예외인지, 인가 예외인지 판단한다
  - 인가 예외이면 익명사용자 인지 확인
  - 익명 사용자라면 인증 예외 케이스로 처리한다
5. 인가 예외라면 AccessDeniedHandler 호출
6. 인증 예외라면 다음 과정 진행 
   - SecurityContext 내부 인증 객체를 NULL 로 초기화 
   - DefaultSavedRequest 를 세션에 저장 ("/user" 경로 저장)
   - AuthenticationEntryPoint 호출 -> login 이동


-----


### 디버깅 기록
- ExceptionTranslationFilter 에서 예외를 잡아 처리 
- ExceptionTranslationFilter 
  - handleAccessDeniedException() : AccessDeniedException 익명 사용자인지, RememberMe 인증 사용자 인지 확인 하는 부분 
- 익명 사용자인 경우, ExceptionTranslationFilter - sendStartAuthentication() 는 인증 예외를 처리하는 부분 
    - context 초기화
    - requestCache(DefaultRequestCache) 에 요청 정보 저장 
    - AuthenticationEntryPoint 실행 