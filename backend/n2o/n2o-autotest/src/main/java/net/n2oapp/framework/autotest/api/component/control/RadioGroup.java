package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.api.metadata.control.list.N2oRadioGroup;

/**
 * Компонент радиокнопок для автотестирования
 */
public interface RadioGroup extends Control {

    void shouldBeChecked(String label);

    void check(String label);

    void shouldHaveOptions(String... options);

    void shouldHaveType(N2oRadioGroup.RadioGroupType type);
}
