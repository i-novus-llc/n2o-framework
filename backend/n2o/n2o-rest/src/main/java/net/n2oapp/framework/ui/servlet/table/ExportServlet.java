package net.n2oapp.framework.ui.servlet.table;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.rest.ExportRequest;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.export.ExportController;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.Map;

import static net.n2oapp.framework.ui.utils.UrlUtil.resolveAbsoluteUrl;

public class ExportServlet extends N2oServlet {

    private final ExportController controller;

    public ExportServlet(ExportController controller) {
        this.controller = controller;
    }

    @Override
    public void safeDoPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ExportRequest exportRequest = objectMapper.readValue(req.getInputStream(), ExportRequest.class);

        String path = getPath(exportRequest.getUrl(), "/n2o/data");
        Map<String, String[]> params = RouteUtil.parseQueryParams(RouteUtil.parseQuery(exportRequest.getUrl()));
        if (params == null)
            throw new N2oException("Query-параметр запроса пустой");

        if (exportRequest.getExternalUrl() == null || exportRequest.getExternalUrl().isEmpty()) {
            GetDataResponse dataResponse = controller.getData(path, params, (UserContext) req.getAttribute(USER));
            ExportResponse exportResponse = controller.export(dataResponse.getList(), exportRequest.getFormat(),
                    exportRequest.getCharset(), exportRequest.getFields(), exportRequest.getFilename());
            resp.setStatus(exportResponse.getStatus());
            resp.setContentType(exportResponse.getContentType());
            resp.setCharacterEncoding(exportResponse.getCharacterEncoding());
            resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, exportResponse.getContentDisposition());
            resp.setContentLength(exportResponse.getFile().length);

            ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write(exportResponse.getFile());
        } else {
            exportRequest.setExternalUrl(resolveAbsoluteUrl(exportRequest.getExternalUrl(), req));
            controller.exportByExternalService(exportRequest, resp.getOutputStream(), metadata -> {
                resp.setStatus(metadata.getStatus());
                resp.setContentType(metadata.getContentType());
                resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, metadata.getContentDisposition());
                if (metadata.getContentLength() > 0) {
                    resp.setContentLength(metadata.getContentLength());
                }
            });
        }
    }

    private String getPath(String url, String prefix) {
        return RouteUtil.parsePath(url.substring(url.indexOf(prefix) + prefix.length()));
    }
}
