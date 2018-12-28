package net.n2oapp.framework.ui.servlet.data;


import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.DataController;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Универсальный сервлет обработки данных в json
 */
public class DataServlet extends N2oServlet {
    private DataController controller;

    public DataServlet(DataController controller) {
        this.controller = controller;
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void safeDoGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        GetDataResponse result = controller.getData(req.getPathInfo(),
                req.getParameterMap(),
                (UserContext) req.getAttribute(USER));
        res.setContentType("application/json");
        objectMapper.writeValue(res.getWriter(), result);

    }

    @Override
    protected void safeDoPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        SetDataResponse result = controller.setData(req.getPathInfo(),
                req.getParameterMap(),
                getRequestBody(req),
                (UserContext) req.getAttribute(USER));
        res.setContentType("application/json");
        objectMapper.writeValue(res.getWriter(), result);
    }

    private Object getRequestBody(HttpServletRequest request) {
        try {
            if (request.getReader() == null) return new DataSet();
            String body = IOUtils.toString(request.getReader()).trim();
            if (body.startsWith("[")) {
                return objectMapper.<List<DataSet>>readValue(body,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, DataSet.class)
                );
            } else {
                return objectMapper.readValue(body, DataSet.class);
            }
        } catch (IOException e) {
            throw new N2oException(e);
        }
    }

}
