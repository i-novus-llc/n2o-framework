package net.n2oapp.framework.sandbox.resource;

import net.n2oapp.framework.sandbox.client.SandboxRestClient;
import net.n2oapp.framework.sandbox.resource.model.CategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private SandboxRestClient restClient;
    @Autowired
    private XsdSchemaParser schemaParser;

    @GetMapping("/templates")
    public List<CategoryModel> getProjectTemplates() {
        return restClient.getProjectTemplates();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/project/schemas")
    public ResponseEntity<Resource> loadSchema(@RequestParam(name = "name") String schemaNamespace) throws IOException {
        Resource schema = schemaParser.getSchema(schemaNamespace);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + schema.getFilename() + "\"")
                .body(schema);
    }

}
