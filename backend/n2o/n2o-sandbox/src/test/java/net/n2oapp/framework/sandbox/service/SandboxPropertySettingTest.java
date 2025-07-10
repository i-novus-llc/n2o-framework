package net.n2oapp.framework.sandbox.service;

import lombok.SneakyThrows;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.view.SandboxApplicationBuilderConfigurer;
import net.n2oapp.framework.sandbox.view.SandboxContext;
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

import static net.n2oapp.framework.sandbox.view.SandboxContext.USER_PROPERTIES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;

/**
 * Тест на проверку установки свойств
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SandboxTestApplication.class, ViewController.class, SandboxPropertyResolver.class,
                SandboxContext.class, XsdSchemaParser.class, SandboxApplicationBuilderConfigurer.class, ProjectTemplateHolder.class})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
class SandboxPropertySettingTest {

    @MockBean
    private HttpServletRequest request;

    @Autowired
    private ViewController viewController;

    @MockBean
    private FileStorage fileStorage;

    @SneakyThrows
    @Test
    void testApplicationProperties() {
        List<FileModel> fileModels = new ArrayList<>();
        FileModel fileModel1 = new FileModel();
        fileModel1.setFile("index.page.xml");
        fileModel1.setSource("<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<simple-page xmlns=\"http://n2oapp.net/framework/config/schema/page-3.0\"\n" +
                "             name=\"Placeholder context\">\n" +
                "    <form>\n" +
                "        <fields>\n" +
                "            <output-text id=\"email\" default-value=\"#{email}\"/>\n" +
                "            <output-text id=\"roles\" default-value=\"#{roles}\"/>\n" +
                "        </fields>\n" +
                "    </form>\n" +
                "</simple-page>");
        fileModels.add(fileModel1);
        FileModel fileModel2 = new FileModel();
        fileModel2.setFile("user.properties");
        fileModel2.setSource("email=test@example.com\nusername=Joe\nroles=[USER,ADMIN]");
        fileModels.add(fileModel2);
        doReturn(fileModels).when(fileStorage).getProjectFiles("myProjectId");
        doReturn("").when(fileStorage).getFileContent("myProjectId", USER_PROPERTIES);
        doReturn("n2o.api.header.src=CustomHeader\n" +
                "n2o.api.footer.src=CustomFooter\n" +
                "n2o.api.page.simple.src=CustomPage\n" +
                "n2o.api.widget.table.src=CustomTable\n" +
                "n2o.api.widget.form.src=CustomForm").when(fileStorage).getFileContent("myProjectId", "application.properties");
        doReturn("").when(fileStorage).getFileContent("myProjectId", "config.json");
        doReturn("/n2o/page/").when(request).getRequestURI();

        JSONObject config = new JSONObject(viewController.getConfig("myProjectId"));
        assertThat(config.getJSONObject("menu").getJSONObject("header").getString("src"), is("CustomHeader"));
        assertThat(config.getJSONObject("menu").getJSONObject("footer").getString("src"), is("CustomFooter"));

        Page page = viewController.getPage("myProjectId", request);
        assertThat(page.getSrc(), is("CustomPage"));
        assertThat(((SimplePage) page).getWidget().getSrc(), is("CustomForm"));
    }

    @SneakyThrows
    @Test
    void testUserProperties() {
        List<FileModel> fileModels = new ArrayList<>();
        FileModel fileModel1 = new FileModel();
        fileModel1.setFile("index.page.xml");
        fileModel1.setSource("<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<simple-page xmlns=\"http://n2oapp.net/framework/config/schema/page-3.0\"\n" +
                "             name=\"Placeholder context\">\n" +
                "    <form>\n" +
                "        <fields>\n" +
                "            <output-text id=\"email\" default-value=\"#{email}\"/>\n" +
                "            <output-text id=\"roles\" default-value=\"#{roles}\"/>\n" +
                "        </fields>\n" +
                "    </form>\n" +
                "</simple-page>");
        fileModels.add(fileModel1);
        FileModel fileModel2 = new FileModel();
        fileModel2.setFile("user.properties");
        fileModel2.setSource("email=test@example.com\nusername=Joe\nroles=[USER,ADMIN]");
        fileModels.add(fileModel2);
        doReturn(fileModels).when(fileStorage).getProjectFiles("myProjectId");
        doReturn("email=test@example.com\nusername=Joe\nroles=[USER,ADMIN]").when(fileStorage).getFileContent("myProjectId", USER_PROPERTIES);
        doReturn("").when(fileStorage).getFileContent("myProjectId", "application.properties");
        doReturn("").when(fileStorage).getFileContent("myProjectId", "config.json");
        doReturn("/n2o/page/").when(request).getRequestURI();

        JSONObject config = new JSONObject(viewController.getConfig("myProjectId"));
        assertThat(config.getJSONObject("user").getString("email"), is("test@example.com"));
        assertThat(config.getJSONObject("user").getString("name"), is("null"));
        assertThat(config.getJSONObject("user").getString("permissions"), is("null"));
        assertThat(config.getJSONObject("user").getJSONArray("roles").get(0), is("USER"));
        assertThat(config.getJSONObject("user").getJSONArray("roles").get(1), is("ADMIN"));
        assertThat(config.getJSONObject("user").getString("surname"), is("null"));
        assertThat(config.getJSONObject("user").getString("username"), is("Joe"));

        Page page = viewController.getPage("myProjectId", request);
        assertThat(page.getModels().get("resolve['_w1'].email").getValue(), is("test@example.com"));
        assertThat((page.getModels().get("resolve['_w1'].roles").getValue()), is("[USER, ADMIN]"));
    }
}
