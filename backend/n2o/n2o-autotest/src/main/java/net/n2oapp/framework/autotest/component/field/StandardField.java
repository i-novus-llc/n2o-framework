package net.n2oapp.framework.autotest.component.field;

import net.n2oapp.framework.autotest.N2oMatcher;
import net.n2oapp.framework.autotest.component.control.Control;

public interface StandardField extends Field {
    <T extends Control> T control(Class<T> componentClass);

    void shouldBeRequired();
    void shouldNotBeRequired();
    void shouldHaveLabel(N2oMatcher which);
    void shouldHaveMessage(N2oMatcher which);
    void shouldHaveDescription(N2oMatcher which);

}
