package hello.itemservice.subject.security.step9.RequestMatcherDelegating;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;

public class CustomRequestMatcherDelegatingAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	RequestMatcherDelegatingAuthorizationManager manager;
	public CustomRequestMatcherDelegatingAuthorizationManager(List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings){
		 manager = RequestMatcherDelegatingAuthorizationManager.builder().mappings(maps -> maps.addAll(mappings)).build();
		 // 1. RequestMatcherDelegatingAuthorizationManager 가 가지고 있는 mappings 에 우리가 만든 요청 Matcher, 권한 처리하는 매니저 전달
	}
	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		// 2. 내부적으로는 RequestMatcherDelegatingAuthorizationManager 를 다시 사용하여 권한 처리
		return manager.check(authentication, object.getRequest());

	}

	@Override
	public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		AuthorizationManager.super.verify(authentication, object);
	}


}
