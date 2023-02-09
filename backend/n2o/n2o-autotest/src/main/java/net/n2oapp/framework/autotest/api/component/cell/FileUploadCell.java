package net.n2oapp.framework.autotest.api.component.cell;

import java.io.File;

/**
 * Загрузка файла в ячейке для автотестирования
 */
public interface FileUploadCell extends Cell {

    File uploadFile(File... file);

    File uploadFromClasspath(String... fileName);

    void deleteFile(int index);

    void shouldHaveUploadFilesOfSize(int size);

    void shouldHaveUploadFileNamed(int index, String fileName);

    void shouldHaveUploadFileWithSize(int index, String fileSize);

    void shouldHaveUploadFileWithLink(int index, String href);
}
