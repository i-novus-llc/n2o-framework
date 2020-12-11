package net.n2oapp.framework.autotest.api.component.region;

/**
 * Регион с горизонтальным делителем для автотестирования
 */
public interface LineRegion extends Region, Сollapsible {
    RegionItems content();

    void shouldBeCollapsible();

    void shouldNotBeCollapsible();

    void shouldHaveLabel(String title);
}
