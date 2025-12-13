package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.list.N2oListWidget;
import org.springframework.stereotype.Component;

/**
 * Слияние виджетов {@code <list>}
 */
@Component
public class N2oListMerger extends N2oWidgetMerger<N2oListWidget> {

    @Override
    public N2oListWidget merge(N2oListWidget ref, N2oListWidget source) {
        setIfNotNull(source::setPagination, source::getPagination, ref::getPagination);
        addIfNotNull(ref, source, N2oListWidget::setContent, N2oListWidget::getContent);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oListWidget.class;
    }
}
