package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Ячейка с индикатором прогресса
 */
@Getter
@Setter
public class N2oProgressBarCell extends N2oAbstractCell {
    private SizeEnum size;
    private Boolean striped;
    private Boolean active;
    private String color;

    @RequiredArgsConstructor
    @Getter
    public enum SizeEnum implements N2oEnum {
        SMALL("small"),
        NORMAL("normal"),
        LARGE("large");

        private final String id;
    }
}
