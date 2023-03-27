package net.n2oapp.framework.sandbox.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.JvmProxyConfigurer;
import lombok.SneakyThrows;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.meta.control.Text;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.sandbox.client.SandboxRestClientImpl;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.view.SandboxApplicationBuilderConfigurer;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
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
                XsdSchemaParser.class, SandboxApplicationBuilderConfigurer.class, ProjectTemplateHolder.class},
        properties = {"n2o.sandbox.url=http://${n2o.sandbox.api.host}:${n2o.sandbox.api.port}"})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
public class SandboxMetadataRetrievalTest {

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
        request.setRequestURI("/sandbox/view/myProjectId/n2o/page/");
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
    public void testGetConfig(){
        wireMockServer.stubFor(get("/project/myProjectId").withHost(equalTo(host)).withPort(port).willReturn(aResponse().withHeader("Content-Type", "application/json")));
        wireMockServer.stubFor(get("/project/myProjectId/application.properties").withHost(equalTo(host)).withPort(port).willReturn(aResponse()));
        wireMockServer.stubFor(get("/project/myProjectId/user.properties").withHost(equalTo(host)).withPort(port).willReturn(aResponse()));
        wireMockServer.stubFor(get("/project/myProjectId/config.json").withHost(equalTo(host)).withPort(port).willReturn(aResponse()));
        JSONObject config = new JSONObject(viewController.getConfig("myProjectId", null));

        assertThat(config.getString("project"), is("myProjectId"));

        assertThat(config.getJSONObject("menu").getJSONObject("header").getString("extraMenu"), is("{}"));
        assertThat(config.getJSONObject("menu").getJSONObject("header").getString("menu"), is("{}"));
        assertThat(config.getJSONObject("menu").getJSONObject("header").getString("src"), is("Header"));
        assertThat(config.getJSONObject("menu").getJSONObject("header").getJSONObject("logo").getString("title"), is("N2O"));

        assertThat(config.getJSONObject("menu").getJSONObject("footer").getString("src"), is("DefaultFooter"));

        assertThat(config.getJSONObject("menu").getJSONObject("layout").getBoolean("fixed"), is(false));
        assertThat(config.getJSONObject("menu").getJSONObject("layout").getBoolean("fullSizeHeader"), is(true));

        assertThat(config.getJSONObject("user").getString("username"), is("null"));
        assertThat(config.getJSONObject("user").getString("permissions"), is("null"));
        assertThat(config.getJSONObject("user").getString("roles"), is("null"));
    }

    @SneakyThrows
    @Test
    public void testGetPage() {
        wireMockServer.stubFor(get("/project/myProjectId").withHost(equalTo(host)).withPort(port).willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withBody(StreamUtils.copyToString(new ClassPathResource("data/testMetadataRetrieval.json").getInputStream(), Charset.defaultCharset()))));
        wireMockServer.stubFor(get("/project/myProjectId/application.properties").withHost(equalTo(host)).withPort(port).willReturn(aResponse()));
        Page page = viewController.getPage("myProjectId", request);

        assertThat(page.getId(), is("_"));
        assertThat(page.getModels().size(), is(0));
        assertThat(page.getSrc(), is("SimplePage"));

        assertThat(page.getBreadcrumb().get(0).getLabel(), is("Моя первая страница"));

        assertThat(page.getDatasources().get("_main").getDependencies().size(), is(0));
        assertThat(page.getDatasources().get("_main").getId(), is("_main"));
        assertThat(page.getDatasources().get("_main").getPaging().getSize(), is(1));
        assertThat(page.getDatasources().get("_main").getValidations().size(), is(0));

        assertThat(page.getPageProperty().getHtmlTitle(), is("Моя первая страница"));

        assertThat(page.getRoutes().getPathMapping().size(), is(0));
        assertThat(page.getRoutes().getQueryMapping().size(), is(0));
        assertThat(page.getRoutes().getList().get(0).getIsOtherPage(), is(false));
        assertThat(page.getRoutes().getList().get(0).getExact(), is(true));
        assertThat(page.getRoutes().getList().get(0).getPath(), is("/"));

        assertThat(((SimplePage) page).getWidget().getId(), is("_main"));
        assertThat(((SimplePage) page).getWidget().getDatasource(), is("_main"));
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
