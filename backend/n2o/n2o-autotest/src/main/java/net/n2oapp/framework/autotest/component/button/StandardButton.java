package net.n2oapp.framework.autotest.component.button;

public interface StandardButton extends Button {
    void click();

    void shouldBeDisabled();
    void shouldBeEnabled();
}
