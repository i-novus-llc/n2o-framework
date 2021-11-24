package net.n2oapp.framework.api.data.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.script.ScriptProcessor;

import javax.script.ScriptException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Клиентская модель валидации условия значений полей
 */
@Getter
@Setter
@NoArgsConstructor
public class ConditionValidation extends Validation {
    @JsonProperty
    private String expression;
    private String expressionOn;

    public ConditionValidation(ConditionValidation validation) {
        super(validation);
        this.expression = validation.getExpression();
        this.expressionOn = validation.getExpressionOn();
    }

    public void setExpression(String expression) {
        if (expression == null)
            return;
        expression = expression.replaceAll("\n", "").replace("\r", "").trim();
        this.expression = expression;
    }

    private Set<String> getExpressionsOn() {
        Set<String> res = new HashSet<>();
        if (expressionOn != null && expressionOn.length() > 0) {
            String[] expressions = expressionOn.split(",");
            for (String exp : expressions) {
                res.add(exp.trim());
            }
        }
        return res;
    }

    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback) {
        try {
            DataSet copiedDataSet = new DataSet(dataSet);
            for (String key : getExpressionsOn()) {
                Object value = dataSet.get(key);
                if (value instanceof Date) {
                    copiedDataSet.put(key, DomainProcessor.getInstance().serialize(value));
                }
            }
            if (!(boolean) ScriptProcessor.getInstance().eval(getExpression(), copiedDataSet))
                callback.onFail(getJsonMessage());
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getType() {
        return "condition";
    }
}
