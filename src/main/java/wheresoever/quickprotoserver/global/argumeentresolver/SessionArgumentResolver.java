package wheresoever.quickprotoserver.global.argumeentresolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
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

        Optional<Cookie> sessionCookie = Arrays.stream(httpRequest.getCookies()).filter(cookie -> cookie.getName().equals("SESSION")).findFirst();
        boolean isPresentSessionCookie = sessionCookie.isPresent();

        if (session == null) {
            if (isPresentSessionCookie) {
                log.error("session: {} is invalid", sessionCookie.get().getValue());
            } else {
                log.error("Session Cookies is not exists");
            }
            throw new SessionNotFoundException();
        }

        Long memberId = (Long) session.getAttribute(SessionConst.SESSION_MEMBER_ID);
        log.info("session: {} --> memberId: {}", sessionCookie.get().getValue(), memberId);

        return memberId;
    }
}
