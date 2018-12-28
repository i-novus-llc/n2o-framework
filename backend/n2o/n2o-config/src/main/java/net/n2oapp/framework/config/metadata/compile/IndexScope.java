package net.n2oapp.framework.config.metadata.compile;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Генератор индекса для уникальной идентификации метаданных
 */
public class IndexScope {
    private AtomicInteger index;
    private boolean first = true;

    /**
     * Конструктор индекса со стартовым значением
     *
     * @param start Стартовое значение
     */
    public IndexScope(int start) {
        this.index = new AtomicInteger(start);
    }

    /**
     * Конструктор индекса со стартовым значением по умолчанию
     */
    public IndexScope() {
        index = new AtomicInteger(0);
    }

    /**
     * Получить текущий индекс и уввеличить на 1 для следующих
     */
    public int get() {
        this.first = false;
        return index.getAndIncrement();
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
