package security.demo.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.demo.domain.dto.AccountContext;
import security.demo.domain.dto.AccountDto;
import security.demo.domain.entity.Account;
import security.demo.domain.entity.Role;
import security.demo.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service("userDetailsService")
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = userRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        List<GrantedAuthority> authorities = account.getUserRoles()
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet())
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        ModelMapper mapper = new ModelMapper();
        AccountDto accountDto = mapper.map(account, AccountDto.class);

        return new AccountContext(accountDto, authorities);
    }
}
