package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.widget.Widget;

/**
 * Виджеты для автотестирования
 */
public interface Widgets extends ComponentsCollection {
    /**
     * <p>
     *     Возвращает найденный по индексу виджет
     * </p>
     *
     * <p>For example: {@code
     *     content().widget(1, TestWidget.class)
     * }</p>
     *
     * @param index номер виджета на странице
     * @param componentClass возвращаемый тип
     * @return Компонент виджет для автотестирования
     */
    <T extends Widget> T widget(int index, Class<T> componentClass);

    /**
     * <p>
     *     Возвращает первый найденный виджет
     * </p>
     *
     * <p>For example: {@code
     *     content().widget(TestWidget.class)
     * }</p>
     *
     * @param componentClass возвращаемый тип
     * @return Компонент виджет для автотестирования
     */
    <T extends Widget> T widget(Class<T> componentClass);
}
