
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

### [1단계] FrontControllerServletV1
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

### [2단계] View 역할 분리

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

<br>

### [3단계] Model을 통한 데이터 전달

프론트 컨트롤러가 컨트롤러(핸들러)들을 호출할 때, 인자로 서블릿에서 제공하는 객체인  request, response를 전달하고 있다.
필요한 데이터만 받아 처리하도록 서블릿 종속성을 제거하자
```java
MyView process(HttpServletRequest request, HttpServletResponse response)
```
또한 View 의 이름도 위치가 중복되어 표시되는 것을 볼 수 있다.
```java
new MyView("/WEB-INF/views/save-result.jsp");
```

View 이름 문제와, 서블릿 종속성을 제거하기 위해 ModelView를 만든다.
```java
public class ModelView {
    private String viewName;
    private Map<String, Object> model = new HashMap<>();
	
    public ModelView(String viewName) {
        this.viewName = viewName;
    }
	
    public String getViewName() {
        return viewName;
    }
	
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
	
    public Map<String, Object> getModel() {
        return model;
    }
	
	public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
```

컨트롤러의 변화
```java
public interface ControllerV3 {
    ModelView process(Map<String, String> paramMap);
}
```
1. 요청으로 들어오는 데이터는 Map으로 받는다.

2. 컨트롤러는 결과 처리 후 view이름과 응답할 데이터가 있는 ModelView를 반환하고,
프론트 컨트롤러가 이를 처리한다.

<br>

회원 저장을 담당하는 컨트롤러가 다음과 같이 변경되었다.

```java
public class MemberSaveControllerV3 implements ControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
	
    @Override	
    public ModelView process(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));
		
        Member member = new Member(username, age);
        memberRepository.save(member);
		
        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);
        return mv;
    }
}
```
<br>

그럼 이 프론트 컨트롤러는 ModelView 를 받게되고, 프론트 컨트롤러는 이전에 만든 MyView를 통해 처리한다.

```java
private MyView viewResolver(String viewName) {
    return new MyView("/WEB-INF/views/" + viewName + ".jsp");
 }
```
이후 MyView의 render를 통해 view로 이동한다.



