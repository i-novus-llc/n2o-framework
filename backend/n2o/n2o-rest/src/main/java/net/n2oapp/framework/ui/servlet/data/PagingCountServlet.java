package net.n2oapp.framework.ui.servlet.data;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.DataController;

import java.io.IOException;

public class PagingCountServlet extends N2oServlet {

    private final DataController controller;

    public PagingCountServlet(DataController controller) {
        this.controller = controller;
    }

    @Override
    protected void safeDoGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer result = controller.getCount(req.getPathInfo(),
                req.getParameterMap(),
                (UserContext) req.getAttribute(USER));
        resp.setContentType("application/json");
        resp.getWriter().print(result);
    }
}
