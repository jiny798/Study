package study.mvc.web.frontcontroller.v3.controller;

import java.util.List;
import java.util.Map;

import study.mvc.domain.member.Member;
import study.mvc.domain.member.MemberRepository;
import study.mvc.web.frontcontroller.ModelView;
import study.mvc.web.frontcontroller.v3.ControllerV3;

public class MemberListControllerV3 implements ControllerV3 {
	private MemberRepository memberRepository = MemberRepository.getInstance();

	@Override
	public ModelView process(Map<String, String> paramMap) {
		List<Member> members = memberRepository.findAll();

		ModelView mv = new ModelView("members");
		mv.getModel().put("members", members);
		return mv;
	}
}