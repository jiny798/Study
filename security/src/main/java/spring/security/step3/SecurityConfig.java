package spring.security.step3;

import java.util.Timer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import spring.security.step3.authentication_manager.CustomAuthenticationFilter;

//@EnableWebSecurity
//@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();            // build() 는 최초 한번 만 호출해야 한다
		// 초기화 과정속에서 authenticationManagerBuilder.build() 가 호출 되는데

		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/","/api/login").permitAll()
				.anyRequest().authenticated())
			.authenticationManager(authenticationManager)
			.addFilterBefore(customFilter(http, authenticationManager), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	public CustomAuthenticationFilter customFilter(HttpSecurity http, AuthenticationManager authenticationManager) {
		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(http);
		customAuthenticationFilter.setAuthenticationManager(authenticationManager);
		return customAuthenticationFilter;
	}

	@Bean
	public UserDetailsService userDetailsService(){
		UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
		return  new InMemoryUserDetailsManager(user);
	}
}
