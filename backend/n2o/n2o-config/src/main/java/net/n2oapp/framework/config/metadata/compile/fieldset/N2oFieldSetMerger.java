package net.n2oapp.framework.config.metadata.compile.fieldset;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.fieldset.N2oFieldSet;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;
import org.springframework.stereotype.Component;

/**
 * Слияние двух филдсетов
 */
@Component
public class N2oFieldSetMerger<T extends N2oFieldSet> implements BaseSourceMerger<T> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFieldSet.class;
    }

    @Override
    public T merge(T source, T override) {
        setIfNotNull(source::setId, override::getId);
        setIfNotNull(source::setItems, override::getItems);
        setIfNotNull(source::setLabel, override::getLabel);
        setIfNotNull(source::setDescription, override::getDescription);
        setIfNotNull(source::setCssClass, override::getCssClass);
        setIfNotNull(source::setStyle, override::getStyle);
        setIfNotNull(source::setSrc, override::getSrc);
        setIfNotNull(source::setFieldLabelLocation, override::getFieldLabelLocation);
        setIfNotNull(source::setFieldLabelAlign, override::getFieldLabelAlign);
        setIfNotNull(source::setFieldLabelWidth, override::getFieldLabelWidth);
        setIfNotNull(source::setDependencyCondition, override::getDependencyCondition);
        setIfNotNull(source::setDependsOn, override::getDependsOn);
        setIfNotNull(source::setVisible, override::getVisible);
        setIfNotNull(source::setEnabled, override::getEnabled);
        setIfNotNull(source::setDependsOn, override::getDependsOn);
        mergeExtAttributes(source, override);
        return source;
    }
}
