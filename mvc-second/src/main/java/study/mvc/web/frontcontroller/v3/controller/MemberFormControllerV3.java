package study.mvc.web.frontcontroller.v3.controller;

import java.util.Map;

import study.mvc.web.frontcontroller.ModelView;
import study.mvc.web.frontcontroller.v3.ControllerV3;

public class MemberFormControllerV3 implements ControllerV3 {
	@Override
	public ModelView process(Map<String, String> paramMap) {
		return new ModelView("new-form");
	}
}
