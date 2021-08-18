package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.FileUploadControl;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.autotest.run.FileStoreController;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.dataprovider.TestDataProviderIOv1;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openjdk.nashorn.internal.ir.annotations.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Автотесты поля загрузки файлов
 */
public class FileUploadAT extends AutoTestBase {

    private SimplePage simplePage;

    @Autowired
    private FileStoreController fileStoreController;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/fileupload/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oControlsV2IOPack(), new N2oQueriesPack());
        builder.ios(new TestDataProviderIOv1());
    }

    @Test
    public void wrongRestTest() {
        FileUploadControl fileUpload = getFields().field("FileUpload1").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();
        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/test1.json");
        fileUpload.uploadFilesShouldBe(1);
        fileUpload.uploadFileNameShouldBe(0, "test1.json");
//        fileUpload.uploadFileSizeShouldBe(0, "91 Б");//работает по разному на разных ОС, windows "105 Б", linux "91 Б"
        fileUpload.deleteFile(0);
        fileUpload.uploadFilesShouldBe(0);
    }

    @Test
    public void oneFileUploadTest() {
        FileUploadControl fileUpload = getFields().field("FileUpload2").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();

        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/test1.json");
        fileUpload.uploadFilesShouldBe(1);

        fileUpload.uploadFileShouldHaveLink(0, "http://localhost:" + port + "/files/test1.json");
        fileUpload.uploadFileNameShouldBe(0, "test1.json");
//        fileUpload.uploadFileSizeShouldBe(0, "91 Б");//работает по разному на разных ОС, windows "105 Б", linux "91 Б"

        assertThat(fileStoreController.getFileStore().size(), is(1));
        fileUpload.deleteFile(0);
        fileUpload.uploadFilesShouldBe(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));

        // загрузка файла с неразрешенным расширением
        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/fileupload/index.page.xml");
        // загрузка не произошла
        fileUpload.uploadFilesShouldBe(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));
    }

    // убрали аннатоцию тест, потому что у selenide есть баг с загрузкой нескольких файлов и тест конфликтует с FileUploadCellAT
    public void serialTwoFileUploadTest() {
        FileUploadControl fileUpload = getFields().field("FileUpload3").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();

        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/test1.json",
                "net/n2oapp/framework/autotest/control/test2.json");
        fileUpload.uploadFilesShouldBe(2);

        fileUpload.uploadFileShouldHaveLink(0, "http://localhost:" + port + "/files/test1.json");
        fileUpload.uploadFileNameShouldBe(0, "test1.json");
        fileUpload.uploadFileSizeShouldBe(0, "105");
        fileUpload.uploadFileShouldHaveLink(1, "http://localhost:" + port + "/files/test2.json");
        fileUpload.uploadFileNameShouldBe(1, "test2.json");
//        fileUpload.uploadFileSizeShouldBe(1, "91 Б");//работает по разному на разных ОС, windows "105 Б", linux "91 Б"

        assertThat(fileStoreController.getFileStore().size(), is(2));
        fileUpload.deleteFile(1);
        fileUpload.deleteFile(0);
        fileUpload.uploadFilesShouldBe(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));
    }

    private Fields getFields() {
        return simplePage.widget(FormWidget.class).fields();
    }

}
