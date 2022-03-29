package net.n2oapp.framework.sandbox.server;

import lombok.SneakyThrows;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на проверку обработки запросов на получение xsd схем и шаблонов xml
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ViewController.class, SandboxPropertyResolver.class, XsdSchemaParser.class, SandboxRestClientImpl.class})
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
        String templateFile = viewController.getTemplateFile("open.page.xml");
        assertThat(templateFile, is(StreamUtils.copyToString(new ClassPathResource("templates/page.xml").getInputStream(), Charset.defaultCharset())));
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
