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
    public T merge(T source, T override) {
        setIfNotNull(source::setSrc, override::getSrc);
        setIfNotNull(source::setCssClass, override::getCssClass);
        setIfNotNull(source::setStyle, override::getStyle);
        return source;
    }
}
