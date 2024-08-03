### 인가 구조

![img.png](img.png)

- Spring Security에서 Authentication 내부에 GrantedAuthority 클래스를 통해 권한 목록을 관리
- AuthenticationManager 에서 인증을 처리할 때, Authentication 객체에 GrantedAuthority 객체를 저장한다
- 이후 인가(Authorization)처리는 AuthorizationManager 를 통해 Authentication의 GrandtedAuthority 객체를 읽어들여 처리한다



### AuthorizationManager
- 권한 부여 처리는 AuthorizationFilter 에서 시작되며, 해당 필터가 AuthorizationManager 를 호출해서 권한을 부여할지 결정한다
- 메서드는 verify 와 check 를 가진다

![img_1.png](img_1.png)

check() : 권한을 줘야할지 결정하기 위한 정보들이 전달된다. 예를들어 인증객체, 요청 정보, 권한 정보 등,

또한 액세스가 허용되면 true를 나타내는 AuthorizationDecision, 거부되면 false를 포함하는 AuthorizationDecision가 반환되며
, 결정을 못내리는 경우 null 을 반환

verify() : check를 호출해서 반환된 값이 false를 가진 AuthorizationDecision 인 경우, AccessDeniedException을 throw 해주는 함수


### 