package hello.itemservice.subject.security.step9.custom;

import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	private static final String REQUIRED_ROLE = "ROLE_CUSTOM";

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		return null;
	}
}
