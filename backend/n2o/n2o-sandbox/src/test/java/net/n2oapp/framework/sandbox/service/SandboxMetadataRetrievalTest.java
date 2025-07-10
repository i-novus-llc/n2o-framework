package net.n2oapp.framework.sandbox.service;

import lombok.SneakyThrows;
import net.n2oapp.framework.api.metadata.meta.control.Text;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.view.SandboxApplicationBuilderConfigurer;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;

/**
 * Тест на проверку обработки запросов на получение конфигурации и страницы примера
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SandboxTestApplication.class, ViewController.class, SandboxPropertyResolver.class,
                XsdSchemaParser.class, SandboxApplicationBuilderConfigurer.class, ProjectTemplateHolder.class})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
class SandboxMetadataRetrievalTest {

    @MockBean
    private HttpServletRequest request;

    @Autowired
    private ViewController viewController;

    @MockBean
    private FileStorage fileStorage;

    @SneakyThrows
    @Test
    void testGetConfig(){
        mockGetProjectFiles();
        JSONObject config = new JSONObject(viewController.getConfig("myProjectId"));

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

    private void mockGetProjectFiles() {
        List<FileModel> fileModels = new ArrayList<>();
        FileModel fileModel = new FileModel();
        fileModel.setFile("index.page.xml");
        fileModel.setSource("<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<simple-page xmlns=\"http://n2oapp.net/framework/config/schema/page-3.0\"\n" +
                "             name=\"Моя первая страница\">\n" +
                "    <form>\n" +
                "        <fields>\n" +
                "            <text id=\"hello\">Привет Мир!</text>\n" +
                "        </fields>\n" +
                "    </form>\n" +
                "</simple-page>");
        fileModels.add(fileModel);
        doReturn(fileModels).when(fileStorage).getProjectFiles("myProjectId");
    }

    @SneakyThrows
    @Test
    void testGetPage() {
        mockGetProjectFiles();
        doReturn("/n2o/page/").when(request).getRequestURI();
        Page page = viewController.getPage("myProjectId", request);

        assertThat(page.getId(), is("_"));
        assertThat(page.getModels().size(), is(0));
        assertThat(page.getSrc(), is("SimplePage"));

        assertThat(page.getBreadcrumb().get(0).getLabel(), is("Моя первая страница"));

        assertThat(page.getDatasources().get("_w1").getDependencies().size(), is(0));
        assertThat(page.getDatasources().get("_w1").getId(), is("_w1"));
        assertThat(page.getDatasources().get("_w1").getPaging().getSize(), is(1));
        assertThat(page.getDatasources().get("_w1").getValidations().size(), is(0));

        assertThat(page.getPageProperty().getHtmlTitle(), is("Моя первая страница"));

        assertThat(page.getRoutes().getPathMapping().size(), is(0));
        assertThat(page.getRoutes().getQueryMapping().size(), is(0));
        assertThat(page.getRoutes().getList().get(0).getIsOtherPage(), is(false));
        assertThat(page.getRoutes().getList().get(0).getExact(), is(true));
        assertThat(page.getRoutes().getList().get(0).getPath(), is("/"));

        assertThat(((SimplePage) page).getWidget().getId(), is("_w1"));
        assertThat(((SimplePage) page).getWidget().getDatasource(), is("_w1"));
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
