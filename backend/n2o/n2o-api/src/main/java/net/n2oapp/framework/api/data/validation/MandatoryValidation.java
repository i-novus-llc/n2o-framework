package net.n2oapp.framework.api.data.validation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.metadata.meta.control.Field;

import java.util.List;
import java.util.Map;

/**
 * Клиентская модель валидации обязательности заполнения поля
 */
@Getter
@Setter
@NoArgsConstructor
public class MandatoryValidation extends Validation {
    protected Field field;

    //лучше создавать N2oMandatoryValidation и скомпилировать
    @Deprecated
    public MandatoryValidation(String id, String message, String fieldId) {
        setId(id);
        setMessage(message);
        setJsonMessage(message);
        setFieldId(fieldId);
    }

    public MandatoryValidation(MandatoryValidation v) {
        super(v);
        this.field = v.getField();
    }


    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback,
                         DomainProcessor domainProcessor) {
        Boolean success = null;
        var fieldId = dataSet.get(getFieldId());

        if (fieldId != null) {
            if (fieldId instanceof String str)
                success = !str.isEmpty();
            else if (fieldId instanceof List list)
                success = !list.isEmpty();
            else if (fieldId instanceof Map map)
                success = !map.isEmpty();
            else
                success = true;
        }

        if (!Boolean.TRUE.equals(success))
            callback.onFail(getMessage());
    }

    @Override
    public String getType() {
        return "required";
    }

}
