package net.n2oapp.framework.api.util;

/**
 * Загрузка файлов, в которые выносится часть кода из метаданных
 */
public interface ExternalFilesLoader {

    /**
     * Получение содержимого файла, например js или html файла используемого в метаданных
     * @param uri   путь к файлу
     * @return   содержимое файла
     */
    String getContentByUri(String uri);
}
