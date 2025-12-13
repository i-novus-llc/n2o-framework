package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель виджета Плитки
 */
@Getter
@Setter
public class N2oTiles extends N2oAbstractListWidget {
    private Integer colsSm;
    private Integer colsMd;
    private Integer colsLg;
    private String height;
    private String width;
    private N2oBlock[] content;
}
