package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.component.control.FileUploadControl;

import java.io.File;

/**
 * Загрузка файла для автотестирования
 */
public class N2oFileUploadControl extends N2oControl implements FileUploadControl {


    @Override
    public void shouldBeEmpty() {
        //ToDo реализовать
    }

    @Override
    public void shouldHaveValue(String value) {
        //ToDo реализовать
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
        files().get(index).$(".n2o-file-uploader-remove")
                .hover()
                .shouldBe(Condition.visible)
                .click();
    }

    @Override
    public void shouldHaveUploadFiles(int count) {
        files().shouldHave(CollectionCondition.size(count));
    }

    @Override
    public void uploadFileShouldHaveName(int index, String fileName) {
        files().get(index).$(".n2o-file-uploader-file-name")
                .shouldHave(Condition.text(fileName));
    }

    @Override
    public void uploadFileShouldHaveSize(int index, String fileSize) {
        files().get(index).$(".n2o-file-uploader-item-size")
                .shouldHave(Condition.text(fileSize));
    }

    @Override
    public void uploadFileShouldHaveLink(int index, String href) {
        files().get(index).$(".n2o-file-uploader-link")
                .shouldHave(Condition.attribute("href", href));
    }

    protected ElementsCollection files() {
        return element().parent().$$(".n2o-file-uploader-files-list .n2o-file-uploader-files-item");
    }
}
