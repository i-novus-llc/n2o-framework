package net.n2oapp.framework.engine.rest;

import net.n2oapp.framework.api.exception.N2oExceptionUtil;

import java.util.List;
import java.util.Map;

/**
 * Ошибка выполнения REST запроса
 */
public class N2oRestException extends N2oExceptionWithForeignStackTrace {
    private String url;
    private String method;
    private Map<String, Object> args;
    private String response;

    public N2oRestException() {
        super();
    }

    public N2oRestException(Throwable cause) {
        super(cause);
    }

    public N2oRestException(String stackTrace, int httpStatus) {
        super(stackTrace);
        setHttpStatus(httpStatus);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getArgs() {
        return N2oExceptionUtil.parametersToList(args);
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
