package net.n2oapp.framework.api.metadata.global.dao.object.field;

import lombok.NoArgsConstructor;

/**
 * Исходная модель поля-списка.
 */
@NoArgsConstructor
public class ObjectListField extends ObjectReferenceField {

    public ObjectListField(ObjectListField field) {
        super(field);
    }
}
