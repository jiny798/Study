package spring.security.step7;

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

	

	@Bean
	SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {

		CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
		XorCsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new XorCsrfTokenRequestAttributeHandler();

		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/csrf", "/csrfToken").permitAll()
				.anyRequest().authenticated())
			.formLogin(Customizer.withDefaults())
			.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)); // csrfTokenRepository


		// http.csrf(csrf -> csrf.ignoringRequestMatchers("/test/*")); // 해당 URL 은 csrf 기능 적용하지 않는다

		return http.build();
	}
}
