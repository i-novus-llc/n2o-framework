package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент Таблетки для автотестирования
 */
public interface Pills extends Control {

    /**
     * Выбор поля по метке
     * @param label метка поля
     */
    void check(String label);

    /**
     * Снять выбранность таблетки по метке
     * @param label метка поля
     */
    void uncheck(String label);

    /**
     * Проверка выбранности поля по метке
     * @param label метка поля
     */
    void shouldBeChecked(String label);

    /**
     * Проверка не выбранности поля по метке
     * @param label метка поля
     */
    void shouldBeUnchecked(String label);

    /**
     * Проверка наличия опций
     * @param options ожидаемые опция
     */
    void shouldHaveOptions(String... options);
}
