package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Исходная модель переключаемой ячейки
 */
@Getter
@Setter
public class N2oSwitchCell extends N2oAbstractCell {
    private String valueFieldId;
    private Case[] cases;
    private N2oAbstractCell defaultCase;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Case implements Source, Compiled {
        private String value;
        private N2oAbstractCell cell;
    }
}
