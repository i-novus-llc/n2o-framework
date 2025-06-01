package net.n2oapp.framework.api.metadata.meta.widget.chart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Виды графиков / диаграмм
 */
@RequiredArgsConstructor
@Getter
public enum ChartTypeEnum implements N2oEnum {
    AREA("area"),       // диаграмма-область
    BAR("bar"),        // гистограмма
    LINE("line"),       // линейный график
    PIE("pie");         // круговая диаграмма

    private final String id;
}
