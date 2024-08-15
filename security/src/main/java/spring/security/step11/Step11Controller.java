package spring.security.step11;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/step11")
public class Step11Controller {

	@GetMapping("/")
	public String index(HttpServletRequest request) {
		return "index";
	}

	@GetMapping("/login")
	public String login(HttpServletRequest request, MemberDto memberDto) throws ServletException {
		request.login(memberDto.getName(), memberDto.getPassword());
		return "login is successful";
	}

	@GetMapping("/user")
	public List<MemberDto> login(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		boolean authenticate = req.authenticate(res);
		if (authenticate) {
			return List.of(new MemberDto("user", "1111"));
		}
		System.out.println("login is successful");
		return Collections.emptyList();
	}
}
