package net.n2oapp.framework.autotest.api.component.cell;

import java.io.File;

/**
 * Загрузка файла в ячейке для автотестирования
 */
public interface FileUploadCell extends Cell {

    File uploadFile(File... file);

    File uploadFromClasspath(String... fileName);

    void deleteFile(int index);

    void uploadFilesShouldBe(int size);

    void uploadFileNameShouldBe(int index, String fileName);

    void uploadFileSizeShouldBe(int index, String fileSize);

    void uploadFileShouldHaveLink(int index, String href);
}
