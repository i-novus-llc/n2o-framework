package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.api.component.cell.FileUploadCell;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
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
 * Автотесты поля загрузки файлов в ячейке
 */
class FileUploadCellAT extends AutoTestBase {

    @Autowired
    private FileStoreController fileStoreController;

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oCellsPack(), new N2oControlsV2IOPack(), new N2oQueriesPack());
        builder.ios(new TestDataProviderIOv1());
    }

    @Test
    void oneFileUploadTest() {
        setResourcePath("net/n2oapp/framework/autotest/cells/file_upload/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/file_upload/simple/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/file_upload/simple/index.page.xml"));
        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();
        TableWidget tableWidget = simplePage.widget(TableWidget.class);
        tableWidget.shouldExists();
        TableWidget.Rows rows = tableWidget.columns().rows();
        FileUploadCell fileUpload = rows.row(0).cell(0, FileUploadCell.class);
        fileUpload.shouldExists();
        fileStoreController.clearFileStore();

        // загрузка файла с неразрешенным расширением
        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/cells/file_upload/simple/index.page.xml");
        // Есть вывод ошибки
        fileUpload.shouldHaveSize(1);
        // загрузка не произошла
        assertThat(fileStoreController.getFileStore().size(), is(0));

        fileUpload.deleteFile(0);
        fileUpload.shouldHaveSize(0);

        // загрузка нормального файла
        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/test1.json");
        fileUpload.shouldHaveSize(1);
        fileUpload.uploadFileShouldHaveLink(0, "http://localhost:" + port + "/files/test1.json");
        fileUpload.uploadFileShouldHaveName(0, "test1.json");
        assertThat(fileStoreController.getFileStore().size(), is(1));
        fileUpload.deleteFile(0);
        fileUpload.shouldHaveSize(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));
    }

    @Test
    @Disabled("У selenide есть баг с загрузкой нескольких файлов и тест конфликтует с FileUploadAT")
    void multiFileUploadTest() {
        setResourcePath("net/n2oapp/framework/autotest/cells/file_upload/multi");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/file_upload/multi/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/file_upload/multi/index.page.xml"));
        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();
        TableWidget tableWidget = simplePage.widget(TableWidget.class);
        tableWidget.shouldExists();
        TableWidget.Rows rows = tableWidget.columns().rows();
        FileUploadCell fileUpload = rows.row(0).cell(0, FileUploadCell.class);
        fileUpload.shouldExists();
        fileStoreController.clearFileStore();

        // загрузка нормального файла
        fileUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/test1.json",
                "net/n2oapp/framework/autotest/control/test2.json");
        fileUpload.shouldHaveSize(2);
        fileUpload.uploadFileShouldHaveLink(0, "http://localhost:" + port + "/files/test1.json");
        fileUpload.uploadFileShouldHaveName(0, "test1.json");
        fileUpload.uploadFileShouldHaveLink(1, "http://localhost:" + port + "/files/test2.json");
        fileUpload.uploadFileShouldHaveName(1, "test2.json");
        assertThat(fileStoreController.getFileStore().size(), is(2));
        fileUpload.deleteFile(0);
        fileUpload.shouldHaveSize(1);
        assertThat(fileStoreController.getFileStore().size(), is(1));
    }
}
