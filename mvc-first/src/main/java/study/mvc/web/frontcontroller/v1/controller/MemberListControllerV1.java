package study.mvc.web.frontcontroller.v1.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import study.mvc.domain.member.Member;
import study.mvc.domain.member.MemberRepository;
import study.mvc.web.frontcontroller.v1.ControllerV1;

public class MemberListControllerV1 implements ControllerV1 {
	private MemberRepository memberRepository = MemberRepository.getInstance();
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Member> members = memberRepository.findAll();
		request.setAttribute("members", members);
		String viewPath = "/WEB-INF/views/members.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
		dispatcher.forward(request, response);
	}
}
