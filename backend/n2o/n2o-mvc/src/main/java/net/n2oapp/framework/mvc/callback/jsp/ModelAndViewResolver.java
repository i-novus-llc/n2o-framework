package net.n2oapp.framework.mvc.callback.jsp;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author iryabov
 * @since 24.03.2016
 */
@FunctionalInterface
public interface ModelAndViewResolver {
    ModelAndView resolve(HttpServletRequest request);
}
