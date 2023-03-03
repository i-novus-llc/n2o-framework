package net.n2oapp.framework.autotest.api.component.field;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.control.Control;

/**
 * Стандартное поле формы для автотестирования
 */
public interface StandardField extends Field {

    /**
     * Возвращает поле ввода для редактирования
     * @param componentClass тип возвращаемого поля
     * @return компонент поле ввода для автотестирования
     */
    <T extends Control> T control(Class<T> componentClass);

    /**
     * @return компонент панель кнопок для автотестирования
     */
    Toolbar toolbar();

    /**
     * Проверка обязательности заполнения поля
     */
    void shouldBeRequired();

    /**
     * Проверка необязательности поля
     */
    void shouldNotBeRequired();

    /**
     * Проверка соответствия метки условию
     * @param condition ожидаемое условие
     */
    void shouldHaveLabelBy(Condition condition);

    /**
     * Проверка положения метки
     * @param position ожидаемое положение метки
     */
    void shouldHaveLabelLocation(FieldSet.LabelPosition position);

    /**
     * Проверка сообщения валидации на соответствие условию
     * @param condition ожидаемое условие
     */
    void shouldHaveValidationMessage(Condition condition);

}
