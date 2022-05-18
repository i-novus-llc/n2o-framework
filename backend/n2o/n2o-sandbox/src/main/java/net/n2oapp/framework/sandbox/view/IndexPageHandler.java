package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${server.servlet.context-path:/}")
    private String servletContext;
    @Autowired
    private SandboxRestClient restClient;

    private static final String RELATIVE_PATH = "./";
    private static final String HREF_PATTERN = "href=\"";
    private static final String SRC_PATTERN = "src=\"";
    private static final String VIEW_INDEX_HTML = "META-INF/resources/index.html";

    @CrossOrigin(origins = "*")
    @GetMapping("/view/{projectId}/")
    public ResponseEntity<Resource> getIndex(@PathVariable(value = "projectId") String projectId) {
        if (!restClient.isProjectExists(projectId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project " + projectId + " not found");

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .cacheControl(CacheControl.noCache())
                .body(processHtml());
    }

    private ByteArrayResource processHtml() {
        try (InputStream io = new ClassPathResource(VIEW_INDEX_HTML).getInputStream()) {
            String html = IOUtils.toString(io, StandardCharsets.UTF_8);
            String rp = RELATIVE_PATH;
            if (!servletContext.endsWith("/"))
                servletContext += "/";
            html = html.replace(HREF_PATTERN + rp, HREF_PATTERN + servletContext)
                    .replace(SRC_PATTERN + rp, SRC_PATTERN + servletContext);
            return new ByteArrayResource(html.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
