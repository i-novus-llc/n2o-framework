package net.n2oapp.framework.api.metadata.global.dao.object.field;

import lombok.NoArgsConstructor;

/**
 * Исходная модель поля-множества.
 */
@NoArgsConstructor
public class ObjectSetField extends ObjectReferenceField {

    public ObjectSetField(ObjectSetField field) {
        super(field);
    }
}
