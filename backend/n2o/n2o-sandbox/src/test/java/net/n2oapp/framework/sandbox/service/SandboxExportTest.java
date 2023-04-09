package net.n2oapp.framework.sandbox.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.JvmProxyConfigurer;
import lombok.SneakyThrows;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.view.SandboxApplicationBuilderConfigurer;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import net.n2oapp.framework.ui.controller.DataController;
import org.apache.catalina.util.ParameterMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

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

    @MockBean
    private DataController dataController;

    @Mock
    private GetDataResponse dataResponse;

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
        String exp = "id;name;list\n" +
                "1;test1;[1, 2, 3]\n" +
                "2;test2;[1, 2, 3]\n" +
                "3;test3;[1, 2, 3]\n";

        DataSet body1 = new DataSet();
        DataSet body2 = new DataSet();
        DataSet body3 = new DataSet();

        body1.put("id", "1");
        body1.put("name", "test1");
        body1.put("list", Arrays.asList(1, 2, 3));

        body2.put("id", "2");
        body2.put("name", "test2");
        body2.put("list", Arrays.asList(1, 2, 3));

        body3.put("id", "3");
        body3.put("name", "test3");
        body3.put("list", Arrays.asList(1, 2, 3));

        List<DataSet> list = new ArrayList<>();
        list.add(body1);
        list.add(body2);
        list.add(body3);

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
        wireMockServer.stubFor(get("/project/myProjectId/test.json").withHost(equalTo(host)).withPort(port).willReturn(aResponse().withBody("[\n" +
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

        doReturn(dataResponse).when(dataController).getData(anyString(), anyMap(), any());
        doReturn(list).when(dataResponse).getList();

        ResponseEntity<byte[]> response = viewController.export("myProjectId", request);
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(exp.getBytes(StandardCharsets.UTF_8)));
        HttpHeaders headers = response.getHeaders();
        assertThat(headers.getContentDisposition(), is("attachment;filename=export.csv"));
        assertThat(headers.getContentType(), is("csv;charset=UTF-8"));
        assertThat(headers.getContentLength(), is(exp.getBytes(StandardCharsets.UTF_8).length));
        assertThat(headers.get("Content-Type"), is("csv;charset=UTF-8"));
        assertThat(headers.get("Content-Encoding"), is("UTF-8"));
    }
}
