package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSetField;
import org.springframework.stereotype.Component;

@Component
public class N2oObjectSetFieldMerger extends N2oObjectReferenceFieldMerger {
    @Override
    public Class<? extends Source> getSourceClass() {
        return ObjectSetField.class;
    }
}
