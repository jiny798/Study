package spring.security.step3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// @EnableWebSecurity
// @Configuration
public class SecurityConfig {


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
		AuthenticationManager authenticationManager = builder.build();
		// AuthenticationManager authenticationManager = builder.getObject();

		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/api/login").permitAll()
				.anyRequest().authenticated())
			.formLogin(Customizer.withDefaults())
			.authenticationManager(authenticationManager);
		return http.build();
	}
}
