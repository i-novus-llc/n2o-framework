package net.n2oapp.framework.autotest.api.component;

/**
 *
 */
public interface Dropdown {
    void expand();

    void collapse();

    void shouldBeExpanded();

    void shouldBeCollapsed();
}
