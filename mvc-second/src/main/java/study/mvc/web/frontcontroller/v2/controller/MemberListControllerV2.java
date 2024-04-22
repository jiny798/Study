package study.mvc.web.frontcontroller.v2.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import study.mvc.domain.member.Member;
import study.mvc.domain.member.MemberRepository;
import study.mvc.web.frontcontroller.MyView;
import study.mvc.web.frontcontroller.v2.ControllerV2;

public class MemberListControllerV2 implements ControllerV2 {
	private MemberRepository memberRepository = MemberRepository.getInstance();

	@Override
	public MyView process(HttpServletRequest request, HttpServletResponse response) throws
		ServletException,
		IOException {
		List<Member> members = memberRepository.findAll();

		request.setAttribute("members", members);
		return new MyView("/WEB-INF/views/members.jsp");
	}
}