package spring.security.step10;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import spring.security.step10.custom_event.CustomAuthenticationSuccessEvent;

import java.io.IOException;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    // 이벤트를 발행하기 위한 Publisher
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationContext applicationContext;
    // private final AuthenticationProvider authenticationProvider;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (webSecurity) -> {
            webSecurity.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user").hasAuthority("ROLE_USER")
                        .requestMatchers("/db").hasAuthority("ROLE_DB")
                        .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .successHandler((request, response, authentication) -> {
                            // CustomAuthenticationSuccessEvent 을 발행하면, AuthenticationEvents 에서 수신하면 된다
                            eventPublisher.publishEvent(new CustomAuthenticationSuccessEvent(authentication));

                            // applicationContext.publishEvent(new CustomAuthenticationSuccessEvent(authentication));
                            response.sendRedirect("/");
                        }))
                .csrf(AbstractHttpConfigurer::disable)
//                .authenticationProvider(authenticationProvider);
                .authenticationProvider(customAuthenticationProvider2());

        return http.build();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider2(){
        return new CustomAuthenticationProvider2(authenticationEventPublisher(null));
    }

    @Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        DefaultAuthenticationEventPublisher authenticationEventPublisher =
            new DefaultAuthenticationEventPublisher(applicationEventPublisher);
        return authenticationEventPublisher;
    }
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
        UserDetails db = User.withUsername("db").password("{noop}1111").roles("DB").build();
        UserDetails admin = User.withUsername("admin").password("{noop}1111").roles("ADMIN","SECURE").build();
        return  new InMemoryUserDetailsManager(user, db, admin);
    }
}