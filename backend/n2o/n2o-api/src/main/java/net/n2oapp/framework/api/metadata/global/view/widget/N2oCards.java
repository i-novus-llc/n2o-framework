package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oPagination;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.AbstractColumn;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.meta.widget.Cards;

/**
 * Исходная модель виджета карточки
 */
@Getter
@Setter
public class N2oCards extends N2oAbstractListWidget {
    private Cards.Position verticalAlign;
    private String height;
    private N2oPagination pagination;
    private Col[] content;

    @Getter
    @Setter
    public static class Col implements Source {
        private Integer size;
        private Block[] blocks;
    }

    @Getter
    @Setter
    public static class Block extends AbstractColumn {
        private N2oCell component;
    }
}
