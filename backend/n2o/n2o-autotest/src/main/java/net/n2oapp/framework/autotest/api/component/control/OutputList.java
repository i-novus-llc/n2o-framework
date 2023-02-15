package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.api.metadata.meta.control.OutputList.Direction;

/**
 * Компонент вывода многострочного текста для автотестирования
 */
public interface OutputList extends Control {

    /**
     * Проверка соответствия значений и их разделителя
     * @param separator ожидаемый разделитель
     * @param values ожидаемые значения
     */
    void shouldHaveValues(String separator, String... values);

    /**
     * Проверка соответствия значений и их разделителя у полей с ссылками
     * @param separator ожидаемый разделитель
     * @param values ожидаемые значения
     */
    void shouldHaveLinkValues(String separator, String... values);

    /**
     * Проверка направления отображения данных
     * @param direction ожидаемое направление
     */
    void shouldHaveDirection(Direction direction);

    /**
     * Проверка соответствия ссылки у отображаемой опции
     * @param itemValue значение отображаемой опции
     * @param link ожидаемая ссылка
     */
    void shouldHaveLink(String itemValue, String link);
}
