package net.n2oapp.framework.api.metadata.global.view.action.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Цель закрытия: текущая вкладка браузера или страница
 */
@RequiredArgsConstructor
@Getter
public enum CloseTargetEnum implements N2oEnum {
    TAB("tab"),
    PAGE("page");

    private final String id;
}