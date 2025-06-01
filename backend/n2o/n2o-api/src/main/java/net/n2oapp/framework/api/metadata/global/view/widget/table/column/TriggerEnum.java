package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Тип действия, при котором раскрывается список
 */
@RequiredArgsConstructor
@Getter
public enum TriggerEnum implements N2oEnum {
    HOVER("hover"),
    CLICK("click");

    private final String id;
}