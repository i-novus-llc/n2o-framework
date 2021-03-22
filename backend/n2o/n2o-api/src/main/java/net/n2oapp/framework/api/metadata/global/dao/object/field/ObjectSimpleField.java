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
    private String mappingCondition;
    private String param;
    private String validationFailKey;

    public ObjectSimpleField() {
    }

    public ObjectSimpleField(String id, String name, String mapping, Boolean required, String domain) {
        this.setId(id);
        this.setName(name);
        this.setMapping(mapping);
        this.setRequired(required);
        this.domain = domain;
    }
}
