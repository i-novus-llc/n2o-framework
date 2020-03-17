package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент Checkbox для автотестирования
 */
public interface Checkbox extends Control {
    boolean isChecked();

    void setChecked(boolean val);

    void shouldBeChecked();

    void shouldBeUnchecked();
}
