package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.ElementsCollection;

/**
 * Коллекция компонентов для автотестирования
 */
public interface ComponentsCollection {
    /**
     * Метод позволяющий взаимодействовать с коллекцией web элементов напрямую
     * @return коллекция web-элементов селенида
     */
    ElementsCollection elements();

    /**
     * Устанавливает в поле класса elements соответсвующую коллекцию web-элементов, чтобы далее получать доступ к этим элементам
     * @param elements коллекция web-элементов селенида
     */
    void setElements(ElementsCollection elements);

    /**
     * Проверка количества элементов в коллекции
     * @param size ожидаемое количество элементов
     */
    void shouldHaveSize(int size);

    /**
     * Проверка того, что коллекция пустая
     */
    void shouldBeEmpty();
}
