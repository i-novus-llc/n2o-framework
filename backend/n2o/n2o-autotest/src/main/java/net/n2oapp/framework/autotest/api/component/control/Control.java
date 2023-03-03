package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Общий интерфейс для всех компонентов ввода для автотестирования
 */
public interface Control extends Component {
    /**
     * Проверка доступности поля
     */
    void shouldBeEnabled();

    /**
     * Проверка недоступности поля
     */
    void shouldBeDisabled();

    /**
     * Проверка пустоты поля
     */
    void shouldBeEmpty();

    /**
     * Проверка значения в поле
     * @param value ожидаемое значение
     */
    void shouldHaveValue(String value);
}
