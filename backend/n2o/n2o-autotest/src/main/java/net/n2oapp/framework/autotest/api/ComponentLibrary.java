package net.n2oapp.framework.autotest.api;

import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.component.Component;

import java.util.Collections;
import java.util.List;

/**
 * Библиотека компонентов для автотестирования
 */
public interface ComponentLibrary {

    /**
     * Возвращает список всех классов компонентов
     */
    default List<Class<? extends Component>> components() {
        return Collections.emptyList();
    }

    /**
     * Возвращает список всех классов коллекций компонентов
     */
    default List<Class<? extends ComponentsCollection>> collections() {
        return Collections.emptyList();
    }
}
