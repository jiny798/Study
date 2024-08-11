package spring.security.step8;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// @EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
// @EnableWebSecurity
// @Configuration
// public class SecurityConfig2 {
//
// 	// @Bean
// 	// public WebSecurityCustomizer webSecurityCustomizer(){
// 	// 	return new WebSecurityCustomizer() {
// 	// 		@Override
// 	// 		public void customize(WebSecurity web) {
// 	// 			web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
// 	// 		}
// 	// 	};
// 	// }
// 	@Bean
// 	SecurityFilterChain filterChain1(HttpSecurity http, ApplicationContext context) throws Exception {
//
// 		http
// 			.authorizeHttpRequests(auth -> auth
// 				.requestMatchers("/images/**").permitAll()
// 				.anyRequest().authenticated())
// 			.formLogin(Customizer.withDefaults())
// 			.csrf(AbstractHttpConfigurer::disable);
//
// 		return http.build();
// 	}
//
// 	@Bean
// 	public RoleHierarchy roleHierarchy() {
// 		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
// 		roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER\n" +
// 			"ROLE_USER > ROLE_ANONYMOUS");
// 		return roleHierarchy;
// 	}
// 	@Bean
// 	public UserDetailsService userDetailsService(){
// 		UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
// 		UserDetails admin = User.withUsername("admin").password("{noop}1111").roles("ADMIN","SECURE").build();
// 		return new InMemoryUserDetailsManager(user,admin);
// 	}
// }
