package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import org.springframework.stereotype.Component;

@Component
public class N2oObjectReferenceFieldMerger extends N2oObjectFieldMerger<ObjectReferenceField> {
    @Override
    public ObjectReferenceField merge(ObjectReferenceField source, ObjectReferenceField override) {
        setIfNotNull(source::setEntityClass, override::getEntityClass);
        setIfNotNull(source::setReferenceObjectId, override::getReferenceObjectId);
        return super.merge(source, override);
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return ObjectReferenceField.class;
    }
}
