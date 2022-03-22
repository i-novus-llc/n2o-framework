package net.n2oapp.framework.sandbox.server;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import net.n2oapp.framework.api.metadata.meta.control.Text;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.view.SandboxContext;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.apache.catalina.util.ParameterMap;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тест на проверку обработки запросов на получение конфинурации и страницы примера
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ViewController.class, SandboxPropertyResolver.class, SandboxRestClientImpl.class, SandboxContext.class,
                SandboxTestDataProviderEngine.class},
        properties = {"n2o.access.deny_objects=false"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
public class SandboxServerTest {

    private static final HttpServletRequest request = mock(HttpServletRequest.class);
    private static final WireMockServer wireMockServer = new WireMockServer();

    @Autowired
    private ViewController viewController;
    @Autowired
    private HttpSession session;

    @BeforeAll
    static void setUp() {
        when(request.getRequestURI()).thenReturn("/sandbox/view/rrtTG/n2o/page/");
        when(request.getParameterMap()).thenReturn(new ParameterMap<>());
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

    @SneakyThrows
    @Test
    public void testApplicationProperties() {
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("{\"id\":\"myProjectId\",\"name\":null,\"viewUrl\":\"/sandbox/view/myProjectId/\",\"files\":[{\"file\":\"index.page.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\r\\n<simple-page xmlns=\\\"http://n2oapp.net/framework/config/schema/page-3.0\\\"\\r\\n             name=\\\"Главная страница\\\">\\r\\n    <form/>\\r\\n</simple-page>\"},{\"file\":\"application.properties\",\"source\":\"n2o.api.header.src=CustomHeader\\r\\nn2o.api.footer.src=CustomFooter\\r\\nn2o.api.page.simple.src=CustomPage\\r\\nn2o.api.widget.form.src=CustomForm\"}]}")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse().withBody("n2o.api.header.src=CustomHeader\n" +
                "n2o.api.footer.src=CustomFooter\n" +
                "n2o.api.page.simple.src=CustomPage\n" +
                "n2o.api.widget.form.src=CustomForm")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/user.properties")).willReturn(aResponse().withBody("")));

        JSONObject config = new JSONObject(viewController.getConfig("myProjectId"));
        assertThat(config.getJSONObject("menu").getJSONObject("header").getString("src"), is("CustomHeader"));
        assertThat(config.getJSONObject("menu").getJSONObject("footer").getString("src"), is("CustomFooter"));

        Page page = viewController.getPage("myProjectId", request, null);
        assertThat(page.getSrc(), is("CustomPage"));
        assertThat(((SimplePage) page).getWidget().getSrc(), is("CustomForm"));
    }

    @SneakyThrows
    @Test
    public void testUserProperties() {
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("{\"id\":\"myProjectId\",\"name\":null,\"viewUrl\":\"/sandbox/view/myProjectId/\",\"files\":[{\"file\":\"index.page.xml\",\"source\":\"<?xml version='1.0' encoding='UTF-8'?>\\n<simple-page xmlns=\\\"http://n2oapp.net/framework/config/schema/page-3.0\\\"\\n             name=\\\"Placeholder context\\\">\\n    <form>\\n        <fields>\\n            <output-text id=\\\"email\\\" default-value=\\\"#{email}\\\"/>\\n            <output-text id=\\\"roles\\\" default-value=\\\"#{roles}\\\"/>\\n        </fields>\\n    </form>\\n</simple-page>\"},{\"file\":\"user.properties\",\"source\":\"email=test@example.com\\nusername=Joe\\nroles=[USER,ADMIN]\"}]}")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse().withBody("")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/user.properties")).willReturn(aResponse().withBody("email=test@example.com\n" +
                "username=Joe\n" +
                "roles=[USER,ADMIN]")));

        JSONObject config = new JSONObject(viewController.getConfig("myProjectId"));
        assertThat(config.getJSONObject("user").getString("email"), is("test@example.com"));
        assertThat(config.getJSONObject("user").getString("name"), is("null"));
        assertThat(config.getJSONObject("user").getString("permissions"), is("null"));
        assertThat(config.getJSONObject("user").getJSONArray("roles").get(0), is("USER"));
        assertThat(config.getJSONObject("user").getJSONArray("roles").get(1), is("ADMIN"));
        assertThat(config.getJSONObject("user").getString("surname"), is("null"));
        assertThat(config.getJSONObject("user").getString("username"), is("Joe"));

        Page page = viewController.getPage("myProjectId", request, null);
        assertThat(page.getModels().get("resolve['main'].email").getValue(), is("test@example.com"));
        assertThat(((List) page.getModels().get("resolve['main'].roles").getValue()).get(0), is("USER"));
        assertThat(((List) page.getModels().get("resolve['main'].roles").getValue()).get(1), is("ADMIN"));
    }

    @Test
    public void testGetData() {
        when(request.getRequestURI()).thenReturn("/sandbox/view/rrtTG/n2o/data/main");
        when(request.getParameterMap()).thenReturn(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
        //viewController.getPage("rrtTG", request, null);
        viewController.getData("rrtTG", request, session);
    }
}