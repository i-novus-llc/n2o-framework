package net.n2oapp.framework.mvc.callback;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.mvc.exception.ControllerArgumentException;
import net.n2oapp.framework.mvc.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;


/**
 * @author iryabov
 * @since 24.03.2016
 */
public interface SimpleServletCallback extends ServletCallback {

    void onService(Map<String, String> params, PrintWriter out)
            throws ControllerArgumentException, IOException, ServletException;

    void onError(N2oException e, Map<String, String> params, PrintWriter out) throws IOException;

    default void onService(HttpServletRequest req, HttpServletResponse res)
            throws ControllerArgumentException, IOException, ServletException {
        onService(ServletUtil.decodeParameters(req), res.getWriter());
    }

    default void onError(N2oException e, HttpServletRequest req, HttpServletResponse res) throws IOException {
        onError(e, ServletUtil.decodeParameters(req), res.getWriter());
    }
}
