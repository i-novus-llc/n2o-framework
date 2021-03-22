package net.n2oapp.framework.api.metadata.global.dao.object.field;

/**
 * Исходная модель поля-множества.
 */
public class ObjectSetField extends ObjectReferenceField {
    @Override
    public final String getPostfix() {
        return "set";
    }
}
