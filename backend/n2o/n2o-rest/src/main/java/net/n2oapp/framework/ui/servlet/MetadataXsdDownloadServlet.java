package net.n2oapp.framework.ui.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User: iryabov
 * Date: 16.08.13
 * Time: 15:55
 */
public class MetadataXsdDownloadServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String schemaPath = request.getPathInfo();
        if (schemaPath == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (schemaPath.endsWith(".xsd")) {
            schemaPath = schemaPath.substring(0, schemaPath.indexOf(".xsd"));
        }
        schemaPath = schemaPath + ".xsd";

        String fileType = "application/octet-stream";
        response.setContentType(fileType);
        int idx = schemaPath.lastIndexOf("/");
        String fileName = schemaPath.substring(idx + 1);
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);

        try (OutputStream out = response.getOutputStream()) {
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(
                    "net/n2oapp/framework/config/schema" + schemaPath)) {
                if (in == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }
}
