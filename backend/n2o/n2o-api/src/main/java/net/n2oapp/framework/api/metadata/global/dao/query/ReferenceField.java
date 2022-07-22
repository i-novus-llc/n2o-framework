package net.n2oapp.framework.api.metadata.global.dao.query;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель составного поля выборки
 */
@Getter
@Setter
public class ReferenceField extends AbstractField {
    private AbstractField[] fields;
}
