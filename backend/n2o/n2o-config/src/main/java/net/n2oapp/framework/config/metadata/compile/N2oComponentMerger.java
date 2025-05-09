package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import org.springframework.stereotype.Component;

/**
 * Слияние компонентов
 */
@Component
public class N2oComponentMerger<T extends N2oComponent> implements BaseSourceMerger<T> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oComponent.class;
    }

    @Override
    public T merge(T ref, T source) {
        setIfNotNull(source::setSrc, source::getSrc, ref::getSrc);
        setIfNotNull(source::setCssClass, source::getCssClass, ref::getCssClass);
        setIfNotNull(source::setStyle, source::getStyle, ref::getStyle);
        mergeExtAttributes(ref, source);
        return source;
    }
}
