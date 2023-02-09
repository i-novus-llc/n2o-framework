package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент редактирования текста для автотестирования
 */
public interface TextEditor extends Control {
    void setValue(String value);

    void click();
}
