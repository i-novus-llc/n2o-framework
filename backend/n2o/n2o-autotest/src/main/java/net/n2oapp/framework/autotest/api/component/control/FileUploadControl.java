package net.n2oapp.framework.autotest.api.component.control;

import java.io.File;

/**
 * Загрузка файла для автотестирования
 */
public interface FileUploadControl extends Control {

    File uploadFile(File... file);

    File uploadFromClasspath(String... fileName);

    void deleteFile(int index);

    void uploadFilesShouldBe(int size);

    void uploadFileNameShouldBe(int index, String fileName);

    void uploadFileSizeShouldBe(int index, String fileSize);
}
