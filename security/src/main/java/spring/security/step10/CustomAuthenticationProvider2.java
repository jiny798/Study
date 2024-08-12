package spring.security.step10;

import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomAuthenticationProvider2 implements AuthenticationProvider {

    // 내부적으로는 applicationEventPublisher 을 사용
    private final AuthenticationEventPublisher authenticationEventPublisher;

    public CustomAuthenticationProvider2(AuthenticationEventPublisher authenticationEventPublisher) {
        this.authenticationEventPublisher = authenticationEventPublisher;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(!authentication.getName().equals("user")) {
            authenticationEventPublisher.publishAuthenticationFailure(new BadCredentialsException("DisabledException"), authentication);
            // 내부적으로 예외에 따라 이벤트가 mapping 되어 있다.
            // DefaultAuthenticationEventPublisher.java 참고

            throw new BadCredentialsException("BadCredentialsException"); // -> AuthenticationFailureBadCredentialsEvent 발생
        }
        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}