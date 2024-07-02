package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.Collections;
import java.util.List;

/**
 * Исходная модель <simple-page> - страницы с единственным виджетом
 */
@Getter
@Setter
public class N2oSimplePage extends N2oPage {

    private N2oWidget widget;

    @Override
    public List<N2oWidget> getWidgets() {
        return widget == null ?
                Collections.emptyList() :
                Collections.singletonList(widget);
    }

    @Override
    public String getObjectId() {
        return (widget != null && widget.getDatasource() != null) ?
                widget.getDatasource().getObjectId() :
                null;
    }

    @Override
    public void setObjectId(String objectId) {
        throw new UnsupportedOperationException();
    }
}
