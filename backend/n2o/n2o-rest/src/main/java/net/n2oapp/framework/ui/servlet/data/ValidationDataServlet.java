package net.n2oapp.framework.ui.servlet.data;


import net.n2oapp.framework.api.rest.ValidationDataResponse;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.DataController;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Универсальный сервлет проверки данных
 */
public class ValidationDataServlet extends N2oServlet {

    private DataController controller;

    public ValidationDataServlet(DataController controller) {
        this.controller = controller;
    }

    @Override
    protected void safeDoPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        ValidationDataResponse result = controller.validateData(req.getPathInfo(), getRequestBody(req));
        res.setStatus(result.getStatus());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(res.getWriter(), result);
    }

}
