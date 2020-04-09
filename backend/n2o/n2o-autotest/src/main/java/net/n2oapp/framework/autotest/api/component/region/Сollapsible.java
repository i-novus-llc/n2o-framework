package net.n2oapp.framework.autotest.api.component.region;

/**
 * Регион который можно свернуть
 */
public interface Сollapsible {
    void toggleCollapse();

    void shouldBeExpanded();

    void shouldBeCollapsed();
}
