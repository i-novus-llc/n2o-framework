package net.n2oapp.framework.mvc.callback.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.JsonUtil;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.mvc.callback.ServletCallback;
import net.n2oapp.framework.mvc.exception.ControllerArgumentException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.n2oapp.context.StaticSpringContext.getBean;

/**
 * Абстрактный обработчик возврата json в сервлете
 */
public abstract class JsonServletCallback implements ServletCallback {
    protected ObjectMapper objectMapper;
    private AlertMessageBuilder messageBuilder;

    protected JsonServletCallback() {
        this.objectMapper = JsonUtil.getMapper();
        this.messageBuilder = getBean(AlertMessageBuilder.class);
    }

    public JsonServletCallback(ObjectMapper objectMapper, AlertMessageBuilder messageBuilder) {
        this.objectMapper = objectMapper;
        this.messageBuilder = messageBuilder;
    }

    @Override
    public void onService(HttpServletRequest req, HttpServletResponse res)
            throws ControllerArgumentException, IOException {
        getObjectMapper().writeValue(res.getWriter(), resolveSuccessModel(req, res));
    }


    @Override
    public void onError(N2oException e, HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        if (!res.isCommitted()) {
            res.setStatus(e.getHttpStatus());
            getObjectMapper().writeValue(res.getWriter(), resolveFailModel(e, req, res));
        }
    }

    protected void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private ObjectMapper getObjectMapper() {
        return objectMapper;
    }



    @Override
    public String getContentType() {
        return "application/json";
    }

    protected ResponseMessage createResponseMsg(N2oException e) {
        return messageBuilder.build(e);
    }


    public abstract Object resolveSuccessModel(HttpServletRequest req, HttpServletResponse res)
            throws ControllerArgumentException, IOException;

    public abstract Object resolveFailModel(N2oException e, HttpServletRequest req, HttpServletResponse res);
}
