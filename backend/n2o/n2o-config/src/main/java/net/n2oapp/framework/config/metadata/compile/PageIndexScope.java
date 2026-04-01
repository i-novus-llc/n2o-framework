package net.n2oapp.framework.config.metadata.compile;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Генератор индекса для уникальной идентификации метаданных на странице
 */
public class PageIndexScope {
    private AtomicInteger index;
    private boolean first = true;
    private String pageId;
    private Map<String, Integer> actionIdCounters = new HashMap<>();

    /**
     * Конструктор индекса со стартовым значением
     *
     * @param start Стартовое значение
     * @param pageId идентификатор страницы
     */
    public PageIndexScope(String pageId, int start) {
        this.index = new AtomicInteger(start);
        this.pageId = pageId;
    }

    /**
     * Конструктор индекса со стартовым значением по умолчанию
     * @param pageId идентификатор страницы
     */
    public PageIndexScope(String pageId) {
        this.pageId = pageId;
        this.index = new AtomicInteger(0);
    }

    /**
     * Получить текущий индекс и увеличить на 1 для следующих
     */
    public int get() {
        this.first = false;
        return index.getAndIncrement();
    }

    /**
     * Получить идентификатор страницы
     */
    public String getPageId() {
        return pageId;
    }

    /**
     * Были ли уже вызовы {@link #get()} ?
     *
     * @return true не были, false были
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * Генерирует уникальный идентификатор кнопки на основе actionId.
     *
     * @param actionId идентификатор действия, на основе которого генерируется ID кнопки
     * @return уникальный идентификатор кнопки в формате actionId или actionId_counter
     */
    public String registerButtonActionId(String actionId) {
        int counter = actionIdCounters.getOrDefault(actionId, 0);
        actionIdCounters.put(actionId, counter + 1);
        return counter == 0 ? actionId : actionId + "_" + counter;
    }
}
