package security.demo.security.service;

import security.demo.security.mapper.UrlRoleMapper;

import java.util.Map;

/*
* Map 을 만드는 Mapper 클래스를 사용(위임)해서 권한 정보를 가져오는 클래스
*  Service 가 반드시 필요하지는 않아보임
*/

public class DynamicAuthorizationService {
    private final UrlRoleMapper delegate;
    public DynamicAuthorizationService(UrlRoleMapper delegate) {
        this.delegate = delegate;
    }
    public Map<String, String> getUrlRoleMappings() {
            return delegate.getUrlRoleMappings();
    }
}