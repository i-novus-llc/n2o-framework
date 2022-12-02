package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oPagination;
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
    private N2oCol[] content;

    @Getter
    @Setter
    public static class N2oCol implements Source {
        private Integer size;
        private N2oBlock[] blocks;
    }
}
