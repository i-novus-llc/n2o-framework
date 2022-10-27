package net.n2oapp.framework.config.metadata.merge.object;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import org.springframework.stereotype.Component;

@Component
public class N2oObjectSimpleFieldMerger extends N2oObjectFieldMerger<ObjectSimpleField> {

    @Override
    public ObjectSimpleField merge(ObjectSimpleField source, ObjectSimpleField override) {
        super.merge(source, override);
        setIfNotNull(source::setDefaultValue, override::getDefaultValue);
        setIfNotNull(source::setDomain, override::getDomain);
        setIfNotNull(source::setParam, override::getParam);
        setIfNotNull(source::setValidationFailKey, override::getValidationFailKey);
        return source;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return ObjectSimpleField.class;
    }
}
