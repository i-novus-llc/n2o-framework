package net.n2oapp.framework.ui.servlet.table;

import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.ExportController;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ExportServlet extends N2oServlet {

    private final ExportController controller;

    public ExportServlet(ExportController controller) {
        this.controller = controller;
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    public void safeDoGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String format = req.getParameter("format");
        String charset = req.getParameter("charset");
        String url = req.getParameter("url");

        GetDataResponse result = controller.getData(
                getPath(url, "/n2o/data"),
                RouteUtil.parseQueryParams(RouteUtil.parseQuery(url)),
                (UserContext) req.getAttribute(USER));

        ExportResponse exportResponse = controller.export(result.getList(), format, charset);

        resp.setStatus(exportResponse.getStatus());
        resp.setContentType(exportResponse.getContentType());
        resp.setCharacterEncoding(exportResponse.getCharacterEncoding());
        resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, exportResponse.getContentDisposition());
        resp.setContentLength(exportResponse.getFile().length);

        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.write(exportResponse.getFile());
    }

    private String getPath(String url, String prefix) {
        return RouteUtil.parsePath(url.substring(url.indexOf(prefix) + prefix.length()));
    }
}
