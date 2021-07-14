package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.ImageUploadControl;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.autotest.run.FileStoreController;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.dataprovider.TestDataProviderIOv1;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Автотесты поля загрузки изображений
 */
public class ImageUploadAT extends AutoTestBase {

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

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/image_upload/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/image_upload/files.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        simplePage = open(SimplePage.class);
        simplePage.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());

        builder.ios(new TestDataProviderIOv1());
    }

    @Test
    public void defaultImageUploadTest() {
        ImageUploadControl imageUpload = getFields().field("imageUpload1").control(ImageUploadControl.class);
        imageUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();

        imageUpload.uploadAreaShouldHaveIcon("fa fa-upload");

        imageUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/image_upload/image.png");
        imageUpload.shouldHaveSize(1);

        imageUpload.nameInfoShouldNotExist(0);
        imageUpload.sizeInfoShouldNotBeVisible(0);
        imageUpload.shouldNotHavePreview(0);

        assertThat(fileStoreController.getFileStore().size(), is(1));
        imageUpload.shouldHaveDeleteButton(0);
        imageUpload.deleteImage(0);
        imageUpload.shouldHaveSize(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));
    }

    @Test
    public void oneImageUploadTest() {
        ImageUploadControl imageUpload = getFields().field("imageUpload2").control(ImageUploadControl.class);
        imageUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();

        imageUpload.shouldHaveSize(0);
        imageUpload.uploadAreaShouldHaveIcon("fa fa-plus");
        imageUpload.uploadAreaIconShouldHaveSize(100);
        imageUpload.uploadAreaShouldHaveWidth(200);
        imageUpload.uploadAreaShouldHaveHeight(200);

        // загрузка файла с неразрешенным расширением
        imageUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/fileupload/index.page.xml");
        // загрузка не произошла
        imageUpload.shouldHaveSize(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));

        imageUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/image_upload/image.png");
        imageUpload.shouldHaveSize(1);

        imageUpload.nameInfoShouldExist(0);
        imageUpload.uploadAreaShapeShouldBe(ImageShape.circle);
        imageUpload.nameShouldBe(0, "image.png");
        imageUpload.sizeShouldBe(0, "186");
        imageUpload.shouldHavePreview(0);

        ImageUploadControl.PreviewDialog previewDialog = imageUpload.openPreviewDialog(simplePage, 0);
        previewDialog.shouldExists();
        previewDialog.shouldHaveLink("http://localhost:" + port + "/files/image.png");
        previewDialog.close();

        assertThat(fileStoreController.getFileStore().size(), is(1));
        imageUpload.shouldNotHaveDeleteButton(0);
    }

    @Test
    public void multiImageUploadTest() {
        ImageUploadControl imageUpload = getFields().field("imageUpload3").control(ImageUploadControl.class);
        imageUpload.shouldBeEnabled();
        fileStoreController.clearFileStore();
        assertThat(fileStoreController.getFileStore().size(), is(0));

        imageUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/image_upload/image.png");
        imageUpload.shouldHaveSize(1);
        assertThat(fileStoreController.getFileStore().size(), is(1));
        imageUpload.shouldHavePreview(0);

        ImageUploadControl.PreviewDialog previewDialog = imageUpload.openPreviewDialog(simplePage, 0);
        previewDialog.shouldExists();
        previewDialog.shouldHaveLink("http://localhost:" + port + "/files/image.png");
        previewDialog.close();

        Selenide.refresh();

        imageUpload.uploadFromClasspath("net/n2oapp/framework/autotest/control/image_upload/image2.png");
        imageUpload.shouldHaveSize(2);
        assertThat(fileStoreController.getFileStore().size(), is(2));
        imageUpload.shouldHavePreview(1);

        previewDialog = imageUpload.openPreviewDialog(simplePage, 1);
        previewDialog.shouldExists();
        previewDialog.shouldHaveLink("http://localhost:" + port + "/files/image2.png");
        previewDialog.close();

        assertThat(fileStoreController.getFileStore().size(), is(2));
        imageUpload.deleteImage(1);
        imageUpload.deleteImage(0);
        imageUpload.shouldHaveSize(0);
        assertThat(fileStoreController.getFileStore().size(), is(0));
    }

    private Fields getFields() {
        return simplePage.widget(FormWidget.class).fields();
    }

}