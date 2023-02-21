package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.component.badge.Badge;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.Expandable;

/**
 * Виджет дерево для автотестирования
 */
public interface TreeWidget extends StandardWidget {

    /**
     * Возвращает элемент дерева по номеру
     * @param index номер возвращаемого элемента
     * @return Элемент дерева для автотестирования
     */
    TreeWidget.TreeItem item(int index);

    /**
     * Проверка количества элементов в дереве
     * @param size ожидаемое количество элементов
     */
    void shouldHaveItems(int size);

    /**
     * Элемент дерева для автотестирования
     */
    interface TreeItem extends Expandable, Component, Badge {

        /**
         * Проверяет наличие элемента по метки
         * @param label метка для поиска
         */
        void shouldHaveItem(String label);
    }
}
