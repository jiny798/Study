package study.mvc.web.frontcontroller.v5;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import study.mvc.web.frontcontroller.ModelView;

/*
 * 어댑터는 실제 컨트롤러를 호출하고, 그 결과로 ModelView를 반환
 * 실제 컨트롤러가 ModelView를 반환하지 못하면, 어댑터가 ModelView를 직접 생성해서라도 반환해야한다.
 * 이전에는 프론트 컨트롤러가 실제 컨트롤러를 호출했지만 이제는 이 어댑터를 통해서 실제 컨트롤러가 호출된다.
 */
public interface MyHandlerAdapter {

	boolean supports(Object handler);

	ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;

}
