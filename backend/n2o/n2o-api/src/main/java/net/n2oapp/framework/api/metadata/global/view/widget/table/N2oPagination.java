package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Исходная модель для паджинации таблицы.
 */
@Getter
@Setter
public class N2oPagination implements Source {

    private String src;
    private Boolean prev;
    private Boolean next;
    private ShowCountType showCount;
    private Boolean showLast;
    @Deprecated
    private String prevLabel;
    private String prevIcon;
    private String nextLabel;
    private String nextIcon;
    private String className;
    private String style;
    private Place place;
}
