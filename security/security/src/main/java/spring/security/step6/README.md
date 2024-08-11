
### ExceptionTranslationFilter
- AuthorizationFilter 로 부터 인가 예외를 받아 처리를 하는 필터

<br>

### ExceptionTranslationFilter 프로세스 

1. AuthorizationFilter(필터중 마지막 필터) 는 인가관련 필터로, 요청 경로로 접근이 가능한지 확인 
2. AuthorizationFilter -> AuthorizationManager 를 통해 권한 확인
3. AuthorizationFilter <- AuthorizationManager : 접근 권한 Exception 발생 
4. 해당 예외를 ExceptionTranslationFilter 에서 확인
5. ExceptionTranslationFilter 는 AccessDeniedException 인지 확인 
6. AccessDeniedException 이라면 익명 사용자인지, 인증을 했어도 RememberMe로 인증한 경우 AuthenticationException 처리하는 곳으로 이동

#### AuthenticationException 처리
 - SecurityContext 에 null 로 초기화 
 - 세션에 이전 요청 정보를 담은 DefaultSavedRequest 를 저장 

#### AccessDeniedException 처리
- AccessDeniedHandler 실행 



### 디버깅
- ExceptionTranslationFilter 에서 예외를 잡아 처리 
- ExceptionTranslationFilter 
  - handleAccessDeniedException() : AccessDeniedException 익명 사용자인지, RememberMe 인증 사용자 인지 확인 하는 부분 
- 익명 사용자인 경우, ExceptionTranslationFilter - sendStartAuthentication() 는 인증 예외를 처리하는 부분 
    - context 초기화
    - requestCache(DefaultRequestCache) 에 요청 정보 저장 
    - AuthenticationEntryPoint 실행 