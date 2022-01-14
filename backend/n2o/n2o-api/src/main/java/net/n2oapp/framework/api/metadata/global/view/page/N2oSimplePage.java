package net.n2oapp.framework.api.metadata.global.view.page;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.Collections;
import java.util.List;

/**
 * Модель страницы с единственным виджетом
 */
public class N2oSimplePage extends N2oPage {

    private N2oWidget widget;

    @Override
    public List<N2oWidget> getWidgets() {
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
        return widget != null ? widget.getDatasource() != null ? widget.getDatasource().getObjectId() : null : null;
    }

    @Override
    public void setObjectId(String objectId) {
        throw new UnsupportedOperationException();
    }
}
