package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Сторона боковой панели
 */
@RequiredArgsConstructor
@Getter
public enum SidebarSideEnum implements N2oEnum {
    LEFT("left"),
    RIGHT("right");

    private final String id;
}