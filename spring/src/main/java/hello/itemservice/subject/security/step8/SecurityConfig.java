package hello.itemservice.subject.security.step8;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	// @Bean
	// SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
	//
	// 	http
	// 		.authorizeHttpRequests(auth -> auth
	// 			.requestMatchers("/user/{name}")
	// 			.access(new WebExpressionAuthorizationManager("#name == authentication.name"))
	//
	// 			.requestMatchers("/admin/db")
	// 			.access(new WebExpressionAuthorizationManager("hasAuthority('ROLE_ADMIN')"))
	//
	// 			.anyRequest().authenticated())
	// 		.formLogin(Customizer.withDefaults());
	//
	// 	return http.build();
	// }

	// @Bean
	// SecurityFilterChain defaultFilterChain(HttpSecurity http, ApplicationContext context) throws Exception {
	//
	// 	DefaultHttpSecurityExpressionHandler expressionHandler = new DefaultHttpSecurityExpressionHandler();
	// 	expressionHandler.setApplicationContext(context);
	//
	// 	WebExpressionAuthorizationManager authorizationManager = new WebExpressionAuthorizationManager("@customWebSecurity.check(authentication,request)");
	// 	authorizationManager.setExpressionHandler(expressionHandler); // 핸들러가 처리하기 때문에 핸들러 등록
	//
	// 	http
	// 		.authorizeHttpRequests(auth -> auth
	// 			.requestMatchers("/custom/**").access(authorizationManager) // custom/** 이 오면 커스텀하게 만든 authorizationManager 로 처리하도록 지시
	// 			.anyRequest().authenticated())
	// 		.formLogin(Customizer.withDefaults());
	//
	// 	return http.build();
	// }

	// @Bean
	// SecurityFilterChain defaultFilterChain(HttpSecurity http, ApplicationContext context) throws Exception {
	//
	// 	http
	// 		.authorizeHttpRequests(auth -> auth
	// 			.requestMatchers(new CustomRequestMatcher("/user")).hasAuthority("ROLE_USER")
	// 			.anyRequest().authenticated())
	// 		.formLogin(Customizer.withDefaults());
	//
	// 	return http.build();
	// }


	//SecurityMatcher
	@Bean
	SecurityFilterChain filterChain1(HttpSecurity http, ApplicationContext context) throws Exception {

		http
			.authorizeHttpRequests(auth -> auth
				.anyRequest().authenticated())
			.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	@Order(1) // 위 필터체인에 @Order(1) 를 넣으면 다른 결과 발생, 생성 순서에 따라 처리
	SecurityFilterChain filterChain2(HttpSecurity http, ApplicationContext context) throws Exception {

		http.securityMatchers(matchers -> matchers.requestMatchers("/api/**", "/api2/**"))
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll())
			.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(){
		UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();

		return new InMemoryUserDetailsManager(user);
	}
}
