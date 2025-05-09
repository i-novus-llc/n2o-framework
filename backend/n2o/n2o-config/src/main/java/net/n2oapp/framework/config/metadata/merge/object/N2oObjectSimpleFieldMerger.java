package net.n2oapp.framework.config.metadata.merge.object;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import org.springframework.stereotype.Component;

@Component
public class N2oObjectSimpleFieldMerger extends N2oObjectFieldMerger<ObjectSimpleField> {

    @Override
    public ObjectSimpleField merge(ObjectSimpleField ref, ObjectSimpleField source) {
        super.merge(ref, source);
        setIfNotNull(source::setDefaultValue, source::getDefaultValue, ref::getDefaultValue);
        setIfNotNull(source::setDomain, source::getDomain, ref::getDomain);
        setIfNotNull(source::setParam, source::getParam, ref::getParam);
        setIfNotNull(source::setValidationFailKey, source::getValidationFailKey, ref::getValidationFailKey);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return ObjectSimpleField.class;
    }
}
