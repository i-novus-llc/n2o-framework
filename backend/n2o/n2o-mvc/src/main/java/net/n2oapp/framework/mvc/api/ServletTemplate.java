package net.n2oapp.framework.mvc.api;

import net.n2oapp.framework.mvc.callback.ServletCallback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Шаблон для обработки сервлетов
 */
public interface ServletTemplate {
    /**
     * Сервис для отлова ошибок, выставления заголовков, статусов и других мелочей при работе сервлетов
     * @param req - HttpRequest
     * @param res - HttpResponse
     * @param callback - обработка тела сервлета
     */
    void doService(HttpServletRequest req, HttpServletResponse res, ServletCallback callback) throws IOException;
}
