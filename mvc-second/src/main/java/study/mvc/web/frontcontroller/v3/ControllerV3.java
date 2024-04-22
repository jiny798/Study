package study.mvc.web.frontcontroller.v3;

import java.util.Map;

import study.mvc.web.frontcontroller.ModelView;

public interface ControllerV3 {
	// 이 컨트롤러는 서블릿 기술을 전혀 사용하지 않는다. 따라서 구현이 매우 단순해지고, 테스트 코드 작성시 테스트 하기 쉽다.
	ModelView process(Map<String, String> paramMap);
}
