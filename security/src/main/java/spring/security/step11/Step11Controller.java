package spring.security.step11;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/step11")
public class Step11Controller {

	AuthenticationTrustResolverImpl trustResolver = new AuthenticationTrustResolverImpl();

    @GetMapping("/")
    public String index(HttpServletRequest request) {
    	// 시큐리티와 종속되어 있다
		Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
		return trustResolver.isAnonymous(authentication) ? "anonymous" : "authenticated";

    }

    // 서블릿 통합
    @GetMapping("/login")
    public String login(HttpServletRequest request, MemberDto memberDto) throws ServletException {
        request.login(memberDto.getName(), memberDto.getPassword());
        return "login is successful";
    }

    // 서블릿 통합
    @GetMapping("/user")
    public List<MemberDto> login(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        boolean authenticate = req.authenticate(res);
        if (authenticate) {
            return List.of(new MemberDto("user", "1111"));
        }
        System.out.println("login is successful");
        return Collections.emptyList();
    }

    // MVC 통합 : AuthenticationPrincipal
    @GetMapping("/mvc/user")
    public String user(@AuthenticationPrincipal User user) {  // AuthenticationPrincipalArgumentResolver 에서 처리
        return "user";
    }

    // MVC 통합 : expression
    @GetMapping("/mvc/user2")
    public String user(@AuthenticationPrincipal(expression = "username") String username) {
        // User 내부의 속성 값을 불러옴
        return username;
    }

    // MVC 통합 : 메타 주석
    @GetMapping("/mvc/login-user")
    public String loginUser(@LoginUser User user) {  // AuthenticationPrincipalArgumentResolver 에서 처리
        return "loginUser";
    }

    // MVC 통합 : 메타 주석
    @GetMapping("/mvc/login-user-name")
    public String loginUserName(@LoginUserName String userName) {  // AuthenticationPrincipalArgumentResolver 에서 처리
        return "loginUserName : " + userName;
    }


}
