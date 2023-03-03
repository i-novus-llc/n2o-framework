package net.n2oapp.framework.autotest.api.component.header;

/**
 * Кнопка с выпадающим списком для автотестирования
 */
public interface DropdownMenuItem extends MenuItem {

    /**
     * Возвращает кнопку из выпадающего списка
     * @param index номер возвращаемой кнопки
     * @return Кнопка с ссылкой для автотестирования
     */
    AnchorMenuItem item(int index);

    /**
     * Возвращает элемент из выпадающего списка
     * @param index номер элемента
     * @param componentClass тип возвращаемого элемента
     * @return кнопка меню
     */
    <T extends MenuItem> T item(int index, Class<T> componentClass);

    /**
     * Проверка количества элементов внутри выпадающего списка
     * @param size ожидаемое количество элементов
     */
    void shouldHaveSize(int size);
}
