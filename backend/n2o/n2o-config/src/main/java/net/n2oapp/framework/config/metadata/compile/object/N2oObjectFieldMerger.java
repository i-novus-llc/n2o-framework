package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.config.metadata.compile.BaseSourceMerger;

public abstract class N2oObjectFieldMerger<T extends AbstractParameter> implements BaseSourceMerger<T> {

    @Override
    public T merge(T source, T override) {
        setIfNotNull(source::setMapping, override::getMapping);
        setIfNotNull(source::setRequired, override::getRequired);
        setIfNotNull(source::setEnabled, override::getEnabled);
        return source;
    }
}
