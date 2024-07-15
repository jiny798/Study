package study.mvc.web.frontcontroller.v2.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import study.mvc.domain.member.Member;
import study.mvc.domain.member.MemberRepository;
import study.mvc.web.frontcontroller.MyView;
import study.mvc.web.frontcontroller.v2.ControllerV2;

public class MemberSaveControllerV2 implements ControllerV2 {
	private MemberRepository memberRepository = MemberRepository.getInstance();

	/*
	 * 컨트롤러는 필요한 데이터만 받고, VIEW 보내면 되는데
	 * HttpServletRequest, HttpServletResponse 가 필요한 것인가
	 */
	@Override
	public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		int age = Integer.parseInt(request.getParameter("age"));
		Member member = new Member(username, age);

		memberRepository.save(member);

		request.setAttribute("member", member);
		return new MyView("/WEB-INF/views/save-result.jsp");
	}
}