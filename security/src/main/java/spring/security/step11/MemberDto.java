package spring.security.step11;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {
	public String name;
	public String password;
}
