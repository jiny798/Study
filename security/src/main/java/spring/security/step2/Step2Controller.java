package spring.security.step2;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/step2")
public class Step2Controller {

	@GetMapping("/")
	@ResponseBody
	public String index() {
		return "index";
	}

	@GetMapping("/anonymous")
	public String anonymous() {
		return "anonymous";
	}

	@GetMapping("/anonymous")
	public String authentication(Authentication authentication) {
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "anonymous";
		} else {
			return "not anonymous";
		}
	}

	@GetMapping("/anonymousContext")
	public String authentication(@CurrentSecurityContext SecurityContext context) {
		return context.getAuthentication().getName();
	}
}
