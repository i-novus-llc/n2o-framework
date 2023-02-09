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

    void shouldHaveNameInfo(int index);

    void shouldNotHaveNameInfo(int index);

    void shouldHaveName(int index, String fileName);

    void shouldHaveVisibleSizeInfo(int index);

    void shouldNotHaveVisibleSizeInfo(int index);

    void shouldHaveSize(int index, String fileSize);

    void shouldHaveUploadAreaShape(ShapeType shape);

    void shouldHaveUploadAreaIcon(String icon);

    void shouldHaveUploadAreaIconWithSize(int size);

    void shouldHaveUploadAreaWithWidth(int width);

    void shouldHaveUploadAreaWithHeight(int height);

    interface PreviewDialog {

        void shouldExists();

        void close();

        void shouldHaveLink(String link);

    }

}
