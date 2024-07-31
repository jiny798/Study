package hello.itemservice.subject.security.step8;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/2")
public class Method2Controller {

	@GetMapping("/user")
	@Secured("ROLE_USER")
	public String user(){
		return "user";
	}

	@GetMapping("/admin")
	@RolesAllowed("ADMIN")
	public String admin(){
		return "admin";
	}

	@GetMapping("/permitAll")
	@PermitAll // Config에 모든 요청에 인증하도록 설정했다면 , 해당 메서드도 인증은 해야함
	public String permitAll(){
		return "permitAll";
	}

	@GetMapping("/denyAll")
	@DenyAll
	public String denyAll(){
		return "denyAll";
	}
}
