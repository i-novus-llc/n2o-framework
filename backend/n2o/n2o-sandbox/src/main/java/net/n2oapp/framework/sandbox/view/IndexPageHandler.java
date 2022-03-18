package net.n2oapp.framework.sandbox.view;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Процессинг index.html
 */
@Controller
public class IndexPageHandler {

    @Value("${server.servlet.context-path}")
    private String servletContext;

    private static final String SERVICE_WORKER_JS = "serviceWorker.js";
    private static final String VIEW_INDEX_HTML = "META-INF/resources/index.html";

    @CrossOrigin(origins = "*")
    @GetMapping("/view/{projectId}/")
    public ResponseEntity<Resource> getIndex(@PathVariable(value = "projectId") String projectId) {
//        if (api.isProjectNotExists(projectId))
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project " + projectId + " not found");

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .cacheControl(CacheControl.noCache())
                .body(processHtml());
    }

    private ByteArrayResource processHtml() {
        try (InputStream io = new ClassPathResource(VIEW_INDEX_HTML).getInputStream()) {
            String html = IOUtils.toString(io, StandardCharsets.UTF_8);

            String rp = findReplaceablePath(html);

            html = html.replace(rp + "static/", servletContext + "/static/")
                    .replace(rp + "favicon.ico", servletContext + "/favicon.ico")
                    .replace(rp + "logo192.png", servletContext + "/logo192.png")
                    .replace(rp + "manifest.json", servletContext + "/manifest.json")
                    .replace(rp + SERVICE_WORKER_JS, servletContext + "/" + SERVICE_WORKER_JS);
            return new ByteArrayResource(html.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String findReplaceablePath(String html) {
        int swj = html.indexOf(SERVICE_WORKER_JS);
        int sw2 = html.lastIndexOf("\"", swj);
        return swj != -1 ? html.substring(sw2 + 1, swj) : html;
    }

}
