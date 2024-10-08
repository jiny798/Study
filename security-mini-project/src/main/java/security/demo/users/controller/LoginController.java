package security.demo.users.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import security.demo.domain.dto.AccountDto;

@Controller
public class LoginController {
    @GetMapping(value="/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model){
        model.addAttribute("error",error);
        model.addAttribute("exception",exception);
        return "login/login";
    }

    @GetMapping(value="/api/login")
    public String restLogin(){
        return "rest/login";
    }

    @GetMapping(value="/signup")
    public String signup() {
        return "login/signup";
    }


    // Post(CSRF 방어)가 아닌 Get 으로 직접 만들어서 로그아웃 기능 구현 가능
    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            // 쿠기 삭제 핸들러, 세션 삭제 핸들러등 종류가 많은데,
            // 로그아웃 필터에서 여러 핸들러의 logout()를 호출하여 진행함
            // 그중에서 로그아웃으로 인한 세션 정리는 SecurityContextLogoutHandler 을 통해서 진행하기에, 여기서 사용
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/login";
    }

    @GetMapping(value="/denied")
    public String accessDenied(@RequestParam(value = "exception", required = false) String exception, @AuthenticationPrincipal AccountDto accountDto, Model model) {

        model.addAttribute("username", accountDto.getUsername());
        model.addAttribute("exception", exception);

        return "login/denied";
    }
}