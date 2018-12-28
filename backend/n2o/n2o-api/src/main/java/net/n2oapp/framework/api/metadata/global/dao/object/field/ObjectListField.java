package net.n2oapp.framework.api.metadata.global.dao.object.field;

import net.n2oapp.framework.api.metadata.global.dao.object.PluralityType;

/**
 * Исходная модель поля-списка.
 */
public class ObjectListField extends ObjectReferenceField {
    public ObjectListField() {
        setPluralityType(PluralityType.list);
    }
}
