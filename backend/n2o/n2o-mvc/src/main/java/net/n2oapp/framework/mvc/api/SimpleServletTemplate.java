package net.n2oapp.framework.mvc.api;

import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.mvc.callback.ServletCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.n2oapp.framework.api.exception.N2oException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Простой шаблон для обработки сервлетов
 */
public class SimpleServletTemplate implements ServletTemplate {
    private Logger log = LoggerFactory.getLogger(SimpleServletTemplate.class);

    @Override
    public void doService(HttpServletRequest req, HttpServletResponse res, ServletCallback callback) throws IOException {
        res.setContentType(callback.getContentType());
        try {
            callback.onService(req, res);
        } catch (Exception e) {
            String url = req.getRequestURI() + "?" + req.getQueryString();
            if (e instanceof N2oException) {
                if (e instanceof N2oUserException) {
                    log.debug(url + " user error:" + e.getLocalizedMessage());
                } else {
                    log.error(url + " system error: " + e.getMessage(), e);
                }
                res.setStatus(((N2oException)e).getHttpStatus());
                callback.onError(((N2oException)e), req, res);
            } else {
                log.error(url + " system error: " + e.getMessage(), e);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                callback.onError(new N2oException(e), req, res);
            }
        } finally {
            res.getWriter().close();
        }
    }
}
