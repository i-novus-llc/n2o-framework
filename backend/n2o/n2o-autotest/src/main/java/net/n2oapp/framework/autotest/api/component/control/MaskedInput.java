package net.n2oapp.framework.autotest.api.component.control;

/**
 * Интерфейс поля {@code <masked-input>} для автотестов
 */
public interface MaskedInput extends MaskedControl {
    /**
     * @return значения из поля ввода
     */
    String getValue();

    /**
     * Проверка наличия единиц измерения
     */
    void shouldHaveMeasure();

    /**
     * Проверка соответствия единицы измерения
     * @param text ожидаемая единица измерения
     */
    void shouldHaveMeasureText(String text);
}
