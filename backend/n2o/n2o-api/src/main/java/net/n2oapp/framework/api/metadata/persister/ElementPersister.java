package net.n2oapp.framework.api.metadata.persister;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Интерфейс для преобразования entity в элемент доступа
 * User: iryabov
 * Date: 06.09.13
 * Time: 16:45
 */
public interface ElementPersister<T> {

    /**
     * Собирает из entity элемент доступа
     * @param entity инстанс нужного нам типа
     * @return элемент доступа
     */
    Element persist(T entity, Namespace namespace);
}
