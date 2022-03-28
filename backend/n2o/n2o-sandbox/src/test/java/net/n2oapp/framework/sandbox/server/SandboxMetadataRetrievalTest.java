package net.n2oapp.framework.sandbox.server;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import net.n2oapp.framework.api.metadata.meta.control.Text;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.resource.TemplatesHolder;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тест на проверку обработки запросов на получение конфигурации и страницы примера
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {ViewController.class, SandboxPropertyResolver.class, SandboxRestClientImpl.class,
                TemplatesHolder.class, XsdSchemaParser.class})
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
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json")));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse()));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/user.properties")).willReturn(aResponse()));
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

    @SneakyThrows
    @Test
    public void testGetPage() {
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId")).willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withBody(StreamUtils.copyToString(new ClassPathResource("data/testMetadataRetrieval.json").getInputStream(), Charset.defaultCharset()))));
        stubFor(get(urlMatching("/sandbox/api/project/myProjectId/application.properties")).willReturn(aResponse()));
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
}