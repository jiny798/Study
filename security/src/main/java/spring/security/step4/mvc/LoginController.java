package spring.security.step4.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

//    private final AuthenticationManager authenticationManager;
//    private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

//    @PostMapping("/login")
//    public Authentication customLogin(@RequestBody LoginRequest login, HttpServletRequest request, HttpServletResponse response) {
//
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
//        Authentication authentication = authenticationManager.authenticate(token);
//
//        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//        securityContext.setAuthentication(authentication);
//        SecurityContextHolder.getContextHolderStrategy().setContext(securityContext);
//
//        securityContextRepository.saveContext(securityContext, request, response);
//
//        return authentication;
//    }

//    @GetMapping
//    public String index(){
//        return "ok";
//    }
}