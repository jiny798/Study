package hello.itemservice.subject.security.step7;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@ResponseBody
public class CsrfController {

	@GetMapping("/csrfToken")
	public String csrfToken(HttpServletRequest request){
		// 지연된 토큰 객체를 얻을 수 있다.
		CsrfToken csrfToken1 = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
		CsrfToken csrfToken2 = (CsrfToken)request.getAttribute("_csrf");

		String token = csrfToken1.getToken(); // 이때 실제 토큰을 받을 수 있다.
		return "csrfToken";
	}
}
