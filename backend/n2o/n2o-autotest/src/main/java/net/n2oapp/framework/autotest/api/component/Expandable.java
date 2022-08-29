package net.n2oapp.framework.autotest.api.component;

/**
 * Компоненты с выпадающим списком
 */
public interface Expandable {
    void expand();

    void collapse();

    void shouldBeExpanded();

    void shouldBeCollapsed();
}
