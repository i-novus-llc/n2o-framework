package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.ImageUploadControl;

import java.io.File;

/**
 * Загрузка изображения для автотестирования
 */
public class N2oImageUploadControl extends N2oControl implements ImageUploadControl {


    @Override
    public void shouldBeEmpty() {

    }

    @Override
    public void shouldHaveValue(String value) {

    }

    @Override
    public File uploadImage(File... file) {
        return element().$("input").uploadFile(file);
    }

    @Override
    public File uploadFromClasspath(String... fileName) {
        return element().$("input").uploadFromClasspath(fileName);
    }

    @Override
    public void deleteImage(int index) {
        element().parent().$$(".n2o-file-uploader-files-item-info  .n2o-image-uploader__watch .n2o-image-uploader__watch--trash")
                .get(index).hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldHavePreview(int index) {
        element().parent().$$(".n2o-file-uploader-files-item-info  .n2o-image-uploader__watch .n2o-image-uploader__watch--eye")
                .get(index).shouldBe(Condition.exist);
    }

    @Override
    public void shouldDontHavePreview(int index) {
        element().parent().$$(".n2o-file-uploader-files-item-info  .n2o-image-uploader__watch .n2o-image-uploader__watch--eye")
                .get(index).shouldNotBe(Condition.exist);
    }

    @Override
    public void openPreviewDialog(int index) {
        element().parent().$$(".n2o-file-uploader-files-item-info  .n2o-image-uploader__watch .n2o-image-uploader__watch--eye")
                .get(index).hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void shouldHaveSize(int size) {
        element().parent().$$(".n2o-image-uploader-container .n2o-image-uploader-files-item").shouldHaveSize(size);
    }

    @Override
    public void nameInfoShouldExist(int index) {
        element().parent().$$(".n2o-file-uploader-files-item-info .n2o-image-uploader-img-info .n2o-image-uploader-img-info__file-name")
                .get(index).shouldBe(Condition.exist);
    }

    @Override
    public void nameInfoShouldNotExist(int index) {
        element().parent().$$(".n2o-file-uploader-files-item-info .n2o-image-uploader-img-info .n2o-image-uploader-img-info__file-name")
                .get(index).shouldNotBe(Condition.exist);
    }

    @Override
    public void nameShouldBe(int index, String fileName) {
        element().parent().$$(".n2o-file-uploader-files-item-info .n2o-image-uploader-img-info .n2o-image-uploader-img-info__file-name")
                .get(index).shouldHave(Condition.text(fileName));
    }

    @Override
    public void sizeInfoShouldBeVisible(int index) {
        element().parent().$$(".n2o-file-uploader-files-item-info .n2o-image-uploader-img-info .n2o-image-uploader-img-info__file-size")
                .get(index).shouldBe(Condition.visible);
    }

    @Override
    public void sizeInfoShouldBeInvisible(int index) {
        element().parent().$$(".n2o-file-uploader-files-item-info .n2o-image-uploader-img-info .n2o-image-uploader-img-info__file-size")
                .get(index).shouldNotBe(Condition.visible);
    }

    @Override
    public void sizeShouldBe(int index, String fileSize) {
        element().parent().$$(".n2o-file-uploader-files-item-info .n2o-image-uploader-img-info .n2o-image-uploader-img-info__file-size")
                .get(index).shouldHave(Condition.text(fileSize));
    }

    @Override
    public void uploadImageShouldHaveLink(int index, String href) {
        element().parent().$$(".n2o-file-uploader-files-item-info .n2o-image-uploader__watch .n2o-image-uploader--img")
                .get(index).shouldHave(Condition.attribute("href", href));
    }

}