package net.n2oapp.framework.mvc.exception;

import net.n2oapp.framework.api.exception.N2oException;

import javax.servlet.http.HttpServletResponse;

/**
 * User: iryabov
 * Date: 10.05.13
 * Time: 17:41
 */
public class ControllerArgumentException extends N2oException {

    public static void throwRequiredParameter(String paramName) throws ControllerArgumentException {
        throw new ControllerArgumentException("need parameter '" + paramName + "'");
    }

    public static void throwUnknownParameter(String paramName) throws ControllerArgumentException {
        throw new ControllerArgumentException("unknown parameter '" + paramName + "'");
    }


    public ControllerArgumentException(String message) {
        super(message);
    }

    @Override
    public int getHttpStatus() {
        return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
    }


}
