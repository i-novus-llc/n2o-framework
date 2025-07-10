package net.n2oapp.framework.sandbox.file_storage;

import net.n2oapp.framework.sandbox.file_storage.model.FileModel;

import java.util.List;

public interface FileStorage {

    /**
     * Сохранение файла
     *
     * @param projectId Идентификатор проекта
     * @param file      Имя файла
     * @param source    Содержимое файла
     */
    void saveFile(String projectId, String file, String source);

    /**
     * Получение содержимого файла
     *
     * @param projectId Идентификатор проекта
     * @param file      Имя файла
     * @return Содержимое файла
     */
    String getFileContent(String projectId, String file);

    /**
     * Получение списка файлов
     *
     * @param projectId Идентификатор проекта
     * @return Список моделей файлов
     */
    List<FileModel> getProjectFiles(String projectId);

    /**
     * Проверка существования проекта
     *
     * @param projectId Идентификатор проекта
     * @return true - проект существует, false - проект не существует
     */
    boolean isProjectExists(String projectId);
}
