package net.n2oapp.framework.mvc.callback.json;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

/**
 * Функция для отложенного получения данных для json
 */
@FunctionalInterface
public interface DataResolver extends Function<HttpServletRequest, Object> {
}
