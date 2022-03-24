package net.n2oapp.framework.sandbox.server;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.resource.ResourceController;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.resource.model.CategoryModel;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на проверку обработки запросов на получение xsd схем и шаблонов xml
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ResourceController.class, SandboxPropertyResolver.class, SandboxRestClientImpl.class,
                XsdSchemaParser.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
public class SandboxResourceRetrievalTest {

    private static final WireMockServer wireMockServer = new WireMockServer();

    @Autowired
    private ResourceController resourceController;
    @Autowired
    private XsdSchemaParser schemaParser;

    @BeforeAll
    static void setUp() {
        wireMockServer.start();
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @SneakyThrows
    @Test
    public void testTemplatesRetrieval() {
        stubFor(get(urlMatching("/sandbox/api/project/templates")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
                StreamUtils.copyToString(new ClassPathResource("data/testTemplatesRetrieval.json").getInputStream(), Charset.defaultCharset()))));
        List<CategoryModel> projectTemplates = resourceController.getProjectTemplates();
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
        resourceController.loadSchema("search-1.0");
        ResponseEntity<Resource> responseSchema = resourceController.loadSchema("search-1.0");
        assertThat(responseSchema.getStatusCodeValue(), is(200));
        assertThat(responseSchema.getHeaders().get("Content-Type").get(0), is("application/octet-stream"));
        assertThat(responseSchema.getBody(), is(schemaParser.getSchema("search-1.0")));
    }
}
