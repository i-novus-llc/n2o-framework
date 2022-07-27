package net.n2oapp.framework.api.metadata.global.dao.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель спискового поля выборки
 */
@Getter
@Setter
public class ListField extends ReferenceField {

    public ListField(ReferenceField field) {
        super(field);
    }

    public ListField() {
    }
}
