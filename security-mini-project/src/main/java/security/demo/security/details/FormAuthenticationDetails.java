package security.demo.security.details;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Getter
public class FormAuthenticationDetails extends WebAuthenticationDetails {
    private final String secretKey;

    public FormAuthenticationDetails(HttpServletRequest request) {
        super(request); // 부모에서는 IP 주소등 정보를 저장함
        this.secretKey = request.getParameter("secret_key");


    }

}
