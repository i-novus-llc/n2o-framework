package net.n2oapp.framework.sandbox.server;

import com.github.tomakehurst.wiremock.WireMockServer;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;

import javax.servlet.http.HttpSession;
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
        classes = {ViewController.class, SandboxPropertyResolver.class,
                SandboxRestClientImpl.class, SandboxTestDataProviderEngine.class},
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

    @Test
    public void testGetData() {
        mockedUtil.when(() -> ProjectUtil.getFromSession(session, "myProjectId")).thenReturn(null);
        request.setRequestURI("/sandbox/view/myProjectId/n2o/data/main");
        request.setParameters(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("{\"id\":\"myProjectId\",\"name\":null,\"viewUrl\":\"/sandbox/view/myProjectId/\",\"files\":[{\"file\":\"index.page.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<simple-page xmlns=\\\"http://n2oapp.net/framework/config/schema/page-3.0\\\"\\r\\n             name=\\\"CRUD Операции\\\">\\r\\n    <table query-id=\\\"test\\\" auto-focus=\\\"true\\\">\\r\\n        <columns>\\r\\n            <column text-field-id=\\\"id\\\"/>\\r\\n            <column text-field-id=\\\"name\\\"/>\\r\\n        </columns>\\r\\n        <toolbar generate=\\\"crud\\\"/>\\r\\n    </table>\\r\\n</simple-page>\\r\\n\"},{\"file\":\"test.json\",\"source\":\"[\\r\\n  {\\r\\n    \\\"id\\\": 1,\\r\\n    \\\"name\\\": \\\"test1\\\"\\r\\n  },\\r\\n  {\\r\\n    \\\"id\\\": 2,\\r\\n    \\\"name\\\": \\\"test2\\\"\\r\\n  },\\r\\n  {\\r\\n    \\\"id\\\": 3,\\r\\n    \\\"name\\\": \\\"test3\\\"\\r\\n  },\\r\\n  {\\r\\n    \\\"id\\\": 4,\\r\\n    \\\"name\\\": \\\"test4\\\"\\r\\n  }\\r\\n]\\r\\n\"},{\"file\":\"test.object.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<object xmlns=\\\"http://n2oapp.net/framework/config/schema/object-4.0\\\">\\r\\n    <operations>\\r\\n        <operation id=\\\"create\\\">\\r\\n            <invocation>\\r\\n                <test file=\\\"test.json\\\" operation=\\\"create\\\"/>\\r\\n            </invocation>\\r\\n            <in>\\r\\n                <field id=\\\"name\\\"/>\\r\\n            </in>\\r\\n            <out>\\r\\n                <field id=\\\"id\\\"/>\\r\\n            </out>\\r\\n        </operation>\\r\\n\\r\\n        <operation id=\\\"update\\\">\\r\\n            <invocation>\\r\\n                <test file=\\\"test.json\\\" operation=\\\"update\\\"/>\\r\\n            </invocation>\\r\\n            <in>\\r\\n                <field id=\\\"id\\\"/>\\r\\n                <field id=\\\"name\\\"/>\\r\\n            </in>\\r\\n        </operation>\\r\\n\\r\\n        <operation id=\\\"delete\\\">\\r\\n            <invocation>\\r\\n                <test file=\\\"test.json\\\" operation=\\\"delete\\\"/>\\r\\n            </invocation>\\r\\n            <in>\\r\\n                <field id=\\\"id\\\"/>\\r\\n            </in>\\r\\n        </operation>\\r\\n    </operations>\\r\\n</object>\\r\\n\"},{\"file\":\"test.page.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<simple-page xmlns=\\\"http://n2oapp.net/framework/config/schema/page-3.0\\\">\\r\\n    <form query-id=\\\"test\\\">\\r\\n        <fields>\\r\\n            <input-text id=\\\"name\\\"/>\\r\\n        </fields>\\r\\n    </form>\\r\\n</simple-page>\\r\\n\"},{\"file\":\"test.query.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<query xmlns=\\\"http://n2oapp.net/framework/config/schema/query-4.0\\\"\\r\\n       object-id=\\\"test\\\">\\r\\n    <list>\\r\\n        <test file=\\\"test.json\\\" operation=\\\"findAll\\\"/>\\r\\n    </list>\\r\\n\\r\\n    <fields>\\r\\n        <field id=\\\"id\\\" domain=\\\"integer\\\">\\r\\n            <select/>\\r\\n            <filters>\\r\\n                <eq filter-id=\\\"id\\\"/>\\r\\n            </filters>\\r\\n        </field>\\r\\n        <field id=\\\"name\\\">\\r\\n            <select/>\\r\\n        </field>\\r\\n    </fields>\\r\\n</query>\\r\\n\"}]}")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse().withBody("")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/user.properties")).willReturn(aResponse().withBody("")));
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

    @Test
    public void testSetData() {
        mockedUtil.when(() -> ProjectUtil.getFromSession(session, "myProjectId")).thenReturn(null);
        request.setRequestURI("/sandbox/view/myProjectId/n2o/data/main/3/update/submit");
        request.setParameters(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("{\"id\":\"myProjectId\",\"name\":null,\"viewUrl\":\"/sandbox/view/myProjectId/\",\"files\":[{\"file\":\"index.page.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<simple-page xmlns=\\\"http://n2oapp.net/framework/config/schema/page-3.0\\\"\\r\\n             name=\\\"CRUD Операции\\\">\\r\\n    <table query-id=\\\"test\\\" auto-focus=\\\"true\\\">\\r\\n        <columns>\\r\\n            <column text-field-id=\\\"id\\\"/>\\r\\n            <column text-field-id=\\\"name\\\"/>\\r\\n        </columns>\\r\\n        <toolbar generate=\\\"crud\\\"/>\\r\\n    </table>\\r\\n</simple-page>\\r\\n\"},{\"file\":\"test.json\",\"source\":\"[\\r\\n  {\\r\\n    \\\"id\\\": 1,\\r\\n    \\\"name\\\": \\\"test1\\\"\\r\\n  },\\r\\n  {\\r\\n    \\\"id\\\": 2,\\r\\n    \\\"name\\\": \\\"test2\\\"\\r\\n  },\\r\\n  {\\r\\n    \\\"id\\\": 3,\\r\\n    \\\"name\\\": \\\"test3\\\"\\r\\n  },\\r\\n  {\\r\\n    \\\"id\\\": 4,\\r\\n    \\\"name\\\": \\\"test4\\\"\\r\\n  }\\r\\n]\\r\\n\"},{\"file\":\"test.object.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<object xmlns=\\\"http://n2oapp.net/framework/config/schema/object-4.0\\\">\\r\\n    <operations>\\r\\n        <operation id=\\\"create\\\">\\r\\n            <invocation>\\r\\n                <test file=\\\"test.json\\\" operation=\\\"create\\\"/>\\r\\n            </invocation>\\r\\n            <in>\\r\\n                <field id=\\\"name\\\"/>\\r\\n            </in>\\r\\n            <out>\\r\\n                <field id=\\\"id\\\"/>\\r\\n            </out>\\r\\n        </operation>\\r\\n\\r\\n        <operation id=\\\"update\\\">\\r\\n            <invocation>\\r\\n                <test file=\\\"test.json\\\" operation=\\\"update\\\"/>\\r\\n            </invocation>\\r\\n            <in>\\r\\n                <field id=\\\"id\\\"/>\\r\\n                <field id=\\\"name\\\"/>\\r\\n            </in>\\r\\n        </operation>\\r\\n\\r\\n        <operation id=\\\"delete\\\">\\r\\n            <invocation>\\r\\n                <test file=\\\"test.json\\\" operation=\\\"delete\\\"/>\\r\\n            </invocation>\\r\\n            <in>\\r\\n                <field id=\\\"id\\\"/>\\r\\n            </in>\\r\\n        </operation>\\r\\n    </operations>\\r\\n</object>\\r\\n\"},{\"file\":\"test.page.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<simple-page xmlns=\\\"http://n2oapp.net/framework/config/schema/page-3.0\\\">\\r\\n    <form query-id=\\\"test\\\">\\r\\n        <fields>\\r\\n            <input-text id=\\\"name\\\"/>\\r\\n        </fields>\\r\\n    </form>\\r\\n</simple-page>\\r\\n\"},{\"file\":\"test.query.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<query xmlns=\\\"http://n2oapp.net/framework/config/schema/query-4.0\\\"\\r\\n       object-id=\\\"test\\\">\\r\\n    <list>\\r\\n        <test file=\\\"test.json\\\" operation=\\\"findAll\\\"/>\\r\\n    </list>\\r\\n\\r\\n    <fields>\\r\\n        <field id=\\\"id\\\" domain=\\\"integer\\\">\\r\\n            <select/>\\r\\n            <filters>\\r\\n                <eq filter-id=\\\"id\\\"/>\\r\\n            </filters>\\r\\n        </field>\\r\\n        <field id=\\\"name\\\">\\r\\n            <select/>\\r\\n        </field>\\r\\n    </fields>\\r\\n</query>\\r\\n\"}]}")));
        stubFor(put(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withStatus(200)));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse().withBody("")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/user.properties")).willReturn(aResponse().withBody("")));
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
