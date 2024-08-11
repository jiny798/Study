package spring.security.step1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// @EnableWebSecurity
// @Configuration
public class SecurityConfig {

	// 스프링 시큐리티7 버전부터는 람다 형식만 지원 할 예정
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
			.formLogin(Customizer.withDefaults());
		return http.build(); // 자동 설정에 의한 SecurityFilterChain 은 생성되지 않는다.
	}

	@Bean
	public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
		UserDetails user = User.withUsername("user")
			.password("{noop}111")
			.authorities("ROLE_USER")
			.build();
		return new InMemoryUserDetailsManager(user);
	}
}
