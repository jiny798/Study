package spring.security.step8;

import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;

public class CustomRequestMatcher implements RequestMatcher {

	private final String urlPattern;

	public CustomRequestMatcher(String urlPattern){
		this.urlPattern = urlPattern;
	}
	@Override
	public boolean matches(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return requestURI.startsWith("urlPattern");  // requestURI 이 urlPattern 으로 시작하는지
	}
}
