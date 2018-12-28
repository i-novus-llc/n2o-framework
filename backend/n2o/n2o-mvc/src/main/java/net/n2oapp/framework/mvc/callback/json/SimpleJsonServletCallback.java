package net.n2oapp.framework.mvc.callback.json;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.mvc.exception.ControllerArgumentException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Простой обработчик возврата json в сервлете
 */
public class SimpleJsonServletCallback extends JsonServletCallback {
    private Object data;
    private DataResolver dataResolver;

    /**
     * @param data данные для преобразования в json
     */
    public SimpleJsonServletCallback(Object data) {
        this.data = data;
    }

    /**
     * @param dataResolver функция для отложенного получения данных для преобразования в json
     */
    public SimpleJsonServletCallback(DataResolver dataResolver) {
        this.dataResolver = dataResolver;
    }

    @Override
    public Object resolveFailModel(N2oException e, HttpServletRequest req, HttpServletResponse res) {
        return createResponseMsg(e);
    }


    @Override
    public Object resolveSuccessModel(HttpServletRequest req, HttpServletResponse res)
            throws ControllerArgumentException, IOException {
        return data != null ? data : dataResolver.apply(req);
    }
}
