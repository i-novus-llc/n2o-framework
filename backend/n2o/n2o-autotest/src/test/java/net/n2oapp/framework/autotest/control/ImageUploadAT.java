package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.ImageUploadControl;
import net.n2oapp.framework.autotest.api.component.modal.ImagePreviewModal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.autotest.run.FileStoreController;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.dataprovider.TestDataProviderIOv1;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.codeborne.selenide.Configuration.headless;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Автотесты поля загрузки файлов
 */
public class ImageUploadAT extends AutoTestBase {

    private SimplePage simplePage;

    @Autowired
    private FileStoreController fileStoreController;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
        headless = false;
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/image_upload/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/image_upload/files.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack(), new N2oAllDataPack());

        builder.ios(new TestDataProviderIOv1());
    }

    @Test
    public void defaultImageUploadTest() {
        ImageUploadControl imageUpload = getFields().field("imageUpload1").control(ImageUploadControl.class);
        imageUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();

        imageUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/image_upload/image.png");
        imageUpload.shouldHaveSize(1);

        imageUpload.nameInfoShouldNotExist(0);
        imageUpload.sizeInfoShouldBeInvisible(0);
        imageUpload.shouldDontHavePreview(0);

        assertThat(fileStoreController.getFileStore().size(), is(1));
        imageUpload.deleteImage(0);
        imageUpload.shouldHaveSize(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));
    }

    @Test
    public void oneImageUploadTest() {
        ImageUploadControl imageUpload = getFields().field("imageUpload2").control(ImageUploadControl.class);
        imageUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();

        imageUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/image_upload/image.png");
        imageUpload.shouldHaveSize(1);

        imageUpload.nameInfoShouldExist(0);
        imageUpload.nameShouldBe(0, "image.png");
        imageUpload.sizeInfoShouldBeVisible(0);
        imageUpload.sizeShouldBe(0, "186 Б");
        imageUpload.shouldHavePreview(0);
        imageUpload.openPreviewDialog(0);
        ImagePreviewModal modal = N2oSelenide.modal(ImagePreviewModal.class);
        modal.shouldExists();
        modal.imageLink("http://localhost:" + port + "/files/image.png");
        modal.close();

        assertThat(fileStoreController.getFileStore().size(), is(1));
        imageUpload.deleteImage(0);
        imageUpload.shouldHaveSize(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));

        // загрузка файла с неразрешенным расширением
        imageUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/fileupload/index.page.xml");
        // загрузка не произошла
        imageUpload.shouldHaveSize(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));
    }

    //    @Test
    public void multiImageUploadTest() {
        ImageUploadControl imageUpload = getFields().field("imageUpload3").control(ImageUploadControl.class);
        imageUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();

        imageUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/image_upload/image.png");
        imageUpload.shouldHaveSize(1);
        imageUpload.shouldHavePreview(0);
        imageUpload.openPreviewDialog(0);
        ImagePreviewModal modal = N2oSelenide.modal(ImagePreviewModal.class);
        modal.shouldExists();
        modal.imageLink("http://localhost:" + port + "/files/image.png");
        modal.close();

        //todo загружает этот и предыдущий файл, значение с прошлого инпут поля не очищается и добавляется к текущему
        imageUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/image_upload/image2.png");
        imageUpload.shouldHaveSize(2);
        imageUpload.shouldHavePreview(1);
        imageUpload.openPreviewDialog(1);
        modal = N2oSelenide.modal(ImagePreviewModal.class);
        modal.shouldExists();
        modal.imageLink("http://localhost:" + port + "/files/image2.png");
        modal.close();

        assertThat(fileStoreController.getFileStore().size(), is(2));
        imageUpload.deleteImage(1);
        imageUpload.deleteImage(0);
        imageUpload.shouldHaveSize(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));
    }

    private Fields getFields() {
        return simplePage.single().widget(FormWidget.class).fields();
    }

}