package net.n2oapp.framework.autotest.api.component.button;

public interface StandardButton extends Button {
    void click();

    void shouldBeDisabled();
    void shouldBeEnabled();
}
