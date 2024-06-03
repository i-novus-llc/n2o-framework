package net.n2oapp.framework.mvc.callback;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.mvc.exception.ControllerArgumentException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Обработка вызова в сервлете
 */
public interface ServletCallback {

    void onService(HttpServletRequest req, HttpServletResponse res)
            throws ControllerArgumentException, IOException, ServletException;

    void onError(N2oException e, HttpServletRequest req, HttpServletResponse res) throws IOException;

    /**
     * @return Тип контента http ответа (content-type)
     */
    String getContentType();
}
