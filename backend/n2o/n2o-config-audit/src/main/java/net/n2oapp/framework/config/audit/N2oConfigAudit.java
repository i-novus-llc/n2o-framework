package net.n2oapp.framework.config.audit;

import net.n2oapp.framework.config.register.audit.model.N2oConfigCommit;
import net.n2oapp.framework.config.register.audit.model.N2oConfigConflict;
import net.n2oapp.framework.config.register.audit.model.N2oConfigHistory;

import java.util.List;

/**
 * Аудит конфигураций
 */
public interface N2oConfigAudit {

    /**
     * Инициализация аудита
     */
    void init();

    /**
     * Включен ли аудит
     * @return  true - если аудит включен, иначе - false
     */
    boolean isEnabled();

    /**
     * Проверка статуса аудита. Возвращает информацию о состоянии аудита.
     * @return состояние аудита
     */
    String checkStatus();

    /**
     * Слитие системной ветки в сервеную
     */
    void merge(String branchName, boolean isOrigin);

    /**
     * коммит
     * @param localPath путь к директории, файлы из которой будут закоммичены, или путь к конкретному файлу
     * @param message сообщение коммита
     */
    void commit(String localPath, String message);

    /**
     * коммит всех файлов в рабочей директории
     * @param message сообщение коммита
     */
    void commitAll(String message);

    N2oConfigConflict retrieveConflict(String localFilePath);

    String retrieveGraph();

    /**
     * возвращает историю изменений репозитория
     * @return история изменений
     */
    List<N2oConfigCommit> getCommits();

    /**
     * получение истории конкретного файла, вместе с измененем содержимого
     * @param localPath путь к файлу
     * @return история изменений файла
     */
    List<N2oConfigHistory> getHistory(String localPath);

    /**
     * Восстановление состояния после возможного падения
     */
    void reestablish();

    /**
     * Обновление данных в системной ветке
     */
    void updateSystem();

    /**
     * Получить изменения из центрального репозитория
     */
    void pull();

    /**
     * Отправить изменения в центральный репозиторий
     */
    void push();

    /**
     * Синхронизироваться с центральным репозиторием
     */
    default void synchronize() {
        pull();
        push();
    }

    /**
     * возможно ли выполнить обновление системной ветки
     * @return  true - если можно, false - если нет
     */
    boolean isUpdatable();

    void setUpdatable(boolean updatable);
}
