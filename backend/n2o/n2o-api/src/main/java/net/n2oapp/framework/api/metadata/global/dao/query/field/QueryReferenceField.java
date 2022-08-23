package net.n2oapp.framework.api.metadata.global.dao.query.field;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.query.AbstractField;

/**
 * Модель составного поля выборки
 */
@Getter
@Setter
public class QueryReferenceField extends AbstractField {
    private String selectKey;
    private AbstractField[] fields;

    public QueryReferenceField(QueryReferenceField field) {
        super(field);
        this.fields = field.getFields();
        this.selectKey = field.getSelectKey();
    }

    public QueryReferenceField() {
    }
}
