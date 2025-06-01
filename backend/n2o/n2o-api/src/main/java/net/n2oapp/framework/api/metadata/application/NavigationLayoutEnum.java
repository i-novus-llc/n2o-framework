package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Макет отображения элементов управления
 */
@RequiredArgsConstructor
@Getter
public enum NavigationLayoutEnum implements N2oEnum {
    /**
     * Шапка во всю ширину экрана
     */
    FULL_SIZE_HEADER("fullSizeHeader"),

    /**
     * Боковая панель во всю высоту экрана
     */
    FULL_SIZE_SIDEBAR("fullSizeSidebar");

    private final String id;
}
