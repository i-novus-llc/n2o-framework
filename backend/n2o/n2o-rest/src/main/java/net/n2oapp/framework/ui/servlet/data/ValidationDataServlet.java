package net.n2oapp.framework.ui.servlet.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.n2oapp.framework.api.rest.ValidationDataResponse;
import net.n2oapp.framework.api.ui.AlertMessagesConstructor;
import net.n2oapp.framework.mvc.cache.ClientCacheTemplate;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.DataController;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * Универсальный сервлет проверки данных
 */
public class ValidationDataServlet extends N2oServlet {
    private final DataController controller;

    public ValidationDataServlet(DataController controller,
                                 ObjectMapper objectMapper,
                                 AlertMessagesConstructor messagesConstructor) {
        super(objectMapper, messagesConstructor);
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