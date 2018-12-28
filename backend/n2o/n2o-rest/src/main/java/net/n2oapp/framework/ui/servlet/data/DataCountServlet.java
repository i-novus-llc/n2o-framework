package net.n2oapp.framework.ui.servlet.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.count.CountController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Сервлет получающий количество записей
 */
public class DataCountServlet extends N2oServlet {

    private CountController controller;
    private ObjectMapper objectMapper = new ObjectMapper();

    public DataCountServlet(CountController controller) {
        this.controller = controller;
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void safeDoGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        Map<String, Integer> result = Collections.singletonMap("count", controller.getCount(req, res));
        res.setContentType("application/json");
        objectMapper.writeValue(res.getWriter(), result);
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
