package wheresoever.quickprotoserver.api.argumeentresolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wheresoever.quickprotoserver.api.constant.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class SessionArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Session.class) && Long.class.isAssignableFrom(parameter.getParameterType());
    }

    /**
     *  TODO
     *   - session이 없는 경우 throw exception
     *
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = httpRequest.getSession(false);

        if (session == null) {
            return null;
        }
        return session.getAttribute(SessionConst.SESSION_MEMBER_ID);
    }
}
