package wheresoever.quickprotoserver.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wheresoever.quickprotoserver.api.argumeentresolver.SessionArgumentResolver;

import java.util.List;

@Configuration
public class ApiConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SessionArgumentResolver());
    }
}
