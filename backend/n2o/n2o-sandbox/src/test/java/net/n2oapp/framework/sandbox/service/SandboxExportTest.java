package net.n2oapp.framework.sandbox.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.JvmProxyConfigurer;
import lombok.SneakyThrows;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.view.SandboxApplicationBuilderConfigurer;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.apache.catalina.util.ParameterMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ViewController.class, SandboxPropertyResolver.class, SandboxRestClientImpl.class, ProjectTemplateHolder.class,
                SandboxTestDataProviderEngine.class, XsdSchemaParser.class, SandboxApplicationBuilderConfigurer.class},
        properties = {"n2o.access.deny_objects=false", "n2o.sandbox.url=http://${n2o.sandbox.api.host}:${n2o.sandbox.api.port}"})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
public class SandboxExportTest {

    private static final MockHttpServletRequest request = new MockHttpServletRequest();
    private static final WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort()
            .enableBrowserProxying(true));

    @Value("${n2o.sandbox.api.host}")
    private String host;

    @Value("${n2o.sandbox.api.port}")
    private Integer port;


    @Autowired
    private ViewController viewController;


    @BeforeAll
    static void setUp() {
        wireMockServer.start();
        JvmProxyConfigurer.configureFor(wireMockServer);
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
        JvmProxyConfigurer.restorePrevious();
    }

    @SneakyThrows
    @Test
    public void export() {
        String expectedBody = "id;name\n" +
                "1;test1\n" +
                "2;test2\n" +
                "3;test3\n" +
                "4;test4\n";

        request.setRequestURI("/sandbox/view/myProjectId/n2o/export/_main");
        request.setParameters(new ParameterMap<>(Map.of(
                "page", new String[]{"1"},
                "size", new String[]{"10"},
                "format", new String[]{"csv"},
                "charset", new String[]{"UTF-8"},
                "url", new String[]{"/n2o/data/_main?main_minPrice=5000&page=1&size=10&sorting.name=DESC"})));
        wireMockServer.stubFor(get(urlMatching("/project/myProjectId")).withHost(equalTo(host)).withPort(port).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
                StreamUtils.copyToString(new ClassPathResource("data/testDataProvider.json").getInputStream(), Charset.defaultCharset()))));
        wireMockServer.stubFor(get("/project/myProjectId/application.properties").withHost(equalTo(host)).withPort(port).willReturn(aResponse()));
        wireMockServer.stubFor(get("/project/myProjectId/user.properties").withHost(equalTo(host)).withPort(port).willReturn(aResponse()));
        wireMockServer.stubFor(get("/project/myProjectId/test.json").withHost(equalTo(host)).withPort(port).willReturn(aResponse().withBody(
                "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"test1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"test2\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 3,\n" +
                "    \"name\": \"test3\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 4,\n" +
                "    \"name\": \"test4\"\n" +
                "  }\n" +
                "]")));

        ResponseEntity<byte[]> response = viewController.export("myProjectId", request);
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(expectedBody.getBytes(StandardCharsets.UTF_8)));
        HttpHeaders headers = response.getHeaders();
        assertThat(headers.getContentDisposition().toString(), is("attachment; filename=\"export.csv\""));

        Optional<MediaType> contentType = Optional.ofNullable(headers.getContentType());
        assertTrue(contentType.isPresent());
        assertThat(contentType.get().toString(), is("text/csv"));

        Optional<List<String>> contentEncoding = Optional.ofNullable(headers.get("Content-Encoding"));
        assertTrue(contentEncoding.isPresent());
        assertThat(contentEncoding.get().toString(), is("[UTF-8]"));

        assertThat(headers.getContentLength(), is(Integer.toUnsignedLong(expectedBody.getBytes(StandardCharsets.UTF_8).length)));
    }
}
