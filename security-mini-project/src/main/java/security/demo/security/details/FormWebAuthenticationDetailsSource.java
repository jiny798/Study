package security.demo.security.details;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
public class FormWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, FormAuthenticationDetails> {

    @Override
    public FormAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new FormAuthenticationDetails(request);
    }
}
