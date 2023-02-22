package net.n2oapp.framework.autotest.api.component.region;

/**
 * Простой регион для автотестирования
 */
public interface SimpleRegion extends Region {

    /**
     * @return Элемент региона (виджет/регион) для автотестирования
     */
    RegionItems content();
}
