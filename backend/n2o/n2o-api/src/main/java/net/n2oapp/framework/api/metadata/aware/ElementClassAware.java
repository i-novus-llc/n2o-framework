package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.exception.N2oException;
import org.jdom.Element;

/**
 * Знание о типе сущности
 * @param <T> тип сущности
 */
public interface ElementClassAware<T> {
    /**
     * @return тип сущности
     */
    Class<T> getElementClass();

    /**
     * Создание инстанса сущности по элементу
     * @param element элемент
     * @return сущность
     */
    default T newInstance(Element element) {
        try {
            return getElementClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new N2oException(e);
        }
    }
}
