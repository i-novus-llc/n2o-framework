package net.n2oapp.framework.ui.context;

import net.n2oapp.framework.api.context.ContextEngine;
import net.n2oapp.framework.api.user.UserContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Хранение значений контекста в http сессии пользователя
 */
public class SessionContextEngine implements ContextEngine {

    @Override
    public Object get(String name) {
        HttpSession session = getHttpSession();
        if (UserContext.SESSION.equals(name)) {
            return session.getId();
        }
        return session.getAttribute(name);
    }

    @Override
    public void set(Map<String, Object> dataSet) {
        final HttpSession httpSession = getHttpSession();
        dataSet.forEach(httpSession::setAttribute);
    }

    @Override
    public Object get(String param, Map<String, Object> baseParams) {
        if (baseParams.containsKey(param))
            return baseParams.get(param);
        return get(param);
    }

    @Override
    public void set(Map<String, Object> dataSet, Map<String, Object> baseParams) {
        Map<String, Object> result = new HashMap<>(baseParams);
        result.putAll(dataSet);
        set(result);
    }

    private HttpSession getHttpSession() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof NativeWebRequest) {
            HttpServletRequest request = (HttpServletRequest) ((NativeWebRequest) attrs).getNativeRequest();
            return request.getSession();
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest().getSession();
    }
}
