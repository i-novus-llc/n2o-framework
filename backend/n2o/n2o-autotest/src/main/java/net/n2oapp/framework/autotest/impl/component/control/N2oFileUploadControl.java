package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.FileUploadControl;

import java.io.File;

/**
 * Загрузка файла для автотестирования
 */
public class N2oFileUploadControl extends N2oControl implements FileUploadControl {


    @Override
    public void shouldBeEmpty() {

    }

    @Override
    public void shouldHaveValue(String value) {

    }

    @Override
    public File uploadFile(File... file) {
        return element().$("input").uploadFile(file);
    }

    @Override
    public File uploadFromClasspath(String... fileName) {
        return element().$("input").uploadFromClasspath(fileName);
    }

    @Override
    public void deleteFile(int index) {
        element().parent().$$(".n2o-file-uploader-files-list .n2o-file-uploader-remove")
                .get(index).hover().shouldBe(Condition.visible).click();
    }

    @Override
    public void uploadFilesShouldBe(int size) {
        element().parent().$$(".n2o-file-uploader-files-item").shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void uploadFileNameShouldBe(int index, String fileName) {
        element().parent().$$(".n2o-file-uploader-files-list .n2o-file-uploader-file-name")
                .get(index).shouldHave(Condition.text(fileName));
    }

    @Override
    public void uploadFileSizeShouldBe(int index, String fileSize) {
        element().parent().$$(".n2o-file-uploader-files-list .n2o-file-uploader-item-size")
                .get(index).shouldHave(Condition.text(fileSize));
    }

    @Override
    public void uploadFileShouldHaveLink(int index, String href) {
        element().parent().$$(".n2o-file-uploader-files-list .n2o-file-uploader-link")
                .get(index).shouldHave(Condition.attribute("href", href));
    }
}
