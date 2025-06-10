package net.n2oapp.framework.sandbox.service;

import lombok.SneakyThrows;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на проверку обработки запросов на получение xsd схем и шаблонов xml
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SandboxTestApplication.class, ViewController.class, SandboxPropertyResolver.class, XsdSchemaParser.class,
                ProjectTemplateHolder.class})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
class SandboxResourceRetrievalTest {

    @Autowired
    private ViewController viewController;
    @Autowired
    private XsdSchemaParser schemaParser;
    @MockBean
    private FileStorage fileStorage;

    @SneakyThrows
    @Test
    void testTemplatesRetrieval() {
        String templateFile = viewController.getTemplateFile("open.page.xml");
        assertThat(templateFile, is(StreamUtils.copyToString(new ClassPathResource("templates/page.xml").getInputStream(), Charset.defaultCharset())));
    }

    @SneakyThrows
    @Test
    void testSchemasRetrieval() {
        viewController.loadSchema("base-1.0");
        ResponseEntity<Resource> responseSchema = viewController.loadSchema("base-1.0");
        assertThat(responseSchema.getStatusCode().value(), is(200));
        assertThat(responseSchema.getHeaders().get("Content-Type").get(0), is("application/octet-stream"));
        assertThat(responseSchema.getBody(), is(schemaParser.getSchema("base-1.0")));
    }
}
