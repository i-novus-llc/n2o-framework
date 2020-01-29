package net.n2oapp.framework.api.metadata.global.view.page;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.Collections;
import java.util.List;

/**
 * "Сырая" модель обычной страницы (page)
 */
public class N2oSimplePage extends N2oPage {

    private N2oWidget widget;

    @Override
    public List<N2oWidget> getContainers() {
        if (widget == null)
            return Collections.emptyList();
        return Collections.singletonList(widget);
    }

    public N2oWidget getWidget() {
        return widget;
    }

    public void setWidget(N2oWidget widget) {
        this.widget = widget;
    }

    @Override
    public String getObjectId() {
        return widget != null ? widget.getObjectId() : null;
    }
}
