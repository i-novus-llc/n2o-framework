package net.n2oapp.framework.autotest.api.component;

public interface PopupControl extends Element {
    void openPopup();

    void closePopup();

    void shouldBeOpened();

    void shouldBeClosed();
}
