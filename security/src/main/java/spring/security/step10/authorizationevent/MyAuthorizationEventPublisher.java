package spring.security.step10.authorizationevent;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Supplier;

public class MyAuthorizationEventPublisher implements AuthorizationEventPublisher {
    private final AuthorizationEventPublisher delegate;
    private final ApplicationEventPublisher eventPublisher;

    public MyAuthorizationEventPublisher(AuthorizationEventPublisher delegate, ApplicationEventPublisher eventPublisher) {
        this.delegate = delegate;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public <T> void publishAuthorizationEvent(Supplier<Authentication> authentication,
                                              T object, AuthorizationDecision decision) {
        if (decision == null) {
            return;
        }
        if (!decision.isGranted()) {
            this.delegate.publishAuthorizationEvent(authentication, object, decision);
            return;
        }

        // 위에서 인증은 되었으니, 경로에 매칭된 권한만 검사해도, 특정 권한에 대해서 인가 처리를 할 수 있다
        if (shouldThisEventBePublished(decision)) {
            AuthorizationGrantedEvent<T> granted = new AuthorizationGrantedEvent<>(
                    authentication, object, decision);
            eventPublisher.publishEvent(granted);
        }
    }

    private boolean shouldThisEventBePublished(AuthorizationDecision decision) {
        if (!(decision instanceof AuthorityAuthorizationDecision)) {
            //AuthorizationDecision 은 성공 실패 true false 만 있고
            // AuthorityAuthorizationDecision 은 권한 목록을 가지고 있음
            // 예를 들어 /user는 ROLE_USER, ROLE_ADMIN 이 필요하다 등 정보를 AuthorityAuthorizationDecision 가 들고있음
            return false;
        }

        Collection<GrantedAuthority> authorities = ((AuthorityAuthorizationDecision) decision).getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}