package net.n2oapp.framework.api.metadata.global.dao.object.field;

import net.n2oapp.framework.api.metadata.global.dao.object.PluralityType;

/**
 * Исходная модель поля-множества.
 */
public class ObjectSetField extends ObjectReferenceField {
    public ObjectSetField() {
        setPluralityType(PluralityType.set);
    }
}
