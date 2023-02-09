package net.n2oapp.framework.autotest.api.component.control;

import java.io.File;

/**
 * Загрузка файла для автотестирования
 */
public interface FileUploadControl extends Control {

    File uploadFile(File... file);

    File uploadFromClasspath(String... fileName);

    void deleteFile(int index);

    void shouldHaveUploadFilesOfSize(int size);

    void shouldHaveUploadFileNamed(int index, String fileName);

    void shouldHaveUploadFileWithSize(int index, String fileSize);

    void shouldHaveUploadFileWithLink(int index, String href);
}
