package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.N2oPagination;

/**
 * Исходная модель виджета Плитки
 */
@Getter
@Setter
public class N2oTiles extends N2oAbstractListWidget {
    private Integer colsSm;
    private Integer colsMd;
    private Integer colsLg;
    private Integer height;
    private Integer width;
    private N2oPagination pagination;
    private N2oBlock[] content;
}
