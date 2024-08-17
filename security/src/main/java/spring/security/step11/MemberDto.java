package spring.security.step11;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data @Getter
@AllArgsConstructor
public class MemberDto {
	public String name;
	public String password;
}
