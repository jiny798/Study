
## MVC 


### 서블릿 컨테이너
[웹 애플리케이션 서버의 구조]

![img.png](image/img.png)

- HTTP 요청 메시지를 기반으로 웹서버에서 request 객체를 생성한다.
- 서블릿에서 로직을 처리하고, response 객체에 응답 정보를 추가한다.
- response 객체 청보로 HTTP 응답을 생성해서 내보낸다.

<br>


## Spring MVC 프레임워크 만들기

### 스프링이 프론트 컨트롤러를 쓰는 이유

[1단계]
FrontControllerServletV1
```java
protected void service(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {		

		String requestURI = request.getRequestURI();
		// 컨트롤러(핸들러) 조회
		ControllerV1 controller = controllerMap.get(requestURI);

		if (controller == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// 컨트롤러 로직 실행
		controller.process(request, response);
	}

```
프론트 컨트롤러에서 요청 URI 를 확인하고,
controller.process(request, response) 를 호출하는데, void 를 반환한다.
<br> <br>
MemberSaveController의 process() 내부 마지막 부분을 보면
```java
String viewPath = "/WEB-INF/views/save-result.jsp";
RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
dispatcher.forward(request, response);
```
**컨트롤러(핸들러)가 로직처리, view 이동 처리를 모두 맡게되는 것을 볼 수 있고, 중복되는 코드가 많아진다.**

<br>

[2단계] View 역할 분리

V2 에서는 컨트롤러의(핸들러) view 처리를 외부로 꺼낸다. <br>
컨트롤러(핸들러) 가 아닌, 프론트 컨트롤러에서 View 처리를 하는 것이다.

```java
MyView process(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException;
```

회원 저장을 담당하는 컨트롤러는 회원 저장이 완료되면 MyView 객체를 반환한다.
```java
public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));
 
    Member member = new Member(username, age);
    memberRepository.save(member);
    request.setAttribute("member", member);
 
    return new MyView("/WEB-INF/views/save-result.jsp");
 }
```

MyView 코드
- dispatcher 를 통해 해당 view 로 이동한다.
```java
public class MyView {
    private String viewPath;
 
 public MyView(String viewPath) {
    this.viewPath = viewPath;
 }
 
 public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
    dispatcher.forward(request, response);
 }
}
```

그럼 프론트 컨트롤러는 컨트롤러(핸들러)들에게 process() 로 일을 시키고, <br>
MyView 를 받아 render()를 통해 view 로 이동만 하면된다.

```java
MyView view = controller.process(request, response);
view.render(request, response);
```