package hello.itemservice.subject.security.step7;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

// @EnableWebSecurity
// @Configuration
public class SecurityConfig {
	// http.csrf(Customizer.withDefaults());
	// http.csrf(csrf -> csrf.ignoringRequestMatchers("/test/*"));
	@Bean
	SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {

		CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
		XorCsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new XorCsrfTokenRequestAttributeHandler();

		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/csrf").permitAll()
				.anyRequest().authenticated())
			.formLogin(Customizer.withDefaults())
			.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)); // csrfTokenRepository

		return http.build();
	}
}
