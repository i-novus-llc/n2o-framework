package net.n2oapp.framework.api.data.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.script.ScriptProcessor;

import javax.script.ScriptException;
import java.util.*;

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
            Arrays.asList(expressionOn.split(",")).forEach(e -> res.add(e.trim()));
        }
        return res;
    }

    private DataSet getCopiedDataSet (DataSet dataSet) {
        DataSet copiedDataSet = new DataSet(dataSet);
        for (String key : getExpressionsOn()) {
            Object value = dataSet.get(key);
            if (value instanceof Date) {
                copiedDataSet.put(key, DomainProcessor.getInstance().serialize(value));
            }
        }
        return copiedDataSet;
    }

    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback) {
        try {
            DataSet copiedDataSet = getCopiedDataSet(dataSet);
            if (!(boolean) ScriptProcessor.getInstance().eval(getExpression(), copiedDataSet))
                callback.onFail(StringUtils.resolveLinks(getMessage(), copiedDataSet));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getType() {
        return "condition";
    }
}
