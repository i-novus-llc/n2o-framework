package net.n2oapp.framework.ui.servlet.table;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.export.ExportController;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.Map;

public class ExportServlet extends N2oServlet {

    private final ExportController controller;

    public ExportServlet(ExportController controller) {
        this.controller = controller;
    }

    @Override
    public void safeDoGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String format = req.getParameter("format");
        String charset = req.getParameter("charset");
        String url = req.getParameter("url");

        String path = getPath(url, "/n2o/data");
        Map<String, String[]> params = RouteUtil.parseQueryParams(RouteUtil.parseQuery(url));
        if (params == null)
            throw new N2oException("Query-параметр запроса пустой");

        GetDataResponse dataResponse = controller.getData(path, params, (UserContext) req.getAttribute(USER));
        Map<String, String> headers = controller.getHeaders(path, params);
        ExportResponse exportResponse = controller.export(dataResponse.getList(), format, charset, headers);

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
