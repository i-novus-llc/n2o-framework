package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.control.FileUploadControl;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Автотесты поля загрузки файлов
 */
class FileUploadAT extends AutoTestBase {

    private SimplePage simplePage;

    @Autowired
    private FileStoreController fileStoreController;

    @BeforeAll
    static void beforeClass() {
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
        builder.packs(new N2oPagesPack(),
                new N2oApplicationPack(),
                new N2oWidgetsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack(),
                new N2oControlsV2IOPack(),
                new N2oActionsPack(),
                new N2oAllDataPack());
        builder.ios(new TestDataProviderIOv1());
    }

    @Test
    void wrongRestTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/file_upload/index.page.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
        FileUploadControl fileUpload = getFields().field("FileUpload1").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();
        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/test1.json");
        fileUpload.shouldHaveUploadFiles(1);
        fileUpload.uploadFileShouldHaveName(0, "test1.json");
//        fileUpload.uploadFileSizeShouldBe(0, "91 Б");//работает по разному на разных ОС, windows "105 Б", linux "91 Б"
        fileUpload.deleteFile(0);
        fileUpload.shouldHaveUploadFiles(0);
    }

    @Test
    void oneFileUploadTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/file_upload/index.page.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
        FileUploadControl fileUpload = getFields().field("FileUpload2").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();

        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/test1.json");
        fileUpload.shouldHaveUploadFiles(1);

        fileUpload.uploadFileShouldHaveLink(0, "http://localhost:" + port + "/files/test1.json");
        fileUpload.uploadFileShouldHaveName(0, "test1.json");
//        fileUpload.uploadFileSizeShouldBe(0, "91 Б");//работает по разному на разных ОС, windows "105 Б", linux "91 Б"

        assertThat(fileStoreController.getFileStore().size(), is(1));
        fileUpload.deleteFile(0);
        fileUpload.shouldHaveUploadFiles(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));

        // загрузка файла с неразрешенным расширением
        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/file_upload/index.page.xml");
        // Есть вывод ошибки
        fileUpload.shouldHaveUploadFiles(1);
        // загрузка не произошла
        assertThat(fileStoreController.getFileStore().size(), is(0));

    }

    @Test
    @Disabled("У selenide есть баг с загрузкой нескольких файлов и тест конфликтует с FileUploadCellAT")
    void serialTwoFileUploadTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/file_upload/index.page.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
        FileUploadControl fileUpload = getFields().field("FileUpload3").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();

        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/test1.json",
                "net/n2oapp/framework/autotest/control/test2.json");
        fileUpload.shouldHaveUploadFiles(2);

        fileUpload.uploadFileShouldHaveLink(0, "http://localhost:" + port + "/files/test1.json");
        fileUpload.uploadFileShouldHaveName(0, "test1.json");
        fileUpload.uploadFileShouldHaveSize(0, "105");
        fileUpload.uploadFileShouldHaveLink(1, "http://localhost:" + port + "/files/test2.json");
        fileUpload.uploadFileShouldHaveName(1, "test2.json");
//        fileUpload.uploadFileSizeShouldBe(1, "91 Б");//работает по разному на разных ОС, windows "105 Б", linux "91 Б"

        assertThat(fileStoreController.getFileStore().size(), is(2));
        fileUpload.deleteFile(1);
        fileUpload.deleteFile(0);
        fileUpload.shouldHaveUploadFiles(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));
    }

    @Test
    void fileUploaderCleanable() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/file_upload/cleanable/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/file_upload/cleanable/test.object.xml")
        );

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
        FileUploadControl fileUpload = getFields().field("Загрузка файлов").control(FileUploadControl.class);
        fileUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();
        assertThat(fileStoreController.getFileStore().size(), is(0));

        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/test1.json");
        fileUpload.shouldHaveUploadFiles(1);
        fileUpload.uploadFileShouldHaveName(0, "test1.json");
        assertThat(fileStoreController.getFileStore().size(), is(1));

        Toolbar toolbar = simplePage.widget(FormWidget.class).toolbar().bottomRight();

        toolbar.button("Очистить").click();
        fileUpload.shouldHaveUploadFiles(0);

        InputText inputText = simplePage.widget(FormWidget.class).fieldsets().fieldset(0, SimpleFieldSet.class).fields().field("Обязательное поле").control(InputText.class);
        inputText.setValue("Любой текст");
        inputText.shouldHaveValue("Любой текст");
        toolbar.button("Отправить").click();
        getFields().field("Загрузка файлов").shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
    }

    private Fields getFields() {
        return simplePage.widget(FormWidget.class).fields();
    }
}
