package net.n2oapp.framework.config.metadata.compile;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Генератор индекса для уникальной идентификации метаданных на странице
 */
public class PageIndexScope {
    private AtomicInteger index;
    private boolean first = true;
    private String pageId;

    /**
     * Конструктор индекса со стартовым значением
     *
     * @param start Стартовое значение
     * @param pageId идентификатор страницы
     */
    public PageIndexScope(String pageId, int start)
    {
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
}
