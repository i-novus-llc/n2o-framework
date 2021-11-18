package net.n2oapp.framework.api.data.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.metadata.control.ValidationReference.Target;
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
    @JsonProperty("expression")
    private String enablingExpression;
    private String expressionOn;

    public MandatoryValidation(String id, String message, String fieldId) {
        setId(id);
        setMessage(message);
        setFieldId(fieldId);
        setTarget(Target.field);
    }

    public MandatoryValidation(MandatoryValidation v) {
        super(v);
        this.enablingExpression = v.getEnablingExpression();
        this.expressionOn = v.getExpressionOn();
    }


    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback) {
        Boolean success = null;
        if (dataSet.get(getFieldId()) != null) {
            if (dataSet.get(getFieldId()) instanceof String)
                success = !((String) dataSet.get(getFieldId())).isEmpty();
            else if (dataSet.get(getFieldId()) instanceof List)
                success = !((List) dataSet.get(getFieldId())).isEmpty();
            else if (dataSet.get(getFieldId()) instanceof Map)
                success = !((Map) dataSet.get(getFieldId())).isEmpty();
            else
                success = true;
        }

        if (!Boolean.TRUE.equals(success))
            callback.onFail(String.valueOf(getMessage()));
    }

    @Override
    public String getType() {
        return "required";
    }

}
