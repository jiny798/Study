package hello.itemservice.subject.security.step9;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import hello.itemservice.subject.security.step9.custom.CustomAuthorizationManager;

// @EnableWebSecurity
// @Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, ApplicationContext context) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/user").hasRole("USER")
				.requestMatchers("/db").access(new WebExpressionAuthorizationManager("hasRole('DB')"))
				.requestMatchers("/admin").hasRole("ADMIN")
				.requestMatchers("/custom").access(new CustomAuthorizationManager())
				.anyRequest().authenticated())
			.formLogin(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable);

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(){
		UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
		UserDetails db = User.withUsername("db").password("{noop}1111").roles("DB").build();
		UserDetails admin = User.withUsername("admin").password("{noop}1111").roles("ADMIN","SECURE").build();
		return new InMemoryUserDetailsManager(user,db,admin);
	}
}
