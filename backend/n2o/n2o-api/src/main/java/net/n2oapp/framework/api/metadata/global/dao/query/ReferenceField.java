package net.n2oapp.framework.api.metadata.global.dao.query;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * Модель составного поля выборки
 */
@Getter
@Setter
public class ReferenceField extends AbstractField {
    private AbstractField[] fields;

    public ReferenceField(ReferenceField field) {
        super(field);
        if (field.getFields() != null)
            this.fields = Arrays.stream(field.getFields()).map(AbstractField::of).toArray(AbstractField[]::new);
    }

    public ReferenceField() {
    }
}
