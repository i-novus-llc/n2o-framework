package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент редактирования текста для автотестирования
 */
public interface TextEditor extends Control {

    /**
     * Установка значения в поле
     * @param value устанавливаемое значение
     */
    void setValue(String value);

    /**
     * Клик по полю
     */
    void click();
}
