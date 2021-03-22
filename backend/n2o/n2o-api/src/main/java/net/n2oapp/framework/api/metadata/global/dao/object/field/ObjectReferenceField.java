package net.n2oapp.framework.api.metadata.global.dao.object.field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;

/**
 * Исходная модель поля, ссылающегося на другой объект.
 */
@Getter
@Setter
@NoArgsConstructor
public class ObjectReferenceField extends AbstractParameter {
    private String referenceObjectId;
    private String entityClass;
    private AbstractParameter[] fields;

    public ObjectReferenceField(ObjectReferenceField field) {
        super(field);
        this.entityClass = field.getEntityClass();
        this.fields = field.getFields();
    }

    @Override
    public String getPostfix() {
        return "reference";
    }
}
