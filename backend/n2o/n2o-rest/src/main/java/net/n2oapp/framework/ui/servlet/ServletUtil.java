package net.n2oapp.framework.ui.servlet;

import net.n2oapp.criteria.api.Direction;
import net.n2oapp.framework.mvc.exception.ControllerArgumentException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Утилиты для работы в сервлетах
 */
public abstract class ServletUtil {

    public static String normalizePath(HttpServletRequest req) {
        String path = req.getPathInfo();
        if (path == null) {
            path = req.getRequestURI();
        }
        if (!path.startsWith("/")) path = "/" + path;
        if (path.endsWith("/") && (path.length() > 1)) path = path.substring(0, path.length() - 1);
        return path;
    }

    public static boolean isAjax(HttpServletRequest servletRequest) {
        String header = servletRequest.getHeader("X-Requested-With");
        return header != null && header.equalsIgnoreCase("XMLHttpRequest");
    }

    public static String getRequestParam(HttpServletRequest request, String paramName) {
        String parameter = request.getParameter(paramName);
        if (parameter != null && !parameter.isEmpty()) {
            return parameter;
        } else
            return null;
    }

    public static String getRequestParamRequired(HttpServletRequest request, String paramName) {
        String parameter = request.getParameter(paramName);
        if (parameter != null && !parameter.isEmpty()) {
            return parameter;
        } else
            throw new ControllerArgumentException("unknown parameter '" + paramName + "'");
    }

    public static Integer getRequestParamInteger(HttpServletRequest request, String paramName) {
        String parameter = getRequestParam(request, paramName);
        return parameter != null ? Integer.valueOf(parameter) : null;
    }

    public static int getRequestParamInt(HttpServletRequest request, String paramName) {
        String parameter = getRequestParam(request, paramName);
        return parameter != null ? Integer.valueOf(parameter) : 0;
    }

    public static Map<String, String> getRequestParamsWithPrefix(HttpServletRequest request, String paramPrefix) {
        Map<String, String> values = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if (entry.getKey().startsWith(paramPrefix)) {
                String parameter = entry.getKey().substring(paramPrefix.length());
                String value = entry.getValue() != null && entry.getValue().length > 0 && entry.getValue()[0] != null
                        ? entry.getValue()[0] : null;
                values.put(parameter, value);
            }
        }
        return values;
    }
}
