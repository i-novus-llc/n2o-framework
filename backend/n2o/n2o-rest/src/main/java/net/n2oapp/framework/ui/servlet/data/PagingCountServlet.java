package net.n2oapp.framework.ui.servlet.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.DataController;

import java.io.IOException;

public class PagingCountServlet extends N2oServlet {
    private final DataController controller;

    public PagingCountServlet(DataController controller,
                              ObjectMapper objectMapper,
                              AlertMessagesConstructor messagesConstructor) {
        super(objectMapper, messagesConstructor);
        this.controller = controller;
    }

    @Override
    protected void safeDoGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        GetDataResponse response = controller.getData(req.getPathInfo(),
                req.getParameterMap(),
                (UserContext) req.getAttribute(USER));
        resp.setStatus(response.getStatus());
        resp.setContentType("application/json");
        resp.getWriter().print(response.getCount());
    }
}