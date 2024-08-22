package security.demo.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountDto {
    private String id;
    private String username;
    private String password;
    private int age;
    private String roles;
}