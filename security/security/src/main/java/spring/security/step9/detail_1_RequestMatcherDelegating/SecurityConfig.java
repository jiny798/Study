package spring.security.step9.detail_1_RequestMatcherDelegating;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

// @EnableWebSecurity
// @Configuration
public class SecurityConfig {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, ApplicationContext context) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.anyRequest().access(authorizationManager(null))) // 어떠한 요청 -> CustomRequestMatcherDelegatingAuthorizationManager 가 처리함
			.formLogin(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable);

		return http.build();
	}

	@Bean
	public AuthorizationManager<RequestAuthorizationContext> authorizationManager(
		HandlerMappingIntrospector introspector) {
		List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings = new ArrayList<>();

		// 요청 정보와 권한 처리를 진행하는 매니저를 직접 생성한다
		RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> requestMatcherEntry1
			= new RequestMatcherEntry<>(new MvcRequestMatcher(introspector, "/user"),
			AuthorityAuthorizationManager.hasAuthority("ROLE_USER"));

		RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> requestMatcherEntry2
			= new RequestMatcherEntry<>(new MvcRequestMatcher(introspector, "/db"),
			AuthorityAuthorizationManager.hasAuthority("ROLE_DB"));

		RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> requestMatcherEntry3
			= new RequestMatcherEntry<>(new MvcRequestMatcher(introspector, "/admin"),
			AuthorityAuthorizationManager.hasAuthority("ROLE_ADMIN"));

		RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> requestMatcherEntry4
			= new RequestMatcherEntry<>(AnyRequestMatcher.INSTANCE, new AuthenticatedAuthorizationManager<>());

		mappings.add(requestMatcherEntry1);
		mappings.add(requestMatcherEntry2);
		mappings.add(requestMatcherEntry3);
		mappings.add(requestMatcherEntry4);

		return new CustomRequestMatcherDelegatingAuthorizationManager(mappings);
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
		UserDetails db = User.withUsername("db").password("{noop}1111").roles("DB").build();
		UserDetails admin = User.withUsername("admin").password("{noop}1111").roles("ADMIN", "SECURE").build();
		return new InMemoryUserDetailsManager(user, db, admin);
	}
}
