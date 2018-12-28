package net.n2oapp.framework.mvc.callback.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.context.StaticSpringContext;
import net.n2oapp.framework.api.JsonUtil;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.api.ui.ErrorMessageBuilder;
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
    private ErrorMessageBuilder errorMessageBuilder;

    protected JsonServletCallback() {
        this.objectMapper = JsonUtil.getMapper();
        this.errorMessageBuilder = getBean(ErrorMessageBuilder.class);
    }

    public JsonServletCallback(ObjectMapper objectMapper, ErrorMessageBuilder errorMessageBuilder) {
        this.objectMapper = objectMapper;
        this.errorMessageBuilder = errorMessageBuilder;
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
        return errorMessageBuilder.build(e);
    }


    public abstract Object resolveSuccessModel(HttpServletRequest req, HttpServletResponse res)
            throws ControllerArgumentException, IOException;

    public abstract Object resolveFailModel(N2oException e, HttpServletRequest req, HttpServletResponse res);
}
