package net.n2oapp.framework.api.metadata.global.dao.object.field;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Исходная модель поля, ссылающегося на другой объект.
 */
@Getter
@Setter
public class ObjectReferenceField extends AbstractParameter {
    private String referenceObjectId;
    private String entityClass;
    private AbstractParameter[] fields;
    private List<ObjectSimpleField> objectReferenceFields = new ArrayList<>();

    public ObjectReferenceField() {
    }

    public ObjectReferenceField(String id, String name, String mapping, Boolean required, String referenceObjectId) {
        this.setId(id);
        this.setName(name);
        this.setMapping(mapping);
        this.setRequired(required);
        this.referenceObjectId = referenceObjectId;
    }
}
