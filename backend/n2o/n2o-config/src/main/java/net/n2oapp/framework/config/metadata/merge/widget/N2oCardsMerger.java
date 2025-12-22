package net.n2oapp.framework.config.metadata.merge.widget;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oCards;
import org.springframework.stereotype.Component;

/**
 * Слияние двух виджетов Карточки
 */
@Component
public class N2oCardsMerger extends N2oWidgetMerger<N2oCards> {

    @Override
    public N2oCards merge(N2oCards ref, N2oCards source) {
        super.merge(ref, source);
        setIfNotNull(source::setVerticalAlign, source::getVerticalAlign, ref::getVerticalAlign);
        setIfNotNull(source::setHeight, source::getHeight, ref::getHeight);
        setIfNotNull(source::setPagination, source::getPagination, ref::getPagination);
        addIfNotNull(ref, source, N2oCards::setContent, N2oCards::getContent);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oCards.class;
    }
}

