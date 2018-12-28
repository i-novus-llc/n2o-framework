package net.n2oapp.framework.config.metadata.compile.context;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

/**
 * Контекст сборки виджета
 */
@Getter
@Setter
public class WidgetContext extends BaseCompileContext<Widget, N2oWidget> {

    public WidgetContext(String widgetId) {
        super(widgetId, N2oWidget.class, Widget.class);
    }

    public WidgetContext(String sourceId, String route) {
        super(route, sourceId, N2oWidget.class, Widget.class);
    }
}
