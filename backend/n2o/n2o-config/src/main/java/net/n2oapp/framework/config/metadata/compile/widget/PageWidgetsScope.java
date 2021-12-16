package net.n2oapp.framework.config.metadata.compile.widget;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.Map;

/**
 * Информация о скомпилированных виджетах страницы
 */
@Getter
@Setter
public class PageWidgetsScope {
    private Map<String, Widget<?>> widgets;

    public PageWidgetsScope(Map<String, Widget<?>> widgets) {
        this.widgets = widgets;
    }
}
