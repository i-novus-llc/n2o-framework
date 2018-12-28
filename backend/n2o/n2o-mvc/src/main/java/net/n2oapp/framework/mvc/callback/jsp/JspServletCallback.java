package net.n2oapp.framework.mvc.callback.jsp;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.mvc.exception.ControllerArgumentException;
import net.n2oapp.framework.mvc.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * User: iryabov
 * Date: 01.10.13
 * Time: 17:24
 */
public class JspServletCallback extends AbstractJspServletCallback {
    private static final Logger logger = LoggerFactory.getLogger(JspServletCallback.class);
    private ModelAndView modelAndView;
    private ModelAndViewResolver supplierModelAndView;

    public JspServletCallback(ServletContext servletContext, Type type, String view, Map<String, Object> model) {
        super(servletContext, type);
        this.modelAndView = new ModelAndView(model, view);
    }

    public JspServletCallback(ServletContext servletContext, String view, Map<String, Object> model) {
        this(servletContext, Type.FORWARD, view, model);
    }


    public JspServletCallback(ServletContext servletContext, Type type, ModelAndViewResolver supplierModelAndView) {
        super(servletContext, type);
        this.supplierModelAndView = supplierModelAndView;
    }

    public JspServletCallback(ServletContext servletContext, ModelAndViewResolver supplierModelAndView) {
        this(servletContext, Type.FORWARD, supplierModelAndView);
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void onService(HttpServletRequest req, HttpServletResponse res)
            throws ControllerArgumentException, IOException, ServletException {
        ModelAndView result = modelAndView != null ? modelAndView : supplierModelAndView.resolve(req);
        if (getType().equals(Type.FORWARD))
            forward(req, res, result.getView(), result.getModel());
        else
            include(req, res, result.getView(), result.getModel());
    }

    @Override
    public void onError(N2oException e, HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        logger.error(e.getMessage(), e);
        if (!res.isCommitted()) {
            req.setAttribute("javax.servlet.error.exception", e);
            res.setContentType("text/html");
            res.sendError(e.getHttpStatus());
        }
    }

}
