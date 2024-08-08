package hello.itemservice.subject.security.step9.detail_3_PreAuthorizeAuthorizationManager.method;

import java.util.function.Supplier;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;

public class MyPreAuthorizationManager implements AuthorizationManager<MethodInvocation> {
	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocation object) {
		Authentication auth = authentication.get();
		if(auth instanceof AnonymousAuthenticationToken)
			return new AuthorizationDecision(false);

		return new AuthorizationDecision(auth.isAuthenticated());
	}
}
