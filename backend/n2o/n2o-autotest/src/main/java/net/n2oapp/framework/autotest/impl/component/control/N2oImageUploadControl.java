package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.autotest.api.component.control.ImageUploadControl;
import net.n2oapp.framework.autotest.api.component.page.Page;

import java.io.File;
import java.time.Duration;

/**
 * Загрузка изображения для автотестирования
 */
public class N2oImageUploadControl extends N2oControl implements ImageUploadControl {
    private static final String UPLOAD_SHAPE_CLASS = "n2o-image-uploader-control--shape-";

    @Override
    public void shouldBeEmpty() {
        //ToDo: реализовать
        throw new UnsupportedOperationException();
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        //ToDo: реализовать
        throw new UnsupportedOperationException();
    }

    @Override
    public File uploadImage(File... image) {
        return element().$("input").uploadFile(image);
    }

    @Override
    public File uploadFromClasspath(String... imageName) {
        return element().$("input").uploadFromClasspath(imageName);
    }

    @Override
    public void shouldHaveDeleteButton(int index) {
        getTrashElement(index).shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHaveDeleteButton(int index) {
        getTrashElement(index).shouldNotBe(Condition.exist);
    }

    @Override
    public void deleteImage(int index) {
        getTrashElement(index).hover().shouldBe(Condition.visible).click();
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
    public PreviewDialog openPreviewDialog(Page page, int index) {
        getPreviewElement(index).hover().shouldBe(Condition.visible).click();
        return new PreviewDialogImpl(page);
    }

    @Override
    public void shouldHaveSize(int size) {
        getFilesItems().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void shouldHaveNameInfo(int index) {
        getNameElement(index).shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHaveNameInfo(int index) {
        getNameElement(index).shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldHaveName(int index, String fileName, Duration... duration) {
        should(Condition.text(fileName), getNameElement(index), duration);
    }

    @Override
    public void shouldHaveVisibleSizeInfo(int index) {
        getSizeElement(index).shouldBe(Condition.visible);
    }

    @Override
    public void shouldNotHaveVisibleSizeInfo(int index) {
        getSizeElement(index).shouldNotBe(Condition.visible);
    }

    @Override
    public void shouldHaveSize(int index, String fileSize) {
        getSizeElement(index).shouldHave(Condition.text(fileSize));
    }

    @Override
    public void uploadAreaShouldHaveShape(ShapeType shape) {
        element().shouldHave(Condition.cssClass(String.format("%s%s", UPLOAD_SHAPE_CLASS, shape.getId())));
    }

    @Override
    public void uploadAreaShouldHaveIcon(String icon) {
        getUploadAreaElement().shouldHave(Condition.attribute("class", icon));
    }

    @Override
    public void uploadAreaShouldHaveIconSize(int size) {
        getUploadAreaElement().shouldHave(Condition.attributeMatching("style",
                String.format(".*font-size: %dpx.*", size)));
    }

    @Override
    public void uploadAreaShouldHaveWidth(int width) {
        element().shouldHave(Condition.attributeMatching("style",
                String.format(".*max-width: %dpx.*", width)));
    }

    @Override
    public void uploadAreaShouldHaveHeight(int height) {
        element().shouldHave(Condition.attributeMatching("style",
                String.format(".*max-height: %dpx.*", height)));
    }

    @Override
    public void shouldBeDisabled() {
        element().$("input").shouldBe(Condition.disabled);
    }

    protected ElementsCollection getFilesItems() {
        return element().parent().$$(".n2o-file-uploader-files-item-info");
    }

    protected SelenideElement getPreviewElement(int index) {
        return getFilesItems().get(index)
                .$(".n2o-image-uploader__watch .n2o-image-uploader__watch--eye");
    }

    protected SelenideElement getNameElement(int index) {
        return getFilesItems().get(index)
                .$(".n2o-image-uploader-img-info .n2o-image-uploader-img-info__file-name");
    }

    protected SelenideElement getSizeElement(int index) {
        return getFilesItems().get(index)
                .$(".n2o-image-uploader-img-info .n2o-image-uploader-img-info__file-size");
    }

    protected SelenideElement getUploadAreaElement() {
        return element().$("div");
    }

    protected SelenideElement getTrashElement(int index) {
        return getFilesItems().get(index)
                .$(".n2o-image-uploader__watch .n2o-image-uploader__watch--trash");
    }

    public static class PreviewDialogImpl implements PreviewDialog {

        private final SelenideElement element;

        public PreviewDialogImpl(Page page) {
            element = page.element().$(".n2o-image-uploader__modal--body");
        }

        @Override
        public void shouldExists() {
            element.should(Condition.exist);
        }

        @Override
        public void shouldHaveLink(String link) {
            element.$(".n2o-image-uploader__modal--image")
                    .shouldHave(Condition.attribute("src", link));
        }

        @Override
        public void close() {
            element.$(".n2o-image-uploader__modal--icon-close")
                    .click();
        }
    }

}
