package study.mvc.web.frontcontroller.v5.adapter;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import study.mvc.web.frontcontroller.ModelView;
import study.mvc.web.frontcontroller.v4.ControllerV4;
import study.mvc.web.frontcontroller.v5.MyHandlerAdapter;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {
	@Override
	public boolean supports(Object handler) {
		return (handler instanceof ControllerV4);
	}

	@Override
	public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		ControllerV4 controller = (ControllerV4)handler;
		Map<String, String> paramMap = createParamMap(request);
		Map<String, Object> model = new HashMap<>();
		
		String viewName = controller.process(paramMap, model);

		// v4는 논리뷰만 반환하기에 어탭터에서 ModelView 를 만들어 반환
		ModelView mv = new ModelView(viewName);
		mv.setModel(model);

		return mv;
	}

	private Map<String, String> createParamMap(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<>();
		request.getParameterNames().asIterator()
			.forEachRemaining(paramName -> paramMap.put(paramName,
				request.getParameter(paramName)));
		return paramMap;
	}
}