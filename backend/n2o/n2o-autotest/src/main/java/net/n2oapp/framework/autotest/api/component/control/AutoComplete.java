package net.n2oapp.framework.autotest.api.component.control;

import java.time.Duration;

/**
 * Компонент ввода текста с автозаполнением для автотестирования
 */
public interface AutoComplete extends Control {
    /**
     * Устанавливает текст в поле ввода
     *
     * <p>For example:
     * {@code autoComplete.click();
     * autoComplete.setValue("текст");
     * } </p>
     *
     * @param value вводимый текст
     */
    void setValue(String value);

    /**
     * Клик по полю ввода
     */
    void click();

    /**
     * Очистка поля ввода
     *
     * <p>For example:
     * {@code autoComplete.click();
     * autoComplete.clear();
     * } </p>
     */
    void clear();

    /**
     * Нажатие по кнопки Enter, для добавления тэга
     */
    void enter();

    /**
     * Удаление тега в случае, если значения в поле отображаются тегами
     * @param value значение удаляемого тега
     */
    void removeTag(String value);

    /**
     * Проверка выбранных тегов
     * @param tags список ожидаемых выбранных тегов
     */
    void shouldHaveTags(String[] tags, Duration... duration);

    /**
     * Проверка наличия опций в раскрывающемся списке
     * @param values список ожидаемых опций
     */
    void shouldHaveDropdownOptions(String[] values, Duration... duration);

    /**
     * Проверка того, что нет ни одной опции в раскрывающемся списке
     */
    void shouldNotHaveDropdownOptions();

    /**
     * Выбор опции из раскрывающегося списка
     * @param value значения выбираемой опции
     */
    void chooseDropdownOption(String value);
}
