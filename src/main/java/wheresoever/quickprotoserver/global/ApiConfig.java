package wheresoever.quickprotoserver.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wheresoever.quickprotoserver.global.argumeentresolver.SessionArgumentResolver;

import java.util.List;

@Configuration
public class ApiConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SessionArgumentResolver());
    }

    /**
     * Cookie에 있는 SESSION을 이용하는 방식에서 Header에 있는 X-Auth-Token으로 변경
     * HttpSessionIdResolver 역할
     * 1. X-Auth-Token이라는 HTTP Header안에 있는 토큰으로 sessionId를 찾는다
     * 2. 토큰 발급할 때, 헤더에 토큰을 준다.
     */
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        CookieHttpSessionIdResolver resolver = new CookieHttpSessionIdResolver();
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setUseBase64Encoding(false); // 토큰을 encode X
        resolver.setCookieSerializer(cookieSerializer);

        return HeaderHttpSessionIdResolver.xAuthToken();
    }
}
