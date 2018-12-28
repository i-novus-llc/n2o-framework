package net.n2oapp.framework.export;

import net.n2oapp.framework.mvc.n2o.N2oServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет экспорта данных таблицы
 */
public class ExportDataServlet extends N2oServlet {

    private ExportController controller;

    public ExportDataServlet(ExportController controller) {
        this.controller = controller;
    }

    @Override
    protected void safeDoGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        controller.export(req, res);
    }


}
