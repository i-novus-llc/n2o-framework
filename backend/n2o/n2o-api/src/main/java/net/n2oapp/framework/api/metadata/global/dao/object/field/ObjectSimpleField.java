package net.n2oapp.framework.api.metadata.global.dao.object.field;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;

/**
 * Исходная модель простого поля объекта.
 */
@Getter
@Setter
public class ObjectSimpleField extends AbstractParameter {
    private String domain;
    private String defaultValue;
    private String normalize;
    private String param;
    private String validationFailKey;

    public ObjectSimpleField() {
    }

    public ObjectSimpleField(ObjectSimpleField field) {
        this.setId(field.getId());
        this.setMapping(field.getMapping());
        this.setEnabled(field.getEnabled());
        this.setRequired(field.getRequired());
        this.setDomain(field.getDomain());
        this.setDefaultValue(field.getDefaultValue());
        this.setNormalize(field.getNormalize());
    }
}
