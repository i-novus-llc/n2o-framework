package net.n2oapp.framework.api.metadata.meta.widget.chart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип интерполяции линии на графике
 */

@RequiredArgsConstructor
@Getter
public enum ChartLineTypeEnum implements N2oEnum {
    BASIS("basis"),
    BASIS_CLOSED("basisClosed"),
    BASIS_OPEN("basisOpen"),
    LINEAR("linear"),
    LINEAR_CLOSED("linearClosed"),
    NATURAL("natural"),
    MONOTONE_X("monotoneX"),
    MONOTONE_Y("monotoneY"),
    MONOTONE("monotone"),
    STEP("step"),
    STEP_BEFORE("stepBefore"),
    STEP_AFTER("stepAfter");

    private final String id;
}