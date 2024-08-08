package hello.itemservice.subject.security.step9.detail_3_PreAuthorizeAuthorizationManager.method;

import org.springframework.aop.Advisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authorization.method.AuthorizationManagerAfterMethodInterceptor;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity(prePostEnabled = false) // 직접 만든 클래스만 작동하도록
@Configuration
public class MethodSecurityConfig {

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public Advisor preAuthorize() {
		return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(new MyPreAuthorizationManager());
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public Advisor postAuthorize() {
		return AuthorizationManagerAfterMethodInterceptor.postAuthorize(new MyPostAuthorizationManager());
	}
}
