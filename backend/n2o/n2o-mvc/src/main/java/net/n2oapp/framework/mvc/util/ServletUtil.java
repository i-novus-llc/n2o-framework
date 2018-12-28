package net.n2oapp.framework.mvc.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.Collections.emptyMap;

/**
 * @author iryabov
 * @since 24.03.2016
 */
public class ServletUtil {

    public static Map<String, String> decodeParameters(HttpServletRequest request) {
        try {
            if (request.getQueryString() == null)
                return emptyMap();

            Map<String, String> map = new LinkedHashMap<>();
            String[] parameters = request.getQueryString().split("&");
            for (String parameter : parameters) {
                String[] keyValue = parameter.split("=");
                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                if (keyValue.length == 2) {
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    map.put(key, value);
                } else if (keyValue.length == 1) {
                    map.put(key, null);
                }
            }
            return map;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> filterParameters(Map<String, String> parameters, Predicate<String> filter) {
        Map<String, String> result = new LinkedHashMap<>(parameters);
        parameters.keySet().stream().filter(filter.negate()).forEach(result::remove);
        return result;
    }
}
