package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.autotest.api.component.page.Page;

import java.io.File;

/**
 * Загрузка изображения для автотестирования
 */
public interface ImageUploadControl extends Control {

    File uploadImage(File... file);

    File uploadFromClasspath(String... fileName);

    void shouldHaveDeleteButton(int index);

    void shouldNotHaveDeleteButton(int index);

    void deleteImage(int index);

    void shouldHavePreview(int index);

    void shouldNotHavePreview(int index);

    PreviewDialog openPreviewDialog(Page page, int index);

    void shouldHaveSize(int size);

    void nameInfoShouldExist(int index);

    void nameInfoShouldNotExist(int index);

    void nameShouldBe(int index, String fileName);

    void sizeInfoShouldBeVisible(int index);

    void sizeInfoShouldNotBeVisible(int index);

    void sizeShouldBe(int index, String fileSize);

    void uploadAreaShapeShouldBe(ShapeType shape);

    void uploadAreaShouldHaveIcon(String icon);

    void uploadAreaIconShouldHaveSize(int size);

    void uploadAreaShouldHaveWidth(int width);

    void uploadAreaShouldHaveHeight(int height);

    interface PreviewDialog {

        void shouldExists();

        void close();

        void shouldHaveLink(String link);

    }

}
