package net.n2oapp.framework.api.metadata.global.dao.query.field;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель спискового поля выборки
 */
@Getter
@Setter
public class QueryListField extends QueryReferenceField {

    public QueryListField(QueryReferenceField field) {
        super(field);
    }

    public QueryListField() {
    }
}
