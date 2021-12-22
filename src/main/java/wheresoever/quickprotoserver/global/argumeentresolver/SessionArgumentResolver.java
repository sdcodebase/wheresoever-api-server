package wheresoever.quickprotoserver.global.argumeentresolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wheresoever.quickprotoserver.global.constant.HeaderConst;
import wheresoever.quickprotoserver.global.constant.SessionConst;
import wheresoever.quickprotoserver.global.error.exception.SessionNotFoundException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class SessionArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Session.class) && Long.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = httpRequest.getSession(false);

        String authToken = httpRequest.getHeader(HeaderConst.authHeader);
        if (session == null) {
            if (authToken == null) {
                log.error("{} is null", HeaderConst.authHeader);
            } else {
                log.error("{}[{}] is invalid token", HeaderConst.authHeader, authToken);
            }
            throw new SessionNotFoundException();
        }

        Long memberId = (Long) session.getAttribute(SessionConst.SESSION_MEMBER_ID);
        log.info("{}:[{}] --> memberId: [{}]", HeaderConst.authHeader, authToken, memberId);

        return memberId;
    }
}
