package net.n2oapp.framework.sandbox.server;

import lombok.SneakyThrows;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.resource.TemplatesHolder;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.resource.model.CategoryModel;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на проверку обработки запросов на получение xsd схем и шаблонов xml
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ViewController.class, SandboxPropertyResolver.class, TemplatesHolder.class,
                XsdSchemaParser.class, SandboxRestClientImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
public class SandboxResourceRetrievalTest {

    @Autowired
    private ViewController viewController;
    @Autowired
    private XsdSchemaParser schemaParser;

    @SneakyThrows
    @Test
    public void testTemplatesRetrieval() {
        List<CategoryModel> projectTemplates = viewController.getProjectTemplates();
        assertThat(projectTemplates.size(), is(7));
        assertThat(projectTemplates.get(0).getName(), is("Примеры"));
        assertThat(projectTemplates.get(0).getSections().size(), is(1));
        assertThat(projectTemplates.get(0).getSections().get(0).getTemplates().size(), is(24));
        assertThat(projectTemplates.get(0).getSections().get(0).getTemplates().get(0).getProjectId(), is("examples_hello_world"));
        assertThat(projectTemplates.get(0).getSections().get(0).getTemplates().get(0).getTemplateId(), is("examples/hello_world"));
        assertThat(projectTemplates.get(0).getSections().get(0).getTemplates().get(0).getName(), is("Привет мир!"));
    }

    @SneakyThrows
    @Test
    public void testSchemasRetrieval() {
        viewController.loadSchema("search-1.0");
        ResponseEntity<Resource> responseSchema = viewController.loadSchema("search-1.0");
        assertThat(responseSchema.getStatusCodeValue(), is(200));
        assertThat(responseSchema.getHeaders().get("Content-Type").get(0), is("application/octet-stream"));
        assertThat(responseSchema.getBody(), is(schemaParser.getSchema("search-1.0")));
    }
}
