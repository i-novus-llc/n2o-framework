package net.n2oapp.framework.sandbox.server;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import net.n2oapp.framework.api.metadata.meta.control.Text;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на проверку обработки запросов на получение конфинурации и страницы примера
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ViewController.class, SandboxPropertyResolver.class, SandboxRestClientImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
public class SandboxMetadataRetrievalTest {

    private static final MockHttpServletRequest request = new MockHttpServletRequest();
    private static final WireMockServer wireMockServer = new WireMockServer();

    @Autowired
    private ViewController viewController;

    @BeforeAll
    static void setUp() {
        request.setRequestURI("/sandbox/view/myProjectId/n2o/page/");
        wireMockServer.start();
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @SneakyThrows
    @Test
    public void testGetConfig() {
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("{\"id\":\"myProjectId\",\"name\":null,\"viewUrl\":\"/sandbox/view/myProjectId/\",\"files\":[{\"file\":\"index.page.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\n<simple-page xmlns=\\\"http://n2oapp.net/framework/config/schema/page-3.0\\\"\\n             name=\\\"Моя первая страница\\\">\\n    <form>\\n        <fields>\\n            <text id=\\\"hello\\\">Привет Мир!</text>\\n        </fields>\\n    </form>\\n</simple-page>\"}]}")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse().withBody("")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/user.properties")).willReturn(aResponse().withBody("")));
        JSONObject config = new JSONObject(viewController.getConfig("myProjectId"));

        assertThat(config.getString("project"), is("myProjectId"));

        assertThat(config.getJSONObject("menu").getJSONObject("header").getString("extraMenu"), is("{}"));
        assertThat(config.getJSONObject("menu").getJSONObject("header").getString("menu"), is("{}"));
        assertThat(config.getJSONObject("menu").getJSONObject("header").getString("src"), is("Header"));
        assertThat(config.getJSONObject("menu").getJSONObject("header").getJSONObject("logo").getString("title"), is("N2O"));

        assertThat(config.getJSONObject("menu").getJSONObject("footer").getString("src"), is("DefaultFooter"));
        assertThat(config.getJSONObject("menu").getJSONObject("footer").getString("textLeft"), is("N2O 7.22.0-SNAPSHOT © 2013-2021"));

        assertThat(config.getJSONObject("menu").getJSONObject("layout").getBoolean("fixed"), is(false));
        assertThat(config.getJSONObject("menu").getJSONObject("layout").getBoolean("fullSizeHeader"), is(true));

        assertThat(config.getJSONObject("user").getString("username"), is("null"));
        assertThat(config.getJSONObject("user").getString("permissions"), is("null"));
        assertThat(config.getJSONObject("user").getString("roles"), is("null"));
    }

    @Test
    public void testGetPage() {
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("{\"id\":\"myProjectId\",\"name\":null,\"viewUrl\":\"/sandbox/view/myProjectId/\",\"files\":[{\"file\":\"index.page.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\n<simple-page xmlns=\\\"http://n2oapp.net/framework/config/schema/page-3.0\\\"\\n             name=\\\"Моя первая страница\\\">\\n    <form>\\n        <fields>\\n            <text id=\\\"hello\\\">Привет Мир!</text>\\n        </fields>\\n    </form>\\n</simple-page>\"}]}")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse().withBody("")));
        Page page = viewController.getPage("myProjectId", request, null);

        assertThat(page.getId(), is("_"));
        assertThat(page.getModels().size(), is(0));
        assertThat(page.getSrc(), is("SimplePage"));

        assertThat(page.getBreadcrumb().get(0).getLabel(), is("Моя первая страница"));

        assertThat(page.getDatasources().get("main").getDependencies().size(), is(0));
        assertThat(page.getDatasources().get("main").getId(), is("main"));
        assertThat(page.getDatasources().get("main").getSize(), is(1));
        assertThat(page.getDatasources().get("main").getValidations().size(), is(0));

        assertThat(page.getPageProperty().getHtmlTitle(), is("Моя первая страница"));

        assertThat(page.getRoutes().getPathMapping().size(), is(0));
        assertThat(page.getRoutes().getQueryMapping().size(), is(0));
        assertThat(page.getRoutes().getList().get(0).getIsOtherPage(), is(false));
        assertThat(page.getRoutes().getList().get(0).getExact(), is(true));
        assertThat(page.getRoutes().getList().get(0).getPath(), is("/"));

        assertThat(((SimplePage) page).getWidget().getId(), is("main"));
        assertThat(((SimplePage) page).getWidget().getDatasource(), is("main"));
        assertThat(((SimplePage) page).getWidget().getSrc(), is("FormWidget"));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getAutoFocus(), is(false));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getModelPrefix(), is("resolve"));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getPrompt(), is(false));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getAutoFocus(), is(false));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getFieldsets().get(0).getSrc(), is("StandardFieldset"));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0).getId(), is("hello"));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0).getDependencies().size(), is(0));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0).getEnabled(), is(true));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0).getNoLabelBlock(), is(false));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0).getRequired(), is(false));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0).getSrc(), is("TextField"));
        assertThat(((Form) ((SimplePage) page).getWidget()).getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0).getVisible(), is(true));
        assertThat(((Text) ((Form) ((SimplePage) page).getWidget()).getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getText(), is("Привет Мир!"));
    }

//    @Test
//    public void testGetData() {
//        when(request.getRequestURI()).thenReturn("/sandbox/view/myProjectId/n2o/data/main");
//        when(request.getParameterMap()).thenReturn(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
//        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("{\"id\":\"myProjectId\",\"name\":null,\"viewUrl\":\"/sandbox/view/myProjectId/\",\"files\":[{\"file\":\"index.page.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<simple-page xmlns=\\\"http://n2oapp.net/framework/config/schema/page-3.0\\\"\\r\\n             name=\\\"CRUD Операции\\\">\\r\\n    <table query-id=\\\"test\\\" auto-focus=\\\"true\\\">\\r\\n        <columns>\\r\\n            <column text-field-id=\\\"id\\\"/>\\r\\n            <column text-field-id=\\\"name\\\"/>\\r\\n        </columns>\\r\\n        <toolbar generate=\\\"crud\\\"/>\\r\\n    </table>\\r\\n</simple-page>\\r\\n\"},{\"file\":\"test.json\",\"source\":\"[\\r\\n  {\\r\\n    \\\"id\\\": 1,\\r\\n    \\\"name\\\": \\\"test1\\\"\\r\\n  },\\r\\n  {\\r\\n    \\\"id\\\": 2,\\r\\n    \\\"name\\\": \\\"test2\\\"\\r\\n  },\\r\\n  {\\r\\n    \\\"id\\\": 3,\\r\\n    \\\"name\\\": \\\"test3\\\"\\r\\n  },\\r\\n  {\\r\\n    \\\"id\\\": 4,\\r\\n    \\\"name\\\": \\\"test4\\\"\\r\\n  }\\r\\n]\\r\\n\"},{\"file\":\"test.object.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<object xmlns=\\\"http://n2oapp.net/framework/config/schema/object-4.0\\\">\\r\\n    <operations>\\r\\n        <operation id=\\\"create\\\">\\r\\n            <invocation>\\r\\n                <test file=\\\"test.json\\\" operation=\\\"create\\\"/>\\r\\n            </invocation>\\r\\n            <in>\\r\\n                <field id=\\\"name\\\"/>\\r\\n            </in>\\r\\n            <out>\\r\\n                <field id=\\\"id\\\"/>\\r\\n            </out>\\r\\n        </operation>\\r\\n\\r\\n        <operation id=\\\"update\\\">\\r\\n            <invocation>\\r\\n                <test file=\\\"test.json\\\" operation=\\\"update\\\"/>\\r\\n            </invocation>\\r\\n            <in>\\r\\n                <field id=\\\"id\\\"/>\\r\\n                <field id=\\\"name\\\"/>\\r\\n            </in>\\r\\n        </operation>\\r\\n\\r\\n        <operation id=\\\"delete\\\">\\r\\n            <invocation>\\r\\n                <test file=\\\"test.json\\\" operation=\\\"delete\\\"/>\\r\\n            </invocation>\\r\\n            <in>\\r\\n                <field id=\\\"id\\\"/>\\r\\n            </in>\\r\\n        </operation>\\r\\n    </operations>\\r\\n</object>\\r\\n\"},{\"file\":\"test.page.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<simple-page xmlns=\\\"http://n2oapp.net/framework/config/schema/page-3.0\\\">\\r\\n    <form query-id=\\\"test\\\">\\r\\n        <fields>\\r\\n            <input-text id=\\\"name\\\"/>\\r\\n        </fields>\\r\\n    </form>\\r\\n</simple-page>\\r\\n\"},{\"file\":\"test.query.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<query xmlns=\\\"http://n2oapp.net/framework/config/schema/query-4.0\\\"\\r\\n       object-id=\\\"test\\\">\\r\\n    <list>\\r\\n        <test file=\\\"test.json\\\" operation=\\\"findAll\\\"/>\\r\\n    </list>\\r\\n\\r\\n    <fields>\\r\\n        <field id=\\\"id\\\" domain=\\\"integer\\\">\\r\\n            <select/>\\r\\n            <filters>\\r\\n                <eq filter-id=\\\"id\\\"/>\\r\\n            </filters>\\r\\n        </field>\\r\\n        <field id=\\\"name\\\">\\r\\n            <select/>\\r\\n        </field>\\r\\n    </fields>\\r\\n</query>\\r\\n\"}]}")));
//        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse().withBody("")));
//        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/user.properties")).willReturn(aResponse().withBody("")));
//        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/test.json")).willReturn(aResponse().withBody("[\n" +
//                "  {\n" +
//                "    \"id\": 1,\n" +
//                "    \"name\": \"test1\"\n" +
//                "  },\n" +
//                "  {\n" +
//                "    \"id\": 2,\n" +
//                "    \"name\": \"test2\"\n" +
//                "  },\n" +
//                "  {\n" +
//                "    \"id\": 3,\n" +
//                "    \"name\": \"test3\"\n" +
//                "  },\n" +
//                "  {\n" +
//                "    \"id\": 4,\n" +
//                "    \"name\": \"test4\"\n" +
//                "  }\n" +
//                "]")));
//
//        ResponseEntity<GetDataResponse> response = viewController.getData("myProjectId", request, null);
//        assertThat(response.getBody().getCount(), is(4));
//        assertThat(response.getBody().getList().get(0).get("id"), is(1L));
//        assertThat(response.getBody().getList().get(1).get("id"), is(2L));
//        assertThat(response.getBody().getList().get(2).get("id"), is(3L));
//        assertThat(response.getBody().getList().get(3).get("id"), is(4L));
//        assertThat(response.getBody().getList().get(0).get("name"), is("test1"));
//        assertThat(response.getBody().getList().get(1).get("name"), is("test2"));
//        assertThat(response.getBody().getList().get(2).get("name"), is("test3"));
//        assertThat(response.getBody().getList().get(3).get("name"), is("test4"));
//
//    }
//
//    @Test
//    public void testSetData() {
//        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
//        servletRequest.setRequestURI("/sandbox/view/T0BZE/n2o/data/main/5/update/submit");
//        servletRequest.setParameters(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
//        viewController.setData("T0BZE", new LinkedHashMap<>(Map.of("name", "qwe1", "id", 5)), servletRequest, session);
//    }
}