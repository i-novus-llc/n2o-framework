package net.n2oapp.framework.ui.servlet.table;

import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.mvc.n2o.N2oServlet;
import net.n2oapp.framework.ui.controller.ExportController;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

        String path = getPath(url, "/n2o/data");

        GetDataResponse result = controller.getData(path,
                getParameters(url),
                (UserContext) req.getAttribute(USER));

        ExportResponse exportResponse = controller.export(result.getList(), format, charset);

        resp.setStatus(exportResponse.getStatus());
        resp.setContentType(exportResponse.getContentType());
        resp.setCharacterEncoding(exportResponse.getCharacterEncoding());
        resp.setHeader("Content-Disposition", exportResponse.getContentDisposition());
        resp.setContentLength(exportResponse.getFile().length);

        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.write(exportResponse.getFile());
    }

    private String getPath(String url, String prefix) {
        String urlWithoutParameters = url.substring(0, url.indexOf("?"));
        String path = urlWithoutParameters.substring(urlWithoutParameters.indexOf(prefix) + prefix.length());

        return RouteUtil.normalize(!path.isEmpty() ? path : "/");
    }

    private Map<String, String[]> getParameters(String url) {
        Map<String, String[]> parameters = new HashMap<>();

        String[] strings = url.substring(url.indexOf("?") + 1).split("&");

        for (String str : strings) {
            String key = str.split("=")[0];
            String value = str.split("=")[1];

            parameters.put(key, new String[] {value});
        }

        return parameters;
    }
}
