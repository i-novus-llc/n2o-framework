package net.n2oapp.framework.autotest.api.component.fieldset;

/**
 * Филдсет с динамическим числом полей для автотестирования
 */
public interface MultiFieldSet extends FieldSet {
    /**
     * Проверка количества филдсетов
     * @param count ожидаемое количество филдсетов
     */
    void shouldHaveItems(int count);

    /**
     * Возвращает филдсет по номеру
     * @param index номер возвращаемого филдсета
     * @return Элемент филдсета с динамическим числом
     */
    MultiFieldSetItem item(int index);

    /**
     * Проверка наличия кнопки добавления филдсетов
     */
    void shouldHaveAddButton();

    /**
     * Проверка отсутствия кнопки добавления филдсетов
     */
    void shouldNotHaveAddButton();

    /**
     * Проверка доступности кнопки добавления филдсетов
     */
    void addButtonShouldBeEnabled();

    /**
     * Проверка недоступности кнопки добавления филдсетов
     */
    void addButtonShouldBeDisabled();

    /**
     * Проверка метки кнопки добавления филдсетов
     * @param label ожидаемое значение метки
     */
    void addButtonShouldHaveLabel(String label);

    /**
     * Клик на кнопку добавления филдсета
     */
    void clickAddButton();

    /**
     * Проверка наличия кнопки удаления всех филдсетов
     */
    void shouldHaveRemoveAllButton();

    /**
     * Проверка отсутствия кнопки удаления всех филдсетов
     */
    void shouldNotHaveRemoveAllButton();

    /**
     * Проверка метки кнопки удаления всех филдсетов
     * @param label ожидаемое значение метки
     */
    void removeAllButtonShouldHaveLabel(String label);

    /**
     * Клик на кнопку удаления всех филдсетов
     */
    void clickRemoveAllButton();
}
