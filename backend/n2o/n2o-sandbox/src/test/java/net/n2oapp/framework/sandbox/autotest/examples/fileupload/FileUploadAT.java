package net.n2oapp.framework.sandbox.autotest.examples.fileupload;

import net.n2oapp.framework.autotest.api.component.control.FileUploadControl;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"server.servlet.context-path=/sandbox", "n2o.engine.test.classpath=/examples/file_upload/"},
        classes = FileUploadATConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileUploadAT extends AutoTestBase {

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
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("/examples/file_upload/index.page.xml"),
                new CompileInfo("/examples/file_upload/files.query.xml"));
    }

    @Override
    protected String getBaseUrl() {
        return "http://localhost:" + port + "/sandbox";
    }

    @Test
    public void oneFileUploadTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница загрузки файлов");
        FileUploadControl fileUpload = page.widget(FormWidget.class).fields().field("Загрузка файлов").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();

        fileStoreController.clear();
        fileUpload.uploadFromClasspath("examples/file_upload/files.query.xml");
        fileUpload.shouldHaveUploadFiles(1);
        fileUpload.uploadFileShouldHaveName(0, "files.query.xml");
        assertEquals(1, fileStoreController.size());
        fileUpload.deleteFile(0);
        fileUpload.shouldHaveUploadFiles(0);
        assertEquals(0, fileStoreController.size());
    }

    @Test
    public void twoFileUploadTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница загрузки файлов");
        FileUploadControl fileUpload = page.widget(FormWidget.class).fields().field("Загрузка файлов").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();

        fileStoreController.clear();
        fileUpload.uploadFromClasspath("examples/file_upload/index.page.xml");
        fileUpload.shouldHaveUploadFiles(1);
        fileUpload.uploadFileShouldHaveName(0, "index.page.xml");
        assertEquals(1, fileStoreController.size());

        page = open(SimplePage.class); //что бы очистить значение формы загрузки файлов
        page.shouldExists();
        fileUpload.uploadFromClasspath("examples/file_upload/files.query.xml");
        fileUpload.shouldHaveUploadFiles(2);
        fileUpload.uploadFileShouldHaveName(1, "files.query.xml");
        assertEquals(2, fileStoreController.size());

        fileUpload.deleteFile(1);
        fileUpload.shouldHaveUploadFiles(1);
        assertEquals(1, fileStoreController.size());
        fileUpload.deleteFile(0);
        fileUpload.shouldHaveUploadFiles(0);
        assertEquals(0, fileStoreController.size());
    }
}
