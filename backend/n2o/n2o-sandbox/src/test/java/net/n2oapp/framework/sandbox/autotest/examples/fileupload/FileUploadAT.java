package net.n2oapp.framework.sandbox.autotest.examples.fileupload;

import net.n2oapp.framework.autotest.api.component.control.FileUploadControl;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"server.servlet.context-path=/sandbox", "n2o.engine.test.classpath=/examples/file_upload/",
        "n2o.sandbox.project-id=examples_file_upload"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileUploadAT extends SandboxAutotestBase {

    @Autowired
    private FileStorageController fileStoreController;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        addRuntimeProperty("n2o.sandbox.editor.url", "/sandbox");
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/default/default.application.xml"));
    }

    @Override
    protected String getBaseUrl() {
        return "http://localhost:" + port + "/sandbox";
    }

    @Test
    public void oneFileUploadTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Страница загрузки файлов");
        FileUploadControl fileUpload = page.widget(FormWidget.class).fields().field("Загрузка файлов").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();

        fileStoreController.clear();
        fileUpload.uploadFromClasspath("META-INF/conf/test.query.xml");
        fileUpload.uploadFilesShouldBe(1);
        fileUpload.uploadFileNameShouldBe(0, "test.query.xml");
        assertEquals(1, fileStoreController.size());
        fileUpload.deleteFile(0);
        fileUpload.uploadFilesShouldBe(0);
        assertEquals(0, fileStoreController.size());
    }

    @Test
    public void twoFileUploadTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Страница загрузки файлов");
        FileUploadControl fileUpload = page.widget(FormWidget.class).fields().field("Загрузка файлов").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();

        fileStoreController.clear();
        fileUpload.uploadFromClasspath("META-INF/conf/test.query.xml");
        fileUpload.uploadFilesShouldBe(1);
        fileUpload.uploadFileNameShouldBe(0, "test.query.xml");
        assertEquals(1, fileStoreController.size());

        page = open(SimplePage.class); //что бы очистить значение формы загрузки файлов
        page.shouldExists();
        fileUpload.uploadFromClasspath("META-INF/conf/test.page.xml");
        fileUpload.uploadFilesShouldBe(2);
        fileUpload.uploadFileNameShouldBe(1, "test.page.xml");
        assertEquals(2, fileStoreController.size());

        fileUpload.deleteFile(1);
        fileUpload.uploadFilesShouldBe(1);
        assertEquals(1, fileStoreController.size());
        fileUpload.deleteFile(0);
        fileUpload.uploadFilesShouldBe(0);
        assertEquals(0, fileStoreController.size());
    }
}
