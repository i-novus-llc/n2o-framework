package net.n2oapp.framework.api.metadata;

import java.util.List;

/**
 * Интерфейс для объектов, имеющих внутренние элементы
 */
public interface Itemable<T> {
    List<T> getItems();
}
