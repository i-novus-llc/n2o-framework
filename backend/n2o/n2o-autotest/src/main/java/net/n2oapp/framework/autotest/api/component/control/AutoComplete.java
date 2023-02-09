package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода текста с автозаполнением для автотестирования
 */
public interface AutoComplete extends Control {
    void setValue(String value);

    void click();

    void clear();

    void addTag(String value);

    void removeTag(String value);

    void shouldHaveTags(String... tags);

    void shouldHaveDropdownOptions(String... values);

    void shouldNotHaveDropdownOptions();

    void chooseDropdownOption(String value);
}
