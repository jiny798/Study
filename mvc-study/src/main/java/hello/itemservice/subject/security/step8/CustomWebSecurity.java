package hello.itemservice.subject.security.step8;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component("customWebSecurity")
public class CustomWebSecurity {

	public boolean check(Authentication authentication, HttpServletRequest request){
		return authentication.isAuthenticated();
	}
}
