package net.n2oapp.framework.api.ui;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.Validation;

/**
 * Информация о запросе валидации
 */
@Getter
@Setter
public class ValidationRequestInfo extends RequestInfo {

    /**
     * Валидация
     */
    private Validation validation;
    /**
     * Данные формы
     */
    private DataSet data;

}
