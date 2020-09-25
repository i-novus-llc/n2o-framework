package net.n2oapp.framework.autotest.api.component.region;

/**
 * Регион который можно свернуть
 */
public interface Сollapsible {
    void expandContent();

    void collapseContent();

    void shouldBeExpanded();

    void shouldBeCollapsed();
}
