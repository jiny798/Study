package hello.itemservice.domain.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import hello.itemservice.domain.exception.resolver.MyHandlerExceptionResolver;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	// configureHandlerExceptionResolvers(..) 를 사용하면 스프링이 기본으로 등록하는
	// ExceptionResolver 가 제거되므로 extendHandlerExceptionResolvers 를 사용
	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		resolvers.add(new MyHandlerExceptionResolver());
	}

}

