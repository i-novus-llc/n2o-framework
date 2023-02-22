package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент радиокнопок для автотестирования
 */
public interface RadioGroup extends Control {

    /**
     * Проверка того, что радио-кнопка с заданной меткой выбрана
     * @param label метка проверяемой радио-кнопки
     */
    void shouldBeChecked(String label);

    /**
     * Выбрать радио-кнопку по метке, если радио-кнопка выбрана, то ничего изменено не будет
     * @param label метка радио-кнопки, которую надо выбрать
     */
    void check(String label);

    /**
     * Проверка существования радио-кнопок с заданными метками в радио-группе
     * @param labels метки ожидаемых радио-кнопок
     */
    void shouldHaveOptions(String... labels);

    /**
     * Проверка соответствия типа радио-кнопок
     * @param type ожидаемый тип радио-кнопок
     */
    void shouldHaveType(RadioType type);

    enum RadioType {
        DEFAULT,
        BTN,
        TABS;

        public String name(String prefix) {
            return prefix + name().toLowerCase();
        }
    }
}
