package net.n2oapp.framework.sandbox.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.JvmProxyConfigurer;
import lombok.SneakyThrows;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест получения и установки значений провайдером данных
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ViewController.class, SandboxPropertyResolver.class, SandboxRestClientImpl.class,
                SandboxTestDataProviderEngine.class, XsdSchemaParser.class, SandboxApplicationBuilderConfigurer.class},
        properties = {"n2o.access.deny_objects=false", "n2o.sandbox.url=http://${n2o.sandbox.api.host}:${n2o.sandbox.api.port}"})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
public class SandboxDataProviderTest {

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
    public void testGetData() {
        request.setRequestURI("/sandbox/view/myProjectId/n2o/data/_main");
        request.setParameters(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
        wireMockServer.stubFor(get(urlMatching("/api/project/myProjectId")).withHost(equalTo(host)).withPort(port).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
                StreamUtils.copyToString(new ClassPathResource("data/testDataProvider.json").getInputStream(), Charset.defaultCharset()))));
        wireMockServer.stubFor(get("/api/project/myProjectId/application.properties").withHost(equalTo(host)).withPort(port).willReturn(aResponse()));
        wireMockServer.stubFor(get("/api/project/myProjectId/user.properties").withHost(equalTo(host)).withPort(port).willReturn(aResponse()));
        wireMockServer.stubFor(get("/api/project/myProjectId/test.json").withHost(equalTo(host)).withPort(port).willReturn(aResponse().withBody("[\n" +
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

        ResponseEntity<GetDataResponse> response = viewController.getData("myProjectId", request);
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody().getCount(), is(4));
        assertThat(response.getBody().getList().get(0).get("id"), is(1L));
        assertThat(response.getBody().getList().get(1).get("id"), is(2L));
        assertThat(response.getBody().getList().get(2).get("id"), is(3L));
        assertThat(response.getBody().getList().get(3).get("id"), is(4L));
        assertThat(response.getBody().getList().get(0).get("name"), is("test1"));
        assertThat(response.getBody().getList().get(1).get("name"), is("test2"));
        assertThat(response.getBody().getList().get(2).get("name"), is("test3"));
        assertThat(response.getBody().getList().get(3).get("name"), is("test4"));

    }

    @SneakyThrows
    @Test
    public void testSetData() {
        request.setRequestURI("/sandbox/view/myProjectId/n2o/data/main/3/update/submit");
        request.setParameters(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
        wireMockServer.stubFor(get("/api/project/myProjectId").willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
                StreamUtils.copyToString(new ClassPathResource("data/testDataProvider.json").getInputStream(), Charset.defaultCharset()))));
        wireMockServer.stubFor(put("/api/project/myProjectId").withHost(equalTo(host)).withPort(port).willReturn(aResponse().withStatus(200)));
        wireMockServer.stubFor(get("/api/project/myProjectId/application.properties").withHost(equalTo(host)).withPort(port).willReturn(aResponse()));
        wireMockServer.stubFor(get("/api/project/myProjectId/user.properties").withHost(equalTo(host)).withPort(port).willReturn(aResponse()));
        wireMockServer.stubFor(get("/api/project/myProjectId/test.json").withHost(equalTo(host)).withPort(port).willReturn(aResponse().withBody("[\n" +
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

        ResponseEntity<SetDataResponse> response = viewController.setData("myProjectId", new LinkedHashMap<>(Map.of("name", "name3", "id", 3)), request);
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody().getData().get("id"), is(3));
        assertThat(response.getBody().getData().get("name"), is("name3"));
        assertThat(response.getBody().getMeta().getAlert().getMessages().get(0).getText(), is("Данные сохранены"));
    }
}
