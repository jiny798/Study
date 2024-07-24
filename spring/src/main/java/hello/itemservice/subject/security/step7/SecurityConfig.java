package hello.itemservice.subject.security.step7;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
		// http.csrf(Customizer.withDefaults());
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/test/*"));
		return http.build();
	}
}
