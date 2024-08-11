package hello.itemservice.subject.security.step9.detail_3_PreAuthorizeAuthorizationManager.method;

import java.util.function.Supplier;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.method.MethodInvocationResult;
import org.springframework.security.core.Authentication;

import hello.itemservice.subject.security.step8.Account;

public class MyPostAuthorizationManager implements AuthorizationManager<MethodInvocationResult> {

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocationResult object) {
		Authentication auth = authentication.get();
		if(auth instanceof AnonymousAuthenticationToken)
			return new AuthorizationDecision(false);

		Account account = (Account) object.getResult();
		boolean isGranted = account.getOwner().equals(auth.getName());

		return new AuthorizationDecision(isGranted);
	}
}
