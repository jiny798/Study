package security.demo.security.manager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import security.demo.admin.repository.ResourcesRepository;
import security.demo.security.mapper.MapBasedUrlRoleMapper;
import security.demo.security.mapper.PersistentUrlRoleMapper;
import security.demo.security.service.DynamicAuthorizationService;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomDynamicAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    // 각 엔드포인트(Matcher), 권한(entry > Manager) 을 가지고 있음
    List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;

    // 현재 요청이 mappings 조건에 맞지 않아서 바로 return 하기 위해 생성
    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);
    private static final AuthorizationDecision ACCESS = new AuthorizationDecision(true);

    private final HandlerMappingIntrospector handlerMappingIntrospector;

    private final ResourcesRepository resourcesRepository;

    @PostConstruct
    public void mapping() {
        DynamicAuthorizationService dynamicAuthorizationService = new DynamicAuthorizationService(new PersistentUrlRoleMapper(resourcesRepository)); // new MapBasedUrlRoleMapper()
        mappings = dynamicAuthorizationService.getUrlRoleMappings() // 서비스를 통해 Map객체를 반환 받고 
                .entrySet().stream()
                .map(entry -> new RequestMatcherEntry<>( // Map에 하나씩 꺼내서 순회
                        new MvcRequestMatcher(handlerMappingIntrospector, entry.getKey()), // 앤드포인트 URL
                        customAuthorizationManager(entry.getValue()))) // 권한 정보
                .collect(Collectors.toList());
    }
    
    // 권한 체크
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext request) {

        for (RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : this.mappings) {

            RequestMatcher matcher = mapping.getRequestMatcher();
            RequestMatcher.MatchResult matchResult = matcher.matcher(request.getRequest());

            if (matchResult.isMatch()) {
                AuthorizationManager<RequestAuthorizationContext> manager = mapping.getEntry();
                return manager.check(authentication,
                        new RequestAuthorizationContext(request.getRequest(), matchResult.getVariables()));
            }
        }
        return ACCESS;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        AuthorizationManager.super.verify(authentication, object);
    }

    // 권한을 처리하는 매니저 생성
    private AuthorizationManager<RequestAuthorizationContext> customAuthorizationManager(String role) {
        if (role.startsWith("ROLE")) {
            // ROLE 로 시작하면 AuthorityAuthorizationManager 에서 처리
            return AuthorityAuthorizationManager.hasAuthority(role);
        }else{
            // 그외에는 표현식으로 간주 - permitAll 같은 것을 처리하는 Manager
            return new WebExpressionAuthorizationManager(role);
        }
    }
}