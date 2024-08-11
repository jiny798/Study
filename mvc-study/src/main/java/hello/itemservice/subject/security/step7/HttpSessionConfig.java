package hello.itemservice.subject.security.step7;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;

// @Configuration
// @EnableSpringHttpSession // JSESSIONID(WAS에서 관리) -> SESSION(스프링) 으로 변경,
public class HttpSessionConfig {

	@Bean
	public CookieSerializer cookieSerializerCustomizer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setUseSecureCookie(true); // 보안 쿠키 사용 - 스크립트 언어로 접근 불가
		serializer.setUseHttpOnlyCookie(true); // HTTP 통신에만 사용
		serializer.setSameSite("Strict"); // Same Site 강도 설정

		return serializer; // SESSION 쿠키만을 의미
	}

	@Bean
	public SessionRepository<MapSession> sessionRepository(){
		return new MapSessionRepository(new ConcurrentHashMap<>());
	}
}
