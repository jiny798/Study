package spring.security.step10.authorizationevent;

import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.authorization.event.AuthorizationEvent;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationEvents {

    @EventListener
    public void onAuthorization(AuthorizationEvent event){
        // 인가 상위 이벤트
        System.out.println("event = " + event.getAuthentication().get().getAuthorities());
    }
    @EventListener
    public void onAuthorization(AuthorizationDeniedEvent failure){
        System.out.println("event = " + failure.getAuthentication().get().getAuthorities());
    }

    @EventListener
    public void onAuthorization(AuthorizationGrantedEvent success){
        System.out.println("event = " + success.getAuthentication().get().getAuthorities());
    }
}