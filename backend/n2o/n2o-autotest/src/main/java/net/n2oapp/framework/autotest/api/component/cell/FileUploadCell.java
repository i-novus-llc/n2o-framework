package net.n2oapp.framework.autotest.api.component.cell;

import java.io.File;

/**
 * Загрузка файла в ячейке для автотестирования
 */
public interface FileUploadCell extends Cell {

    /**
     * Загрузка файла(ов) в ячейку
     * @param file массив загружаемых файлов
     * @return Первый загруженный файл
     */
    File uploadFile(File... file);

    /**
     * Загрузка файла(ов) в ячейку
     * @param fileName имена загружаемых файлов из classpath
     * @return Первый загруженный файл
     */
    File uploadFromClasspath(String... fileName);

    /**
     * Удаление файла по номеру
     * @param index номер удаляемого файла
     */
    void deleteFile(int index);

    /**
     * Проверка количества загруженных файлов на соответствие ожидаемому количеству
     * @param size ожидаемое количество загруженных файлов
     */
    void shouldHaveSize(int size);

    /**
     * Проверка того, что имя файла по индексу соответствует ожидаемому значению
     * @param index индекс проверяемого файла
     * @param fileName ожидаемое именя файла
     */
    void uploadFileShouldHaveName(int index, String fileName);

    /**
     * Проверка того, что размер файла по индексу соответствует ожидаемому значению
     * @param index индекс проверяемого файла
     * @param fileSize ожидаемый размер файла
     */
    void uploadFileShouldHaveSize(int index, String fileSize);

    /**
     * Проверка того, что ссылка на файл по индексу соответствует ожидаемому значению
     * @param index индекс проверяемого файла
     * @param href ожидаемая ссылка на файл
     */
    void uploadFileShouldHaveLink(int index, String href);
}
