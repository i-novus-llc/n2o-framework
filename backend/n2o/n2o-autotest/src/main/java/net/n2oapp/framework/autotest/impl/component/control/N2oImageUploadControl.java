package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.ImageUploadControl;
import net.n2oapp.framework.autotest.api.component.page.Page;

import java.io.File;

/**
 * Загрузка изображения для автотестирования
 */
public class N2oImageUploadControl extends N2oControl implements ImageUploadControl {


    private Page page;

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
        getPreviewElement(index).shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHavePreview(int index) {
        getPreviewElement(index).shouldNotBe(Condition.exist);
    }

    @Override
    public void openPreviewDialog(Page page, int index) {
        this.page = page;
        getPreviewElement(index).hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void closePreviewDialog() {
        page.element().$(".n2o-image-uploader__modal--body .n2o-image-uploader__modal--icon-close").click();
    }

    @Override
    public void previewDialogShouldExists() {
        page.element().$(".n2o-image-uploader__modal--body").should(Condition.exist);
    }

    @Override
    public void previewDialogShouldHaveLink(String link) {
        page.element().$(".n2o-image-uploader__modal--body .n2o-image-uploader__modal--image").shouldHave(Condition.attribute("src", link));
    }

    @Override
    public void shouldHaveSize(int size) {
        element().parent().$$(".n2o-image-uploader-container .n2o-image-uploader-files-item").shouldHaveSize(size);
    }

    @Override
    public void nameInfoShouldExist(int index) {
        getNameElement(index).shouldBe(Condition.exist);
    }

    @Override
    public void nameInfoShouldNotExist(int index) {
        getNameElement(index).shouldNotBe(Condition.exist);
    }

    @Override
    public void nameShouldBe(int index, String fileName) {
        getNameElement(index).shouldHave(Condition.text(fileName));
    }

    @Override
    public void sizeInfoShouldBeVisible(int index) {
        getSizeElement(index).shouldBe(Condition.visible);
    }

    @Override
    public void sizeInfoShouldBeInvisible(int index) {
        getSizeElement(index).shouldNotBe(Condition.visible);
    }

    @Override
    public void sizeShouldBe(int index, String fileSize) {
        getSizeElement(index).shouldHave(Condition.text(fileSize));
    }

    @Override
    public void uploadImageShouldHaveLink(int index, String href) {
        element().parent().$$(".n2o-file-uploader-files-item-info .n2o-image-uploader__watch .n2o-image-uploader--img")
                .get(index).shouldHave(Condition.attribute("href", href));
    }

    private SelenideElement getPreviewElement(int index) {
        return element().parent().$$(".n2o-file-uploader-files-item-info  .n2o-image-uploader__watch .n2o-image-uploader__watch--eye")
                .get(index);
    }

    private SelenideElement getNameElement(int index) {
        return element().parent().$$(".n2o-file-uploader-files-item-info .n2o-image-uploader-img-info .n2o-image-uploader-img-info__file-name")
                .get(index);
    }

    private SelenideElement getSizeElement(int index) {
        return element().parent().$$(".n2o-file-uploader-files-item-info .n2o-image-uploader-img-info .n2o-image-uploader-img-info__file-size")
                .get(index);
    }

}