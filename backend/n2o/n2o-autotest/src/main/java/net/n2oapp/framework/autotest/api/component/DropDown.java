package net.n2oapp.framework.autotest.api.component;

import net.n2oapp.framework.autotest.api.component.badge.Badge;

/**
 * Выпадающий список для автотестирования
 */
public interface DropDown extends Component {

    /**
     * Возвращает элемент из выпадающего списка по номеру
     * @param index номер элемента
     * @return Элемент выпадающего списка для автотестирования
     */
    DropDownItem item(int index);

    /**
     * Проверка количества элементов в выпадающем списке
     * @param size ожидаемое количество
     */
    void shouldHaveItems(int size);

    /**
     * Элемент выпадающего списка для автотестирования
     */
    interface DropDownItem extends Component, Badge {
        void shouldHaveValue(String value);
    }
}
