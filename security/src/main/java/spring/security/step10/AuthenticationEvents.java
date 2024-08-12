package spring.security.step10;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.*;
import org.springframework.stereotype.Component;

import spring.security.step10.custom_event.CustomAuthenticationFailureEvent;
import spring.security.step10.custom_event.CustomAuthenticationSuccessEvent;

/*
** 리스너를 통해 수신하고자 하는 이벤트를 등록할 수 있다
 */

@Component
public class AuthenticationEvents {
    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        // 스프링 시큐리티에서 로그인 성공시, 기본으로 사용하는 인증 성공 이벤트
        System.out.println("success = " + success.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        System.out.println("failures = " + failures.getException().getMessage());
    }

    @EventListener
    public void onSuccess(InteractiveAuthenticationSuccessEvent success) {
        // 스프링 시큐리티에서 인증 완료 시, 마지막 이벤트
        System.out.println("success = " + success.getAuthentication().getName());
    }

    @EventListener
    public void onSuccess(CustomAuthenticationSuccessEvent success) {
        System.out.println("success = " + success.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent failures) {
        System.out.println("failures = " + failures.getException().getMessage());
    }

    @EventListener
    public void onFailure(AuthenticationFailureProviderNotFoundEvent failures) {
        System.out.println("failures = " + failures.getException().getMessage());
    }

    @EventListener
    public void onFailure(CustomAuthenticationFailureEvent failures) {
        System.out.println("failures = " + failures.getException().getMessage());
    }
}