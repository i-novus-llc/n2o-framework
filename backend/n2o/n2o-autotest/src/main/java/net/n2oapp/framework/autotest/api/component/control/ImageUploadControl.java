package net.n2oapp.framework.autotest.api.component.control;

import java.io.File;

/**
 * Загрузка изображения для автотестирования
 */
public interface ImageUploadControl extends Control {

    File uploadImage(File... file);

    File uploadFromClasspath(String... fileName);

    void deleteImage(int index);

    void shouldHavePreview(int index);

    void shouldNotHavePreview(int index);

    void openPreviewDialog(int index);

    void shouldHaveSize(int size);

    void nameInfoShouldExist(int index);

    void nameInfoShouldNotExist(int index);

    void nameShouldBe(int index, String fileName);

    void sizeInfoShouldBeVisible(int index);

    void sizeInfoShouldBeInvisible(int index);

    void sizeShouldBe(int index, String fileSize);

    void uploadImageShouldHaveLink(int index, String href);

}