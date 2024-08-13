package spring.security.step10.authenticationevent;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.event.AuthenticationFailureProviderNotFoundEvent;
import lombok.RequiredArgsConstructor;

// @Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

   private final ApplicationContext applicationEventPublisher;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if(!authentication.getName().equals("user")) {

           applicationEventPublisher.publishEvent
                   (new AuthenticationFailureProviderNotFoundEvent(authentication, new BadCredentialsException("BadCredentialException")));

            throw new BadCredentialsException("BadCredentialsException");
        }

        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}