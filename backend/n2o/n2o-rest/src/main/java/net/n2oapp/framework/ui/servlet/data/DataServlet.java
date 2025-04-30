package net.n2oapp.framework.ui.servlet.data;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.DataController;

import java.io.IOException;

/**
 * Универсальный сервлет обработки данных в json
 */
public class DataServlet extends N2oServlet {
    private DataController controller;

    public DataServlet(DataController controller) {
        this.controller = controller;
    }

    @Override
    protected void safeDoGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        GetDataResponse result = controller.getData(req.getPathInfo(),
                req.getParameterMap(),
                (UserContext) req.getAttribute(USER));
        res.setStatus(result.getStatus());
        res.setContentType("application/json");
        objectMapper.writeValue(res.getWriter(), result);

    }

    @Override
    protected void safeDoPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        SetDataResponse result = controller.setData(req.getPathInfo(),
                req.getParameterMap(),
                getHeaders(req),
                getRequestBody(req),
                (UserContext) req.getAttribute(USER));
        res.setStatus(result.getStatus());
        res.setContentType("application/json");
        objectMapper.writeValue(res.getWriter(), result);
    }

    @Override
    protected void safeDoPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        safeDoPost(req, resp);
    }

    @Override
    protected void safeDoDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        safeDoPost(req, resp);
    }

}
