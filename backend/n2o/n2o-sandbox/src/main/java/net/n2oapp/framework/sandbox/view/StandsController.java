package net.n2oapp.framework.sandbox.view;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@RestController
public class StandsController {

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/stands", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getStands() throws IOException {
        ClassPathResource resource = new ClassPathResource("stands.json");
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
