package study.spring.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//WAS(서블릿컨테이너) 가 req,resp 객체 만들어서 던져준다.
		// 해당 객체들은 인터페이스 표준인데, 톰캣, 제티 등 WAS들이 구현해서 던져준다.

		System.out.println("HelloServlet.service");
		System.out.println("req = " + req);
		System.out.println("res = " + resp);

		String username = req.getParameter("username");
		System.out.println("username = " + username);

		// header 정보에 들어갈 내용
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("utf-8"); // 옛날 시스템이 아니면 euc-kr 쓰면 안됨

		resp.getWriter().write("hello "+ username);
	}
}
