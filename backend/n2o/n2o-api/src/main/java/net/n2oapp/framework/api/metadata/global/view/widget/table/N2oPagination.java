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
    private Boolean last;
    private Boolean first;
    private Boolean showCount;
    private Boolean showSinglePage;
    private Layout layout;
    private String prevLabel;
    private String prevIcon;
    private String nextLabel;
    private String nextIcon;
    private String firstLabel;
    private String firstIcon;
    private String lastLabel;
    private String lastIcon;
    private Integer maxPages;
    private String className;
    private String style;
    private Place place;
}
