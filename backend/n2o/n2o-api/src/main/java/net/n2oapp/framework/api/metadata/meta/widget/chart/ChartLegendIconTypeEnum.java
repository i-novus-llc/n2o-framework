package net.n2oapp.framework.api.metadata.meta.widget.chart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип иконок элементов легенды
 */
@RequiredArgsConstructor
@Getter
public enum ChartLegendIconTypeEnum implements N2oEnum {
    LINE("line"),
    SQUARE("square"),
    RECT("rect"),
    CIRCLE("circle"),
    CROSS("cross"),
    DIAMOND("diamond"),
    STAR("star"),
    TRIANGLE("triangle"),
    WYE("wye");

    private final String id;
}
