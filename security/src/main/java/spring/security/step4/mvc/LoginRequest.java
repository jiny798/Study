package spring.security.step4.mvc;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}