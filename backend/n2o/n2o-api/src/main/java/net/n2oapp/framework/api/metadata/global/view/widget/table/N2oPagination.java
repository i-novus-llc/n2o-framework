package net.n2oapp.framework.api.metadata.global.view.widget.table;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;

/**
 * Исходная модель для паджинации таблицы.
 */
@Getter
@Setter
@VisualComponent
public class N2oPagination implements Source {
    private String src;
    @VisualAttribute
    private Boolean prev;
    @VisualAttribute
    private Boolean next;
    @VisualAttribute
    private Boolean last;
    @VisualAttribute
    private Boolean first;
    @VisualAttribute
    private Boolean showCount;
    @Deprecated
    private Boolean hideSinglePage;
    @VisualAttribute
    private Boolean showSinglePage;
    private Layout layout;
    @VisualAttribute
    private String prevLabel;
    @VisualAttribute
    private String prevIcon;
    @VisualAttribute
    private String nextLabel;
    @VisualAttribute
    private String nextIcon;
    @VisualAttribute
    private String firstLabel;
    @VisualAttribute
    private String firstIcon;
    @VisualAttribute
    private String lastLabel;
    @VisualAttribute
    private String lastIcon;
    @VisualAttribute
    private Integer maxPages;
    @VisualAttribute
    private String className;
    @VisualAttribute
    private String style;
    @VisualAttribute
    private Place place;
}
