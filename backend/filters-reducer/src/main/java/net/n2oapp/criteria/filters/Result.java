package net.n2oapp.criteria.filters;

import lombok.Getter;
import lombok.Setter;

/**
 * Результат слития двух фильтров
 */
@Getter
@Setter
public class Result {

    /**
     * Тип результата слития
     * success - успешно,
     * conflict - фильтры несовместимы(дают пустое множество),
     * notMergeable - не найдено правило для слития
     */
    public enum Type {
        success, conflict, notMergeable;
    }

    public boolean isSuccess() {
        return getType().equals(Type.success);
    }

    private Filter leftFilter;
    private Filter rightFilter;
    private Filter resultFilter;
    private Type type;

}
