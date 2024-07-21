package hello.itemservice.subject.security.step2;

import java.io.IOException;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @EnableWebSecurity
// @Configuration
public class SecurityConfig {


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
		requestCache.setMatchingRequestParameterName("param1=y");
		http.authorizeHttpRequests((auth) -> auth.requestMatchers("/css/**", "favicon.ico").permitAll());
		http
			.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
			.rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer.alwaysRemember(true))
			.formLogin(form -> form
				.successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
						Authentication authentication) throws IOException, ServletException {
						SavedRequest savedRequest = requestCache.getRequest(request, response);
						// String targetUrl = savedRequest.getRedirectUrl(); // continue 또는 위에서 설정한 파라미터  param1=y 가 붙어서 나옴
						// System.out.println("캐시에 저장된 URL "+ targetUrl);
						String url = "/test/test";
						response.sendRedirect(url);
					}
				})
			).requestCache(configure -> configure.requestCache(requestCache));



			return http.build();
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
