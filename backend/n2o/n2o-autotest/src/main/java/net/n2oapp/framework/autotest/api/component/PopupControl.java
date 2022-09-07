package net.n2oapp.framework.autotest.api.component;

public interface PopupControl extends Element {
    void openPopup();

    void closePopup();

    void shouldBeOpened();

    void shouldBeClosed();

    @Deprecated
    void expand();

    @Deprecated
    void collapse();

    @Deprecated
    void shouldBeExpanded();

    @Deprecated
    void shouldBeCollapsed();
}
