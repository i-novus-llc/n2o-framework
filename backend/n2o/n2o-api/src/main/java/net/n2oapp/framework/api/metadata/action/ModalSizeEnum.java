package net.n2oapp.framework.api.metadata.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Размер модального окна
 */

@RequiredArgsConstructor
@Getter
public enum ModalSizeEnum implements N2oEnum {
    SM("sm"),
    MD("md"),
    LG("lg"),
    XL("xl");

    private final String id;
}