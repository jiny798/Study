package security.demo.security.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import security.demo.domain.dto.AccountContext;
import security.demo.security.details.FormAuthenticationDetails;

@Component("authenticationProvider")
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = (String)authentication.getCredentials();
        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(loginId);

        if(!passwordEncoder.matches(password, accountContext.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }

        String secretKey = ((FormAuthenticationDetails)authentication.getDetails()).getSecretKey();
        if(secretKey == null || !secretKey.equals("secret")){
            throw new BadCredentialsException("Invalid secret");
        }

        return new UsernamePasswordAuthenticationToken(accountContext.getAccountDto(), null, accountContext.getAuthorities());
            // accountContext 를 줘도 괜찮긴 함
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
