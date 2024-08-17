package spring.security.step11;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : username " )
public @interface LoginUserName {
    // #this > Principal 자체를 의미
    // anonymousUser 이면 유저 객체가 아닌, String 타입이라 username 속성이 없음, null 처리 필수
}
