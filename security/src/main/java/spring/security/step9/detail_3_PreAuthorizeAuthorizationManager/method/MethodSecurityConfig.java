package spring.security.step9.detail_3_PreAuthorizeAuthorizationManager.method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.method.AuthorizationManagerAfterMethodInterceptor;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity(prePostEnabled = false) // 직접 만든 클래스만 작동하도록
@Configuration
public class MethodSecurityConfig {

	// @Bean
	// @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	// public Advisor preAuthorize() {
	// 	return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(new MyPreAuthorizationManager());
	// }
	//
	// @Bean
	// @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	// public Advisor postAuthorize() {
	// 	return AuthorizationManagerAfterMethodInterceptor.postAuthorize(new MyPostAuthorizationManager());
	// }

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public Advisor pointCutAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution(* hello.itemservice.subject.security.step9.detail_3_PreAuthorizeAuthorizationManager.MethodDataService"
			+ ".getUser(..))");

		AuthorityAuthorizationManager<MethodInvocation> manager = AuthorityAuthorizationManager.hasRole("USER");

		return new AuthorizationManagerBeforeMethodInterceptor(pointcut, manager);
	}

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public Advisor pointCutAdvisor2() {
		AspectJExpressionPointcut pointcut1 = new AspectJExpressionPointcut();
		pointcut1.setExpression("execution(* hello.itemservice.subject.security.step9.detail_3_PreAuthorizeAuthorizationManager.MethodDataService"
			+ ".getUser(..))");

		AspectJExpressionPointcut pointcut2 = new AspectJExpressionPointcut();
		pointcut2.setExpression("execution(* hello.itemservice.subject.security.step9.detail_3_PreAuthorizeAuthorizationManager.MethodDataService"
			+ ".getOwner(..))");

		ComposablePointcut composablePointcut = new ComposablePointcut((Pointcut)pointcut1);
		composablePointcut.union((Pointcut)pointcut2);

		AuthorityAuthorizationManager<MethodInvocation> manager = AuthorityAuthorizationManager.hasRole("USER");

		return new AuthorizationManagerBeforeMethodInterceptor(composablePointcut, manager);
	}
}
