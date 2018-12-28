package net.n2oapp.framework.mvc.callback.jsp;

import net.n2oapp.framework.mvc.callback.ServletCallback;
import net.n2oapp.framework.mvc.exception.ControllerArgumentException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author iryabov
 * @since 24.03.2016
 */
public abstract class AbstractJspServletCallback implements ServletCallback {
    private ServletContext servletContext;
    private Type type = Type.FORWARD;
    protected String prefix = "/";
    protected String suffix = ".jsp";

    public enum Type {
        FORWARD, INCLUDE
    }

    protected AbstractJspServletCallback(ServletContext servletContext, Type type) {
        if (servletContext == null)
            throw new IllegalArgumentException("servletContext is null");
        this.servletContext = servletContext;
        if (type == null)
            throw new IllegalArgumentException("type is null");
        this.type = type;
    }

    protected void forward(HttpServletRequest req, HttpServletResponse res, String view, Map<String, Object> model) throws ServletException, IOException {
        RequestDispatcher dispatcher = servletContext.getRequestDispatcher(getSrcPath(view));
        if (dispatcher != null) {
            setAttributes(req, model);
            dispatcher.forward(req, res);
        } else
            throw new IllegalStateException("dispatcher is null");
    }

    protected void include(HttpServletRequest req, HttpServletResponse res, String view, Map<String, Object> model) throws ServletException, IOException {
        RequestDispatcher dispatcher = servletContext.getRequestDispatcher(getSrcPath(view));
        if (dispatcher != null) {
            setAttributes(req, model);
            dispatcher.include(req, res);
        } else
            throw new IllegalStateException("dispatcher is null");
    }

    protected void setAttributes(HttpServletRequest req, Map<String, Object> model) {
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            req.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    protected String getSrcPath(String view) {
        return prefix + view + suffix;
    }


    @Override
    public String getContentType() {
        return "text/html";
    }

    public Type getType() {
        return type;
    }
}
