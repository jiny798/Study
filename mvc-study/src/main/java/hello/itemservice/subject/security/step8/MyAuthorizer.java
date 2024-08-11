package hello.itemservice.subject.security.step8;

import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.stereotype.Component;

@Component("myAuthorizer")
public class MyAuthorizer {

	public boolean isUser(MethodSecurityExpressionOperations root){
		return root.hasAuthority("ROLE_USER");
	}
}
