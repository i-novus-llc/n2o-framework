package net.n2oapp.framework.sandbox.server;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.utils.ProjectUtil;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.apache.catalina.util.ParameterMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mockStatic;

/**
 * Тест получения и установки значений провайдером данных
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ViewController.class, SandboxPropertyResolver.class, SandboxRestClientImpl.class,
                SandboxTestDataProviderEngine.class, XsdSchemaParser.class},
        properties = {"n2o.access.deny_objects=false"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
public class SandboxDataProviderTest {

    private static final MockHttpServletRequest request = new MockHttpServletRequest();
    private static final WireMockServer wireMockServer = new WireMockServer();
    private static final MockedStatic<ProjectUtil> mockedUtil = mockStatic(ProjectUtil.class);

    @Autowired
    private ViewController viewController;
    @Autowired
    private HttpSession session;

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
    public void testGetData() {
        mockedUtil.when(() -> ProjectUtil.getFromSession(session, "myProjectId")).thenReturn(null);
        request.setRequestURI("/sandbox/view/myProjectId/n2o/data/main");
        request.setParameters(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
                StreamUtils.copyToString(new ClassPathResource("data/testDataProvider.json").getInputStream(), Charset.defaultCharset()))));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse()));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/user.properties")).willReturn(aResponse()));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/test.json")).willReturn(aResponse().withBody("[\n" +
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

        ResponseEntity<GetDataResponse> response = viewController.getData("myProjectId", request, null);
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
        mockedUtil.when(() -> ProjectUtil.getFromSession(session, "myProjectId")).thenReturn(null);
        request.setRequestURI("/sandbox/view/myProjectId/n2o/data/main/3/update/submit");
        request.setParameters(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(
                StreamUtils.copyToString(new ClassPathResource("data/testDataProvider.json").getInputStream(), Charset.defaultCharset()))));
        stubFor(put(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withStatus(200)));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse()));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/user.properties")).willReturn(aResponse()));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/test.json")).willReturn(aResponse().withBody("[\n" +
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

        ResponseEntity<SetDataResponse> response = viewController.setData("myProjectId", new LinkedHashMap<>(Map.of("name", "name3", "id", 3)), request, null);
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody().getData().get("id"), is(3));
        assertThat(response.getBody().getData().get("name"), is("name3"));
        assertThat(response.getBody().getMeta().getAlert().getMessages().get(0).getText(), is("Данные сохранены"));
    }
}
