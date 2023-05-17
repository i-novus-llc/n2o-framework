package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.collection.ComponentsCollection;
import net.n2oapp.framework.autotest.api.component.widget.Widget;

/**
 * Элемент региона (виджет/регион) для автотестирования
 */
public interface RegionItems extends ComponentsCollection {

    /**
     * Возвращает виджет по номеру на странице
     * @param index номер виджета
     * @param componentClass возвращаемый тип виджета
     * @return Виджет для автотестирования
     */
    <T extends Widget> T widget(int index, Class<T> componentClass);

    /**
     * Возвращает первый найденный виджет
     * @param componentClass возвращаемый тип виджета
     * @return Виджет для автотестирования
     */
    <T extends Widget> T widget(Class<T> componentClass);

    /**
     * Возвращает регион по номеру на странице
     * @param index номер региона
     * @param componentClass возвращаемый тип региона
     * @return Регион для автотестирования
     */
    <T extends Region> T region(int index, Class<T> componentClass);

    /**
     * Возвращает первый найденный регион
     * @param componentClass возвращаемый тип региона
     * @return Регион для автотестирования
     */
    <T extends Region> T region(Class<T> componentClass);
}
