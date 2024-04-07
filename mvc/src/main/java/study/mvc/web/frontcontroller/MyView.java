package study.mvc.web.frontcontroller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * View Path를 받아서 렌더시키는 역할
 */
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
