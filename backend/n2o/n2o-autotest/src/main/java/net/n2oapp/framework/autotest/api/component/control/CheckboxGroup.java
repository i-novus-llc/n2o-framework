package net.n2oapp.framework.autotest.api.component.control;


/**
 * Компонент группы чекбоксов для автотестирования
 */
public interface CheckboxGroup extends Control {

    /**
     * Выбрать чекбокс по лейблу, если чекбокс выбран, то ничего изменено не будет
     * @param label метка чекбокса, который надо выбрать
     */
    void check(String label);

    /**
     * Снять выбранность чекбокса по метке, если чекбокс не выбран, то ничего изменено не будет
     * @param label метка чекбокса, с которого надо снять выбрать
     */
    void uncheck(String label);

    /**
     * Проверка того, что чекбокс с заданной меткой выбран
     * @param label метка проверяемого чекбокса
     */
    void shouldBeChecked(String label);

    /**
     * Проверка того, что чекбокс с заданной меткой не выбран
     * @param label метка проверяемого чекбокса
     */
    void shouldBeUnchecked(String label);

    /**
     * Проверка существования чекбоксов с заданными метками в чекбокс группе
     * @param labels метки ожидаемых чекбоксов
     */
    void shouldHaveOptions(String... labels);
}
